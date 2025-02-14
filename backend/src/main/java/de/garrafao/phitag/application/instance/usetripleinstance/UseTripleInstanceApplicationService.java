package de.garrafao.phitag.application.instance.usetripleinstance;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.garrafao.phitag.domain.instance.usetripleinstance.page.UseTripleInstancePageBuilder;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.sampling.data.SamplingEnum;
import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformation;
import de.garrafao.phitag.domain.annotationprocessinformation.error.AnnotationProcessInformationException;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceFactory;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceRepository;
import de.garrafao.phitag.domain.instance.usetripleinstance.error.UseTripleInstanceAlreadyExistsException;
import de.garrafao.phitag.domain.instance.usetripleinstance.query.UseTripleInstanceQueryBuilder;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;
import de.garrafao.phitag.domain.phitagdata.usage.UsageRepository;
import de.garrafao.phitag.domain.phitagdata.usage.error.UsageNotFoundException;

/**
 * Service for use triples.
 * Note that this service assumes that access to the phase is already checked.
 * Only the validity of the use triples is checked.
 */
@Service
public class UseTripleInstanceApplicationService {

    private final UsageRepository usageRepository;

    private final UseTripleInstanceRepository useTripleInstanceRepository;

    private final CommonService commonService;

    @Autowired
    public UseTripleInstanceApplicationService(
            final UsageRepository usageRepository,
            final UseTripleInstanceRepository useTripleInstanceRepository,

            final CommonService commonService) {
        this.usageRepository = usageRepository;
        this.useTripleInstanceRepository = useTripleInstanceRepository;

        this.commonService = commonService;
    }

    // Getter

    /**
     * Get all use triples for a given phase.
     * 
     * @param phase the phase
     * @return a {@link UseTripleInstance} list
     */
    public List<UseTripleInstance> findByPhase(final Phase phase) {
        final Query query = new UseTripleInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.useTripleInstanceRepository.findByQuery(query);
    }

    /**
     * Get all Use Triple Instances for a given phase paged.
     * 
     * @param phase      the phase
     * @param pagesize   the size of the page
     * @param pagenumber the number of the page
     * @param orderBy    the field to order by
     * @return a {@link UseTripleInstance} page
     */
    public Page<UseTripleInstance> findByPhasePaged(
            final Phase phase,
            final int pagesize,
            final int pagenumber,
            final String orderBy) {
        final Query query = new UseTripleInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.useTripleInstanceRepository.findByQueryPaged(query,
                new UseTripleInstancePageBuilder()
                        .withPageSize(pagesize)
                        .withPageNumber(pagenumber)
                        .withOrderBy(orderBy)
                        .build());

    }

    /**
     * Get random use triples for a given phase.
     * 
     * @param phase the phase
     * @return a {@link UseTripleInstance}
     */
    @Transactional
    public UseTripleInstance getAnnotationInstance(final Phase phase, final Annotator annotator) {
        return this.sample(phase, annotator);
    }

    /**
     * Export all use triples for a given phase.
     * 
     * @param phase the phase
     * @return a {@link InputStreamResource} with the CSV data
     */
    public InputStreamResource exportUseTripleInstance(final Phase phase) {
        List<UseTripleInstance> instanceData = this.findByPhase(phase);
        String[] csvHeader = {
                "instanceID", "dataIDs", "label_set", "non_label"
        };
        List<List<String>> csvData = parseUseTripleInstancesToCsvBody(instanceData);

        ByteArrayInputStream outputStream;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = createCsvPrinter(csvHeader, out);
        csvData.forEach(row -> {
            try {
                csvPrinter.printRecord(row);
            } catch (Exception e) {
                throw new CsvParseException();
            }
        });
        try {
            csvPrinter.flush();
            outputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new CsvParseException();
        }

        return new InputStreamResource(outputStream);
    }

    /**
     * Import use triples for a given phase from a CSV file.
     * 
     * @param phase the phase
     * @param file  the CSV file
     */
    @Transactional
    public void save(final Phase phase, final MultipartFile file) {
        validateCsvFile(file);

        parseCsvFile(file).forEach(csvrecord -> {
            final UseTripleInstance instanceData = parseRecordToUseTripleInstance(phase, csvrecord);
            validateUniqueInstance(instanceData);
            this.useTripleInstanceRepository.save(instanceData);
        });

        this.generateSamplingTasks(phase);
    }

    /**
     * Generate instances from all usages for a phase.
     * 
     * @param phase    the phase
     * @param nonLabel
     * @param labels
     */
    @Transactional
    public void generateInstances(Phase phase, List<String> labels, String nonLabel) {
        // fetch all data ids for the project
        List<String> dataIds = this.usageRepository.findAllDataIdsByProjectnameAndOwnername(
                phase.getId().getProjectid().getName(),
                phase.getId().getProjectid().getOwnername());

        // create all possible triples & save them to reduce memory usage
        UseTripleInstanceFactory factory = new UseTripleInstanceFactory();
        factory.withPhase(phase).withLabelSet(String.join(",", labels)).withNonLabel(nonLabel);

        for (int i = 0; i < dataIds.size(); i++) {
            for (int j = i + 1; j < dataIds.size(); j++) {
                Usage usage1 = this.usageRepository.findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(
                        dataIds.get(i), phase.getId().getProjectid().getName(),
                        phase.getId().getProjectid().getOwnername()).orElseThrow(UsageNotFoundException::new);
                Usage usage2 = this.usageRepository.findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(
                        dataIds.get(j), phase.getId().getProjectid().getName(),
                        phase.getId().getProjectid().getOwnername()).orElseThrow(UsageNotFoundException::new);
                Usage usage3 = this.usageRepository.findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(
                        dataIds.get(j), phase.getId().getProjectid().getName(),
                        phase.getId().getProjectid().getOwnername()).orElseThrow(UsageNotFoundException::new);
                UseTripleInstance instance = factory.withInstanceId(UUID.randomUUID().toString()).withFirstUsage(usage1).withSecondUsage(usage2).withThirdUsage(usage3).build();
                this.useTripleInstanceRepository.save(instance);
            }
        }

    }

    // Parser

    private CSVPrinter createCsvPrinter(String[] csvHeader, ByteArrayOutputStream outputStream) {
        CSVPrinter printer = null;

        try {
            PrintWriter writer = new PrintWriter(outputStream);
            CSVFormat format = CSVFormat.Builder.create().setHeader(csvHeader).setDelimiter("\t").build();
            printer = new CSVPrinter(writer, format);
        } catch (Exception e) {
            throw new CsvParseException();
        }

        return printer;
    }

    private List<List<String>> parseUseTripleInstancesToCsvBody(List<UseTripleInstance> useTripleInstances) {
        List<List<String>> csvBody = new ArrayList<>();

        for (UseTripleInstance data : useTripleInstances) {
            List<String> csvRow = new ArrayList<>();

            csvRow.add(data.getId().getInstanceid());
            csvRow.add(String.format("%s,%s,%s", data.getFirstusage().getId().getDataid(),
                    data.getSecondusage().getId().getDataid(),
                    data.getThirdusage().getId().getDataid()));
            csvRow.add(String.join(",", data.getLabelSet()));
            csvRow.add(data.getNonLabel());
            csvBody.add(csvRow);
        }

        return csvBody;
    }

    private Iterable<CSVRecord> parseCsvFile(final MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVFormat format = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setDelimiter("\t")
                    .build();
            CSVParser parser = new CSVParser(reader, format);
            Iterable<CSVRecord> records = parser.getRecords();
            parser.close();

            return records;
        } catch (Exception e) {
            throw new CsvParseException();
        }
    }

    private UseTripleInstance parseRecordToUseTripleInstance(final Phase phase, final CSVRecord csvrecord) {
        String instanceId;
        List<String> dataIds;
        String labelSet;
        String nonLabel;

        try {
            instanceId = csvrecord.get("instanceID");
            dataIds = Arrays.stream(csvrecord.get("dataIDs").split(",")).collect(Collectors.toList());
            labelSet = csvrecord.get("label_set");
            nonLabel = csvrecord.get("non_label");
        } catch (IllegalArgumentException e) {
            throw new CsvParseException("CSV record is not valid, please check the format");
        }

        if (dataIds.size() != 3) {
            throw new CsvParseException();
        }

        Usage firstUsage = this.usageRepository
                .findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(dataIds.get(0),
                        phase.getId().getProjectid().getName(), phase.getId().getProjectid().getOwnername())
                .orElseThrow(() -> new UsageNotFoundException("Usage not found for data id " + dataIds.get(0)));
        Usage secondUsage = this.usageRepository
                .findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(dataIds.get(1),
                        phase.getId().getProjectid().getName(), phase.getId().getProjectid().getOwnername())
                .orElseThrow(() -> new UsageNotFoundException("Usage not found for data id " + dataIds.get(1)));
        Usage thirdUsage = this.usageRepository
                .findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(dataIds.get(2),
                        phase.getId().getProjectid().getName(), phase.getId().getProjectid().getOwnername())
                .orElseThrow(() -> new UsageNotFoundException("Usage not found for data id " + dataIds.get(2)));

        return new UseTripleInstance(instanceId, phase, firstUsage, secondUsage, thirdUsage, labelSet, nonLabel);
    }

    // Validation

    private void validateCsvFile(final MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file is empty");
        }
        if (file.getContentType() == null) {
            throw new IllegalArgumentException("file is not a csv");
        }
    }

    private void validateUniqueInstance(final UseTripleInstance instanceData) {
        if (this.useTripleInstanceRepository
                .findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(
                        instanceData.getId().getInstanceid(), instanceData.getId().getPhaseid().getName(),
                        instanceData.getId().getPhaseid().getProjectid().getName(),
                        instanceData.getId().getPhaseid().getProjectid().getOwnername())
                .isPresent()) {
            throw new UseTripleInstanceAlreadyExistsException();
        }
    }

    // Sampling methods

    /**
     * Generate sampling tasks for a given phase and all annotators
     * 
     * @param phase the phase
     */
    @Transactional
    public void generateSamplingTasks(final Phase phase) {
        this.commonService.getAnnotatorsOfProject(phase.getId().getProjectid().getOwnername(),
                phase.getId().getProjectid().getName()).forEach(annotator -> {
                    this.generateSamplingTask(phase, annotator);
                });
    }

    /**
     * Generate sampling tasks for a given phase and annotator
     * 
     * @param phase the phase
     */
    @Transactional
    public void generateSamplingTask(final Phase phase, final Annotator annotator) {
        List<String> samplingOrder = new ArrayList<>();

        // IF this gets out of hand, switch or some pattern
        if (phase.getSampling().getName().equals(SamplingEnum.SAMPLING_RANDOM_WITH_REPLACEMENT.name())
                || phase.getSampling().getName().equals(SamplingEnum.SAMPLING_RANDOM_WITHOUT_REPLACEMENT.name())) {
            samplingOrder = this.generateSamplingOrderWithoutReplacement(phase);
        } else if (phase.getSampling().getName().equals(SamplingEnum.SAMPLING_ID_ORDER.name())) {
            samplingOrder = this.generateSamplingIDOrder(phase);
        }

        if (samplingOrder.isEmpty()) {
            throw new AnnotationProcessInformationException("Sampling order is empty");
        }

        String flattenedSamplingOrder = String.join(",", samplingOrder);

        final AnnotationProcessInformation annotationProcessInformation = new AnnotationProcessInformation(annotator,
                phase);
        annotationProcessInformation.setOrder(flattenedSamplingOrder);
        annotationProcessInformation.setIndex(0);

        this.commonService.saveAnnotationProcessInformation(annotationProcessInformation);
    }

    /**
     * Generate sampling order for random sampling without replacement.
     * 
     * @param phase
     * @param annotator
     * @return
     */
    private List<String> generateSamplingOrderWithoutReplacement(final Phase phase) {
        List<String> samplingOrder = new ArrayList<>();

        this.commonService.findUseTripleInstanceByPhase(phase).forEach(useTripleInstance -> {
            samplingOrder.add(useTripleInstance.getId().getInstanceid());
        });

        Collections.shuffle(samplingOrder);
        return samplingOrder;
    }

    /**
     * Generate sampling order for ID-based sampling.
     * 
     * @param phase
     * @param annotator
     * @return
     */
    private List<String> generateSamplingIDOrder(final Phase phase) {
        List<String> samplingOrder = new ArrayList<>();

        this.commonService.findUseTripleInstanceByPhase(phase).forEach(useTripleInstance -> {
            samplingOrder.add(useTripleInstance.getId().getInstanceid());
        });

        Collections.sort(samplingOrder, (o1, o2) -> {
            try {
                int i1 = Integer.parseInt(o1);
                int i2 = Integer.parseInt(o2);
                return i1 - i2;
            } catch (Exception e) {
                return o1.compareTo(o2);
            }
        });

        return samplingOrder;
    }

    /**
     * Sample with replacement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * 
     * @return the a use triple instance
     */
    @Transactional
    private UseTripleInstance sample(final Phase phase, final Annotator annotator) {
        AnnotationProcessInformation annotationProcessInformation;

        try {
            annotationProcessInformation = this.commonService.getAnnotationProcessInformation(annotator, phase);
        } catch (AnnotationProcessInformationException e) {
            this.generateSamplingTask(phase, annotator);
            annotationProcessInformation = this.commonService.getAnnotationProcessInformation(annotator, phase);
        }

        String queryId = annotationProcessInformation.next();

        if (phase.getSampling().getName().equals(SamplingEnum.SAMPLING_RANDOM_WITH_REPLACEMENT.name())) {
            queryId = annotationProcessInformation.getOrder()
                    .get((int) (Math.random() * annotationProcessInformation.getOrder().size()));
        }

        // If sampling index is null, return null
        if (queryId == null) {
            return null;
        }

        final Query query = new UseTripleInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(queryId)
                .build();

        final List<UseTripleInstance> instances = this.useTripleInstanceRepository.findByQuery(query);
        if (instances.isEmpty()) {
            throw new UsageNotFoundException();
        }

        return instances.get(0);
    }

}

package de.garrafao.phitag.application.instance.wssiminstance;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import de.garrafao.phitag.domain.instance.wssiminstance.page.WSSIMInstancePageBuilder;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.sampling.data.SamplingEnum;
import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformation;
import de.garrafao.phitag.domain.annotationprocessinformation.error.AnnotationProcessInformationException;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstance;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstanceRepository;
import de.garrafao.phitag.domain.instance.wssiminstance.error.WSSIMInstanceAlreadyExistsException;
import de.garrafao.phitag.domain.instance.wssiminstance.error.WSSIMInstanceNotFoundException;
import de.garrafao.phitag.domain.instance.wssiminstance.query.WSSIMInstanceQueryBuilder;
import de.garrafao.phitag.domain.instance.wssimtag.WSSIMTag;
import de.garrafao.phitag.domain.instance.wssimtag.WSSIMTagRepository;
import de.garrafao.phitag.domain.instance.wssimtag.error.WSSIMTagNotFoundException;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;
import de.garrafao.phitag.domain.phitagdata.usage.UsageRepository;
import de.garrafao.phitag.domain.phitagdata.usage.error.UsageNotFoundException;

@Service
public class WSSIMInstanceApplicationService {

    private final WSSIMInstanceRepository wssimInstanceRepository;

    private final UsageRepository usageRepository;

    private final WSSIMTagRepository wssimTagRepository;

    private final CommonService commonService;

    @Autowired
    public WSSIMInstanceApplicationService(
            final WSSIMInstanceRepository wssimInstanceRepository,
            final UsageRepository usageRepository,
            final WSSIMTagRepository wssimTagRepository,

            final CommonService commonService) {
        this.wssimInstanceRepository = wssimInstanceRepository;
        this.usageRepository = usageRepository;
        this.wssimTagRepository = wssimTagRepository;

        this.commonService = commonService;
    }

    // Getter

    /**
     * Get all WSSIM instances for a given phase.
     * 
     * @param phase the phase
     * @return a {@link WSSIMInstance} list
     */
    public List<WSSIMInstance> findByPhase(final Phase phase) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimInstanceRepository.findByQuery(query);
    }

    /**
     * Get all WSSIM instances for a given phase paged.
     * 
     * 
     * @param phase      the phase
     * @param pagesize   the size of the page
     * @param pagenumber the number of the page
     * @param orderBy    the field to order by
     * @return a {@link WSSIMInstance} page
     */
    public Page<WSSIMInstance> findByPhasePaged(final Phase phase, final int pagesize, final int pagenumber,
            final String orderBy) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimInstanceRepository.findByQueryPaged(query,
                new WSSIMInstancePageBuilder()
                        .withPageSize(pagesize)
                        .withPageNumber(pagenumber)
                        .withOrderBy(orderBy)
                        .build());
    }

    /**
     * Get specific WSSIM instance
     * 
     * @param phase the phase
     * @param id    the id
     * @return a {@link WSSIMInstance}
     */
    public WSSIMInstance getWSSIMInstance(final Phase phase, final String id) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(id)
                .build();

        List<WSSIMInstance> wssimInstances = this.wssimInstanceRepository.findByQuery(query);
        if (wssimInstances.size() != 1) {
            throw new WSSIMInstanceNotFoundException();
        }

        return this.wssimInstanceRepository.findByQuery(query).get(0);
    }

    /**
     * Get random WSSIM instance
     * 
     * @param phase the phase
     * @return a {@link WSSIMInstance}
     */
    public WSSIMInstance getAnnotationInstance(final Phase phase, final Annotator annotator) {
        return this.sample(phase, annotator);
    }

    /**
     * Export all WSSIMTags for a given phase.
     * 
     * @param phase the phase
     * @return a {@link InputStreamResource} with the CSV data
     */
    public InputStreamResource exportWSSIMInstance(final Phase phase) {
        List<WSSIMInstance> instanceData = this.findByPhase(phase);
        String[] csvHeader = {
                "instanceID", "dataIDs", "label_set", "non_label"
        };
        List<List<String>> csvData = parseWSSIMInstanceToCsvBody(instanceData);

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

    // Setter

    /**
     * Import WSIMTags for a given phase from a CSV file.
     * 
     * @param phase the phase
     * @param file  the CSV file
     */
    @Transactional
    public void save(final Phase phase, final MultipartFile file) {
        validateCsvFile(file);

        parseCsvFile(file).forEach(csvrecord -> {
            final WSSIMInstance data = parseRecordToWSSIMInstance(phase, csvrecord);
            validateUniqueInstance(data);
            this.wssimInstanceRepository.save(data);
        });

        this.generateSamplingTasks(phase);
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

    private List<List<String>> parseWSSIMInstanceToCsvBody(List<WSSIMInstance> wssimInstances) {
        List<List<String>> csvBody = new ArrayList<>();

        for (WSSIMInstance data : wssimInstances) {
            List<String> csvRow = new ArrayList<>();

            csvRow.add(data.getId().getInstanceid());
            csvRow.add(
                    String.format("%s,%s", data.getUsage().getId().getDataid(), data.getWssimtag().getId().getTagid()));
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

    private WSSIMInstance parseRecordToWSSIMInstance(final Phase phase, final CSVRecord csvrecord) {
        final String instanceId;
        final String[] dataIds;
        final String labelSet;
        final String nonLabel;

        try {
            instanceId = csvrecord.get("instanceID");
            dataIds = csvrecord.get("dataIDs").split(",");
            labelSet = csvrecord.get("label_set");
            nonLabel = csvrecord.get("non_label");
        } catch (Exception e) {
            throw new CsvParseException("CSV record is not valid, please check the format");
        }

        final WSSIMTag tag = this.wssimTagRepository
                .findByIdTagidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(
                        dataIds[1], phase.getId().getName(), phase.getId().getProjectid().getName(),
                        phase.getId().getProjectid().getOwnername())
                .orElseThrow(() -> new WSSIMTagNotFoundException("Tag with id " + dataIds[1] + " not found"));
        final Usage usage;

        try {
            usage = this.usageRepository
                    .findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(dataIds[0],
                            phase.getId().getProjectid().getName(), phase.getId().getProjectid().getOwnername())
                    .orElseThrow(() -> new UsageNotFoundException("Usage with id " + dataIds[0] + " not found"));
        } catch (NumberFormatException e) {
            throw new CsvParseException();
        }

        return new WSSIMInstance(instanceId, phase, usage, tag, labelSet, nonLabel);
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

    private void validateUniqueInstance(final WSSIMInstance instanceData) {
        if (this.wssimInstanceRepository
                .findByIdInstanceidAndIdPhaseidNameAndIdPhaseidProjectidNameAndIdPhaseidProjectidOwnername(
                        instanceData.getId().getInstanceid(), instanceData.getId().getPhaseid().getName(),
                        instanceData.getId().getPhaseid().getProjectid().getName(),
                        instanceData.getId().getPhaseid().getProjectid().getOwnername())
                .isPresent()) {
            throw new WSSIMInstanceAlreadyExistsException();
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

        this.commonService.findWSSIMInstanceByPhase(phase).forEach(instance -> {
            samplingOrder.add(instance.getId().getInstanceid());
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

        this.commonService.findWSSIMInstanceByPhase(phase).forEach(instance -> {
            samplingOrder.add(instance.getId().getInstanceid());
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
     * @return the a use pair instance
     */
    @Transactional
    private WSSIMInstance sample(final Phase phase, final Annotator annotator) {
        AnnotationProcessInformation annotationProcessInformation;

        try {
            annotationProcessInformation = this.commonService.getAnnotationProcessInformation(annotator, phase);
        } catch (AnnotationProcessInformationException e) {
            this.generateSamplingTask(phase, annotator);
            annotationProcessInformation = this.commonService.getAnnotationProcessInformation(annotator, phase);
        }

        String queryId = annotationProcessInformation.next();

        // If sampling index is null, return null
        if (queryId == null) {
            return null;
        }

        if (phase.getSampling().getName().equals(SamplingEnum.SAMPLING_RANDOM_WITH_REPLACEMENT.name())) {
            queryId = annotationProcessInformation.getOrder()
                    .get((int) (Math.random() * annotationProcessInformation.getOrder().size()));
        }

        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(queryId)
                .build();

        final List<WSSIMInstance> instances = this.wssimInstanceRepository.findByQuery(query);
        if (instances.isEmpty()) {
            throw new UsageNotFoundException();
        }

        return instances.get(0);
    }

}

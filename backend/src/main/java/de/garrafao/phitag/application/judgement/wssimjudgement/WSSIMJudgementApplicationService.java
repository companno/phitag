package de.garrafao.phitag.application.judgement.wssimjudgement;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import de.garrafao.phitag.application.statistics.annotatostatistic.AnnotatorStatisticApplicationService;
import de.garrafao.phitag.application.statistics.phasestatistic.PhaseStatisticApplicationService;
import de.garrafao.phitag.application.statistics.userstatistic.UserStatisticApplicationService;
import de.garrafao.phitag.domain.judgement.wssimjudgement.page.WSSIMJudgementPageBuilder;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.AddWSSIMJudgementCommand;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.DeleteWSSIMJudgementCommand;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.EditWSSIMJudgementCommand;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.authentication.error.AccessDenidedException;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstance;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstanceRepository;
import de.garrafao.phitag.domain.instance.wssiminstance.query.WSSIMInstanceQueryBuilder;
import de.garrafao.phitag.domain.judgement.wssimjudgement.WSSIMJudgement;
import de.garrafao.phitag.domain.judgement.wssimjudgement.WSSIMJudgementRepository;
import de.garrafao.phitag.domain.judgement.wssimjudgement.error.WSSIMJudgementException;
import de.garrafao.phitag.domain.judgement.wssimjudgement.query.WSSIMJudgementQueryBuilder;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phase.error.TutorialException;

@Service
public class WSSIMJudgementApplicationService {

    private final WSSIMJudgementRepository wssimJudgementRepository;

    private final WSSIMInstanceRepository wssimInstanceRepository;

    // Statistics

    private final UserStatisticApplicationService userStatisticApplicationService;

    private final AnnotatorStatisticApplicationService annotatorStatisticApplicationService;

    private final PhaseStatisticApplicationService phaseStatisticApplicationService;

    @Autowired
    public WSSIMJudgementApplicationService(
            final WSSIMJudgementRepository wssimJudgementRepository,
            final WSSIMInstanceRepository wssimInstanceRepository,

            final UserStatisticApplicationService userStatisticApplicationService,
            final AnnotatorStatisticApplicationService annotatorStatisticApplicationService,
            final PhaseStatisticApplicationService phaseStatisticApplicationService) {
        this.wssimJudgementRepository = wssimJudgementRepository;
        this.wssimInstanceRepository = wssimInstanceRepository;

        this.userStatisticApplicationService = userStatisticApplicationService;
        this.annotatorStatisticApplicationService = annotatorStatisticApplicationService;
        this.phaseStatisticApplicationService = phaseStatisticApplicationService;
    }

    // Getter

    /**
     * Get all WSSIM Judgements for a given phase
     * 
     * @param phase
     * @return {@link WSSIMJudgement} list
     */
    public List<WSSIMJudgement> findByPhase(final Phase phase) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimJudgementRepository.findByQuery(query);
    }

    /**
     * Get all WSSIM Judgements for a given phase as a paged list
     * 
     * @param phase
     * @param phase
     * @param pagesize
     * @param pagenumber
     * @param orderBy
     * @return
     */
    public Page<WSSIMJudgement> findByPhase(final Phase phase, final int pagesize, final int pagenumber,
            final String orderBy) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimJudgementRepository.findByQueryPaged(query,
                new WSSIMJudgementPageBuilder()
                        .withPageSize(pagesize)
                        .withPageNumber(pagenumber)
                        .withOrderBy(orderBy)
                        .build());
    }

    /**
     * Get history of WSSIM Judgements for a given annotator
     * 
     * @param phase     The phase.
     * @param annotator The annotator.
     * @return {@link WSSIMJudgement} list
     */
    public List<WSSIMJudgement> getHistory(final Phase phase, final Annotator annotator) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername())
                .build();
        return this.wssimJudgementRepository.findByQuery(query);
    }

    /**
     * Get all use pair judgements for a given annotator as a paged list.
     * 
     * @param phase
     * @param annotator
     * @param pagesize
     * @param pagenumber
     * @param orderBy
     * @return
     */
    public Page<WSSIMJudgement> getHistory(final Phase phase, final Annotator annotator, final int pagesize,
            final int pagenumber, final String orderBy) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername())
                .build();
        return this.wssimJudgementRepository.findByQueryPaged(query,
                new WSSIMJudgementPageBuilder()
                        .withPageSize(pagesize)
                        .withPageNumber(pagenumber)
                        .withOrderBy(orderBy)
                        .build());
    }

    /**
     * Export all use pair judgements for a given phase.
     * 
     * @param phase the phase
     * @return a CSV file as {@link InputStreamResource}
     */
    public InputStreamResource exportWSSIMJudgement(final Phase phase) {
        List<WSSIMJudgement> resultData = this.findByPhase(phase);
        String[] csvHeader = {
                "instanceID", "label", "comment", "annotator"
        };
        List<List<String>> csvData = parseWSSIMJudgementToCsvBody(resultData);

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
     * Count all use pair judgements for a given annotator.
     * 
     * @param annotator the annotator
     * @return the number of use pair judgements
     */
    public int countJudgements(Annotator annotator) {
        return this.wssimJudgementRepository.findByQuery(
                new WSSIMJudgementQueryBuilder()
                        .withAnnotator(annotator.getId().getUsername())
                        .build())
                .size();
    }

    /**
     * Count all use pair judgements for a given annotator and phase.
     * 
     * @param annotator the annotator
     * @param phase     the phase
     * @return the number of use pair judgements
     */
    public int countJudgements(Annotator annotator, Phase phase) {
        return this.wssimJudgementRepository.findByQuery(
                new WSSIMJudgementQueryBuilder()
                        .withAnnotator(annotator.getId().getUsername())
                        .withPhase(phase.getId().getName()).build())
                .size();
    }

    // Setter Files

    /**
     * Import use pair judgements from a CSV file.
     * 
     * @param file  the CSV file
     * @param phase the phase
     */
    @Transactional
    public void save(final Phase phase, final Annotator annotator, final MultipartFile file) {
        validateCsvFile(file);

        parseCsvFile(file).forEach(csvrecord -> {
            final WSSIMJudgement resultData = parseRecordToWSSIMJudgement(phase, annotator, csvrecord);
            this.wssimJudgementRepository.save(resultData);
        });

    }

    /**
     * Edit wssim judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void edit(final Phase phase, final Annotator annotator, final EditWSSIMJudgementCommand command) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();

        final List<WSSIMJudgement> wssimJudgements = this.wssimJudgementRepository.findByQuery(query);
        if (wssimJudgements.size() != 1) {
            throw new WSSIMJudgementException("WSSIM Judgement not found");
        }
        final WSSIMJudgement judgement = wssimJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        if (!command.getLabel().isBlank()) {
            if (!(judgement.getWSSIMInstance().getLabelSet().contains(command.getLabel())
                    || judgement.getWSSIMInstance().getNonLabel().equals(command.getLabel()))) {
                throw new WSSIMJudgementException("Label not found");
            }
            judgement.setLabel(command.getLabel());
        }

        judgement.setComment(command.getComment());

        this.wssimJudgementRepository.save(judgement);
    }

    /**
     * Delete wssim judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void delete(final Phase phase, final Annotator annotator, final DeleteWSSIMJudgementCommand command) {
        final Query query = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();

        final List<WSSIMJudgement> wssimJudgements = this.wssimJudgementRepository.findByQuery(query);
        if (wssimJudgements.size() != 1) {
            throw new WSSIMJudgementException("WSSIM Judgement not found");
        }
        final WSSIMJudgement judgement = wssimJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        this.wssimJudgementRepository.delete(judgement);
    }

    // Setter Command

    /**
     * Add a use pair judgement.
     * 
     * @param phase     the phase to which the use pair judgement belongs
     * @param annotator the annotator who created the use pair judgement
     * @param command   the command
     */
    @Transactional
    public void annotate(final Phase phase, final Annotator annotator, final AddWSSIMJudgementCommand command) {
        final WSSIMInstance instance = this.findCorrespondingInstanceData(phase, command.getInstance());
        validateAddWSSIMJudgementCommand(instance, command);

        final WSSIMJudgement resultData = new WSSIMJudgement(instance, annotator, command.getLabel(),
                command.getComment());
        this.wssimJudgementRepository.save(resultData);

        // Update annotation count for the project
        this.userStatisticApplicationService.incrementAnnotationCountProject(phase.getProject());
        this.annotatorStatisticApplicationService.updateAnnotationStatistic(annotator, phase);
        this.phaseStatisticApplicationService.updatePhaseStatisticForAnnotation(annotator, phase);
    }

    /**
     * Add bulk use pair judgement for a given phase.
     * If the phase is a tutorial phase, the judgements are checked against the
     * tutorial judgements and if they are correct, the tutorial phase is marked as
     * completed.
     * 
     * @param phase     the phase to which the use pair judgement belongs
     * @param annotator the annotator who created the use pair judgement
     * @param commands  the commands
     */
    @Transactional
    public void annotateBulk(final Phase phase, final Annotator annotator,
            final List<AddWSSIMJudgementCommand> commands) {
        if (phase.isTutorial()) {
            tutorialAnnotationCorrectness(phase, annotator, commands);
            return;
        }

        commands.forEach(command -> {
            this.annotate(phase, annotator, command);
        });
    }

    // Helper

    private WSSIMInstance findCorrespondingInstanceData(final Phase phase, final String instanceid) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(instanceid).build();
        List<WSSIMInstance> resultData = this.wssimInstanceRepository.findByQuery(query);
        if (resultData.size() == 1) {
            return resultData.get(0);
        } else {
            throw new CsvParseException(
                    "Instance ID " + instanceid + " not found or ambiguous. Please check your CSV file");
        }
    }

    /**
     * Helper function validating the correctness of the tutorial judgements.
     * 
     * This validation is done by comparing the judgements in the tutorial phase
     * with the judgements of the annotator. If the judgements are correct, the
     * tutorial phase is marked as completed.
     * 
     * NOTE: This is a temporary solution, as there are more sophisticated ways to
     * validate if the tutorial was completed.
     * 
     * @param phase     the phase to which the use pair judgement belongs
     * @param annotator the annotator who created the use pair judgement
     * @param commands  the commands containing the judgements
     */
    @Transactional
    private void tutorialAnnotationCorrectness(final Phase phase, final Annotator annotator,
            List<AddWSSIMJudgementCommand> commands) {
        final Query judgementQuery = new WSSIMJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName()).build();

        final List<WSSIMJudgement> golds = this.wssimJudgementRepository.findByQuery(judgementQuery);

        if (commands.size() != golds.size()) {
            throw new TutorialException(
                    "Tutorial not completed. Please check your judgements. Not all instances were judged");
        }

        commands.forEach(command -> {
            final WSSIMJudgement gold = golds.stream()
                    .filter(j -> j.getId().getInstanceid().getInstanceid().equals(command.getInstance()))
                    .findFirst()
                    .orElseThrow(() -> new TutorialException(
                            "Tutorial not completed. Please check your judgements. Not all instances were judged"));

            if (!gold.getLabel().equals(command.getLabel())) {
                throw new TutorialException("Tutorial not completed. Judgements are incorrect");
            }
        });

        // Finally, mark the tutorial phase as completed for the annotator
        // As the object annotator is managed by the persistence context, we do not need
        // to
        // call the annotator repository and can simply update the object, HOPEFULLY?!?
        annotator.addCompletedTutorial(phase);
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

    private List<List<String>> parseWSSIMJudgementToCsvBody(List<WSSIMJudgement> wssimJudgements) {
        List<List<String>> csvBody = new ArrayList<>();

        for (WSSIMJudgement data : wssimJudgements) {
            List<String> csvRow = new ArrayList<>();

            csvRow.add(data.getWSSIMInstance().getId().getInstanceid());
            csvRow.add(data.getLabel());
            csvRow.add(data.getComment());
            csvRow.add(data.getAnnotator().getId().getUsername());

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

    private WSSIMJudgement parseRecordToWSSIMJudgement(final Phase phase, final Annotator annotator,
            final CSVRecord csvrecord) {
        final String instanceId;
        final String label;
        final String comment;

        try {
            instanceId = csvrecord.get("instanceID");

            label = csvrecord.get("label");
            comment = csvrecord.get("comment");

        } catch (Exception e) {
            throw new CsvParseException("CSV record is not valid, please check the format");
        }

        WSSIMInstance instanceData = findCorrespondingInstanceData(phase, instanceId);
        return new WSSIMJudgement(instanceData, annotator, label, comment);
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

    private void validateAddWSSIMJudgementCommand(final WSSIMInstance instance,
            final AddWSSIMJudgementCommand command) {

        if (!(instance.getLabelSet().contains(command.getLabel())
                || instance.getNonLabel().equals(command.getLabel()))) {
            throw new IllegalArgumentException("Not in label set, therefore not valid judgement");
        }
    }

}

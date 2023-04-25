package de.garrafao.phitag.application.judgement.usepairjudgement;

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
import de.garrafao.phitag.domain.judgement.usepairjudgement.page.UsePairJudgementPageBuilder;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.AddUsePairJudgementCommand;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.DeleteUsePairJudgementCommand;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.EditUsePairJudgementCommand;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.authentication.error.AccessDenidedException;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.instance.usepairinstance.UsePairInstance;
import de.garrafao.phitag.domain.instance.usepairinstance.UsePairInstanceRepository;
import de.garrafao.phitag.domain.instance.usepairinstance.query.UsePairInstanceQueryBuilder;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgement;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgementRepository;
import de.garrafao.phitag.domain.judgement.usepairjudgement.error.UsePairJudgementException;
import de.garrafao.phitag.domain.judgement.usepairjudgement.error.UsePairJudgementNotFoundException;
import de.garrafao.phitag.domain.judgement.usepairjudgement.query.UsePairJudgementQueryBuilder;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phase.error.TutorialException;

@Service
public class UsePairJudgementApplicationService {

    private final UsePairJudgementRepository usePairJudgementRepository;

    private final UsePairInstanceRepository usePairInstanceRepository;

    // Statistics

    private final UserStatisticApplicationService userStatisticApplicationService;

    private final AnnotatorStatisticApplicationService annotatorStatisticApplicationService;

    private final PhaseStatisticApplicationService phaseStatisticApplicationService;

    @Autowired
    public UsePairJudgementApplicationService(
            final UsePairJudgementRepository usePairJudgementRepository,
            final UsePairInstanceRepository usePairInstanceRepository,

            final UserStatisticApplicationService userStatisticApplicationService,
            final AnnotatorStatisticApplicationService annotatorStatisticApplicationService,
            final PhaseStatisticApplicationService phaseStatisticApplicationService) {
        this.usePairJudgementRepository = usePairJudgementRepository;
        this.usePairInstanceRepository = usePairInstanceRepository;

        this.userStatisticApplicationService = userStatisticApplicationService;
        this.annotatorStatisticApplicationService = annotatorStatisticApplicationService;
        this.phaseStatisticApplicationService = phaseStatisticApplicationService;
    }

    // Getter

    /**
     * Get all use pair judgements for a given phase.
     * 
     * @param phase the phase
     * @return a {@link UsePairJudgement} list
     */
    public List<UsePairJudgement> findByPhase(final Phase phase) {
        final Query query = new UsePairJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.usePairJudgementRepository.findByQuery(query);
    }

    /**
     * Get all use pair judgements for a given phase as a paged list.
     * 
     * @param phase
     * @param pagesize
     * @param pagenumber
     * @param orderBy
     * @return
     */
    public Page<UsePairJudgement> findByPhase(
            final Phase phase,
            final int pagesize,
            final int pagenumber,
            final String orderBy) {
        final Query query = new UsePairJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.usePairJudgementRepository.findByQueryPaged(query, new UsePairJudgementPageBuilder()
                .withPageSize(pagesize)
                .withPageNumber(pagenumber)
                .withOrderBy(orderBy)
                .build());
    }

    /**
     * Get all use pair judgements for a given annotator.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @return a {@link UsePairJudgement} list
     */
    public List<UsePairJudgement> getHistory(final Phase phase, final Annotator annotator) {
        final Query query = new UsePairJudgementQueryBuilder().withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName()).withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername()).build();
        return this.usePairJudgementRepository.findByQuery(query);
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
    public Page<UsePairJudgement> getHistory(
            final Phase phase,
            final Annotator annotator,
            final int pagesize,
            final int pagenumber,
            final String orderBy) {
        final Query query = new UsePairJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername())
                .build();
        return this.usePairJudgementRepository.findByQueryPaged(query, new UsePairJudgementPageBuilder()
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
    public InputStreamResource exportUsePairJudgement(final Phase phase) {
        List<UsePairJudgement> resultData = this.findByPhase(phase);
        String[] csvHeader = {
                "instanceID", "label", "comment", "annotator"
        };
        List<List<String>> csvData = parseUsePairJudgementsToCsvBody(resultData);

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
        return this.usePairJudgementRepository.findByQuery(
                new UsePairJudgementQueryBuilder()
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
        return this.usePairJudgementRepository.findByQuery(
                new UsePairJudgementQueryBuilder()
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
            final UsePairJudgement resultData = parseRecordToUsePairJudgement(phase, annotator, csvrecord);
            this.usePairJudgementRepository.save(resultData);
        });

    }

    /**
     * Edit a use pair judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void edit(final Phase phase, final Annotator annotator, final EditUsePairJudgementCommand command) {
        final Query query = new UsePairJudgementQueryBuilder()
                .withOwner(command.getOwner())
                .withProject(command.getProject())
                .withPhase(command.getPhase())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();
        final List<UsePairJudgement> usePairJudgements = this.usePairJudgementRepository.findByQuery(query);
        if (usePairJudgements.size() != 1) {
            throw new UsePairJudgementNotFoundException();

        }
        final UsePairJudgement judgement = usePairJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        if (!command.getLabel().isBlank()) {
            if (!(judgement.getUsePairInstance().getLabelSet().contains(command.getLabel())
                    || judgement.getUsePairInstance().getNonLabel().equals(command.getLabel()))) {
                throw new UsePairJudgementException("Label not found");
            }
            judgement.setLabel(command.getLabel());
        }

        judgement.setComment(command.getComment());
        this.usePairJudgementRepository.save(judgement);
    }

    /**
     * Delete a use pair judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void delete(final Phase phase, final Annotator annotator, final DeleteUsePairJudgementCommand command) {
        final Query query = new UsePairJudgementQueryBuilder()
                .withOwner(command.getOwner())
                .withProject(command.getProject())
                .withPhase(command.getPhase())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();
        final List<UsePairJudgement> usePairJudgements = this.usePairJudgementRepository.findByQuery(query);
        if (usePairJudgements.size() != 1) {
            throw new UsePairJudgementNotFoundException();

        }
        final UsePairJudgement judgement = usePairJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        this.usePairJudgementRepository.delete(judgement);
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
    public void annotate(final Phase phase, final Annotator annotator, final AddUsePairJudgementCommand command) {
        String instanceId = command.getInstance();

        final UsePairInstance instance = this.findCorrespondingInstanceData(phase, instanceId);
        validateAddUsePairJudgementCommand(instance, command);

        final UsePairJudgement resultData = new UsePairJudgement(instance, annotator, command.getLabel(),
                command.getComment());
        this.usePairJudgementRepository.save(resultData);

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
            final List<AddUsePairJudgementCommand> commands) {
        if (phase.isTutorial()) {
            tutorialAnnotationCorrectness(phase, annotator, commands);
            return;
        }

        commands.forEach(command -> {
            this.annotate(phase, annotator, command);
        });
    }

    // Helper

    private UsePairInstance findCorrespondingInstanceData(final Phase phase, final String instanceid) {
        final Query query = new UsePairInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(instanceid).build();
        List<UsePairInstance> resultData = this.usePairInstanceRepository.findByQuery(query);
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
            List<AddUsePairJudgementCommand> commands) {
        final Query judgementQuery = new UsePairJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName()).build();

        final List<UsePairJudgement> golds = this.usePairJudgementRepository.findByQuery(judgementQuery);

        if (commands.size() != golds.size()) {
            throw new TutorialException(
                    "Tutorial not completed. Please check your judgements. Not all instances were judged");
        }

        commands.forEach(command -> {
            final UsePairJudgement gold = golds.stream()
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

    private List<List<String>> parseUsePairJudgementsToCsvBody(List<UsePairJudgement> usePairJudgements) {
        List<List<String>> csvBody = new ArrayList<>();

        for (UsePairJudgement data : usePairJudgements) {
            List<String> csvRow = new ArrayList<>();

            csvRow.add(data.getInstance().getId().getInstanceid());
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

    private UsePairJudgement parseRecordToUsePairJudgement(final Phase phase, final Annotator annotator,
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

        UsePairInstance instanceData = findCorrespondingInstanceData(phase, instanceId);
        return new UsePairJudgement(instanceData, annotator, label, comment);
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

    private void validateAddUsePairJudgementCommand(final UsePairInstance instance,
            final AddUsePairJudgementCommand command) {

        if (!(instance.getLabelSet().contains(command.getLabel())
                || instance.getNonLabel().equals(command.getLabel()))) {
            throw new IllegalArgumentException("Not in label set, therefore not valid judgement");
        }
    }

}

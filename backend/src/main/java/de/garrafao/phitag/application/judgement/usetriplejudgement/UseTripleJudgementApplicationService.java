package de.garrafao.phitag.application.judgement.usetriplejudgement;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
import de.garrafao.phitag.domain.judgement.usetriplejudgement.page.UseTripleJudgementPageBuilder;
import de.garrafao.phitag.application.common.CommonMathService;
import de.garrafao.phitag.application.judgement.usetriplejudgement.data.AddUseTripleJudgementCommand;
import de.garrafao.phitag.application.judgement.usetriplejudgement.data.DeleteUseTripleJudgementCommand;
import de.garrafao.phitag.application.judgement.usetriplejudgement.data.EditUseTripleJudgementCommand;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.authentication.error.AccessDenidedException;
import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;
import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstanceRepository;
import de.garrafao.phitag.domain.instance.usetripleinstance.query.UseTripleInstanceQueryBuilder;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgement;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgementRepository;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.error.UseTripleJudgementException;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.error.UseTripleJudgementNotFoundException;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.query.UseTripleJudgementQueryBuilder;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phase.error.TutorialException;
import de.garrafao.phitag.domain.statistic.statisticannotationmeasure.StatisticAnnotationMeasureEnum;
import de.garrafao.phitag.domain.statistic.tutorialannotationmeasurehistory.TutorialAnnotationMeasureHistory;
import de.garrafao.phitag.domain.statistic.tutorialannotationmeasurehistory.TutorialAnnotationMeasureHistoryRepository;

@Service
public class UseTripleJudgementApplicationService {

    private final UseTripleJudgementRepository useTripleJudgementRepository;

    private final UseTripleInstanceRepository useTripleInstanceRepository;

    private final TutorialAnnotationMeasureHistoryRepository tutorialAnnotationMeasureHistoryRepository;

    // Statistics

    private final UserStatisticApplicationService userStatisticApplicationService;

    private final AnnotatorStatisticApplicationService annotatorStatisticApplicationService;

    private final PhaseStatisticApplicationService phaseStatisticApplicationService;

    private final CommonMathService commonMathService;

    @Autowired
    public UseTripleJudgementApplicationService(
            final UseTripleJudgementRepository useTripleJudgementRepository,
            final UseTripleInstanceRepository useTripleInstanceRepository,
            final TutorialAnnotationMeasureHistoryRepository tutorialAnnotationMeasureHistoryRepository,

            final UserStatisticApplicationService userStatisticApplicationService,
            final AnnotatorStatisticApplicationService annotatorStatisticApplicationService,
            final PhaseStatisticApplicationService phaseStatisticApplicationService,
            final CommonMathService commonMathService) {
        this.useTripleJudgementRepository = useTripleJudgementRepository;
        this.useTripleInstanceRepository = useTripleInstanceRepository;
        this.tutorialAnnotationMeasureHistoryRepository = tutorialAnnotationMeasureHistoryRepository;

        this.userStatisticApplicationService = userStatisticApplicationService;
        this.annotatorStatisticApplicationService = annotatorStatisticApplicationService;
        this.phaseStatisticApplicationService = phaseStatisticApplicationService;
        this.commonMathService = commonMathService;
    }

    // Getter

    /**
     * Get all use triple judgements for a given phase.
     * 
     * @param phase the phase
     * @return a {@link UseTripleJudgement} list
     */
    public List<UseTripleJudgement> findByPhase(final Phase phase) {
        final Query query = new UseTripleJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.useTripleJudgementRepository.findByQuery(query);
    }

    /**
     * Get all use triple judgements for a given phase as a paged list.
     * 
     * @param phase
     * @param pagesize
     * @param pagenumber
     * @param orderBy
     * @return
     */
    public Page<UseTripleJudgement> findByPhase(
            final Phase phase,
            final int pagesize,
            final int pagenumber,
            final String orderBy) {
        final Query query = new UseTripleJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.useTripleJudgementRepository.findByQueryPaged(query, new UseTripleJudgementPageBuilder()
                .withPageSize(pagesize)
                .withPageNumber(pagenumber)
                .withOrderBy(orderBy)
                .build());
    }

    /**
     * Get all use triple judgements for a given annotator.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @return a {@link UseTripleJudgement} list
     */
    public List<UseTripleJudgement> getHistory(final Phase phase, final Annotator annotator) {
        final Query query = new UseTripleJudgementQueryBuilder().withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName()).withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername()).build();
        return this.useTripleJudgementRepository.findByQuery(query);
    }

    /**
     * Get all use triple judgements for a given annotator as a paged list.
     * 
     * @param phase
     * @param annotator
     * @param pagesize
     * @param pagenumber
     * @param orderBy
     * @return
     */
    public Page<UseTripleJudgement> getHistory(
            final Phase phase,
            final Annotator annotator,
            final int pagesize,
            final int pagenumber,
            final String orderBy) {
        final Query query = new UseTripleJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername())
                .build();
        return this.useTripleJudgementRepository.findByQueryPaged(query, new UseTripleJudgementPageBuilder()
                .withPageSize(pagesize)
                .withPageNumber(pagenumber)
                .withOrderBy(orderBy)
                .build());
    }

    /**
     * Export all use triple judgements for a given phase.
     * 
     * @param phase the phase
     * @return a CSV file as {@link InputStreamResource}
     */
    public InputStreamResource exportUseTripleJudgement(final Phase phase) {
        List<UseTripleJudgement> resultData = this.findByPhase(phase);
        String[] csvHeader = {
                "instanceID", "label", "comment", "annotator"
        };
        List<List<String>> csvData = parseUseTripleJudgementsToCsvBody(resultData);

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
     * Count all use triple judgements for a given annotator.
     * 
     * @param annotator the annotator
     * @return the number of use triple judgements
     */
    public int countJudgements(Annotator annotator) {
        return (int) this.useTripleJudgementRepository.findByQueryPaged(
                new UseTripleJudgementQueryBuilder()
                        .withAnnotator(annotator.getId().getUsername())
                        .build(),
                new PageRequestWraper(1, 0)).getTotalElements();
    }

    /**
     * Count all use triple judgements for a given annotator and phase.
     * 
     * @param annotator the annotator
     * @param phase     the phase
     * @return the number of use triple judgements
     */
    public int countJudgements(Annotator annotator, Phase phase) {
        return (int) this.useTripleJudgementRepository.findByQueryPaged(
                new UseTripleJudgementQueryBuilder()
                        .withAnnotator(annotator.getId().getUsername())
                        .withPhase(phase.getId().getName()).build(),
                new PageRequestWraper(1, 0)).getTotalElements();
    }

    // Setter Files

    /**
     * Import use triple judgements from a CSV file.
     * 
     * @param file  the CSV file
     * @param phase the phase
     */
    @Transactional
    public void save(final Phase phase, final Annotator annotator, final MultipartFile file) {
        validateCsvFile(file);

        parseCsvFile(file).forEach(csvrecord -> {
            final UseTripleJudgement resultData = parseRecordToUseTripleJudgement(phase, annotator, csvrecord);
            this.useTripleJudgementRepository.save(resultData);
        });

    }

    /**
     * Edit a use triple judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void edit(final Phase phase, final Annotator annotator, final EditUseTripleJudgementCommand command) {
        final Query query = new UseTripleJudgementQueryBuilder()
                .withOwner(command.getOwner())
                .withProject(command.getProject())
                .withPhase(command.getPhase())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();
        final List<UseTripleJudgement> useTripleJudgements = this.useTripleJudgementRepository.findByQuery(query);
        if (useTripleJudgements.size() != 1) {
            throw new UseTripleJudgementNotFoundException();

        }
        final UseTripleJudgement judgement = useTripleJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        if (!command.getLabel().isBlank()) {
            if (!(judgement.getUseTripleInstance().getLabelSet().contains(command.getLabel())
                    || judgement.getUseTripleInstance().getNonLabel().equals(command.getLabel()))) {
                throw new UseTripleJudgementException("Label not found");
            }
            judgement.setLabel(command.getLabel());
        }

        judgement.setComment(command.getComment());
        this.useTripleJudgementRepository.save(judgement);
    }

    /**
     * Delete a use triple judgement.
     * 
     * @param phase     the phase
     * @param annotator the annotator
     * @param command   the command
     */
    @Transactional
    public void delete(final Phase phase, final Annotator annotator, final DeleteUseTripleJudgementCommand command) {
        final Query query = new UseTripleJudgementQueryBuilder()
                .withOwner(command.getOwner())
                .withProject(command.getProject())
                .withPhase(command.getPhase())
                .withAnnotator(command.getAnnotator())
                .withInstanceid(command.getInstance())
                .withUUID(command.getUUID())
                .build();
        final List<UseTripleJudgement> useTripleJudgements = this.useTripleJudgementRepository.findByQuery(query);
        if (useTripleJudgements.size() != 1) {
            throw new UseTripleJudgementNotFoundException();

        }
        final UseTripleJudgement judgement = useTripleJudgements.get(0);

        if (!annotator.equals(judgement.getAnnotator())) {
            throw new AccessDenidedException();
        }

        this.useTripleJudgementRepository.delete(judgement);
    }

    // Setter Command

    /**
     * Add a use triple judgement.
     * 
     * @param phase     the phase to which the use triple judgement belongs
     * @param annotator the annotator who created the use triple judgement
     * @param command   the command
     */
    @Transactional
    public void annotate(final Phase phase, final Annotator annotator, final AddUseTripleJudgementCommand command) {
        String instanceId = command.getInstance();

        final UseTripleInstance instance = this.findCorrespondingInstanceData(phase, instanceId);
        validateAddUseTripleJudgementCommand(instance, command);

        final UseTripleJudgement resultData = new UseTripleJudgement(instance, annotator, command.getLabel(),
                command.getComment());
        this.useTripleJudgementRepository.save(resultData);

        // Update annotation count for the project
        this.userStatisticApplicationService.incrementAnnotationCountProject(phase.getProject());
        this.annotatorStatisticApplicationService.updateAnnotationStatistic(annotator, phase);
        this.phaseStatisticApplicationService.updatePhaseStatisticForAnnotation(annotator, phase);
    }

    /**
     * Add bulk use triple judgement for a given phase.
     * If the phase is a tutorial phase, the judgements are checked against the
     * tutorial judgements and if they are correct, the tutorial phase is marked as
     * completed.
     * 
     * @param phase     the phase to which the use triple judgement belongs
     * @param annotator the annotator who created the use triple judgement
     * @param commands  the commands
     */
    @Transactional
    public void annotateBulk(final Phase phase, final Annotator annotator,
            final List<AddUseTripleJudgementCommand> commands) {
        if (phase.isTutorial()) {
            tutorialAnnotationCorrectness(phase, annotator, commands);
            return;
        }

        commands.forEach(command -> {
            this.annotate(phase, annotator, command);
        });
    }

    // Helper

    private UseTripleInstance findCorrespondingInstanceData(final Phase phase, final String instanceid) {
        final Query query = new UseTripleInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withInstanceid(instanceid).build();
        List<UseTripleInstance> resultData = this.useTripleInstanceRepository.findByQuery(query);
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
     * @param phase     the phase to which the use triple judgement belongs
     * @param annotator the annotator who created the use triple judgement
     * @param commands  the commands containing the judgements
     */
    @Transactional
    private void tutorialAnnotationCorrectness(final Phase phase, final Annotator annotator,
            List<AddUseTripleJudgementCommand> commands) {
        final Query judgementQuery = new UseTripleJudgementQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName()).build();

        final List<UseTripleJudgement> golds = this.useTripleJudgementRepository.findByQuery(judgementQuery);

        if (commands.size() != golds.size()) {
            throw new TutorialException(
                    "Tutorial not completed. Please check your judgements. Not all instances were judged");
        }

        // Create two lists of the labels of the gold and the annotator
        List<String> goldLabels = golds.stream().map(UseTripleJudgement::getLabel).toList();
        List<String> annotatorLabels = commands.stream().map(AddUseTripleJudgementCommand::getLabel).toList();

        List<List<String>> annotatorLabelList = Arrays.asList(goldLabels, annotatorLabels);
        List<String> categories = golds.get(0).getInstance().getLabelSet();

        // Check if the tutorial phase has a statistic annotation measure 
        if (phase.getStatisticAnnotationMeasure() == null || phase.getStatisticAnnotationMeasureThreshold() == null
                || StatisticAnnotationMeasureEnum.fromId(phase.getStatisticAnnotationMeasure().getId()) == null) {
            throw new TutorialException(
                    "This tutorial is not valid anymore. Please contact the project owner for a new tutorial.");
        }

        // Calculate the annotator agreement
        double agreement = this.commonMathService.calculateAnnotatorAgreement(categories,
                StatisticAnnotationMeasureEnum.fromId(phase.getStatisticAnnotationMeasure().getId()),
                annotatorLabelList);

        // Finally, mark the tutorial phase as completed for the annotator
        // As the object annotator is managed by the persistence context, we do not need
        // to
        // call the annotator repository and can simply update the object, HOPEFULLY?!?
        if (agreement >= phase.getStatisticAnnotationMeasureThreshold()) {
            annotator.addCompletedTutorial(phase);
        }

        tutorialAnnotationMeasureHistoryRepository.save(
                new TutorialAnnotationMeasureHistory(
                        phase,
                        annotator,
                        agreement,
                        agreement >= phase.getStatisticAnnotationMeasureThreshold()));
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

    private List<List<String>> parseUseTripleJudgementsToCsvBody(List<UseTripleJudgement> useTripleJudgements) {
        List<List<String>> csvBody = new ArrayList<>();

        for (UseTripleJudgement data : useTripleJudgements) {
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

    private UseTripleJudgement parseRecordToUseTripleJudgement(final Phase phase, final Annotator annotator,
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

        UseTripleInstance instanceData = findCorrespondingInstanceData(phase, instanceId);
        return new UseTripleJudgement(instanceData, annotator, label, comment);
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

    private void validateAddUseTripleJudgementCommand(final UseTripleInstance instance,
            final AddUseTripleJudgementCommand command) {

        if (!(instance.getLabelSet().contains(command.getLabel())
                || instance.getNonLabel().equals(command.getLabel()))) {
            throw new IllegalArgumentException("Not in label set, therefore not valid judgement");
        }
    }

}

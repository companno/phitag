package de.garrafao.phitag.application.phitagdata.usage;

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

import de.garrafao.phitag.domain.phitagdata.usage.page.UsagePageBuilder;
import de.garrafao.phitag.application.phitagdata.usage.data.EditUsageCommand;
import de.garrafao.phitag.application.phitagdata.usage.data.UsageHeaderEnum;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.error.CsvParseException;
import de.garrafao.phitag.domain.phitagdata.usage.Usage;
import de.garrafao.phitag.domain.phitagdata.usage.UsageRepository;
import de.garrafao.phitag.domain.phitagdata.usage.error.UsageException;
import de.garrafao.phitag.domain.phitagdata.usage.error.UsageExistsException;
import de.garrafao.phitag.domain.phitagdata.usage.error.UsageNotFoundException;
import de.garrafao.phitag.domain.phitagdata.usage.query.UsageQueryBuilder;
import de.garrafao.phitag.domain.project.Project;

/**
 * Service for usage.
 * Note that this service assumes that access to the project is already checked.
 * Only the validity of the usage is checked.
 */
@Service
public class UsageApplicationService {

    private final UsageRepository usageRepository;

    @Autowired
    public UsageApplicationService(final UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    /**
     * Get all usages for a given project.
     * 
     * @param project the project
     * @return
     */
    public List<Usage> findByProject(final Project project) {
        final Query query = new UsageQueryBuilder().withOwner(project.getId().getOwnername())
                .withProject(project.getId().getName()).build();
        return this.usageRepository.findByQuery(query);
    }

    /**
     * Get all usages for a given project.
     * 
     * @param project the project
     * @return
     */
    public Page<Usage> findByProject(final Project project, final Integer pagesize, final Integer pagenumber,
            final String orderBy) {
        final Query query = new UsageQueryBuilder().withOwner(project.getId().getOwnername())
                .withProject(project.getId().getName()).build();
        return this.usageRepository.findByQueryPaged(query,
                new UsagePageBuilder()
                        .withPageNumber(pagenumber)
                        .withPageSize(pagesize)
                        .withOrderBy(orderBy)
                        .build());
    }

    /**
     * Export Usage data as CSV.
     * 
     * @param project the project
     * @return the CSV data as InputStreamResource
     */
    public InputStreamResource exportUsage(final Project project) {
        List<Usage> usagesOfProject = this.findByProject(project);
        String[] csvHeader = {
                "dataID", "context", "indices_target_token", "indices_target_sentence", "lemma", "group",
        };
        List<List<String>> csvData = parseUsagesToCsvBody(usagesOfProject);

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
     * Import Usage data from CSV.
     * 
     * @param project the project
     * @param file    the CSV file
     */
    @Transactional
    public void save(final Project project, final MultipartFile file) {
        this.validateCsvFile(file);

        this.parseCsvFile(file).forEach(csvrecord -> {
            final Usage phaseData = this.parseRecordToUsage(project, csvrecord);
            validateUsage(phaseData);

            this.usageRepository.save(phaseData);
        });
    }

    /**
     * Edit a usage.
     * 
     * @param project the project
     * @param command the command
     */
    @Transactional
    public void edit(final Project project, final EditUsageCommand command) {
        final Usage usage = this.usageRepository.findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(
                command.getDataid(), command.getProject(), command.getOwner()).orElseThrow(UsageNotFoundException::new);

        if (command.getContext() != null && !command.getContext().isBlank()
                && !command.getContext().equals(usage.getContext())) {
            usage.setContext(command.getContext());
        }

        if (command.getIndexTargetSentence() != null && !command.getIndexTargetSentence().isBlank()
                && !command.getIndexTargetSentence().equals(usage.getIndexTargetSentence())) {
            usage.setIndexTargetSentence(command.getIndexTargetSentence());

            try {
                usage.getIndicesTargetSentence().forEach(index -> {
                    if (index.getLeft() > index.getRight()) {
                        throw new UsageException("Invalid index, left index is greater than right index.");
                    }
                });
            } catch (Exception e) {
                throw new UsageException("Invalid indexTargetSentence");
            }
        }

        if (command.getIndexTargetToken() != null && !command.getIndexTargetToken().isBlank()
                && !command.getIndexTargetToken().equals(usage.getIndexTargetToken())) {
            usage.setIndexTargetToken(command.getIndexTargetToken());

            try {
                usage.getIndicesTargetToken().forEach(index -> {
                    if (index.getLeft() > index.getRight()) {
                        throw new UsageException("Invalid index, left index is greater than right index.");
                    }
                });
            } catch (Exception e) {
                throw new UsageException("Invalid indexTargetToken");
            }
        }

        if (command.getLemma() != null && !command.getLemma().isBlank()
                && !command.getLemma().equals(usage.getLemma())) {
            usage.setLemma(command.getLemma());
        }

        if (command.getGroup() != null && !command.getGroup().isBlank()
                && !command.getGroup().equals(usage.getGroup())) {
            usage.setGroup(command.getGroup());
        }

        this.usageRepository.save(usage);
    }

    // Parser

    private CSVPrinter createCsvPrinter(String[] csvHeader, ByteArrayOutputStream outputStream) {
        CSVPrinter printer = null;

        try {
            PrintWriter writer = new PrintWriter(outputStream);
            CSVFormat format = CSVFormat.Builder.create().setHeader(csvHeader).setAllowMissingColumnNames(true)
                    .setDelimiter("\t").build();
            printer = new CSVPrinter(writer, format);
        } catch (Exception e) {
            throw new CsvParseException();
        }

        return printer;
    }

    private List<List<String>> parseUsagesToCsvBody(List<Usage> usages) {
        List<List<String>> csvBody = new ArrayList<>();

        for (Usage usage : usages) {
            List<String> csvRow = new ArrayList<>();
            csvRow.add(usage.getId().getDataid());
            csvRow.add(usage.getContext());
            csvRow.add(usage.getIndexTargetToken());
            csvRow.add(usage.getIndexTargetSentence());
            csvRow.add(usage.getLemma());
            csvRow.add(usage.getGroup());
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

    private Usage parseRecordToUsage(Project project, CSVRecord csvRecord) {
        try {
            String externalId = csvRecord.get(UsageHeaderEnum.dataID);
            String context = csvRecord.get(UsageHeaderEnum.context);
            String indexTargetToken = csvRecord.get(UsageHeaderEnum.indices_target_token);
            String indexTargetSentence = csvRecord.get(UsageHeaderEnum.indices_target_sentence);
            String lemma = csvRecord.get(UsageHeaderEnum.lemma);

            String group = "";

            if (csvRecord.isSet(UsageHeaderEnum.group.name())) {
                group = csvRecord.get(UsageHeaderEnum.group);
            }

            return new Usage(project, externalId, context, indexTargetToken, indexTargetSentence, lemma, group);
        } catch (Exception e) {
            throw new CsvParseException("CSV record is not valid, please check the format");
        }
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

    private void validateUsage(final Usage data) {
        if (this.usageRepository
                .findByIdDataidAndIdProjectidNameAndIdProjectidOwnername(data.getId().getDataid(),
                        data.getId().getProjectid().getName(), data.getId().getProjectid().getOwnername())
                .isPresent()) {
            throw new UsageExistsException();
        }

        try {
            data.getIndicesTargetSentence().forEach(index -> {
                if (index.getLeft() > index.getRight()) {
                    throw new CsvParseException("Invalid index, left index is greater than right index.");
                }
            });
        } catch (Exception e) {
            throw new CsvParseException(
                    "indices_target_sentence is not valid for dataID: " + data.getId().getDataid() + "");
        }

        try {
            data.getIndicesTargetToken().forEach(index -> {
                if (index.getLeft() > index.getRight()) {
                    throw new CsvParseException("Invalid index, left index is greater than right index.");
                }
            });
        } catch (Exception e) {
            throw new CsvParseException(
                    "indices_target_token is not valid for dataID: " + data.getId().getDataid() + "");
        }
    }

}
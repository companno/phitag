package de.garrafao.phitag.application.corpus.data.dto;

import de.garrafao.phitag.domain.corpusinformation.CorpusInformation;
import lombok.Getter;

@Getter
public class CorpusInformationDto {

    private final String id;

    private final String title;
    private final String author;
    private final Integer date;
    private final String language;
    private final String resource;

    public CorpusInformationDto(String id, String title, String author, Integer date, String language,
            String resource) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.language = language;
        this.resource = resource;
    }

    public static CorpusInformationDto from(CorpusInformation corpusInformation) {
        return new CorpusInformationDto(
                corpusInformation.getId(),
                corpusInformation.getTitle(),
                corpusInformation.getAuthor(),
                corpusInformation.getDate(),
                corpusInformation.getLanguage(),
                corpusInformation.getResource());
    }
}

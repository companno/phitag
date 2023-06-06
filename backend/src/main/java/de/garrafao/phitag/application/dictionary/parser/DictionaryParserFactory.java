package de.garrafao.phitag.application.dictionary.parser;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.garrafao.phitag.application.dictionary.dictionary.data.DictionaryFileType;
import de.garrafao.phitag.application.dictionary.parser.customxml.CustomXMLParser;

@Service
public class DictionaryParserFactory {

    private CustomXMLParser customXMLParser;

    // Wanted to make it static, but this is a workaround for my inability
    @Autowired
    public DictionaryParserFactory(final CustomXMLParser customXMLParser) {
        this.customXMLParser = customXMLParser;
    }

    public IDictionaryParser getParser(final DictionaryFileType fileType) {
        switch (fileType) {
            case CUSTOM_XML:
                return customXMLParser;
            default:
                throw new IllegalArgumentException("Unknown file type: " + fileType);
        }
    }

}

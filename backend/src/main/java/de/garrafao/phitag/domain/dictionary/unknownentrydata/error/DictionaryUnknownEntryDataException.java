package de.garrafao.phitag.domain.dictionary.unknownentrydata.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class DictionaryUnknownEntryDataException extends CustomRuntimeException {

    public DictionaryUnknownEntryDataException(final String message) {
        super(message);
    }

}

package de.garrafao.phitag.domain.dictionary.entry.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class DictionaryEntryException extends CustomRuntimeException {

    private static final long serialVersionUID = 1L;

    public DictionaryEntryException(final String message) {
        super(message);
    }

}

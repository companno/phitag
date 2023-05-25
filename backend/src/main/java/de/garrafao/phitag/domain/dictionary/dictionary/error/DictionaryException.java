package de.garrafao.phitag.domain.dictionary.dictionary.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class DictionaryException extends CustomRuntimeException {

    private static final long serialVersionUID = 1L;

    public DictionaryException(final String message) {
        super(message);
    }

}
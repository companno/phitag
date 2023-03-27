package de.garrafao.phitag.domain.authentication.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class AccessDenidedException extends CustomRuntimeException {

    public AccessDenidedException() {
        super("Access denided to resource");
    }

    public AccessDenidedException(String message) {
        super("Access denided to resource: " + message);
    }

}

package de.garrafao.phitag.domain.instance.usetripleinstance.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class UseTripleInstanceAlreadyExistsException extends CustomRuntimeException {

    public UseTripleInstanceAlreadyExistsException() {
        super("Instance already exists");
    }

    public UseTripleInstanceAlreadyExistsException(final String message) {
        super("Instance already exists: " + message);
    }
    
}

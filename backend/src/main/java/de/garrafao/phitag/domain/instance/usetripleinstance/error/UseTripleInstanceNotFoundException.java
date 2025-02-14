package de.garrafao.phitag.domain.instance.usetripleinstance.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class UseTripleInstanceNotFoundException extends CustomRuntimeException {

    public UseTripleInstanceNotFoundException() {
        super("Instance not found");
    }

}

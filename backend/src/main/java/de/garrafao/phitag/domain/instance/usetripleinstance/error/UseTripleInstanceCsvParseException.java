package de.garrafao.phitag.domain.instance.usetripleinstance.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class UseTripleInstanceCsvParseException extends CustomRuntimeException {
    
    public UseTripleInstanceCsvParseException() {
        super("CSV File for Use Triple Instance is not correctly formatted");
    }
}

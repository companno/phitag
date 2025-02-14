package de.garrafao.phitag.domain.judgement.usetriplejudgement.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class UseTripleJudgementCsvParseException extends CustomRuntimeException {
    
    public UseTripleJudgementCsvParseException() {
        super("CSV File for Use Triple Judgements is not correctly formatted");
    }
}

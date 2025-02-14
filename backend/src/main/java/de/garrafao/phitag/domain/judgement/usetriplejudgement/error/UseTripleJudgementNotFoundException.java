package de.garrafao.phitag.domain.judgement.usetriplejudgement.error;

import de.garrafao.phitag.domain.error.CustomRuntimeException;

public class UseTripleJudgementNotFoundException extends CustomRuntimeException {

    public UseTripleJudgementNotFoundException() {
        super("Judgement not found");
    }

}

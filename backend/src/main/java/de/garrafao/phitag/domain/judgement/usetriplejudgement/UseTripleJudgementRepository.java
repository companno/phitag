package de.garrafao.phitag.domain.judgement.usetriplejudgement;

import java.util.List;

import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;

public interface UseTripleJudgementRepository {

    List<UseTripleJudgement> findByQuery(final Query query);

    Page<UseTripleJudgement> findByQueryPaged(final Query query, final PageRequestWraper page);

    UseTripleJudgement save(UseTripleJudgement judgement);

    void delete(UseTripleJudgement judgement);

}

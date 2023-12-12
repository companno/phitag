package de.garrafao.phitag.domain.judgement.usepairjudgement;

import java.util.List;

import org.springframework.data.domain.Page;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;

public interface UsePairJudgementRepository {

    List<UsePairJudgement> findByQuery(final Query query);

    Page<UsePairJudgement> findByQueryPaged(final Query query, final PageRequestWraper page);

    UsePairJudgement save(UsePairJudgement judgement);

    void delete(UsePairJudgement judgement);

    void batchDelete(Iterable<UsePairJudgement> judgements);

}

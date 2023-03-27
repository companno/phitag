package de.garrafao.phitag.infrastructure.persistance.jpa.judgement.usepairjudgement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgement;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgementRepository;
import de.garrafao.phitag.infrastructure.persistance.jpa.judgement.usepairjudgement.query.UsePairJudgementQueryJpa;

@Repository
public class UsePairJudgementRepositoryBridge implements UsePairJudgementRepository {

    private final UsePairJudgementRepositoryJpa judgementRepository;

    @Autowired
    public UsePairJudgementRepositoryBridge(UsePairJudgementRepositoryJpa resultDataRepositoryJpa) {
        this.judgementRepository = resultDataRepositoryJpa;
    }

    @Override
    public List<UsePairJudgement> findByQuery(Query query) {
        return judgementRepository.findAll(new UsePairJudgementQueryJpa(query));
    }

    @Override
    public Page<UsePairJudgement> findByQueryPaged(Query query, PageRequestWraper page) {
        return judgementRepository.findAll(new UsePairJudgementQueryJpa(query), page.getPageRequest());
    }

    @Override
    public UsePairJudgement save(UsePairJudgement judgement) {
        return judgementRepository.save(judgement);
    }

    @Override
    public void delete(UsePairJudgement judgement) {
        judgementRepository.delete(judgement);
    }

}

package de.garrafao.phitag.infrastructure.persistence.jpa.judgement.usetriplejudgement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgement;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgementRepository;
import de.garrafao.phitag.infrastructure.persistence.jpa.judgement.usetriplejudgement.query.UseTripleJudgementQueryJpa;

@Repository
public class UseTripleJudgementRepositoryBridge implements UseTripleJudgementRepository {

    private final UseTripleJudgementRepositoryJpa judgementRepository;

    @Autowired
    public UseTripleJudgementRepositoryBridge(UseTripleJudgementRepositoryJpa resultDataRepositoryJpa) {
        this.judgementRepository = resultDataRepositoryJpa;
    }

    @Override
    public List<UseTripleJudgement> findByQuery(Query query) {
        return judgementRepository.findAll(new UseTripleJudgementQueryJpa(query));
    }

    @Override
    public Page<UseTripleJudgement> findByQueryPaged(Query query, PageRequestWraper page) {
        return judgementRepository.findAll(new UseTripleJudgementQueryJpa(query), page.getPageRequest());
    }

    @Override
    public UseTripleJudgement save(UseTripleJudgement judgement) {
        return judgementRepository.save(judgement);
    }

    @Override
    public void delete(UseTripleJudgement judgement) {
        judgementRepository.delete(judgement);
    }

}

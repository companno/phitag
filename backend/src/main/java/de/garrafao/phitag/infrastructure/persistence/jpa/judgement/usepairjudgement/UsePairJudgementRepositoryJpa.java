package de.garrafao.phitag.infrastructure.persistence.jpa.judgement.usepairjudgement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgement;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgementId;

public interface UsePairJudgementRepositoryJpa
        extends JpaRepository<UsePairJudgement, UsePairJudgementId>, JpaSpecificationExecutor<UsePairJudgement> {

}

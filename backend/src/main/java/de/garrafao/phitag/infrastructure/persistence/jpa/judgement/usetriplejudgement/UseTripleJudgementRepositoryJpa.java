package de.garrafao.phitag.infrastructure.persistence.jpa.judgement.usetriplejudgement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgement;
import de.garrafao.phitag.domain.judgement.usetriplejudgement.UseTripleJudgementId;

public interface UseTripleJudgementRepositoryJpa
        extends JpaRepository<UseTripleJudgement, UseTripleJudgementId>, JpaSpecificationExecutor<UseTripleJudgement> {

}

package de.garrafao.phitag.infrastructure.persistence.jpa.tutorialannotationmeasurehistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.garrafao.phitag.domain.phase.PhaseId;
import de.garrafao.phitag.domain.statistic.tutorialannotationmeasurehistory.TutorialAnnotationMeasureHistory;
import de.garrafao.phitag.domain.statistic.tutorialannotationmeasurehistory.TutorialAnnotationMeasureHistoryId;

public interface TutorialAnnotationMeasureHistoryRepositoryJpa
        extends JpaRepository<TutorialAnnotationMeasureHistory, TutorialAnnotationMeasureHistoryId> {

    List<TutorialAnnotationMeasureHistory> findByIdPhaseid(final PhaseId phaseId);
}

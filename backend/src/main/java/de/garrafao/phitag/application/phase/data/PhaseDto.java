package de.garrafao.phitag.application.phase.data;

import java.util.ArrayList;
import java.util.List;

import de.garrafao.phitag.application.annotationtype.data.AnnotationTypeDto;
import de.garrafao.phitag.application.sampling.data.SamplingDto;
import de.garrafao.phitag.domain.helper.Pair;
import de.garrafao.phitag.domain.phase.Phase;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class PhaseDto {

    private final PhaseIdDto id;

    private final String displayname;

    private final Boolean tutorial;
    private final AnnotationTypeDto annotationType;
    private final SamplingDto sampling;

    private final String description;

    private final String status;

    private final List<Pair<String, Boolean>> tutorialrequirements;

    private PhaseDto(
            final PhaseIdDto id,
            final String displayname,
            final Boolean tutorial,
            final AnnotationTypeDto annotationType,
            final SamplingDto sampling,
            final String description,
            final String status,
            final List<Pair<String, Boolean>> tutorialrequirements) {
        this.id = id;

        this.displayname = displayname;

        this.tutorial = tutorial;
        this.annotationType = annotationType;
        this.sampling = sampling;

        this.description = description;

        this.status = status;

        this.tutorialrequirements = tutorialrequirements;
    }

    public static PhaseDto from(@NonNull Phase phase) {
        return new PhaseDto(
                PhaseIdDto.from(phase.getId()),
                phase.getDisplayname(),
                phase.isTutorial(),
                AnnotationTypeDto.from(phase.getAnnotationType()),
                SamplingDto.from(phase.getSampling()),
                phase.getDescription(),
                phase.getStatus(),
                new ArrayList<>());
    }

    public static PhaseDto from(@NonNull final Phase phase, final List<Pair<String, Boolean>> tutorial) {
        return new PhaseDto(
                PhaseIdDto.from(phase.getId()),
                phase.getDisplayname(),
                phase.isTutorial(),
                AnnotationTypeDto.from(phase.getAnnotationType()),
                SamplingDto.from(phase.getSampling()),
                phase.getDescription(),
                phase.getStatus(),
                tutorial);
    }

}

package de.garrafao.phitag.computationalannotator.usepair.service;

import de.garrafao.phitag.application.annotationtype.data.AnnotationTypeEnum;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.instance.data.IInstanceDto;
import de.garrafao.phitag.application.instance.usepairinstance.UsePairInstanceApplicationService;
import de.garrafao.phitag.application.judgement.usepairjudgement.UsePairJudgementApplicationService;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.AddUsePairJudgementCommand;
import de.garrafao.phitag.application.phase.data.TutorialHistoryDto;
import de.garrafao.phitag.computationalannotator.common.command.ComputationalAnnotatorCommand;
import de.garrafao.phitag.computationalannotator.common.function.CommonFunction;
import de.garrafao.phitag.computationalannotator.common.model.application.data.OpenAPIResponseDto;
import de.garrafao.phitag.computationalannotator.usepair.data.UsePairComputationalAnnotatotInstanceDto;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.statistic.tutorialannotationmeasurehistory.TutorialAnnotationMeasureHistoryRepository;
import de.garrafao.phitag.domain.user.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsePairComputationalAnnotatorService {

    private final UsePairInstanceApplicationService usePairInstanceApplicationService;

    private final UsePairJudgementApplicationService usePairJudgementApplicationService;

    private final UsePairOpenAIService usePairOpenAIService;

    private final CommonFunction commonFunction;


    private final TutorialAnnotationMeasureHistoryRepository tutorialAnnotationMeasureHistoryRepository;

    private final CommonService commonService;

    public UsePairComputationalAnnotatorService(UsePairInstanceApplicationService usePairInstanceApplicationService,
                                                UsePairJudgementApplicationService usePairJudgementApplicationService,
                                                UsePairOpenAIService usePairOpenAIService,
                                                CommonFunction commonFunction, TutorialAnnotationMeasureHistoryRepository tutorialAnnotationMeasureHistoryRepository, CommonService commonService) {
        this.usePairInstanceApplicationService = usePairInstanceApplicationService;
        this.usePairJudgementApplicationService = usePairJudgementApplicationService;
        this.usePairOpenAIService = usePairOpenAIService;
        this.commonFunction = commonFunction;
        this.tutorialAnnotationMeasureHistoryRepository = tutorialAnnotationMeasureHistoryRepository;
        this.commonService = commonService;
    }

    @Transactional
    public void usePairChatGptAnnotation(final String authenticationToken, final ComputationalAnnotatorCommand command) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(command.getOwner(), command.getProject(), command.getPhase());

        List<IInstanceDto> instanceDtos = new ArrayList<>();

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairInstanceApplicationService.findByPhase(phaseEntity)
                    .forEach(usePairInstance -> instanceDtos.add(UsePairComputationalAnnotatotInstanceDto.from(usePairInstance)));
        }


        Annotator annotator = this.commonService.getAnnotator(command.getOwner(), command.getProject(), "ChatGpt");

        for (IInstanceDto instanceDto : instanceDtos) {
            if (instanceDto instanceof UsePairComputationalAnnotatotInstanceDto) {
                UsePairComputationalAnnotatotInstanceDto usePairDto = (UsePairComputationalAnnotatotInstanceDto) instanceDto;

                // Access and use data from UsePairComputationalAnnotatotInstanceDto
                String firstUsage = usePairDto.getFirstusage().getContext();
                String secondUsage = usePairDto.getSecondusage().getContext();
                String lemma = usePairDto.getSecondusage().getLemma();

                final OpenAPIResponseDto response = this.usePairOpenAIService.chat(
                        command.getApiKey(),
                        command.getModel(),
                        command.getPrompt(),
                        firstUsage,
                        secondUsage,
                        lemma

                );


                AddUsePairJudgementCommand usePairJudgementCommand = new AddUsePairJudgementCommand(
                        command.getOwner(), command.getProject(),
                        command.getPhase(), usePairDto.getId().getInstanceId(),
                        response.getJudgement(),
                        ""
                );
                this.usePairJudgementApplicationService.annotate(phaseEntity, annotator, usePairJudgementCommand);

            }
        }

    }

    @Transactional
    public List<TutorialHistoryDto> usePairChatGptTutorial(final String authenticationToken, final ComputationalAnnotatorCommand command) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(command.getOwner(), command.getProject(), command.getPhase());

        List<TutorialHistoryDto> tutorialHistoryDtos = new ArrayList<>();

        for (Phase phase : phaseEntity.getTutorialRequirements()) {
            tutorialHistoryDtos.add(this.annotate(phase, command));
        }

        return tutorialHistoryDtos;
    }

    public TutorialHistoryDto annotate(final Phase phase,
                                             final ComputationalAnnotatorCommand command) {
        List<IInstanceDto> instanceDtos = new ArrayList<>();

        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairInstanceApplicationService.findByPhase(phase)
                    .forEach(usePairInstance -> instanceDtos
                            .add(UsePairComputationalAnnotatotInstanceDto.from(usePairInstance)));
        }

        Annotator annotator = this.commonService.getAnnotator(command.getOwner(),
                command.getProject(),
                "ChatGpt");
        List<AddUsePairJudgementCommand> usePairJudgementCommands = new ArrayList<>();

        for (IInstanceDto instanceDto : instanceDtos) {
            if (instanceDto instanceof UsePairComputationalAnnotatotInstanceDto) {
                UsePairComputationalAnnotatotInstanceDto usePairDto = (UsePairComputationalAnnotatotInstanceDto) instanceDto;

                String firstUsage = usePairDto.getFirstusage().getContext();
                String secondUsage = usePairDto.getSecondusage().getContext();
                String lemma = usePairDto.getSecondusage().getLemma();


                final OpenAPIResponseDto response = this.usePairOpenAIService.chat(
                        command.getApiKey(),
                        command.getModel(),
                        command.getPrompt(),
                        firstUsage,
                        secondUsage,
                        lemma
                );
                AddUsePairJudgementCommand usePairJudgementCommand = new AddUsePairJudgementCommand(
                        command.getOwner(),
                        command.getProject(),
                        command.getPhase(),
                        usePairDto.getId().getInstanceId(),
                        response.getJudgement(),
                        ""
                );

                usePairJudgementCommands.add(usePairJudgementCommand);
            }
        }
        this.usePairJudgementApplicationService.annotateBulk(phase, annotator, usePairJudgementCommands);

        final List<TutorialHistoryDto> tutorialHistoryDtos = this.getTutorialMeasureHistory(phase);
       return this.getLatestTutorialHistory(tutorialHistoryDtos);
    }

    private List<TutorialHistoryDto> getTutorialMeasureHistory(final Phase phase) {

      return this.tutorialAnnotationMeasureHistoryRepository.findByIdPhaseid(phase.getId()).stream()
                .map(TutorialHistoryDto::from).toList();
    }

        private  TutorialHistoryDto getLatestTutorialHistory(List<TutorialHistoryDto> tutorialHistoryDtos) {
            TutorialHistoryDto latestTutorialHistory = null;

            for (TutorialHistoryDto tutorialHistoryDto : tutorialHistoryDtos) {
                if (latestTutorialHistory == null || tutorialHistoryDto.getTimestamp().compareTo(latestTutorialHistory.getTimestamp()) > 0) {
                    latestTutorialHistory = tutorialHistoryDto;
                }
            }

            return latestTutorialHistory;
        }

}

package de.garrafao.phitag.application.judgement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.garrafao.phitag.application.entitlement.data.EntitlementEnum;
import de.garrafao.phitag.application.annotationtype.data.AnnotationTypeEnum;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.judgement.data.IAddJudgementCommand;
import de.garrafao.phitag.application.judgement.data.IDeleteJudgementCommand;
import de.garrafao.phitag.application.judgement.data.IEditJudgementCommand;
import de.garrafao.phitag.application.judgement.data.IJudgementDto;
import de.garrafao.phitag.application.judgement.data.PagedJudgementDto;
import de.garrafao.phitag.application.judgement.lexsubjudgement.LexSubJudgementApplicationService;
import de.garrafao.phitag.application.judgement.lexsubjudgement.data.AddLexSubJudgementCommand;
import de.garrafao.phitag.application.judgement.lexsubjudgement.data.DeleteLexSubJudgementCommand;
import de.garrafao.phitag.application.judgement.lexsubjudgement.data.EditLexSubJudgementCommand;
import de.garrafao.phitag.application.judgement.lexsubjudgement.data.LexJudgementDto;
import de.garrafao.phitag.application.judgement.usepairjudgement.UsePairJudgementApplicationService;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.AddUsePairJudgementCommand;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.DeleteUsePairJudgementCommand;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.EditUsePairJudgementCommand;
import de.garrafao.phitag.application.judgement.usepairjudgement.data.UsePairJudgementDto;
import de.garrafao.phitag.application.judgement.wssimjudgement.WSSIMJudgementApplicationService;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.AddWSSIMJudgementCommand;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.DeleteWSSIMJudgementCommand;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.EditWSSIMJudgementCommand;
import de.garrafao.phitag.application.judgement.wssimjudgement.data.WSSIMJudgementDto;
import de.garrafao.phitag.application.validation.ValidationService;
import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformation;
import de.garrafao.phitag.domain.annotationtype.error.AnnotationTypeNotFoundException;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.authentication.error.AccessDenidedException;
import de.garrafao.phitag.domain.judgement.lexsubjudgement.LexSubJudgement;
import de.garrafao.phitag.domain.judgement.usepairjudgement.UsePairJudgement;
import de.garrafao.phitag.domain.judgement.wssimjudgement.WSSIMJudgement;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.user.User;

@Service
public class JudgementApplicationService {

    // Repository

    // Services

    private final CommonService commonService;

    private final ValidationService validationService;

    private final UsePairJudgementApplicationService usePairJudgementApplicationService;

    private final WSSIMJudgementApplicationService wssimJudgementApplicationService;

    private final LexSubJudgementApplicationService lexSubJudgementApplicationService;

    // Other

    @Autowired
    public JudgementApplicationService(
            final CommonService commonService,
            final ValidationService validationService,

            final UsePairJudgementApplicationService usePairJudgementApplicationService,
            final WSSIMJudgementApplicationService wssimJudgementApplicationService,
            final LexSubJudgementApplicationService lexSubJudgementApplicationService) {
        this.commonService = commonService;
        this.validationService = validationService;

        this.usePairJudgementApplicationService = usePairJudgementApplicationService;
        this.wssimJudgementApplicationService = wssimJudgementApplicationService;
        this.lexSubJudgementApplicationService = lexSubJudgementApplicationService;
    }

    // Getters

    /**
     * Get all judgements for a given phase.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @return a list of all {@IJudgementDto} for the given phase
     */
    public List<IJudgementDto> getJudgementDto(final String authenticationToken, final String owner,
            final String project, final String phase) {

        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAccess(requester, phaseEntity.getProject());

        if (phaseEntity.isTutorial()) {
            this.validationService.projectAdminAccess(requester, phaseEntity.getProject());
        }

        // IF requester is only annotator, only return judgements of the requester
        Annotator annotator = this.commonService.getAnnotator(phaseEntity.getId().getProjectid().getOwnername(),
                phaseEntity.getId().getProjectid().getName(), requester.getUsername());
        if (annotator.getEntitlement().getName().equals(EntitlementEnum.ENTITLEMENT_USER.name())) {
            return this.getHistory(authenticationToken, owner, project, phase);
        }

        List<IJudgementDto> judgementDtos = new ArrayList<>();

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairJudgementApplicationService.findByPhase(phaseEntity).forEach(
                    usePairJudgement -> {
                        judgementDtos.add(UsePairJudgementDto.from(usePairJudgement));
                    });
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            this.wssimJudgementApplicationService.findByPhase(phaseEntity).forEach(
                    wssimJudgement -> {
                        judgementDtos.add(WSSIMJudgementDto.from(wssimJudgement));
                    });
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubJudgementApplicationService.findByPhase(phaseEntity).forEach(
                    lexSubJudgement -> {
                        judgementDtos.add(LexJudgementDto.from(lexSubJudgement));
                    });
        }

        return judgementDtos;
    }

    /**
     * Get all judgements for a given annotator as a page.
     * 
     * @param authenticationToken
     * @param owner
     * @param project
     * @param phase
     * 
     * @param page
     * @param size
     * @param sort
     * @return
     */
    public PagedJudgementDto getPagedJudgementDto(
            final String authenticationToken,
            final String owner,
            final String project,
            final String phase,
            final int page,
            final int size,
            final String sort) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAccess(requester, phaseEntity.getProject());

        if (phaseEntity.isTutorial()) {
            this.validationService.projectAdminAccess(requester, phaseEntity.getProject());
        }

        // IF requester is only annotator, only return judgements of the requester
        Annotator annotator = this.commonService.getAnnotator(phaseEntity.getId().getProjectid().getOwnername(),
                phaseEntity.getId().getProjectid().getName(), requester.getUsername());
        if (annotator.getEntitlement().getName().equals(EntitlementEnum.ENTITLEMENT_USER.name())) {
            return this.getPagedHistory(authenticationToken, owner, project, phase, page, size, sort);
        }

        final PagedJudgementDto pagedJudgementDto;

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            Page<UsePairJudgement> usePairJudgements = this.usePairJudgementApplicationService.findByPhase(phaseEntity,
                    size, page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    usePairJudgements.getContent().stream().map(UsePairJudgementDto::from).collect(Collectors.toList()),
                    usePairJudgements.getNumber(),
                    usePairJudgements.getSize(),
                    usePairJudgements.getTotalElements(),
                    usePairJudgements.getTotalPages());

        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            Page<WSSIMJudgement> wssimJudgements = this.wssimJudgementApplicationService.findByPhase(phaseEntity, size,
                    page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    wssimJudgements.getContent().stream().map(WSSIMJudgementDto::from).collect(Collectors.toList()),
                    wssimJudgements.getNumber(),
                    wssimJudgements.getSize(),
                    wssimJudgements.getTotalElements(),
                    wssimJudgements.getTotalPages());
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            Page<LexSubJudgement> lexSubJudgements = this.lexSubJudgementApplicationService.findByPhase(phaseEntity,
                    size, page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    lexSubJudgements.getContent().stream().map(LexJudgementDto::from).collect(Collectors.toList()),
                    lexSubJudgements.getNumber(),
                    lexSubJudgements.getSize(),
                    lexSubJudgements.getTotalElements(),
                    lexSubJudgements.getTotalPages());
        } else {
            pagedJudgementDto = null;
        }

        return pagedJudgementDto;
    }

    /**
     * Get all judgements for a given annotator.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @return a list of all {@IJudgementDto} for the given annotator
     */
    public List<IJudgementDto> getHistory(final String authenticationToken, final String owner,
            final String project, final String phase) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAnnotatorAccess(requester, phaseEntity.getProject());

        if (phaseEntity.isTutorial()) {
            this.validationService.projectAdminAccess(requester, phaseEntity.getProject());
        }

        final Annotator annotator = this.commonService.getAnnotator(owner, project, requester.getUsername());

        List<IJudgementDto> judgementDtos = new ArrayList<>();

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairJudgementApplicationService.getHistory(phaseEntity, annotator)
                    .forEach(usePairJudgement -> judgementDtos.add(UsePairJudgementDto.from(usePairJudgement)));
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            this.wssimJudgementApplicationService.getHistory(phaseEntity, annotator)
                    .forEach(wssimJudgement -> judgementDtos.add(WSSIMJudgementDto.from(wssimJudgement)));
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubJudgementApplicationService.findByPhaseAndAnnotator(phaseEntity, annotator)
                    .forEach(lexSubJudgement -> judgementDtos.add(LexJudgementDto.from(lexSubJudgement)));
        }

        return judgementDtos;
    }

    /**
     * Get all judgements for a given annotator as a page.
     * 
     * @param authenticationToken
     * @param owner
     * @param project
     * @param phase
     * @param page
     * @param size
     * @param sort
     * @return
     */
    public PagedJudgementDto getPagedHistory(
            final String authenticationToken,
            final String owner,
            final String project,
            final String phase,
            final int page,
            final int size,
            final String sort) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAnnotatorAccess(requester, phaseEntity.getProject());

        if (phaseEntity.isTutorial()) {
            this.validationService.projectAdminAccess(requester, phaseEntity.getProject());
        }

        final Annotator annotator = this.commonService.getAnnotator(owner, project, requester.getUsername());

        final PagedJudgementDto pagedJudgementDto;

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            Page<UsePairJudgement> usePairJudgements = this.usePairJudgementApplicationService.getHistory(phaseEntity,
                    annotator, size, page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    usePairJudgements.getContent().stream().map(UsePairJudgementDto::from).collect(Collectors.toList()),
                    usePairJudgements.getNumber(),
                    usePairJudgements.getSize(),
                    usePairJudgements.getTotalElements(),
                    usePairJudgements.getTotalPages());

        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            Page<WSSIMJudgement> wssimJudgements = this.wssimJudgementApplicationService.getHistory(phaseEntity,
                    annotator, size, page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    wssimJudgements.getContent().stream().map(WSSIMJudgementDto::from).collect(Collectors.toList()),
                    wssimJudgements.getNumber(),
                    wssimJudgements.getSize(),
                    wssimJudgements.getTotalElements(),
                    wssimJudgements.getTotalPages());
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            Page<LexSubJudgement> lexSubJudgements = this.lexSubJudgementApplicationService.findByPhaseAndAnnotator(
                    phaseEntity, annotator, size, page, sort);

            pagedJudgementDto = new PagedJudgementDto(
                    lexSubJudgements.getContent().stream().map(LexJudgementDto::from).collect(Collectors.toList()),
                    lexSubJudgements.getNumber(),
                    lexSubJudgements.getSize(),
                    lexSubJudgements.getTotalElements(),
                    lexSubJudgements.getTotalPages());
        } else {
            pagedJudgementDto = null;
        }

        return pagedJudgementDto;
    }

    /**
     * Export all judgements for a given phase.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @return a {@link InputStreamResource} CSV file containing all judgements for
     *         the given phase
     */
    public InputStreamResource exportJudgement(final String authenticationToken, final String owner,
            final String project, final String phase) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAdminOrBot(requester, phaseEntity.getProject());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.usePairJudgementApplicationService.exportUsePairJudgement(phaseEntity);
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            return this.wssimJudgementApplicationService.exportWSSIMJudgement(phaseEntity);
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.lexSubJudgementApplicationService.exportJudgement(phaseEntity);
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Count all judgements for a given annotator.
     * 
     * @param annotator the annotator
     * @return the number of judgements
     */
    public int countJudgements(Annotator annotator) {
        return this.usePairJudgementApplicationService.countJudgements(annotator)
                + this.wssimJudgementApplicationService.countJudgements(annotator);
    }

    /**
     * Count all judgements for a given phase and annotator.
     * 
     * @param annotator the annotator
     * @param phase     the phase
     * @return the number of judgements
     */
    public int countJudgements(Annotator annotator, Phase phase) {
        return this.usePairJudgementApplicationService.countJudgements(annotator, phase)
                + this.wssimJudgementApplicationService.countJudgements(annotator, phase);
    }

    // Setters

    /**
     * Add judgement for a given phase from a CSV file.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @param file                the CSV file containing the judgements
     */
    @Transactional
    public void addJudgements(final String authenticationToken, final String owner,
            final String project, final String phase, final MultipartFile file) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAdminAccess(requester, phaseEntity.getProject());

        final Annotator annotator = this.commonService.getAnnotator(owner, project, requester.getUsername());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairJudgementApplicationService.save(phaseEntity, annotator, file);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            this.wssimJudgementApplicationService.save(phaseEntity, annotator, file);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubJudgementApplicationService.save(phaseEntity, annotator, file);
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Edit a judgement.
     * 
     * @param authenticationToken
     * @param command
     */
    @Transactional
    public void edit(final String authenticationToken, final IEditJudgementCommand command) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(command.getOwner(), command.getProject(),
                command.getPhase());

        this.validationService.projectAnnotatorAccess(requester, phaseEntity.getProject());

        final Annotator annotator = this.commonService.getAnnotator(command.getOwner(), command.getProject(),
                requester.getUsername());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairJudgementApplicationService.edit(phaseEntity, annotator, (EditUsePairJudgementCommand) command);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            this.wssimJudgementApplicationService.edit(phaseEntity, annotator, (EditWSSIMJudgementCommand) command);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubJudgementApplicationService.edit(phaseEntity, annotator, (EditLexSubJudgementCommand) command);
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Delete a judgement.
     * 
     * @param authenticationToken
     * @param command
     */
    @Transactional
    public void delete(final String authenticationToken, final IDeleteJudgementCommand command) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(command.getOwner(), command.getProject(),
                command.getPhase());

        this.validationService.projectAnnotatorAccess(requester, phaseEntity.getProject());

        final Annotator annotator = this.commonService.getAnnotator(command.getOwner(), command.getProject(),
                requester.getUsername());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairJudgementApplicationService.delete(phaseEntity, annotator,
                    (DeleteUsePairJudgementCommand) command);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            this.wssimJudgementApplicationService.delete(phaseEntity, annotator, (DeleteWSSIMJudgementCommand) command);
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubJudgementApplicationService.delete(phaseEntity, annotator, (DeleteLexSubJudgementCommand) command);
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Add judgement for a given phase.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param command             the command containing the judgement
     */
    @Transactional
    public void annotate(final String authenticationToken, final IAddJudgementCommand command) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(command.getOwner(), command.getProject(),
                command.getPhase());

        this.validationService.phaseAnnotationAccess(requester, phaseEntity);

        if (phaseEntity.isTutorial()) {
            throw new AccessDenidedException(
                    "Tutorial phases are not single-annotatable. Use the bulk-annotation instead.");
        }

        final Annotator annotator = this.commonService.getAnnotator(command.getOwner(), command.getProject(),
                requester.getUsername());
        final AnnotationProcessInformation annotationProcessInformation = this.commonService
                .getAnnotationProcessInformation(annotator, phaseEntity);

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            try {
                this.usePairJudgementApplicationService.annotate(phaseEntity, annotator,
                        (AddUsePairJudgementCommand) command);
                annotationProcessInformation.setIndex(annotationProcessInformation.getIndex() + 1);

            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            try {
                this.wssimJudgementApplicationService.annotate(phaseEntity, annotator,
                        (AddWSSIMJudgementCommand) command);
                annotationProcessInformation.setIndex(annotationProcessInformation.getIndex() + 1);

            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            try {
                this.lexSubJudgementApplicationService.annotate(phaseEntity, annotator,
                        (AddLexSubJudgementCommand) command);
                annotationProcessInformation.setIndex(annotationProcessInformation.getIndex() + 1);

            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Add bulk judgement for a given phase.
     * If the phase is a tutorial phase, the judgements are checked against the
     * tutorial judgements and if they are correct, the tutorial phase is marked as
     * completed.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param commands            list of commands containing the judgement
     */
    @Transactional
    public void annotateBulk(final String authauthenticationToken, final List<IAddJudgementCommand> commands) {
        if (commands.isEmpty()) {
            return;
        }

        final User requester = this.commonService.getUserByAuthenticationToken(authauthenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(commands.get(0).getOwner(), commands.get(0).getProject(),
                commands.get(0).getPhase());

        this.validationService.phaseAnnotationAccess(requester, phaseEntity);

        final Annotator annotator = this.commonService.getAnnotator(commands.get(0).getOwner(),
                commands.get(0).getProject(), requester.getUsername());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            try {
                this.usePairJudgementApplicationService.annotateBulk(phaseEntity, annotator,
                        commands.stream().map(AddUsePairJudgementCommand.class::cast).collect(Collectors.toList()));
            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            try {
                this.wssimJudgementApplicationService.annotateBulk(phaseEntity, annotator,
                        commands.stream().map(AddWSSIMJudgementCommand.class::cast).collect(Collectors.toList()));
            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            try {
                this.lexSubJudgementApplicationService.annotateBulk(phaseEntity, annotator,
                        commands.stream().map(AddLexSubJudgementCommand.class::cast).collect(Collectors.toList()));
            } catch (ClassCastException e) {
                throw new AnnotationTypeNotFoundException();
            }
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

}

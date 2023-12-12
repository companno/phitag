package de.garrafao.phitag.application.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import de.garrafao.phitag.application.instance.userankinstance.UseRankInstanceApplicationService;
import de.garrafao.phitag.application.instance.userankinstance.data.UseRankInstanceDto;
import de.garrafao.phitag.domain.instance.userankinstance.UseRankInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.garrafao.phitag.application.annotationtype.data.AnnotationTypeEnum;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.instance.data.IInstanceDto;
import de.garrafao.phitag.application.instance.data.PagedInstanceDto;
import de.garrafao.phitag.application.instance.lexsubinstance.LexSubInstanceApplicationService;
import de.garrafao.phitag.application.instance.lexsubinstance.data.LexSubInstanceDto;
import de.garrafao.phitag.application.instance.usepairinstance.UsePairInstanceApplicationService;
import de.garrafao.phitag.application.instance.usepairinstance.data.UsePairInstanceDto;
import de.garrafao.phitag.application.instance.wssiminstance.WSSIMInstanceApplicationService;
import de.garrafao.phitag.application.instance.wssiminstance.data.WSSIMInstanceDto;
import de.garrafao.phitag.application.instance.wssimtag.WSSIMTagApplicationService;
import de.garrafao.phitag.application.instance.wssimtag.data.WSSIMTagDto;
import de.garrafao.phitag.application.validation.ValidationService;
import de.garrafao.phitag.domain.annotationtype.error.AnnotationTypeNotFoundException;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.instance.lexsub.LexSubInstance;
import de.garrafao.phitag.domain.instance.usepairinstance.UsePairInstance;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstance;
import de.garrafao.phitag.domain.instance.wssimtag.WSSIMTag;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.user.User;

@Service
public class InstanceApplicationService {

    // Repository

    // Services

    private final CommonService commonService;

    private final ValidationService validationService;

    private final UsePairInstanceApplicationService usePairInstanceApplicationService;

    private final UseRankInstanceApplicationService useRankInstanceApplicationService;

    private final WSSIMInstanceApplicationService wssimInstanceApplicationService;

    private final WSSIMTagApplicationService wssimTagApplicationService;

    private final LexSubInstanceApplicationService lexSubInstanceApplicationService;

    // Other

    @Autowired
    public InstanceApplicationService(
            final CommonService commonService,
            final ValidationService validationService,

            final UsePairInstanceApplicationService usePairInstanceApplicationService,
            final UseRankInstanceApplicationService useRankInstanceApplicationService,
            final WSSIMInstanceApplicationService wssimInstanceApplicationService,
            final WSSIMTagApplicationService wssimTagApplicationService,
            final LexSubInstanceApplicationService lexSubInstanceApplicationService) {
        this.commonService = commonService;
        this.validationService = validationService;

        this.usePairInstanceApplicationService = usePairInstanceApplicationService;
        this.useRankInstanceApplicationService = useRankInstanceApplicationService;
        this.wssimInstanceApplicationService = wssimInstanceApplicationService;
        this.wssimTagApplicationService = wssimTagApplicationService;
        this.lexSubInstanceApplicationService = lexSubInstanceApplicationService;
    }

    // Getters
    /**
     * Get all instances for a given phase.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @param additional          whether to send additional information (e.g. tags
     *                            for WSSIM)
     * @return a list of all {@IInstanceDto} for the given phase
     */
    public List<IInstanceDto> getInstanceDtos(final String authenticationToken, final String owner,
            final String project, final String phase, final boolean additional) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAccess(requester, phaseEntity.getProject());

        List<IInstanceDto> instanceDtos = new ArrayList<>();

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairInstanceApplicationService.findByPhase(phaseEntity)
                    .forEach(usePairInstance -> instanceDtos.add(UsePairInstanceDto.from(usePairInstance)));
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            this.useRankInstanceApplicationService.findByPhase(phaseEntity)
                    .forEach(useRankInstance -> instanceDtos.add(UseRankInstanceDto.from(useRankInstance)));
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (additional) {
                this.wssimTagApplicationService.findByPhase(phaseEntity)
                        .forEach(wssimTag -> instanceDtos.add(WSSIMTagDto.from(wssimTag)));
            } else {
                this.wssimInstanceApplicationService.findByPhase(phaseEntity)
                        .forEach(wssimInstance -> instanceDtos.add(WSSIMInstanceDto.from(wssimInstance)));
            }
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubInstanceApplicationService.findByPhase(phaseEntity)
                    .forEach(lexSubInstance -> instanceDtos.add(LexSubInstanceDto.from(lexSubInstance)));
        }

        return instanceDtos;
    }


    /**
     * Get all instances for a given phase as page
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @param additional          whether to send additional information (e.g. tags
     *                            for WSSIM)
     * @return a page of all {@IInstanceDto} for the given phase
     */
    public PagedInstanceDto getPagedInstanceDto(
            final String authenticationToken,
            final String owner, final String project, final String phase, final boolean additional,
            final int page, final int size, final String order) {

        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAccess(requester, phaseEntity.getProject());

        final PagedInstanceDto pagedInstanceDto;

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            Page<UsePairInstance> pagedInstance = this.usePairInstanceApplicationService.findByPhasePaged(phaseEntity,
                    size, page, order);
            pagedInstanceDto = new PagedInstanceDto(
                    pagedInstance.getContent().stream().map(UsePairInstanceDto::from).collect(Collectors.toList()),
                    pagedInstance.getNumber(),
                    pagedInstance.getSize(),
                    pagedInstance.getTotalElements(),
                    pagedInstance.getTotalPages());

        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (additional) {
                Page<WSSIMTag> pagedInstance = this.wssimTagApplicationService.findByPhasePaged(phaseEntity, size, page,
                        order);

                pagedInstanceDto = new PagedInstanceDto(
                        pagedInstance.getContent().stream().map(WSSIMTagDto::from).collect(Collectors.toList()),
                        pagedInstance.getNumber(),
                        pagedInstance.getSize(),
                        pagedInstance.getTotalElements(),
                        pagedInstance.getTotalPages());
            } else {
                Page<WSSIMInstance> pagedInstance = this.wssimInstanceApplicationService.findByPhasePaged(phaseEntity,
                        size, page, order);

                pagedInstanceDto = new PagedInstanceDto(
                        pagedInstance.getContent().stream().map(WSSIMInstanceDto::from).collect(Collectors.toList()),
                        pagedInstance.getNumber(),
                        pagedInstance.getSize(),
                        pagedInstance.getTotalElements(),
                        pagedInstance.getTotalPages());
            }
        } else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            Page<LexSubInstance> pagedInstance = this.lexSubInstanceApplicationService.findByPhasePaged(phaseEntity,
                    size, page, order);

            pagedInstanceDto = new PagedInstanceDto(
                    pagedInstance.getContent().stream().map(LexSubInstanceDto::from).collect(Collectors.toList()),
                    pagedInstance.getNumber(),
                    pagedInstance.getSize(),
                    pagedInstance.getTotalElements(),
                    pagedInstance.getTotalPages());
        }  else if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            Page<UseRankInstance> pagedInstance = this.useRankInstanceApplicationService.findByPhasePaged(phaseEntity,
                    size, page, order);
            pagedInstanceDto = new PagedInstanceDto(
                    pagedInstance.getContent().stream().map(UseRankInstanceDto::from).collect(Collectors.toList()),
                    pagedInstance.getNumber(),
                    pagedInstance.getSize(),
                    pagedInstance.getTotalElements(),
                    pagedInstance.getTotalPages());

        } else {
            pagedInstanceDto = new PagedInstanceDto();
        }

        return pagedInstanceDto;

    }

    /**
     * Get a random instance for a given phase.
     * 
     * Note: This will be extended and probably replaced with a more sophisticated
     * way
     * to get instances. For now, this is just a placeholder. The current
     * implementation
     * just randomly picks an instance. Later, the instance will be chosen based on
     * sampling-strategies and
     * the user's history.
     * 
     * TODO: Stop annotator if sampling index is out of bounds
     * TODO: Recalculate sampling order if instances are added or removed (update
     * imm), or if an annotator is added (wait till actual annotation)
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @return a random {@IInstanceDto} for the given phase
     */
    public IInstanceDto getAnnotationInstance(final String authenticationToken, final String owner,
            final String project, final String phase) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAccess(requester, phaseEntity.getProject());

        final Annotator annotator = this.commonService.getAnnotator(
                phaseEntity.getId().getProjectid().getOwnername(),
                phaseEntity.getId().getProjectid().getName(),
                requester.getUsername());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return UsePairInstanceDto
                    .from(this.usePairInstanceApplicationService.getAnnotationInstance(phaseEntity, annotator));
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            return UseRankInstanceDto
                    .from(this.useRankInstanceApplicationService.getAnnotationInstance(phaseEntity, annotator));
        }


        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            return WSSIMInstanceDto
                    .from(this.wssimInstanceApplicationService.getAnnotationInstance(phaseEntity, annotator));
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return LexSubInstanceDto
                    .from(this.lexSubInstanceApplicationService.getAnnotationInstance(phaseEntity, annotator));
        }

        return null;
    }

    /**
     * Export all instances as a CSV file.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @param additional          whether to send additional information (e.g. tags
     *                            for WSSIM)
     * @return a {@link InputStreamResource} CSV file containing all instances
     */
    public InputStreamResource exportInstance(final String authenticationToken, final String owner,
            final String project, final String phase, final boolean additional) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAdminAccess(requester, phaseEntity.getProject());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.usePairInstanceApplicationService.exportUsePairInstance(phaseEntity);
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            return this.useRankInstanceApplicationService.exportUseRankInstance(phaseEntity);
        }


        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (additional) {
                return this.wssimTagApplicationService.exportWSSIMTag(phaseEntity);
            }
            return this.wssimInstanceApplicationService.exportWSSIMInstance(phaseEntity);
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.lexSubInstanceApplicationService.exportInstance(phaseEntity);
        }

        throw new AnnotationTypeNotFoundException();

    }

    // Setters

    /**
     * Import instances from a CSV file.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @param phase               the phase
     * @param additional          whether to send additional information (e.g. tags
     *                            for WSSIM)
     * @param file                the CSV file
     */
    @Transactional
    public void addInstances(final String authenticationToken, final String owner,
            final String project, final String phase, final boolean additional, final MultipartFile file) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAdminAccess(requester, phaseEntity.getProject());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairInstanceApplicationService.save(phaseEntity, file);
            return;
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            this.useRankInstanceApplicationService.save(phaseEntity, file);
            return;
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (additional) {
                this.wssimTagApplicationService.save(phaseEntity, file);
                return;
            }
            this.wssimInstanceApplicationService.save(phaseEntity, file);
            return;
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubInstanceApplicationService.save(phaseEntity, file);
            return;
        }

        throw new AnnotationTypeNotFoundException();

    }

    /**
     * Generate instance data for a phase from usages associated with the project.
     * 
     * @param authenticationToken
     *                            The authentication token of the requesting user
     * @param owner
     *                            The owner of the project
     * @param project
     *                            The name of the project
     * @param phase
     *                            The name of the phase
     * @param additional
     *                            Additional Data (e.g. WSSIM -> sense)
     * @param file
     *                            The instancedata to add
     */
    @Transactional
    public void generateInstances(
            final String authenticationToken,
            final String owner, final String project, final String phase,
            final List<String> labels, final String nonLabel, 
            final MultipartFile file) {

        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Phase phaseEntity = this.commonService.getPhase(owner, project, phase);

        this.validationService.projectAdminAccess(requester, phaseEntity.getProject());

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            this.usePairInstanceApplicationService.generateInstances(phaseEntity, labels, nonLabel);
            return;
        }
        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USERANK.name())) {
            this.useRankInstanceApplicationService.generateInstances(phaseEntity, labels, nonLabel);
            return;
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (file.isEmpty()) {
                throw new AnnotationTypeNotFoundException();
            }
            this.wssimTagApplicationService.save(phaseEntity, file);
            this.wssimInstanceApplicationService.generateInstances(phaseEntity, labels, nonLabel);
            return;
        }

        if (phaseEntity.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            this.lexSubInstanceApplicationService.generateInstances(phaseEntity);
            return;
        }

        throw new AnnotationTypeNotFoundException();
    }

    /**
     * Get the WSSIM Tags for a specific lemma
     * 
     * @param authenticationToken The authentication token of the requesting user
     * @param owner               The owner of the project
     * @param project             The name of the project
     * @param phase               The name of the phase
     * @param lemma               The lemma in question
     * @return WSSIM Tags with same lemma for this phase
     */
    public List<WSSIMTagDto> getWssimTagsByLemma(String authenticationToken, String owner, String project,
            String phase, String lemma) {
        return this.wssimTagApplicationService
                .findByPhaseAndLemma(this.commonService.getPhase(owner, project, phase), lemma)
                .stream().map(WSSIMTagDto::from).collect(Collectors.toList());
    }

}

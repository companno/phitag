package de.garrafao.phitag.application.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.garrafao.phitag.application.annotationtype.data.AnnotationTypeEnum;
import de.garrafao.phitag.application.authentication.AuthenticationApplicationService;
import de.garrafao.phitag.application.judgement.lexsubjudgement.LexSubJudgementApplicationService;
import de.garrafao.phitag.application.judgement.usepairjudgement.UsePairJudgementApplicationService;
import de.garrafao.phitag.application.judgement.wssimjudgement.WSSIMJudgementApplicationService;
import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformation;
import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformationRepository;
import de.garrafao.phitag.domain.annotationprocessinformation.error.AnnotationProcessInformationException;
import de.garrafao.phitag.domain.annotationprocessinformation.query.AnnotationProcessInformationQueryBuilder;
import de.garrafao.phitag.domain.annotationtype.AnnotationType;
import de.garrafao.phitag.domain.annotationtype.AnnotationTypeRepository;
import de.garrafao.phitag.domain.annotationtype.error.AnnotationTypeNotFoundException;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.annotator.AnnotatorRepository;
import de.garrafao.phitag.domain.annotator.error.AnnotatorNotFoundException;
import de.garrafao.phitag.domain.annotator.query.AnnotatorQueryBuilder;
import de.garrafao.phitag.domain.core.PageRequestWraper;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.dictionary.dictionary.Dictionary;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryId;
import de.garrafao.phitag.domain.dictionary.dictionary.DictionaryRepository;
import de.garrafao.phitag.domain.dictionary.dictionary.error.DictionaryException;
import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntry;
import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntryId;
import de.garrafao.phitag.domain.dictionary.entry.DictionaryEntryRepository;
import de.garrafao.phitag.domain.dictionary.entry.error.DictionaryEntryException;
import de.garrafao.phitag.domain.dictionary.example.DictionaryEntrySenseExample;
import de.garrafao.phitag.domain.dictionary.example.DictionaryEntrySenseExampleId;
import de.garrafao.phitag.domain.dictionary.example.DictionaryEntrySenseExampleRepository;
import de.garrafao.phitag.domain.dictionary.example.error.DictionaryEntrySenseExampleException;
import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySense;
import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySenseId;
import de.garrafao.phitag.domain.dictionary.sense.DictionaryEntrySenseRepository;
import de.garrafao.phitag.domain.dictionary.sense.error.DictionaryEntrySenseException;
import de.garrafao.phitag.domain.entitlement.Entitlement;
import de.garrafao.phitag.domain.entitlement.EntitlementRepository;
import de.garrafao.phitag.domain.entitlement.error.EntitlementNotFoundException;
import de.garrafao.phitag.domain.instance.IInstance;
import de.garrafao.phitag.domain.instance.lexsub.LexSubInstance;
import de.garrafao.phitag.domain.instance.lexsub.LexSubInstanceRepository;
import de.garrafao.phitag.domain.instance.lexsub.query.LexSubInstanceQueryBuilder;
import de.garrafao.phitag.domain.instance.usepairinstance.UsePairInstance;
import de.garrafao.phitag.domain.instance.usepairinstance.UsePairInstanceRepository;
import de.garrafao.phitag.domain.instance.usepairinstance.query.UsePairInstanceQueryBuilder;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstance;
import de.garrafao.phitag.domain.instance.wssiminstance.WSSIMInstanceRepository;
import de.garrafao.phitag.domain.instance.wssiminstance.query.WSSIMInstanceQueryBuilder;
import de.garrafao.phitag.domain.instance.wssimtag.WSSIMTag;
import de.garrafao.phitag.domain.instance.wssimtag.WSSIMTagRepository;
import de.garrafao.phitag.domain.instance.wssimtag.query.WSSIMTagQueryBuilder;
import de.garrafao.phitag.domain.judgement.IJudgement;
import de.garrafao.phitag.domain.language.Language;
import de.garrafao.phitag.domain.language.LanguageRepository;
import de.garrafao.phitag.domain.language.error.LanguageNotFoundException;
import de.garrafao.phitag.domain.notification.Notification;
import de.garrafao.phitag.domain.notification.NotificationRepository;
import de.garrafao.phitag.domain.phase.Phase;
import de.garrafao.phitag.domain.phase.PhaseRepository;
import de.garrafao.phitag.domain.phase.error.PhaseNotFoundException;
import de.garrafao.phitag.domain.project.Project;
import de.garrafao.phitag.domain.project.ProjectRepository;
import de.garrafao.phitag.domain.project.error.ProjectNotExistsException;
import de.garrafao.phitag.domain.role.Role;
import de.garrafao.phitag.domain.role.RoleRepository;
import de.garrafao.phitag.domain.role.error.RoleNotFoundException;
import de.garrafao.phitag.domain.sampling.Sampling;
import de.garrafao.phitag.domain.sampling.SamplingRepository;
import de.garrafao.phitag.domain.sampling.error.SamplingNotFoundException;
import de.garrafao.phitag.domain.status.Status;
import de.garrafao.phitag.domain.status.StatusRepository;
import de.garrafao.phitag.domain.status.error.StatusNotFoundException;
import de.garrafao.phitag.domain.usecase.Usecase;
import de.garrafao.phitag.domain.usecase.UsecaseRepository;
import de.garrafao.phitag.domain.usecase.error.UsecaseException;
import de.garrafao.phitag.domain.user.User;
import de.garrafao.phitag.domain.user.UserRepository;
import de.garrafao.phitag.domain.user.error.UserNotExistsException;
import de.garrafao.phitag.domain.visibility.Visibility;
import de.garrafao.phitag.domain.visibility.VisibilityRepository;
import de.garrafao.phitag.domain.visibility.error.VisibilityNotFoundException;

/**
 * A service that provides common functionality used or commonly implemented by
 * other services.
 */
@Service
public class CommonService {

    // Repository dependencies

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final AnnotatorRepository annotatorRepository;

    private final PhaseRepository phaseRepository;

    private final NotificationRepository notificationRepository;

    // Dictionary repository dependencies

    private final DictionaryRepository dictionaryRepository;

    private final DictionaryEntryRepository dictionaryEntryRepository;

    private final DictionaryEntrySenseRepository dictionaryEntrySenseRepository;

    private final DictionaryEntrySenseExampleRepository dictionaryEntrySenseExampleRepository;

    // "Static" repository dependencies

    private final RoleRepository roleRepository;

    private final VisibilityRepository visibilityRepository;

    private final LanguageRepository languageRepository;

    private final EntitlementRepository entitlementRepository;

    private final AnnotationTypeRepository annotationTypeRepository;

    private final StatusRepository statusRepository;

    private final SamplingRepository samplingRepository;

    private final UsecaseRepository usecaseRepository;

    // Application service dependencies

    // Instance repository

    private final UsePairInstanceRepository usePairInstanceRepository;

    private final WSSIMTagRepository wssimTagRepository;

    private final WSSIMInstanceRepository wssimInstanceRepository;

    private final LexSubInstanceRepository lexSubInstanceRepository;

    // Instance information repository

    private final AnnotationProcessInformationRepository annotationProcessInformationRepository;

    // Judgement application service

    private final UsePairJudgementApplicationService usePairJudgementApplicationService;

    private final WSSIMJudgementApplicationService wssimJudgementApplicationService;

    private final LexSubJudgementApplicationService lexSubJudgementApplicationService;

    // Authentication application service

    private final AuthenticationApplicationService authenticationApplicationService;

    // Constructor
    @Autowired
    public CommonService(
            final UserRepository userRepository,
            final ProjectRepository projectRepository,
            final AnnotatorRepository annotatorRepository,
            final PhaseRepository phaseRepository,
            final NotificationRepository notificationRepository,

            final DictionaryRepository dictionaryRepository,
            final DictionaryEntryRepository dictionaryEntryRepository,
            final DictionaryEntrySenseRepository dictionaryEntrySenseRepository,
            final DictionaryEntrySenseExampleRepository dictionaryEntrySenseExampleRepository,

            final RoleRepository roleRepository,
            final VisibilityRepository visibilityRepository,
            final LanguageRepository languageRepository,
            final EntitlementRepository entitlementRepository,
            final AnnotationTypeRepository annotationTypeRepository,
            final StatusRepository statusRepository,
            final SamplingRepository samplingRepository,
            final UsecaseRepository usecaseRepository,

            final UsePairInstanceRepository usePairInstanceRepository,
            final WSSIMTagRepository wssimTagRepository,
            final WSSIMInstanceRepository wssimInstanceRepository,
            final LexSubInstanceRepository lexSubInstanceRepository,

            final AnnotationProcessInformationRepository annotationProcessInformationRepository,

            final UsePairJudgementApplicationService usePairJudgementApplicationService,
            final WSSIMJudgementApplicationService wssimJudgementApplicationService,
            final LexSubJudgementApplicationService lexSubJudgementApplicationService,

            final AuthenticationApplicationService authenticationApplicationService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.annotatorRepository = annotatorRepository;
        this.phaseRepository = phaseRepository;
        this.notificationRepository = notificationRepository;

        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.dictionaryEntrySenseRepository = dictionaryEntrySenseRepository;
        this.dictionaryEntrySenseExampleRepository = dictionaryEntrySenseExampleRepository;

        this.roleRepository = roleRepository;
        this.visibilityRepository = visibilityRepository;
        this.languageRepository = languageRepository;
        this.entitlementRepository = entitlementRepository;
        this.annotationTypeRepository = annotationTypeRepository;
        this.statusRepository = statusRepository;
        this.samplingRepository = samplingRepository;
        this.usecaseRepository = usecaseRepository;

        this.usePairInstanceRepository = usePairInstanceRepository;
        this.wssimTagRepository = wssimTagRepository;
        this.wssimInstanceRepository = wssimInstanceRepository;
        this.lexSubInstanceRepository = lexSubInstanceRepository;

        this.annotationProcessInformationRepository = annotationProcessInformationRepository;

        this.usePairJudgementApplicationService = usePairJudgementApplicationService;
        this.wssimJudgementApplicationService = wssimJudgementApplicationService;
        this.lexSubJudgementApplicationService = lexSubJudgementApplicationService;

        this.authenticationApplicationService = authenticationApplicationService;
    }

    // Methods

    /**
     * Returns the user object of the user with the given username.
     * 
     * @param name The username of the user.
     * @return {@User}
     */
    public User getUser(final String name) {
        return this.userRepository.findByUsername(name).orElseThrow(UserNotExistsException::new);
    }

    /**
     * Returns the username of the currently logged in user.
     * 
     * @param authenticationToken The authentication token of the currently logged
     *                            in user.
     * @return The username of the currently logged in user.
     */
    public String getUsernameFromAuthenticationToken(final String authenticationToken) {
        return this.authenticationApplicationService.getUsernameFromAuthenticationToken(authenticationToken);
    }

    /**
     * Returns the associated user object of the currently logged in user.
     * 
     * @param authenticationToken The authentication token of the currently logged
     *                            in user.
     * @return User object of the currently logged in user.
     * @throws UserNotExistsException If the user does not exist.
     */
    public User getUserByAuthenticationToken(final String authenticationToken) {
        return this.userRepository
                .findByUsername(this.getUsernameFromAuthenticationToken(authenticationToken))
                .orElseThrow(UserNotExistsException::new);
    }

    /**
     * Returns the project entity with the given id.
     * 
     * @param owner   The owner name
     * @param project The project name
     * @return The project entity with the given id.
     * @throws ProjectNotExistsException If the project does not exist.
     */
    public Project getProject(final String owner, final String project) {
        return this.projectRepository.findByIdNameAndIdOwnername(project, owner)
                .orElseThrow(ProjectNotExistsException::new);
    }

    /**
     * Returns the phase entity with the given id.
     * 
     * @param owner   The owner name
     * @param project The project name
     * @param phase   The phase name
     * @return The {@Phase} with the given id.
     * @throws PhaseNotFoundException If the phase does not exist.
     */
    public Phase getPhase(final String owner, final String project, final String phase) {
        return this.phaseRepository.findByIdNameAndIdProjectidNameAndIdProjectidOwnername(phase, project, owner)
                .orElseThrow(PhaseNotFoundException::new);
    }

    /**
     * Checks if the user is an annotator of the project.
     * 
     * @param owner    The owner id of the project.
     * @param project  The project id
     * @param username The username of the user.
     * @return True if the user is an annotator of the project, false otherwise.
     */
    public boolean isUserAnnotator(final String owner, final String project, final String username) {
        return this.annotatorRepository
                .findByIdUsernameAndIdProjectidNameAndIdProjectidOwnername(username, project, owner).isPresent();
    }

    /**
     * Return the the annotator entity with the given id.
     * 
     * @param owner    The owner id of the project.
     * @param project  The project id
     * @param username The username of the user.
     * @return The {@link Annotator} entity with the given id.
     */
    public Annotator getAnnotator(final String owner, final String project, final String username) {
        return this.annotatorRepository
                .findByIdUsernameAndIdProjectidNameAndIdProjectidOwnername(username, project, owner)
                .orElseThrow(AnnotatorNotFoundException::new);
    }

    /**
     * Returns a list of all annotators for a given query.
     * 
     * @param query The query.
     * @return A {@link List} of {@link Annotator} entities.
     */
    public List<Annotator> queryAnnotator(final Query query) {
        return this.annotatorRepository.findByQuery(query);
    }

    /**
     * Returns a list of all annotators for a given project.
     * 
     * @param owner   The owner name of the project.
     * @param project The project name.
     * @return A {@link List} of {@link Annotator} entities.
     */
    public List<Annotator> getAnnotatorsOfProject(final String owner, final String project) {
        final Query query = new AnnotatorQueryBuilder()
                .withOwner(owner)
                .withProject(project)
                .build();

        return this.queryAnnotator(query);
    }

    /**
     * Returns if the user has completed the phases tutorial requirements.
     * 
     * @param username The username of the user.
     * @param owner    The owner name of the project.
     * @param project  The project name.
     * @param phase    The phase name.
     * @return True if the user has completed the phases tutorial requirements,
     *         false otherwise.
     */
    public boolean hasAnnotatorCompletedTutorialsOfPhase(String username, String owner, String project, String phase) {
        final Phase phaseEntity = this.getPhase(owner, project, phase);
        final Annotator annotator = this.getAnnotator(owner, project, username);

        return annotator.getCompletedTutorials().containsAll(phaseEntity.getTutorialRequirements());
    }

    /**
     * Returns the annotation process information for a given annotator.
     * 
     * @param owner     The owner name of the project.
     * @param project   The project name.
     * @param phase     The phase name.
     * @param annotator The annotator name.
     * @return The {@link AnnotationProcessInformation} for the given annotator.
     */
    public AnnotationProcessInformation getAnnotationProcessInformation(final String annotator, final String owner,
            final String project,
            final String phase) {
        final Query query = new AnnotationProcessInformationQueryBuilder()
                .withOwner(project)
                .withProject(owner)
                .withPhase(phase)
                .withAnnotator(annotator)
                .build();
        final List<AnnotationProcessInformation> annotatorInformations = this.annotationProcessInformationRepository
                .findByQuery(query);
        if (annotatorInformations.size() != 1) {
            throw new AnnotatorNotFoundException();
        }
        return annotatorInformations.get(0);
    }

    /**
     * Returns the annotation process information for a given annotator.
     * 
     * @param annotator The annotator.
     * @param phase     The phase.
     * @return The {@link AnnotationProcessInformation} for the given annotator.
     */
    public AnnotationProcessInformation getAnnotationProcessInformation(final Annotator annotator, final Phase phase) {
        final Query query = new AnnotationProcessInformationQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .withAnnotator(annotator.getId().getUsername())
                .build();

        final List<AnnotationProcessInformation> annotatorInformations = this.annotationProcessInformationRepository
                .findByQuery(query);

        if (annotatorInformations.size() != 1) {
            throw new AnnotationProcessInformationException("Annotation process information not found.");
        }

        return annotatorInformations.get(0);
    }

    /**
     * Save new annotation process information.
     * 
     * @param annotationProcessInformation The annotation process information.
     * @return The saved annotation process information.
     */
    public AnnotationProcessInformation saveAnnotationProcessInformation(
            final AnnotationProcessInformation annotationProcessInformation) {
        return this.annotationProcessInformationRepository.save(annotationProcessInformation);
    }

    /**
     * Update annotation process information.
     * 
     * @param Phase         The phase.
     * @param Annotator     The annotator.
     * 
     * @param samplingorder The sampling order.
     * @param samplingindex The sampling index.
     * 
     * @return The updated annotation process information.
     */
    @Transactional
    public AnnotationProcessInformation updateAnnotationProcessInformation(final Phase phase, final Annotator annotator,
            final String samplingorder, final Integer samplingindex) {

        AnnotationProcessInformation annotationProcessInformation;

        try {
            annotationProcessInformation = this.getAnnotationProcessInformation(annotator, phase);
        } catch (final AnnotationProcessInformationException e) {
            annotationProcessInformation = new AnnotationProcessInformation(annotator, phase);
        }

        annotationProcessInformation.setOrder(samplingorder);
        annotationProcessInformation.setIndex(samplingindex);
        return this.saveAnnotationProcessInformation(annotationProcessInformation);
    }

    /**
     * Get all instances for a given phase.
     * 
     * @param phase      the phase
     * @param additional if additional instances should be included
     * @return a list of all {@IInstance} for the given phase
     */
    public List<IInstance> getInstancesOfPhase(final Phase phase, final boolean additional) {
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.findUsePairInstanceByPhase(phase).stream()
                    .map(IInstance.class::cast).collect(Collectors.toList());
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (!additional)
                return this.findWSSIMInstanceByPhase(phase).stream()
                        .map(IInstance.class::cast).collect(Collectors.toList());
            else
                return this.findWSSIMTagByPhase(phase).stream()
                        .map(IInstance.class::cast).collect(Collectors.toList());
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.findLexSubInstanceByPhase(phase).stream()
                    .map(IInstance.class::cast).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * Get the number of instances for a given phase.
     * 
     * @param phase      the phase
     * @param additional if additional instances should be included
     * @return the number of {@IInstance} for the given phase
     */
    public long countInstancesOfPhase(final Phase phase, final boolean additional) {
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.countUsePairInstanceByPhase(phase);
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            if (!additional)
                return this.countWSSIMInstanceByPhase(phase);
            else
                return this.countWSSIMTagByPhase(phase);
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.countLexSubInstanceByPhase(phase);
        }

        return 0;
    }

    /**
     * Get all use pair instances for a given phase.
     * 
     * @param phase The phase.
     * @return A list of all {@link UsePairInstance} for the given phase.
     */
    public List<UsePairInstance> findUsePairInstanceByPhase(final Phase phase) {
        final Query query = new UsePairInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.usePairInstanceRepository.findByQuery(query);
    }

    /**
     * Get number of use pair instances for a given phase.
     * 
     * @param phase The phase.
     * @return The number of {@link UsePairInstance} for the given phase.
     */
    public long countUsePairInstanceByPhase(final Phase phase) {
        final Query query = new UsePairInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.usePairInstanceRepository.findByQueryPaged(query, new PageRequestWraper(1, 0, null))
                .getTotalElements();
    }

    /**
     * Get all WSSIM instances for a given phase.
     * 
     * @param phase the phase
     * @return a {@link WSSIMInstance} list
     */
    public List<WSSIMInstance> findWSSIMInstanceByPhase(final Phase phase) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimInstanceRepository.findByQuery(query);
    }

    /**
     * Get number of WSSIM instances for a given phase.
     * 
     * @param phase The phase.
     * @return The number of {@link WSSIMInstance} for the given phase.
     */
    public long countWSSIMInstanceByPhase(final Phase phase) {
        final Query query = new WSSIMInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimInstanceRepository.findByQueryPaged(query, new PageRequestWraper(1, 0, null))
                .getTotalElements();
    }

    /**
     * Get all WSSIMTags for a given phase.
     * 
     * @param phase The phase.
     * @return {@link WSSIMTag} list
     */
    public List<WSSIMTag> findWSSIMTagByPhase(final Phase phase) {
        final Query query = new WSSIMTagQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimTagRepository.findByQuery(query);
    }

    /**
     * Get number of WSSIMTags for a given phase.
     * 
     * @param phase The phase.
     * @return The number of {@link WSSIMTag} for the given phase.
     */
    public long countWSSIMTagByPhase(final Phase phase) {
        final Query query = new WSSIMTagQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();
        return this.wssimTagRepository.findByQueryPaged(query, new PageRequestWraper(1, 0, null))
                .getTotalElements();
    }

    /**
     * Get all LexSub instances for a given phase.
     * 
     * @param phase The phase.
     * @return A list of all {@link LexSubInstance} for the given phase.
     */
    public List<LexSubInstance> findLexSubInstanceByPhase(final Phase phase) {
        final Query query = new LexSubInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();

        return this.lexSubInstanceRepository.findByQuery(query);
    }

    /**
     * Get number of LexSub instances for a given phase.
     * 
     * @param phase The phase.
     * @return The number of {@link LexSubInstance} for the given phase.
     */
    public long countLexSubInstanceByPhase(final Phase phase) {
        final Query query = new LexSubInstanceQueryBuilder()
                .withOwner(phase.getId().getProjectid().getOwnername())
                .withProject(phase.getId().getProjectid().getName())
                .withPhase(phase.getId().getName())
                .build();

        return this.lexSubInstanceRepository.findByQueryPaged(query, new PageRequestWraper(1, 0, null))
                .getTotalElements();
    }

    /**
     * Get all judgements for a given phase.
     * 
     * @param phase The phase.
     * @return A list of all {@link Judgement} for the given phase.
     */
    public List<IJudgement> getJudgementsOfPhase(final Phase phase) {
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.usePairJudgementApplicationService.findByPhase(phase).stream()
                    .map(IJudgement.class::cast).collect(Collectors.toList());
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            return this.wssimJudgementApplicationService.findByPhase(phase).stream()
                    .map(IJudgement.class::cast).collect(Collectors.toList());
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.lexSubJudgementApplicationService.findByPhase(phase).stream()
                    .map(IJudgement.class::cast).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * Get number of judgements for a given phase.
     * 
     * @param phase The phase.
     * @return The number of {@link Judgement} for the given phase.
     */
    public long countJudgementsOfPhase(final Phase phase) {
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_USEPAIR.name())) {
            return this.usePairJudgementApplicationService.findByPhase(phase, 1, 0, null).getTotalElements();
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_WSSIM.name())) {
            return this.wssimJudgementApplicationService.findByPhase(phase, 1, 0, null).getTotalElements();
        }
        if (phase.getAnnotationType().getName().equals(AnnotationTypeEnum.ANNOTATIONTYPE_LEXSUB.name())) {
            return this.lexSubJudgementApplicationService.findByPhase(phase, 0, 0, null).getTotalElements();
        }

        return 0;
    }

    /**
     * Store new Notification.
     * 
     * @param user    The user.
     * @param message The message.
     * @return void
     */
    @Transactional
    public void sendNotification(final User user, final String message) {
        final Notification notification = new Notification(user, message);
        this.notificationRepository.save(notification);
    }

    /**
     * Get a dictionary by its name.
     * 
     * @param dname
     * @param uname
     * @return The dictionary.
     */
    public Dictionary getDictionary(final String dname, final String uname) {
        final DictionaryId id = new DictionaryId(dname, uname);
        return this.dictionaryRepository.findById(id)
                .orElseThrow(() -> new DictionaryException("Dictionary not found."));
    }

    /**
     * Get an entry by its id.
     * 
     * @param entryId
     * @param dname
     * @param uname
     * @return The entry.
     */
    public DictionaryEntry getEntry(final String entryId, final String dname, final String uname) {
        final DictionaryEntryId id = new DictionaryEntryId(entryId, new DictionaryId(dname, uname));
        return this.dictionaryEntryRepository.findById(id)
                .orElseThrow(() -> new DictionaryEntryException("Entry not found."));
    }

    /**
     * Get a sense entry by its id.
     * 
     * @param senseId The sense id.
     * @param entryId The entry id.
     * @param dname   The dictionary name.
     * @param uname   The user name.
     * @return The sense entry.
     */
    public DictionaryEntrySense getSenseEntry(final String senseId, final String entryId, final String dname,
            final String uname) {
        final DictionaryEntrySenseId id = new DictionaryEntrySenseId(senseId,
                new DictionaryEntryId(entryId, new DictionaryId(dname, uname)));
        return this.dictionaryEntrySenseRepository.findById(id)
                .orElseThrow(() -> new DictionaryEntrySenseException("Sense not found."));
    }

    /**
     * Get all sense examples for a given sense entry.
     * 
     * @param exampleId The example id.
     * @param senseId   The sense id.
     * @param entryId   The entry id.
     * @param dname     The dictionary name.
     * @param uname     The user name.
     * @return The example.
     */
    public DictionaryEntrySenseExample getSenseExample(final String exampleId, final String senseId,
            final String entryId, final String dname, final String uname) {
        final DictionaryEntrySenseExampleId id = new DictionaryEntrySenseExampleId(exampleId,
                new DictionaryEntrySenseId(senseId, new DictionaryEntryId(entryId, new DictionaryId(dname, uname))));
        return this.dictionaryEntrySenseExampleRepository.findById(id)
                .orElseThrow(() -> new DictionaryEntrySenseExampleException("Example not found."));
    }

    public Role getRole(final String role) {
        return this.roleRepository.findByName(role).orElseThrow(RoleNotFoundException::new);
    }

    public Visibility getVisibility(final String visibility) {
        return this.visibilityRepository.findByName(visibility).orElseThrow(VisibilityNotFoundException::new);
    }

    public Language getLanguage(String language) {
        return this.languageRepository.findByName(language).orElseThrow(LanguageNotFoundException::new);
    }

    public Entitlement getEntitlement(final String entitlement) {
        return this.entitlementRepository.findByName(entitlement).orElseThrow(EntitlementNotFoundException::new);
    }

    public AnnotationType getAnnotationType(final String annotationType) {
        return this.annotationTypeRepository.findByName(annotationType)
                .orElseThrow(AnnotationTypeNotFoundException::new);
    }

    public Status getStatus(final String status) {
        return this.statusRepository.findByName(status).orElseThrow(StatusNotFoundException::new);
    }

    public Sampling getSampling(final String sampling) {
        return this.samplingRepository.findByName(sampling).orElseThrow(SamplingNotFoundException::new);
    }

    public Usecase getUsecase(final String usecase) {
        return this.usecaseRepository.findByName(usecase).orElseThrow(() -> new UsecaseException("Usecase not found"));
    }
}

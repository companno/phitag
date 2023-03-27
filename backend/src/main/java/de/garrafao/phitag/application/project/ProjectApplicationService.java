package de.garrafao.phitag.application.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.garrafao.phitag.application.statistics.annotatostatistic.AnnotatorStatisticApplicationService;
import de.garrafao.phitag.application.statistics.projectstatic.ProjectStatisticApplicationService;
import de.garrafao.phitag.application.statistics.userstatistic.UserStatisticApplicationService;
import de.garrafao.phitag.application.common.CommonService;
import de.garrafao.phitag.application.entitlement.data.EntitlementEnum;
import de.garrafao.phitag.application.project.data.CreateProjectCommand;
import de.garrafao.phitag.application.project.data.ProjectDto;
import de.garrafao.phitag.application.project.data.ProjectNameRestrictionEnum;
import de.garrafao.phitag.application.validation.ValidationService;
import de.garrafao.phitag.application.visibility.data.VisibilityEnum;
import de.garrafao.phitag.domain.annotator.Annotator;
import de.garrafao.phitag.domain.annotator.AnnotatorRepository;
import de.garrafao.phitag.domain.annotator.query.AnnotatorQueryBuilder;
import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.entitlement.Entitlement;
import de.garrafao.phitag.domain.language.Language;
import de.garrafao.phitag.domain.project.Project;
import de.garrafao.phitag.domain.project.ProjectRepository;
import de.garrafao.phitag.domain.project.error.ProjectExistException;
import de.garrafao.phitag.domain.project.error.ProjectNameRestrictionException;
import de.garrafao.phitag.domain.project.query.ProjectQueryBuilder;
import de.garrafao.phitag.domain.user.User;
import de.garrafao.phitag.domain.visibility.Visibility;

@Service
public class ProjectApplicationService {

    // Repository

    private final ProjectRepository projectRepository;

    private final AnnotatorRepository annotatorRepository;

    // Services

    private final UserStatisticApplicationService userStatisticApplicationService;

    private final ProjectStatisticApplicationService projectStatisticApplicationService;

    private final AnnotatorStatisticApplicationService annotatorStatisticApplicationService;

    // Common

    private final CommonService commonService;

    private final ValidationService validationService;

    // Other

    @Autowired
    public ProjectApplicationService(
            final ProjectRepository projectRepository,
            final AnnotatorRepository annotatorRepository,

            final UserStatisticApplicationService userStatisticApplicationService,
            final ProjectStatisticApplicationService projectStatisticApplicationService,
            final AnnotatorStatisticApplicationService annotatorStatisticApplicationService,

            final CommonService commonService,
            final ValidationService validationService) {
        this.projectRepository = projectRepository;
        this.annotatorRepository = annotatorRepository;

        this.userStatisticApplicationService = userStatisticApplicationService;
        this.projectStatisticApplicationService = projectStatisticApplicationService;
        this.annotatorStatisticApplicationService = annotatorStatisticApplicationService;

        this.commonService = commonService;
        this.validationService = validationService;
    }

    // API methods

    // Getters

    /**
     * Get projects by query with fuzzy search.
     * 
     * @param query query for search
     * @return list of {@ProjectDto}
     */
    public List<ProjectDto> queryProjectDto(final Query query) {
        return this.projectRepository.findByQuery(query).stream().map(ProjectDto::from).toList();
    }

    /**
     * Get public projects of a user
     * 
     * @param authenticationToken
     * @param owner
     * @return list of {@ProjectDto}
     */
    public List<ProjectDto> getPublicProjectOfUserDtos(final String authenticationToken, final String owner) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);

        String visibility = VisibilityEnum.VISIBILITY_PUBLIC.name();
        if (requester.getUsername().equals(owner)) {
            visibility = "";
        }

        final Query query = new ProjectQueryBuilder()
                .withOwner(owner)
                .withVisibility(visibility)
                .build();

        return this.projectRepository.findByQuery(query).stream().map(ProjectDto::from).toList();
    }

    /**
     * Get a project by its id.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param owner               the owner of the project
     * @param project             the project
     * @return list of {@ProjectDto}
     */
    public ProjectDto findProjectOfUserAsDto(final String authenticationToken, final String owner,
            final String project) {
        final User requester = this.commonService.getUserByAuthenticationToken(authenticationToken);
        final Project projectEntity = this.commonService.getProject(owner, project);

        this.validationService.projectAccess(requester, projectEntity);

        return ProjectDto.from(projectEntity);
    }

    /**
     * Get a projects of requesting user, where the requesting user is the owner.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param querystring         the query string
     * @return list of {@ProjectDto}
     */
    public List<ProjectDto> getPersonalProjectDtos(final String authenticationToken, final String querystring) {
        final Query query = new ProjectQueryBuilder()
                .withFuzzySearch(querystring)
                .withOwner(this.commonService.getUsernameFromAuthenticationToken(authenticationToken))
                .build();

        return this.projectRepository.findByQuery(query).stream().map(ProjectDto::from).toList();
    }

    /**
     * Get a projects of requesting user, where the requesting user is an annotator.
     *
     * @param authenticationToken the authentication token of the requesting user
     * @param querystring         the query string
     * @return list of {@ProjectDto}
     */
    public List<ProjectDto> getAnnotatorProjectDtos(final String authenticationToken, final String querystring) {
        final Query query = new AnnotatorQueryBuilder()
                .withFuzzySearch(querystring)
                .withUser(this.commonService.getUsernameFromAuthenticationToken(authenticationToken))
                .build();

        return this.annotatorRepository.findByQuery(query).stream().map(annotator -> {
            return ProjectDto.from(annotator.getProject());
        }).toList();
    }

    // Setters
    /**
     * Create a new project.
     * 
     * @param authenticationToken the authentication token of the requesting user
     * @param command             the command
     */
    @Transactional
    public void create(final String authenticationToken, final CreateProjectCommand command) {
        validateCreateCommand(authenticationToken, command);

        // Create project
        User owner = this.commonService.getUserByAuthenticationToken(authenticationToken);
        Visibility visibility = this.commonService.getVisibility(command.getVisibility());
        Language language = this.commonService.getLanguage(command.getLanguage());

        Project project = projectRepository
                .save(new Project(command.getName(), owner, visibility, language, command.getDescription()));

        // Add owner as annotator with admin entitlement
        Entitlement entitlement = this.commonService.getEntitlement(EntitlementEnum.ENTITLEMENT_ADMIN.name());
        Annotator annotator = this.annotatorRepository.save(new Annotator(owner, project, entitlement));

        // Update user related statistics
        this.userStatisticApplicationService.updateProjectRelatedStatistics(project);

        // Create project statistics
        this.projectStatisticApplicationService.initializeProjectStatistic(project);

        // Create annotator statistics
        this.annotatorStatisticApplicationService.initializeAnnotatorStatistic(annotator);
    }

    // Validators

    private void validateCreateCommand(final String authenticationToken, final CreateProjectCommand command) {
        User owner = this.commonService.getUserByAuthenticationToken(authenticationToken);

        this.validationService
                .name(command.getName())
                .visibility(command.getVisibility())
                .language(command.getLanguage());

        // validate uniqueness of user name + project name
        if (projectRepository.findByIdNameAndIdOwnername(command.getName(), owner.getUsername()).isPresent()) {
            throw new ProjectExistException();
        }

        if (ProjectNameRestrictionEnum.contains(command.getName().toLowerCase())) {
            throw new ProjectNameRestrictionException();
        }

    }

}

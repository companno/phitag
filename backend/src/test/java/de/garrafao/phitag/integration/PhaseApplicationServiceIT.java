package de.garrafao.phitag.integration;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.garrafao.phitag.application.authentication.AuthenticationApplicationService;
import de.garrafao.phitag.application.authentication.data.AuthenticateUserCommand;
import de.garrafao.phitag.application.phase.PhaseApplicationService;
import de.garrafao.phitag.application.phase.data.PhaseDto;
import de.garrafao.phitag.domain.phase.PhaseRepository;
import de.garrafao.phitag.domain.phase.query.PhaseQueryBuilder;

@SpringBootTest
@ActiveProfiles("integration")
public class PhaseApplicationServiceIT {
    
    @Autowired
    private AuthenticationApplicationService authenticationApplicationService;

    @Autowired
    private PhaseApplicationService phaseApplicationService;

    @Autowired
    private PhaseRepository phaseRepository;

    private static String token;


    /**
     * Before all tests, "log in" as "user-0" with password "Password1234!" and get
     * the JWT token.
     */
    @BeforeEach
    public void before() {
        AuthenticateUserCommand authenticateUserCommand = new AuthenticateUserCommand("user-0", "Password1234!");
        PhaseApplicationServiceIT.token = authenticationApplicationService
                .authenticateUser(authenticateUserCommand)
                .getAuthenticationToken();
    }

    // query phases
    @Test
    @Transactional
    public void it_query_empty_all_phases() {
        var query = new PhaseQueryBuilder().build();

        var phases = phaseApplicationService.queryPhaseDtos(PhaseApplicationServiceIT.token, "user-0", "project-0", query);

        for (PhaseDto phaseDto : phases) {
            System.out.println(phaseDto);
        }

        assertEquals(2, phases.size());
    }


}

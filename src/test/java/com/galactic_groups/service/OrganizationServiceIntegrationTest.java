package com.galactic_groups.service;

import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.OrganizationController;
import com.galactic_groups.data.repository.OrganizationRepository;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.utils.RequestProvider;
import com.galactic_groups.utils.RequestResultPostProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.Admin;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrganizationServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String ORGANIZATION_CONTROLLER_URL =
            OrganizationController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private OrganizationRepository organizationRepository;

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void deleteOrganization(UserRole role) {
        assertTrue(organizationRepository.findById(1).isPresent());
        RequestProvider request = () -> delete(ORGANIZATION_CONTROLLER_URL + "/1");
        RequestResultPostProcessor okChecker = resultActions -> {
            resultActions.andExpect(status().isOk());
            assertFalse(organizationRepository.findById(1).isPresent());
        };
        RequestResultPostProcessor errChecker = resultActions -> {
            assertTrue(organizationRepository.findById(1).isPresent());
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, errChecker, Admin);

        multiAuthorizationRequestHelper.performAs(role);
    }

    // TODO
}

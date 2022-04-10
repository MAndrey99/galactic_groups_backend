package com.galactic_groups.service;

import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.OrganizationController;
import com.galactic_groups.data.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.Admin;
import static com.galactic_groups.data.view.UserRole.Employee;
import static com.galactic_groups.utils.TestUtils.authorized;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizationServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String ORGANIZATION_CONTROLLER_URL =
            OrganizationController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void deleteOrganization() throws Exception {
        assertTrue(organizationRepository.findById(1).isPresent());
        mockMvc.perform(authorized(delete(ORGANIZATION_CONTROLLER_URL + "/1"), Employee)).andExpect(status().isForbidden());
        mockMvc.perform(authorized(delete(ORGANIZATION_CONTROLLER_URL + "/1"), Admin)).andExpect(status().isOk());
        assertFalse(organizationRepository.findById(1).isPresent());
        mockMvc.perform(authorized(delete(ORGANIZATION_CONTROLLER_URL + "/1"), Admin)).andExpect(status().isNoContent());
    }

    // TODO
}

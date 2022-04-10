package com.galactic_groups.service;

import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.OrganizationController;
import com.galactic_groups.controllers.UserController;
import com.galactic_groups.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.Admin;
import static com.galactic_groups.utils.TestUtils.authorized;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String USER_CONTROLLER_URL =
            UserController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private UserRepository userRepository;

    @Test
    void deleteUser() throws Exception {
        assertTrue(userRepository.findById(1L).isPresent());
        mockMvc.perform(authorized(delete(USER_CONTROLLER_URL + "/1"), Admin)).andExpect(status().isOk());
        assertFalse(userRepository.findById(1L).isPresent());
        mockMvc.perform(authorized(delete(USER_CONTROLLER_URL + "/1"), Admin)).andExpect(status().isNoContent());
    }

    // TODO
}

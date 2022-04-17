package com.galactic_groups.service;

import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.UserController;
import com.galactic_groups.data.repository.UserRepository;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.utils.RequestProvider;
import com.galactic_groups.utils.RequestResultPostProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.Admin;
import static com.galactic_groups.data.view.UserRole.Owner;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String USER_CONTROLLER_URL =
            UserController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void deleteUser(UserRole role) {
        RequestProvider request = () -> delete(USER_CONTROLLER_URL + "/1");
        RequestResultPostProcessor okChecker = result -> {
            result.andExpect(status().isOk());
            assertFalse(userRepository.findById(1L).isPresent());
        };
        RequestResultPostProcessor errorChecker = result -> {
            assertTrue(userRepository.findById(1L).isPresent());
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, errorChecker, Owner, Admin);
        multiAuthorizationRequestHelper.performAs(role);
    }

    // TODO
}

package com.galactic_groups.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.UserController;
import com.galactic_groups.data.dto.UserInfo;
import com.galactic_groups.data.repository.UserRepository;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.utils.RequestProvider;
import com.galactic_groups.utils.RequestResultPostProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.Admin;
import static com.galactic_groups.data.view.UserRole.Owner;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String USER_CONTROLLER_URL =
            UserController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void createUser(UserRole role) {
        var usersCount = userRepository.count();
        var data = """
            {
                "fullName": "Ivan Ivanov",
                "password": "qwerty",
                "mail": "ivan@wt.ua",
                "organizationId": 1,
                "role": "Employee"
            }
        """;

        RequestProvider request = () -> post(USER_CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .content(data);
        RequestResultPostProcessor okChecker = result -> {
            var returned = objectMapper.readValue(
                    result.andExpect(status().isCreated())
                            .andReturn().getResponse().getContentAsString(),
                    UserInfo.class);
            var user = userRepository.findById(returned.id());
            assertTrue(user.isPresent());
            assertTrue(passwordEncoder.matches("qwerty", user.get().getPassword()));
            // TODO: check other fields
        };
        RequestResultPostProcessor errorChecker = result -> {
            assertEquals(usersCount, userRepository.count());
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, errorChecker, Owner, Admin);
        multiAuthorizationRequestHelper.performAs(role);
    }

    // TODO
}

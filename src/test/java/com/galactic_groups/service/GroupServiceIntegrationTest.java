package com.galactic_groups.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.GroupController;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.utils.RequestProvider;
import com.galactic_groups.utils.RequestResultPostProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.galactic_groups.data.view.UserRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String GROUP_CONTROLLER_URL =
            GroupController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void getStudentsByGroup(UserRole role) {
        RequestProvider request = () -> get(GROUP_CONTROLLER_URL).param("name", "7375");
        RequestResultPostProcessor okChecker = resultActions -> {
            resultActions
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                {
                                    "name": "7375",
                                    "students": [
                                        {
                                            "address": "city 1, st.2",
                                            "fullName": "Maslov Andrey",
                                            "groupName": "7375",
                                            "phone": "88005553535",
                                            "organizationId": 1
                                        },
                                        {
                                            "fullName": "Dmitriy Bond",
                                            "groupName": "7375",
                                            "organizationId": 1
                                        }
                                    ]
                                }
                            """));
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, null, Owner, Employee, Admin);
        multiAuthorizationRequestHelper.performAs(role);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void getAllGroups(UserRole role) {
        RequestProvider request = () -> get(GROUP_CONTROLLER_URL + "/list").param("organizationId", "1");
        RequestResultPostProcessor okChecker = resultActions -> {
            var resp = resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andReturn().getResponse()
                    .getContentAsString();
            var resultType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
            List<String> groups = objectMapper.readValue(resp, resultType);
            assertThat(groups).hasSameElementsAs(List.of("7374", "7375"));
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, null, Owner, Employee, Admin);
        multiAuthorizationRequestHelper.performAs(role);
    }
}

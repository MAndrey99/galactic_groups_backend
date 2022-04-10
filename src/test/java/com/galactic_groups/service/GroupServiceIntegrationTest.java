package com.galactic_groups.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.GroupController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.galactic_groups.data.view.UserRole.Employee;
import static com.galactic_groups.utils.TestUtils.authorized;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String GROUP_CONTROLLER_URL =
            GroupController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudentsByGroup() throws Exception {
        mockMvc.perform(authorized(get(GROUP_CONTROLLER_URL), Employee).param("name", "7375"))
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
    }

    @Test
    void getAllGroups() throws Exception {
        var resp = mockMvc.perform(authorized(get(GROUP_CONTROLLER_URL + "/list"), Employee).contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse()
                .getContentAsString();
        var resultType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
        List<String> groups = objectMapper.readValue(resp, resultType);
        assertThat(groups).hasSameElementsAs(List.of("7374", "7375"));
    }
}

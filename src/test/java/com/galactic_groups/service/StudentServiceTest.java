package com.galactic_groups.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.GroupController;
import com.galactic_groups.controllers.StudentController;
import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.repository.StudentRepository;
import com.galactic_groups.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.galactic_groups.data.view.UserRole.Employee;
import static com.galactic_groups.utils.TestUtils.authorized;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String GROUP_CONTROLLER_URL =
            GroupController.class.getAnnotation(RequestMapping.class).value()[0];
    private static final String STUDENT_CONTROLLER_URL =
            StudentController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

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

    @Test
    void createStudent() throws Exception {
        var newStudent = Student.builder()
                .fullName("Dude")
                .groupName("7373")
                .organizationId(1L)
                .build();

        var request = authorized(post(STUDENT_CONTROLLER_URL), Employee)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent));
        var resp = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var returnedStudent = objectMapper.readValue(resp, Student.class);

        // проверяем, что вернём то что передавали, но с id
        assertNotNull(returnedStudent);
        Long id = returnedStudent.getId();
        assertNotNull(id);
        assertTrue(TestUtils.isEqualsByClassAndFields(returnedStudent, newStudent, "id"));

        // проверяем, что вставили в бд то что передавали
        var created = studentRepository.findById(id);
        assertTrue(created.isPresent());
        assertTrue(TestUtils.isEqualsByClassAndFields(returnedStudent, created.get()));
    }

    @Test
    void tryCreateInvalidStudent() throws Exception {
        var request = authorized(post(STUDENT_CONTROLLER_URL), Employee)
                .contentType(APPLICATION_JSON)
                .content("{\"fullName\":\"D\",\"phone\":\"y+4-478-787-878\", \"organizationId\": 1}");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                    {
                        "phone": "doesn't seem to be a valid phone number",
                        "groupName": "must not be null",
                        "fullName": "size must be between 2 and 64"
                    }
                """));
    }

    @Test
    void tryCreateStudentWithSpecifiedId() throws Exception {
        var request = authorized(post(STUDENT_CONTROLLER_URL), Employee)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":15,\"fullName\":\"Dude\",\"phone\":\"+4-478-787-878\",\"groupName\":\"7070\",\"organizationId\":1}");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                    {
                        "id": "must be null"
                    }
                """));
    }

    @Test
    void deleteStudent() throws Exception {
        assertTrue(studentRepository.findById(1L).isPresent());
        mockMvc.perform(authorized(delete(STUDENT_CONTROLLER_URL + "/1"), Employee)).andExpect(status().isOk());
        assertFalse(studentRepository.findById(1L).isPresent());
        assertTrue(studentRepository.findById(2L).isPresent());
        assertTrue(studentRepository.findById(3L).isPresent());
        mockMvc.perform(authorized(delete(STUDENT_CONTROLLER_URL + "/1"), Employee)).andExpect(status().isNoContent());
    }
}

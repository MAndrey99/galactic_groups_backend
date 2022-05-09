package com.galactic_groups.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galactic_groups.configuration.AbstractIntegrationTest;
import com.galactic_groups.controllers.StudentController;
import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.repository.StudentRepository;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.utils.RequestProvider;
import com.galactic_groups.utils.RequestResultPostProcessor;
import com.galactic_groups.utils.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.galactic_groups.data.view.UserRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentServiceIntegrationTest extends AbstractIntegrationTest {
    private static final String STUDENT_CONTROLLER_URL =
            StudentController.class.getAnnotation(RequestMapping.class).value()[0];

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void createStudent(UserRole role) {
        var newStudent = Student.builder()
                .fullName("Dude")
                .groupName("7373")
                .organizationId(1)
                .build();
        RequestProvider request = () -> post(STUDENT_CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent));
        RequestResultPostProcessor okChecker = resultActions -> {
            var resp = resultActions.andDo(print())
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
        };
        RequestResultPostProcessor errChecker = resultActions -> {
            assertTrue(resultActions.andReturn().getResponse().getContentAsString().isEmpty());
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, errChecker, Employee, Owner);
        multiAuthorizationRequestHelper.performAs(role);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void tryCreateInvalidStudent(UserRole role) {
        RequestProvider request = () -> post(STUDENT_CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"fullName\":\"D\",\"phone\":\"y+4-478-787-878\", \"organizationId\": 1}");
        RequestResultPostProcessor okChecker = resultActions -> {
            resultActions
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                            {
                                "phone": "doesn't seem to be a valid phone number",
                                "groupName": "must not be null",
                                "fullName": "size must be between 2 and 64"
                            }
                            """));
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, null, Admin, Employee, Owner);
        multiAuthorizationRequestHelper.performAs(role);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void tryCreateStudentWithSpecifiedId(UserRole role) {
        RequestProvider request = () -> post(STUDENT_CONTROLLER_URL)
                .contentType(APPLICATION_JSON)
                .content("{\"id\":15,\"fullName\":\"Dude\",\"phone\":\"+4-478-787-878\",\"groupName\":\"7070\",\"organizationId\":1}");
        RequestResultPostProcessor okChecker = resultActions -> {
            resultActions
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                            {
                                "id": "must be null"
                            }
                            """));
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, null, Admin, Employee, Owner);
        multiAuthorizationRequestHelper.performAs(role);
    }

    @ParameterizedTest
    @EnumSource(UserRole.class)
    void deleteStudent(UserRole role) {
        RequestProvider request = () -> delete(STUDENT_CONTROLLER_URL + "/1");
        RequestResultPostProcessor okChecker = resultActions -> {
            resultActions.andExpect(status().isOk());
            assertFalse(studentRepository.findById(1L).isPresent());
            assertTrue(studentRepository.findById(2L).isPresent());
            assertTrue(studentRepository.findById(3L).isPresent());
        };
        RequestResultPostProcessor errChecker = resultActions -> {
            assertTrue(studentRepository.findById(1L).isPresent());
        };
        var multiAuthorizationRequestHelper =
                multiAuthorizationRequestHelperFactory.buildMultiAuthorizationRequestHelper(
                        request, okChecker, errChecker, Employee, Owner);
        multiAuthorizationRequestHelper.performAs(role);
    }
}

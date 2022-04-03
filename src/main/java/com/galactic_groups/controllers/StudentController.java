package com.galactic_groups.controllers;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.service.StudentService;
import com.galactic_groups.data.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
@Validated
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "Create new student", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new student",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    public Student createStudent(@RequestBody @Valid Student student) {
        return studentService.createStudent(student);
    }

    @Operation(summary = "Delete a student by its id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted", content = @Content),
            @ApiResponse(responseCode = "204", description = "Student does not exist", content = @Content)
    })
    @DeleteMapping(path = "/{id}")
    public void deleteStudentById(@PathVariable long id) {
        studentService.deleteById(id);
    }
}

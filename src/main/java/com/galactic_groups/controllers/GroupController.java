package com.galactic_groups.controllers;

import com.galactic_groups.dto.Group;
import com.galactic_groups.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final StudentService studentService;

    @Operation(summary = "Get a group of students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    })
    @GetMapping
    public Group getGroup(@RequestParam(value = "name") String group) {
        return new Group(group, studentService.getStudentsByGroup(group));
    }

    @Operation(summary = "Get list of all existing group names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found zero or more",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/list")
    public List<String> getGroupsList() {
        return studentService.getGroupsList();
    }
}

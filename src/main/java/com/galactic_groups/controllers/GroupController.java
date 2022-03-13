package com.galactic_groups.controllers;

import com.galactic_groups.dto.Group;
import com.galactic_groups.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final StudentService studentService;

    @GetMapping
    public Group getGroup(@RequestParam(value = "name") String group) {
        return new Group(group, studentService.getStudentsByGroup(group));
    }

    @GetMapping("/list")
    public List<String> getGroupsList() {
        return studentService.getGroupsList();
    }
}

package com.galactic_groups.dto;

import com.galactic_groups.model.Student;

import java.util.List;

public record Group (String name, List<Student> students) {}

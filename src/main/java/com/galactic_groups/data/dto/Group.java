package com.galactic_groups.data.dto;

import com.galactic_groups.data.model.Student;

import java.util.List;

public record Group (String name, List<Student> students) {}

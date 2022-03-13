package com.galactic_groups.service;

import com.galactic_groups.model.Student;
import com.galactic_groups.repository.StudentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<Student> getStudentsByGroup(@NonNull String group) {
        return studentRepository.findAllByGroupName(group);
    }

    @Transactional(readOnly = true)
    public List<String> getGroupsList() {
        return studentRepository.findAllGroups();
    }

    @Transactional
    public Student createStudent(@NonNull Student student) {
        studentRepository.save(student);
        return student;
    }

    @Transactional
    public void deleteById(long id) {
        studentRepository.deleteById(id);
    }
}

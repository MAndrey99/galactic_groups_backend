package com.galactic_groups.service;

import com.galactic_groups.model.Student;
import com.galactic_groups.repository.StudentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteById(long id) {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.debug(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
}

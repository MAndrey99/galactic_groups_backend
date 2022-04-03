package com.galactic_groups.service;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.repository.StudentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public List<Student> getStudentsByGroup(@NonNull String group) {
        var user = securityService.getUserWithOrganizationId();
        return studentRepository.findAllByGroupNameAndOrganizationId(group, user.getOrganizationId());
    }

    @Transactional(readOnly = true)
    public List<String> getGroupsList() {
        var user = securityService.getUserWithOrganizationId();
        return studentRepository.findAllGroups(user.getOrganizationId());
    }

    @Transactional
    public Student createStudent(@NonNull Student student) {
        var user = securityService.getUserWithOrganizationId();
        if (!securityService.checkAccessToOrganization(user, student.getOrganizationId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteById(long id) {
        var user = securityService.getUserWithOrganizationId();
        if (studentRepository.deleteByIdAndOrganizationId(id, user.getOrganizationId()) == 0)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

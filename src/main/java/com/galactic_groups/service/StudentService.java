package com.galactic_groups.service;

import com.galactic_groups.data.dto.OrganizationSecurityData;
import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.repository.StudentRepository;
import com.galactic_groups.service.security.AccessMode;
import com.galactic_groups.service.security.SecurityService;
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
        var accessManager = securityService.getAccessManager();
        return studentRepository.findAllByGroupName(group).stream()
                .filter(stud -> accessManager.checkAccessTo(stud, AccessMode.READ).isAllowed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getGroupsList(int organizationId) {
        securityService.require(accessManager -> accessManager.checkAccessTo(
                new OrganizationSecurityData(organizationId), AccessMode.READ));
        return studentRepository.findAllGroups(organizationId);
    }

    @Transactional
    public Student createStudent(@NonNull Student student) {
        securityService.require(accessManager ->
                accessManager.checkAccessTo(student, AccessMode.WRITE));
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteById(long id) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        securityService.require(accessManager ->
                accessManager.checkAccessTo(student, AccessMode.WRITE));
        studentRepository.delete(student);
    }
}

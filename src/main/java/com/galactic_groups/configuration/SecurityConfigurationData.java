package com.galactic_groups.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SecurityConfigurationData {

    @Getter
    @Value("${security.admin_login}")
    private String adminLogin;

    @Getter
    @Value("${security.admin_password}")
    private String adminPassword;

    PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        passwordEncoder = new BCryptPasswordEncoder(12);
        adminPassword = passwordEncoder.encode(adminPassword);
    }
}

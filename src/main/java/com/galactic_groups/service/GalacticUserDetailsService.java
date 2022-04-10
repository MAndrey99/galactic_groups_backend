package com.galactic_groups.service;

import com.galactic_groups.configuration.SecurityConfigurationData;
import com.galactic_groups.data.model.User;
import com.galactic_groups.data.repository.UserRepository;
import com.galactic_groups.data.view.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalacticUserDetailsService implements UserDetailsService {

    private final SecurityConfigurationData securityConfigurationData;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        if (securityConfigurationData.getAdminLogin().equals(mail)) {
            return User.builder()
                    .fullName(securityConfigurationData.getAdminLogin())
                    .password(securityConfigurationData.getAdminPassword())
                    .role(UserRole.Admin)
                    .build();
        }

        if (!isValidMail(mail)) {
            throw new UsernameNotFoundException("User doesn't exists");
        }

        return userRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    private boolean isValidMail(String mailString) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        var mail = new Object() {
            @Email
            String mail;
        };
        mail.mail = mailString;

        return validator.validate(mail).isEmpty();
    }
}

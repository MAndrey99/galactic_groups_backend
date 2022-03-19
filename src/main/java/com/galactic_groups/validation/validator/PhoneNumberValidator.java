package com.galactic_groups.validation.validator;

import com.galactic_groups.validation.annotation.ValidPhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Configurable
@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final Pattern phoneNumberPattern = Pattern.compile("\\+?\\d{8,16}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = value == null || phoneNumberPattern.matcher(value.replace("-", "")).matches();
        log.debug("'{}' is " + (valid ? "" : "not ") + "valid", value);
        return valid;
    }
}

package com.galactic_groups.data.validation.validator;

import com.galactic_groups.data.validation.annotation.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern phoneNumberPattern = Pattern.compile("\\+?\\d{8,16}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValid(value);
    }

    public boolean isValid(String value) {
        boolean valid = value == null || phoneNumberPattern.matcher(value.replace("-", "")).matches();
        log.debug("'{}' is " + (valid ? "" : "not ") + "valid", value);
        return valid;
    }
}

package com.SoftwareDesign.BeautySalon.model.validation;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    public UserValidator() {
    }

    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {

        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(e, "userName", "userName.empty");
        ValidationUtils.rejectIfEmpty(e, "password", "password.empty");
        User user = (User)obj;
        if (!user.getUserType().equals(UserType.ADMIN) && !user.getUserType().equals(UserType.CLIENT) && !user.getUserType().equals(UserType.EMPLOYEE)) {
            e.rejectValue("userType", "userType.invalid");
        }

    }
}
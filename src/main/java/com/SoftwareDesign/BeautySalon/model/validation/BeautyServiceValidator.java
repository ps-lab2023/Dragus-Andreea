package com.SoftwareDesign.BeautySalon.model.validation;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class BeautyServiceValidator implements Validator {
    public BeautyServiceValidator() {
    }

    public boolean supports(Class<?> clazz) {
        return BeautyService.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {

        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(e, "price", "price.empty");

        BeautyService beautyService = (BeautyService) obj;
        if (!beautyService.getEmployeeType().equals(EmployeeType.HAIR_DRESSER) && !beautyService.getEmployeeType().equals(EmployeeType.NAIL_TECH) && !beautyService.getEmployeeType().equals(EmployeeType.MAKEUP_ARTIST)) {
            e.rejectValue("userType", "userType.invalid");
        }

        if(beautyService.getPrice().compareTo(new BigDecimal(0)) < 0 ) {
            e.rejectValue("price", "price.negative");
        }

    }
}

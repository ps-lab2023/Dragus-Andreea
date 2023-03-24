package com.SoftwareDesign.BeautySalon.model.validation;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EmployeeValidator extends UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return  Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;
        super.validate(new User(employee.getId(), employee.getName(), employee.getUserType(), employee.getUserName(), employee.getPassword()), errors);
        if(!employee.getUserType().equals(UserType.EMPLOYEE)) {
            errors.rejectValue("userType", "userType.not.EMPLOYEE");
        }

        if(employee.getAppointments() == null)
        {
            errors.rejectValue("appointments","appointments.null");
        }


    }
}

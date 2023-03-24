package com.SoftwareDesign.BeautySalon.model.validation;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ClientValidator extends UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        super.validate(new User(client.getId(), client.getName(), client.getUserType(), client.getUserName(), client.getPassword()), errors);
        if(!client.getUserType().equals(UserType.CLIENT)) {
            errors.rejectValue("userType", "userType.not.CLIENT");
        }

        if(client.getLoyaltyPoints() < 0) {
            errors.rejectValue("loyaltyPoints", "loyaltyPoints.negativeValue");
        }

        if(client.getLoyaltyPoints() > 100) {
            errors.rejectValue("loyaltyPoints", "loyaltyPoints.invalidValue");
        }

    }
}

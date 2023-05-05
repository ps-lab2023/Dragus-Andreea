package com.SoftwareDesign.BeautySalon.repository.custom;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidBeautyServiceException;


import java.util.Optional;

public interface CustomBeautyServiceRepository {
    Optional<BeautyService> saveIfValid(BeautyService beautyService) throws InvalidBeautyServiceException;
}

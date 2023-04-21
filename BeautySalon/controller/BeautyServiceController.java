package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.BeautyServiceDTO;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.service.BeautyServiceService;
import com.SoftwareDesign.BeautySalon.service.exception.BeautyServiceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/beautyService")
@CrossOrigin(origins = "http://localhost:4200")
public class BeautyServiceController {
    @Autowired
    private BeautyServiceService beautyServiceService;

    @Autowired
    private ModelMapper modelMapper;

    public BeautyServiceController(BeautyServiceService beautyServiceService) {
        this.beautyServiceService = beautyServiceService;
    }

    @GetMapping({"/getBeautyServiceById"})
    public ResponseEntity<BeautyServiceDTO> getBeautyServiceById(@RequestParam Long id) throws BeautyServiceNotFoundException {
        BeautyService beautyService = beautyServiceService.getBeautyServiceById(id);
        BeautyServiceDTO beautyServiceResponse = modelMapper.map(beautyService, BeautyServiceDTO.class);

        return ResponseEntity.ok().body(beautyServiceResponse);

    }

    @GetMapping({"/getBeautyServiceByName"})
    public ResponseEntity<BeautyServiceDTO> getBeautyServiceByName(@RequestParam String name) throws BeautyServiceNotFoundException {
        BeautyService beautyService = beautyServiceService.getBeautyServiceByName(name);
        BeautyServiceDTO beautyServiceResponse = modelMapper.map(beautyService, BeautyServiceDTO.class);

        return ResponseEntity.ok().body(beautyServiceResponse);
    }

    @GetMapping({"getBeautyServicesByAppointmentId"})
    public ResponseEntity<List<BeautyServiceDTO>> getBeautyServicesByAppointmentId(@RequestParam Long appointmentId) {
        return ResponseEntity.ok().body(beautyServiceService.getBeautyServicesByAppointmentId(appointmentId).stream().map(beautyService ->modelMapper.map(beautyService, BeautyServiceDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping({"getBeautyServicesByEmployeeType"})
    public ResponseEntity<List<BeautyServiceDTO>> getBeautyServicesByEmployeeType(@RequestParam EmployeeType employeeType) throws BeautyServiceNotFoundException {
        return ResponseEntity.ok().body(beautyServiceService.getBeautyServicesByEmployeeType( employeeType).stream().map(beautyService ->modelMapper.map(beautyService, BeautyServiceDTO.class))
                .collect(Collectors.toList()));
    }
}

package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.BeautyServiceDTO;
import com.SoftwareDesign.BeautySalon.dto.ClientDTO;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidBeautyServiceException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.BeautyServiceService;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping({"/getById"})
    public ResponseEntity<BeautyServiceDTO> getBeautyServiceById(@RequestParam Long id) throws BeautyServiceNotFoundException {
        BeautyService beautyService = beautyServiceService.getBeautyServiceById(id);
        BeautyServiceDTO beautyServiceResponse = modelMapper.map(beautyService, BeautyServiceDTO.class);

        return ResponseEntity.ok().body(beautyServiceResponse);

    }

    @GetMapping({"/getByName"})
    public ResponseEntity<BeautyServiceDTO> getBeautyServiceByName(@RequestParam String name) throws BeautyServiceNotFoundException {
        BeautyService beautyService = beautyServiceService.getBeautyServiceByName(name);
        BeautyServiceDTO beautyServiceResponse = modelMapper.map(beautyService, BeautyServiceDTO.class);

        return ResponseEntity.ok().body(beautyServiceResponse);
    }

    @GetMapping({"/getByAppointmentId"})
    public ResponseEntity<List<BeautyServiceDTO>> getBeautyServicesByAppointmentId(@RequestParam Long appointmentId) {
        return ResponseEntity.ok().body(beautyServiceService.getBeautyServicesByAppointmentId(appointmentId).stream().map(beautyService ->modelMapper.map(beautyService, BeautyServiceDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping({"/getByEmployeeType"})
    public ResponseEntity<List<BeautyServiceDTO>> getBeautyServicesByEmployeeType(@RequestParam EmployeeType employeeType) throws BeautyServiceNotFoundException {
        return ResponseEntity.ok().body(beautyServiceService.getBeautyServicesByEmployeeType( employeeType).stream().map(beautyService ->modelMapper.map(beautyService, BeautyServiceDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BeautyServiceDTO>> getAllBeautyServices() throws BeautyServiceNotFoundException {
        return ResponseEntity.ok().body(beautyServiceService.getAllBeautyServices().stream().map(beautyService ->modelMapper.map(beautyService, BeautyServiceDTO.class))
                .collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public ResponseEntity<BeautyServiceDTO> addBeautyService(@RequestBody BeautyServiceDTO beautyServiceDTO) throws DataBaseFailException, InvalidBeautyServiceException {
        BeautyService beautyService = modelMapper.map(beautyServiceDTO, BeautyService.class);
        BeautyService addedBeautyService = beautyServiceService.addBeautyService(beautyService);
        BeautyServiceDTO response = modelMapper.map(addedBeautyService, BeautyServiceDTO.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<BeautyServiceDTO> updateBeautyService(@RequestBody BeautyServiceDTO beautyServiceDTO) throws DataBaseFailException, BeautyServiceNotFoundException, InvalidBeautyServiceException {
        BeautyService beautyService = modelMapper.map(beautyServiceDTO, BeautyService.class);
        BeautyService updatedBeautyService = beautyServiceService.updateBeautyService(beautyService);
        BeautyServiceDTO response = modelMapper.map( updatedBeautyService, BeautyServiceDTO.class);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteBeautyService(@RequestParam Long id) throws BeautyServiceNotFoundException {
        beautyServiceService.deleteBeautyService(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Deleted successfully beauty service with id = " + id);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.AppointmentDTO;
import com.SoftwareDesign.BeautySalon.dto.BeautyServiceDTO;
import com.SoftwareDesign.BeautySalon.dto.ClientDTO;
import com.SoftwareDesign.BeautySalon.dto.EmployeeDTO;
import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.BeautyServiceService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    private BeautyServiceService beautyServiceService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private ModelMapper modelMapper;

    public AppointmentController(AppointmentService appointmentService, BeautyServiceService beautyServiceService, ClientService clientService, EmployeeService employeeService) {
        this.appointmentService = appointmentService;
        this.beautyServiceService = beautyServiceService;
        this.clientService = clientService;
        this.employeeService = employeeService;
    }

    @GetMapping("/getById")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@RequestParam Long id) throws AppointmentNotFoundException {
        Appointment appointment = appointmentService.getAppointmentById(id);
        AppointmentDTO appointmentResponse = modelMapper.map(appointment, AppointmentDTO.class);

        return ResponseEntity.ok().body(appointmentResponse);
    }

    @GetMapping("/getByEmployeeAndDateTime")
    public ResponseEntity<AppointmentDTO> getAppointmentByEmployeeNameAndDateTime(@RequestParam String name, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateTime) throws AppointmentNotFoundException {
        Appointment appointment = appointmentService.getAppointmentByEmployeeNameAndDateTime(name, dateTime);
        AppointmentDTO appointmentResponse = modelMapper.map(appointment, AppointmentDTO.class);

        return ResponseEntity.ok().body(appointmentResponse);
    }

    @GetMapping("/getByClientAndDateTime")
    public ResponseEntity<AppointmentDTO> getAppointmentByClientNameAndDateTime(@RequestParam String name, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateTime) throws AppointmentNotFoundException {
        Appointment appointment = appointmentService.getAppointmentByClientNameAndDateTime(name, dateTime);
        AppointmentDTO appointmentResponse = modelMapper.map(appointment, AppointmentDTO.class);

        return ResponseEntity.ok().body(appointmentResponse);
    }

    @GetMapping("/getAllByEmployee")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByEmployee(@RequestParam Long id) throws AppointmentNotFoundException {
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByEmployeeId(id).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllByEmployeeName")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByEmployeeName(@RequestParam String name) throws AppointmentNotFoundException, EmployeeNotFoundException {
        long id = employeeService.getEmployeeByName(name).getId();
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByEmployeeId(id).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllByClient")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByClient(@RequestParam Long id) throws AppointmentNotFoundException {
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByClientId(id).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllByClientName")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByClientName(@RequestParam String name) throws AppointmentNotFoundException, ClientNotFoundException {
        long id = clientService.getClientByName(name).getId();
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByClientId(id).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllByBeautyServicesContaining")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByBeautyServicesContaining(@RequestParam Long beautyServiceId) throws AppointmentNotFoundException, BeautyServiceNotFoundException {
        BeautyService beautyService = beautyServiceService.getBeautyServiceById(beautyServiceId);
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByBeautyServicesContaining(beautyService).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/getAllByTotalPriceGreaterThan")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByTotalPriceGreaterThan(@RequestParam BigDecimal totalPrice) throws AppointmentNotFoundException {
        return ResponseEntity.ok().body(appointmentService.getAllAppointmentsByTotalPriceGreaterThan(totalPrice).stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok().body(appointmentService.getAllAppointments().stream().map(appointment ->modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentDTO appointmentDTO) throws InvalidAppointmentException, DataBaseFailException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        Appointment addedAppointment = appointmentService.addAppointment(appointment);
        AppointmentDTO appointmentResponse = modelMapper.map(addedAppointment, AppointmentDTO.class);

        return new ResponseEntity<>(appointmentResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) throws InvalidAppointmentException, DataBaseFailException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        Appointment updatedAppointment = appointmentService.updateAppointment(appointment);
        AppointmentDTO appointmentResponse = modelMapper.map(updatedAppointment, AppointmentDTO.class);

        return ResponseEntity.ok().body(appointmentResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteAppointmentById(@RequestParam Long id) throws AppointmentNotFoundException {
        appointmentService.deleteAppointmentById(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Deleted successfully appointment with id = " + id);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

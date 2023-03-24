package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.TestDataBuilder;
import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.EmployeeRepository;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import com.SoftwareDesign.BeautySalon.service.impl.AppointmentServiceImpl;
import com.SoftwareDesign.BeautySalon.service.impl.EmployeeServiceImpl;
import com.SoftwareDesign.BeautySalon.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private AppointmentServiceImpl appointmentService;
    @Mock
    private UserServiceImpl userService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenEmployeeObject_whenAddValidEmployee_thenReturnEmployeeObject() {
        Employee validEmployee = TestDataBuilder.buildValidEmployee1();

        try {
            given(employeeRepository.findByUserName(validEmployee.getUserName()))
                    .willReturn(Optional.empty());

            given(employeeRepository.saveIfValid(validEmployee))
                    .willReturn(Optional.of(validEmployee));

            Assertions.assertDoesNotThrow(() -> employeeService.addEmployee(validEmployee));

            then(employeeRepository)
                    .should()
                    .saveIfValid(validEmployee);

        } catch(InvalidUserException | InvalidEmployeeException exc) {
            fail("Should not throw exception");
        }

    }

    @Test
    public void givenInvalidEmployeeObject_whenAddEmployee_thenThrowException() throws InvalidEmployeeException, InvalidUserException {
        Employee invalidEmployee = TestDataBuilder.buildInvalidEmployee();

        given(employeeRepository.findByUserName(invalidEmployee.getUserName()))
                .willReturn(Optional.empty());

        willThrow(new InvalidEmployeeException())
                .given(employeeRepository)
                . saveIfValid(invalidEmployee);

        Assertions.assertThrows(InvalidEmployeeException.class,  () -> employeeService.addEmployee(invalidEmployee));
    }

    @Test
    public void givenExistingUsername_whenAddEmployee_thenThrowException() throws InvalidUserException, InvalidEmployeeException {
        Employee employee = TestDataBuilder.buildValidEmployee1();

        given(employeeRepository.findByUserName(employee.getUserName()))
                .willReturn(Optional.of(employee));

        Assertions.assertThrows(DataBaseFailException.class, () -> employeeService.addEmployee(employee));

        then(employeeRepository)
                .should(never())
                .saveIfValid(employee);

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        Employee employee = TestDataBuilder.buildValidEmployee1();
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundEmployee.set(employeeService.getEmployeeById(employee.getId())));

        Assertions.assertEquals(foundEmployee.get(), employee);

        then(employeeRepository)
                .should()
                .findById(employee.getId());

    }

    @Test
    public void givenNotExistingEmployeeId_whenGetEmployeeById_thenThrowsException() {
        given(employeeRepository.findById(100L)).willReturn(Optional.empty());

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> foundEmployee.set(employeeService.getEmployeeById(100L)));

        Assertions.assertNull(foundEmployee.get());

        then(employeeRepository)
                .should()
                .findById(100L);

    }

    @Test
    public void givenName_whenGetEmployeeByName_thenReturnEmployeeObject() {
        Employee employee = TestDataBuilder.buildValidEmployee1();
        employee.setId(1L);

        given(employeeRepository.findByName(employee.getName()))
                .willReturn(Optional.of(employee));

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundEmployee.set(employeeService.getEmployeeByName(employee.getName())));

        Assertions.assertEquals(foundEmployee.get(), employee);

        then(employeeRepository)
                .should()
                .findByName(employee.getName());

    }

    @Test
    public void givenNotExistingName_whenGetEmployeeByName_thenThrowException() {
        String notExistingName = "John Doe";

        given(employeeRepository.findByName(notExistingName))
                .willReturn(Optional.empty());

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> foundEmployee.set(employeeService.getEmployeeByName(notExistingName)));

        Assertions.assertNull(foundEmployee.get());

        then(employeeRepository)
                .should()
                .findByName(notExistingName);

    }

    @Test
    public void givenUserName_whenGetEmployeeByUserName_thenReturnEmployeeObject() {
        Employee employee = TestDataBuilder.buildValidEmployee1();
        employee.setId(1L);

        given(employeeRepository.findByUserName(employee.getUserName()))
                .willReturn(Optional.of(employee));

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundEmployee.set(employeeService.getEmployeeByUserName(employee.getUserName())));

        Assertions.assertEquals(foundEmployee.get(), employee);

        then(employeeRepository)
                .should()
                .findByUserName(employee.getUserName());

    }

    @Test
    public void givenNotExistingUserName_whenGetEmployeeByUserName_thenThrowException() {
        String notExistingUsername = "johnDoe@domain.com";

        given(employeeRepository.findByUserName(notExistingUsername))
                .willReturn(Optional.empty());

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> foundEmployee.set(employeeService.getEmployeeByUserName(notExistingUsername)));

        Assertions.assertNull(foundEmployee.get());

        then(employeeRepository)
                .should()
                .findByUserName(notExistingUsername);
    }

    @Test
    public void givenEmployeeType_whenGetAllEmployeesByEmployeeType_thenReturnEmployeesList()  {
        Employee employee1 = TestDataBuilder.buildValidEmployee1();
        employee1.setId(1L);

        Employee employee2 = TestDataBuilder.buildValidEmployee3();
        employee2.setId(2L);

        List<Employee> expected = new ArrayList<>();
        expected.add(employee1);
        expected.add(employee2);

        given(employeeRepository.findAllByEmployeeType(EmployeeType.MAKEUP_ARTIST))
                .willReturn(List.of(employee1, employee2));

        AtomicReference<List<Employee>> foundEmployees = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundEmployees.set(employeeService.getAllEmployeesByEmployeeType(EmployeeType.MAKEUP_ARTIST)));

        Assertions.assertEquals(expected, foundEmployees.get());

        then(employeeRepository)
                .should()
                .findAllByEmployeeType(EmployeeType.MAKEUP_ARTIST);
    }

    @Test
    public void givenNotExistingEmployeeType_whenGetAllEmployeesByEmployeeType_thenThrowException()  {
        given(employeeRepository.findAllByEmployeeType(EmployeeType.HAIR_DRESSER))
                .willReturn(Collections.emptyList());

        AtomicReference<List<Employee>> foundEmployees = new AtomicReference<>();
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> foundEmployees.set(employeeService.getAllEmployeesByEmployeeType(EmployeeType.HAIR_DRESSER)));

        Assertions.assertTrue(CollectionUtils.isEmpty(foundEmployees.get()));

        then(employeeRepository)
                .should()
                .findAllByEmployeeType(EmployeeType.HAIR_DRESSER);
    }


    @Test
    public void givenAppointment_whenGetEmployeeByAppointmentsIsContaining_thenReturnEmployee() {
        Employee employee1 = TestDataBuilder.buildValidEmployee1();
        employee1.setId(1L);

        Appointment appointment1 = TestDataBuilder.buildValidAppointment1();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment2();

        employee1.addAppointment(appointment1);
        employee1.addAppointment(appointment2);

        given(employeeRepository.findByAppointmentsIsContaining(appointment1))
                .willReturn(Optional.of(employee1));

        AtomicReference<Employee> foundEmployee = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundEmployee.set(employeeService.getEmployeeByAppointmentsIsContaining(appointment1)));

        Assertions.assertEquals(employee1, foundEmployee.get());

        then(employeeRepository)
                .should()
                .findByAppointmentsIsContaining(appointment1);

    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        Employee savedEmployee = TestDataBuilder.buildValidEmployee1();
        savedEmployee.setId(1L);

        Employee employeeToBeUpdated = TestDataBuilder.buildValidEmployeeToUpdate();
        employeeToBeUpdated.setId(1L);

        User userToBeUpdated = new User(employeeToBeUpdated.getId(), employeeToBeUpdated.getName(), UserType.EMPLOYEE, employeeToBeUpdated.getUserName(), employeeToBeUpdated.getPassword());
        try {
            given(userService.updateUser(userToBeUpdated))
                    .willReturn(userToBeUpdated);

            given(appointmentService.getAllAppointmentsByEmployee(savedEmployee))
                    .willReturn(savedEmployee.getAppointments());

            given(employeeRepository.findById(savedEmployee.getId()))
                    .willReturn(Optional.of(savedEmployee));

            given(employeeRepository.saveIfValid(employeeToBeUpdated))
                    .willReturn(Optional.of(employeeToBeUpdated));

            AtomicReference<Employee> updatedEmployee = new AtomicReference<>();
            Assertions.assertDoesNotThrow(() -> updatedEmployee.set(employeeService.updateEmployee(employeeToBeUpdated)));

            Assertions.assertEquals(updatedEmployee.get(), employeeToBeUpdated);
            Assertions.assertEquals(updatedEmployee.get().getId(), savedEmployee.getId());

            then(employeeRepository)
                    .should()
                    .saveIfValid(employeeToBeUpdated);

        } catch(InvalidUserException | UserNotFoundException | DataBaseFailException | InvalidEmployeeException exception) {
            fail("Should not throw exception at updating");
        }

    }

    @Test
    public void givenNotExistingEmployeeObject_whenUpdateEmployee_thenThrowException() throws InvalidUserException, UserNotFoundException, DataBaseFailException, InvalidEmployeeException {
        long notExistingId = 1L;

        given(employeeRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        Employee employeeToBeUpdated = TestDataBuilder.buildInvalidEmployeeToUpdate();
        employeeToBeUpdated.setId(notExistingId);

        User userToBeUpdated = new User(employeeToBeUpdated.getId(), employeeToBeUpdated.getName(), UserType.EMPLOYEE, employeeToBeUpdated.getUserName(), employeeToBeUpdated.getPassword());
        userToBeUpdated.setId(notExistingId);

        doThrow(UserNotFoundException.class).when(userService).updateUser(userToBeUpdated);

        AtomicReference<Employee> updatedEmployee = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> updatedEmployee.set(employeeService.updateEmployee(employeeToBeUpdated)));

        Assertions.assertNull(updatedEmployee.get());

        then(employeeRepository)
                .should(never())
                .saveIfValid(employeeToBeUpdated);

    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenNothing() {
        Employee employee = TestDataBuilder.buildValidEmployee1();
        employee.setId(1L);

        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.of(employee));


        doNothing().when(employeeRepository).deleteById(employee.getId());

        Assertions.assertDoesNotThrow(() -> employeeService.deleteEmployeeById(employee.getId()));


        then(employeeRepository)
                .should()
                .deleteById(employee.getId());

    }

    @Test
    public void givenNotExistingId_whenDeleteEmployeeById_thenThrowException() {
        long notExistingId = 100L;

        given(employeeRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployeeById(notExistingId));

        then(employeeRepository)
                .should(never())
                .deleteById(notExistingId);

    }


}

package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.TestDataBuilder;
import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.AppointmentRepository;
import com.SoftwareDesign.BeautySalon.service.exception.AppointmentNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import com.SoftwareDesign.BeautySalon.service.impl.AppointmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientService clientService;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAppointmentObject_whenAddValidAppointment_thenReturnAppointmentObject() throws UserNotFoundException, ClientNotFoundException, DataBaseFailException, InvalidClientException, InvalidUserException {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();

        try {
            given(appointmentRepository.findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime()))
                    .willReturn(Optional.empty());

            given(appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime()))
                    .willReturn(Optional.empty());

            given(appointmentRepository.saveIfValid(appointment))
                    .willReturn(Optional.of(appointment));

            given(clientService.updateClient(appointment.getClient()))
                    .willReturn(appointment.getClient());

            Assertions.assertDoesNotThrow(() -> appointmentService.addAppointment(appointment));

            Assertions.assertTrue(appointment.getEmployee().getAppointments().contains(appointment));
            Assertions.assertTrue(appointment.getClient().getAppointments().contains(appointment));

            then(appointmentRepository)
                    .should()
                    .findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime());

            then(appointmentRepository)
                    .should()
                    .findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());

            then(appointmentRepository)
                    .should()
                    .saveIfValid(appointment);

        } catch(InvalidAppointmentException exc) {
            fail("Should not throw exception");
        }

    }

    @Test
    public void givenAppointmentObject_whenAddAppointmentOccupiedTimeSlot_thenThrowException() throws InvalidAppointmentException {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();
        Appointment existingAppointmentOnDateTimeEmployee = TestDataBuilder.buildValidAppointment4();

        given(appointmentRepository.findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime()))
                .willReturn(Optional.of(existingAppointmentOnDateTimeEmployee));

        given(appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime()))
                .willReturn(Optional.empty());

        given(appointmentRepository.saveIfValid(appointment))
                .willReturn(Optional.empty());

        Assertions.assertThrows(InvalidAppointmentException.class, () -> appointmentService.addAppointment(appointment));

        Assertions.assertFalse(appointment.getEmployee().getAppointments().contains(appointment));
        Assertions.assertFalse(appointment.getClient().getAppointments().contains(appointment));

        then(appointmentRepository)
                .should()
                .findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime());

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());

        then(appointmentRepository)
                .should(never())
                .saveIfValid(appointment);

    }

    @Test
    public void givenAppointmentObject_whenAddInvalidAppointmentTime_thenThrowException() throws InvalidAppointmentException {
        Appointment invalidAppointmentTime = TestDataBuilder.buildInvalidAppointmentTime();

        given(appointmentRepository.findByEmployeeNameAndDateTime(invalidAppointmentTime.getEmployee().getName(), invalidAppointmentTime.getDateTime()))
                .willReturn(Optional.empty());

        given(appointmentRepository.findByClientNameAndDateTime(invalidAppointmentTime.getClient().getName(), invalidAppointmentTime.getDateTime()))
                .willReturn(Optional.empty());

        willThrow(new InvalidAppointmentException("Time outside the work schedule"))
                .given(appointmentRepository)
                . saveIfValid(invalidAppointmentTime);

        Assertions.assertThrows(InvalidAppointmentException.class, () -> appointmentService.addAppointment(invalidAppointmentTime));

        Assertions.assertFalse(invalidAppointmentTime.getEmployee().getAppointments().contains(invalidAppointmentTime));
        Assertions.assertFalse(invalidAppointmentTime.getClient().getAppointments().contains(invalidAppointmentTime));

        then(appointmentRepository)
                .should()
                .findByEmployeeNameAndDateTime(invalidAppointmentTime.getEmployee().getName(), invalidAppointmentTime.getDateTime());

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(invalidAppointmentTime.getClient().getName(), invalidAppointmentTime.getDateTime());

        then(appointmentRepository)
                .should()
                .saveIfValid(invalidAppointmentTime);
    }

    @Test
    public void givenAppointmentObject_whenAddInvalidAppointmentPastDate_thenThrowException() throws InvalidAppointmentException {
        Appointment invalidAppointmentPastDate = TestDataBuilder.buildInvalidAppointmentPastDate();

        given(appointmentRepository.findByEmployeeNameAndDateTime(invalidAppointmentPastDate.getEmployee().getName(), invalidAppointmentPastDate.getDateTime()))
                .willReturn(Optional.empty());

        given(appointmentRepository.findByClientNameAndDateTime(invalidAppointmentPastDate.getClient().getName(), invalidAppointmentPastDate.getDateTime()))
                .willReturn(Optional.empty());

        willThrow(new InvalidAppointmentException("Past Date"))
                .given(appointmentRepository)
                . saveIfValid(invalidAppointmentPastDate);

        Assertions.assertThrows(InvalidAppointmentException.class, () -> appointmentService.addAppointment(invalidAppointmentPastDate));

        Assertions.assertFalse(invalidAppointmentPastDate.getEmployee().getAppointments().contains(invalidAppointmentPastDate));
        Assertions.assertFalse(invalidAppointmentPastDate.getClient().getAppointments().contains(invalidAppointmentPastDate));

        then(appointmentRepository)
                .should()
                .findByEmployeeNameAndDateTime(invalidAppointmentPastDate.getEmployee().getName(), invalidAppointmentPastDate.getDateTime());

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(invalidAppointmentPastDate.getClient().getName(), invalidAppointmentPastDate.getDateTime());

        then(appointmentRepository)
                .should()
                .saveIfValid(invalidAppointmentPastDate);
    }

    @Test
    public void givenAppointmentObject_whenAddInvalidAppointmentNotWorkingDay_thenThrowException() throws InvalidAppointmentException {
        Appointment invalidAppointmentNotWorkingDay = TestDataBuilder.buildInvalidAppointmentNotWorkingDate();

        given(appointmentRepository.findByEmployeeNameAndDateTime(invalidAppointmentNotWorkingDay.getEmployee().getName(), invalidAppointmentNotWorkingDay.getDateTime()))
                .willReturn(Optional.empty());

        given(appointmentRepository.findByClientNameAndDateTime(invalidAppointmentNotWorkingDay.getClient().getName(), invalidAppointmentNotWorkingDay.getDateTime()))
                .willReturn(Optional.empty());

        willThrow(new InvalidAppointmentException("Sunday - Not working day"))
                .given(appointmentRepository)
                . saveIfValid(invalidAppointmentNotWorkingDay);

        Assertions.assertThrows(InvalidAppointmentException.class, () -> appointmentService.addAppointment(invalidAppointmentNotWorkingDay));

        Assertions.assertFalse(invalidAppointmentNotWorkingDay.getEmployee().getAppointments().contains(invalidAppointmentNotWorkingDay));
        Assertions.assertFalse(invalidAppointmentNotWorkingDay.getClient().getAppointments().contains(invalidAppointmentNotWorkingDay));

        then(appointmentRepository)
                .should()
                .findByEmployeeNameAndDateTime(invalidAppointmentNotWorkingDay.getEmployee().getName(), invalidAppointmentNotWorkingDay.getDateTime());

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(invalidAppointmentNotWorkingDay.getClient().getName(), invalidAppointmentNotWorkingDay.getDateTime());

        then(appointmentRepository)
                .should()
                .saveIfValid(invalidAppointmentNotWorkingDay);
    }

    @Test
    public void givenAppointmentId_whenGetAppointmentById_thenReturnAppointmentObject() {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();
        appointment.setId(1L);

        given(appointmentRepository.findById(1L))
                .willReturn(Optional.of(appointment));

        AtomicReference<Appointment> foundAppointment = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointment.set(appointmentService.getAppointmentById(appointment.getId())));

        Assertions.assertEquals(foundAppointment.get(), appointment);

        then(appointmentRepository)
                .should()
                .findById(appointment.getId());

    }

    @Test
    public void givenNotExistingAppointmentId_whenGetAppointmentById_thenThrowsException() {
        given(appointmentRepository.findById(100L))
                .willReturn(Optional.empty());

        AtomicReference<Appointment> foundAppointment = new AtomicReference<>();
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> foundAppointment.set(appointmentService.getAppointmentById(100L)));

        Assertions.assertNull(foundAppointment.get());

        then(appointmentRepository)
                .should()
                .findById(100L);

    }

    @Test
    public void givenEmployeeObjectAndLocalDateTime_whenGetAppointmentByEmployeeAndDateTime_thenReturnAppointmentObject() {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();

        given(appointmentRepository.findByEmployeeNameAndDateTime(appointment.getEmployee().getName(),appointment.getDateTime()))
                .willReturn(Optional.of(appointment));

        AtomicReference<Appointment> foundAppointment = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointment.set(appointmentService.getAppointmentByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime())));

        Assertions.assertEquals(foundAppointment.get(), appointment);

        then(appointmentRepository)
                .should()
                .findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime());
    }

    @Test
    public void givenClientObjectAndLocalDateTime_whenGetAppointmentByClientAndDateTime_thenReturnAppointmentObject() {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();

        given(appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(),appointment.getDateTime()))
                .willReturn(Optional.of(appointment));

        AtomicReference<Appointment> foundAppointment = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointment.set(appointmentService.getAppointmentByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime())));

        Assertions.assertEquals(foundAppointment.get(), appointment);

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());
    }

    @Test
    public void givenNotExistingClientObjectAndLocalDateTime_whenGetAppointmentByClientAndDateTime_thenThrowException() {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();

        given(appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(),appointment.getDateTime()))
                .willReturn(Optional.empty());

        AtomicReference<Appointment> foundAppointment = new AtomicReference<>();
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> foundAppointment.set(appointmentService.getAppointmentByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime())));

        Assertions.assertNull(foundAppointment.get());

        then(appointmentRepository)
                .should()
                .findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());
    }

    @Test
    public void givenEmployeeObject_whenGetAllAppointmentsByEmployee_thenReturnAppointmentsList() {
        Appointment appointment1 = TestDataBuilder.buildValidAppointment1();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment2();

        List<Appointment> expected = new ArrayList<>();
        expected.add(appointment1);
        expected.add(appointment2);

        given(appointmentRepository.findAllByEmployeeId(appointment1.getEmployee().getId()))
                .willReturn(List.of(appointment1, appointment2));

        AtomicReference<List<Appointment>> foundAppointments = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointments.set(appointmentService.getAllAppointmentsByEmployeeId(appointment1.getEmployee().getId())));

        Assertions.assertEquals(expected, foundAppointments.get());

        then(appointmentRepository)
                .should()
                .findAllByEmployeeId(appointment1.getEmployee().getId());
    }

    @Test
    public void givenClientObject_whenGetAllAppointmentsByClient_thenReturnAppointmentsList() {
        Appointment appointment1 = TestDataBuilder.buildValidAppointment1();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment3();

        List<Appointment> expected = new ArrayList<>();
        expected.add(appointment1);
        expected.add(appointment2);

        given(appointmentRepository.findAllByClientId(appointment1.getClient().getId()))
                .willReturn(List.of(appointment1, appointment2));

        AtomicReference<List<Appointment>> foundAppointments = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointments.set(appointmentService.getAllAppointmentsByClientId(appointment1.getClient().getId())));

        Assertions.assertEquals(expected, foundAppointments.get());

        then(appointmentRepository)
                .should()
                .findAllByClientId(appointment1.getClient().getId());
    }

    @Test
    public void givenBeautyServiceObject_whenGetAllAppointmentsByBeautyServicesContaining_thenReturnAppointmentList() {
        BeautyService beautyService = TestDataBuilder.buildValidBeautyService2();

        Appointment appointment1 = TestDataBuilder.buildValidAppointment2();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment4();

        List<Appointment> expected = new ArrayList<>();
        expected.add(appointment1);
        expected.add(appointment2);

        given(appointmentRepository.findAllByBeautyServicesContaining(beautyService))
                .willReturn(List.of(appointment1, appointment2));

        AtomicReference<List<Appointment>> foundAppointments = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointments.set(appointmentService.getAllAppointmentsByBeautyServicesContaining(beautyService)));

        Assertions.assertEquals(expected, foundAppointments.get());

        then(appointmentRepository)
                .should()
                .findAllByBeautyServicesContaining(beautyService);
    }

    @Test
    public void givenNotExistingBeautyServiceObject_whenGetAllAppointmentsByBeautyServicesContaining_thenReturnAppointmentList() {
        BeautyService beautyService = TestDataBuilder.buildValidBeautyService5();

        given(appointmentRepository.findAllByBeautyServicesContaining(beautyService))
                .willReturn(Collections.emptyList());

        AtomicReference<List<Appointment>> foundAppointments = new AtomicReference<>();
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> foundAppointments.set(appointmentService.getAllAppointmentsByBeautyServicesContaining(beautyService)));

        Assertions.assertTrue(CollectionUtils.isEmpty(foundAppointments.get()));

        then(appointmentRepository)
                .should()
                .findAllByBeautyServicesContaining(beautyService);
    }

    @Test
    public void givenPrice_whenGetAllAppointmentsByTotalPriceGreaterThan_thenReturnAppointmentList() {
        BigDecimal price = new BigDecimal(120);

        Appointment appointment1 = TestDataBuilder.buildValidAppointment2();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment4();

        List<Appointment> expected = new ArrayList<>();
        expected.add(appointment1);
        expected.add(appointment2);

        given(appointmentRepository.findAllByTotalPriceGreaterThan(price))
                .willReturn(List.of(appointment1, appointment2));

        AtomicReference<List<Appointment>> foundAppointments = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundAppointments.set(appointmentService.getAllAppointmentsByTotalPriceGreaterThan(price)));

        Assertions.assertEquals(expected, foundAppointments.get());

        then(appointmentRepository)
                .should()
                .findAllByTotalPriceGreaterThan(price);
    }

    @Test
    public void givenAppointmentObject_whenUpdateAppointment_thenReturnAppointmentObject() {
        Appointment savedAppointment = TestDataBuilder.buildValidAppointment1();
        Appointment appointmentToBeUpdated = TestDataBuilder.buildValidAppointmentToUpdate();

        try
        {
        given(appointmentRepository.findById(savedAppointment.getId()))
                .willReturn(Optional.of(savedAppointment));

        given(appointmentRepository.saveIfValid(appointmentToBeUpdated))
                .willReturn(Optional.of(appointmentToBeUpdated));


        AtomicReference<Appointment> updatedAppointment = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> updatedAppointment.set(appointmentService.updateAppointment(appointmentToBeUpdated)));

        Assertions.assertEquals(updatedAppointment.get(), appointmentToBeUpdated);
        Assertions.assertEquals(updatedAppointment.get().getId(), savedAppointment.getId());

            then(appointmentRepository)
                    .should()
                    .saveIfValid(appointmentToBeUpdated);
        } catch (InvalidAppointmentException e) {
            fail("Should not throw at update");
        }
    }

    @Test
    public void givenNotExistingAppointmentObject_whenUpdateAppointment_thenThrowException() throws InvalidAppointmentException {
        Appointment appointmentToBeUpdated = TestDataBuilder.buildValidAppointmentToUpdate();
        appointmentToBeUpdated.setId(100L);

        given(appointmentRepository.findById(100L))
                .willReturn(Optional.empty());

        willThrow(new AppointmentNotFoundException("Appointment not found"))
                .given(appointmentRepository)
                . saveIfValid(appointmentToBeUpdated);

        AtomicReference<Appointment> updatedAppointment = new AtomicReference<>();
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> updatedAppointment.set(appointmentService.updateAppointment(appointmentToBeUpdated)));

        Assertions.assertNull(updatedAppointment.get());

        then(appointmentRepository)
                .should(never())
                .saveIfValid(appointmentToBeUpdated);
    }

    @Test
    public void givenAppointmentId_whenDeleteAppointmentById_doNothing() {
        Appointment appointment = TestDataBuilder.buildValidAppointment1();

        given(appointmentRepository.findById(appointment.getId()))
                .willReturn(Optional.of(appointment));

        doNothing().when(appointmentRepository).deleteById(appointment.getId());

        Assertions.assertDoesNotThrow(() -> appointmentService.deleteAppointmentById(appointment.getId()));

        then(appointmentRepository)
                .should()
                .deleteById(appointment.getId());
    }

    @Test
    public void givenNotExistingAppointmentId_whenDeleteAppointmentById_doNothing() {
        long notExistingId = 100L;

        given(appointmentRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        willThrow(new AppointmentNotFoundException("Appointment not found"))
                .given(appointmentRepository)
                .findById(notExistingId);

        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointmentById(notExistingId));

        then(appointmentRepository)
                .should(never())
                .deleteById(notExistingId);
    }


}

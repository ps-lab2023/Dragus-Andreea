package com.SoftwareDesign.BeautySalon;

import com.SoftwareDesign.BeautySalon.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TestDataBuilder {
    public static User buildValidUser1() {
        return new User("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus", "employee1");
    }
    public static User buildValidUser2() {
        return new User("Anca Chetan", UserType.EMPLOYEE, "ancachetan", "employee2");
    }

    public static User buildInvalidUser() {
        return new User("Maria Popescu", UserType.CLIENT, "", "client1");
    }

    public static User buildValidUserToUpdate() {
        return new User("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus33", "employee33");
    }

    public static User buildInvalidUserToUpdate() {
        return new User("Andreea Dragus", UserType.EMPLOYEE, "", "employee33");
    }

    public static Employee buildValidEmployee1() {
        Employee employee = new Employee("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus", "employee1", EmployeeType.MAKEUP_ARTIST);
        return employee;
    }

    public static Employee buildValidEmployee2() {
        Employee employee = new Employee("Anca Chetan", UserType.EMPLOYEE, "ancachetan", "employee2", EmployeeType.NAIL_TECH);
        return employee;
    }

    public static Employee buildValidEmployee3() {
        Employee employee =  new Employee("Maria Ionescu", UserType.EMPLOYEE, "mariaionescu", "employee3", EmployeeType.MAKEUP_ARTIST);
        return employee;
    }

    public static Employee buildInvalidEmployee() {
        return new Employee("Andreea Dragus", UserType.ADMIN, "andreeadragus", "employee1", EmployeeType.MAKEUP_ARTIST);
    }

    public static Employee buildValidEmployeeToUpdate() {
        Employee employee = new Employee("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus333", "employee1", EmployeeType.NAIL_TECH);
        return employee;
    }

    public static Employee buildInvalidEmployeeToUpdate() {
        Employee employee = new Employee("Andreea Dragus", UserType.EMPLOYEE, "", "employee1", EmployeeType.NAIL_TECH);
        return employee;
    }


    public static Client buildValidClient1() {
        return new Client("Alexandra Dumitru", "alexandradumitru", "client1", 0);
    }

    public static Client buildValidClient2() {
        return new Client("Maria Dumitrescu", "mariadumitrescu", "client2", 0);
    }

    public static Client buildInvalidClient() {
        return new Client("Alexandra Dumitru", "", "client1", 0);
    }

    public static Client buildValidClientToUpdate() {
        return new Client("Alexandra Dumitru", "alexandradumitru", "client12345", 0);
    }


    public static BeautyService buildValidBeautyService1() {
        BeautyService beautyService1 = new BeautyService("Natural Make-up", new BigDecimal(150));
        beautyService1.setId(1L);

        return beautyService1;
    }

    public static BeautyService buildValidBeautyService2() {
        BeautyService beautyService2 = new BeautyService("Glam Make-up", new BigDecimal(300));
        beautyService2.setId(2L);

        return beautyService2;
    }

    public static BeautyService buildValidBeautyService3() {
        BeautyService beautyService3 = new BeautyService("Bridal Make-up", new BigDecimal(350));
        beautyService3.setId(3L);

        return beautyService3;
    }

    public static BeautyService buildValidBeautyService4() {
        BeautyService beautyService4 = new BeautyService("Simple manicure", new BigDecimal(50));
        beautyService4.setId(4L);

        return beautyService4;
    }

    public static BeautyService buildValidBeautyService5() {
        BeautyService beautyService5 = new BeautyService("Gel manicure", new BigDecimal(100));
        beautyService5.setId(5L);

        return beautyService5;
    }

    public static Appointment buildValidAppointment1() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient1();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.AUGUST, 1, 10,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(1L);
        appointment.addBeautyService(buildValidBeautyService1());

        return appointment;

    }


    public static Appointment buildValidAppointment2() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient2();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.SEPTEMBER, 20, 15,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(2L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;
    }

    public static Appointment buildValidAppointment3() {
        Employee employee = buildValidEmployee2();
        employee.setId(1L);

        Client client = buildValidClient1();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.JULY, 20, 12,30);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(3L);

        appointment.addBeautyService(buildValidBeautyService4());

        return appointment;
    }

    public static Appointment buildValidAppointment4() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient2();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.AUGUST, 1, 10,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(4L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

    public static Appointment buildInvalidAppointmentTime() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient2();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.AUGUST, 1, 23,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(5L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

    public static Appointment buildInvalidAppointmentPastDate() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient2();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.AUGUST, 1, 23,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(5L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

    public static Appointment buildInvalidAppointmentNotWorkingDate() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient2();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.AUGUST, 13, 23,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(5L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

    public static Appointment buildInvalidAppointmentInvalidClient() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildInvalidClient();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2022, Month.AUGUST, 13, 23,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(5L);
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

    public static Appointment buildValidAppointmentToUpdate() {
        Employee employee = buildValidEmployee1();
        employee.setId(1L);

        Client client = buildValidClient1();
        client.setId(2L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.JULY, 1, 10,0);
        Appointment appointment = new Appointment(client, employee, localDateTime);
        appointment.setId(1L);
        appointment.addBeautyService(buildValidBeautyService1());
        appointment.addBeautyService(buildValidBeautyService2());

        return appointment;

    }

}

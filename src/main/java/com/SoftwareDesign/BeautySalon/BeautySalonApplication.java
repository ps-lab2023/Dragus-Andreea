package com.SoftwareDesign.BeautySalon;

import com.SoftwareDesign.BeautySalon.controller.UserController;
import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.repository.ClientRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomAppointmentRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class BeautySalonApplication {
	private final CustomAppointmentRepository appointmentRepository;
	private final ClientRepository clientRepository;

	public BeautySalonApplication(@Qualifier("appointmentRepository") CustomAppointmentRepository appointmentRepository,
								  ClientRepository clientRepository) {
		this.appointmentRepository = appointmentRepository;
		this.clientRepository = clientRepository;
	}

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(BeautySalonApplication.class, args);

	}

	@Bean
	CommandLineRunner init(UserController userController, BeautyServiceRepository beautyServiceRepository, UserService userService, EmployeeService employeeService, ClientService clientService, AppointmentService appointmentService) {
		return args -> {
           /*
		   Employee employee = new Employee("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus", "admin1", EmployeeType.MAKEUP_ARTIST);
		   Employee savedEmployee = employeeService.addEmployee(employee);

		   Employee employeeToBeUpdated =  new Employee(savedEmployee.getId(), "Mariana Marinara", UserType.EMPLOYEE, savedEmployee.getUserName(), savedEmployee.getPassword(), EmployeeType.MAKEUP_ARTIST);
		   Employee updatedEmployee = employeeService.updateEmployee(employeeToBeUpdated);
           */
           /*
		   Client client = new Client("Anca Chetan","ancachetan", "client1", 100);
		   Client savedClient = clientService.addClient(client);
		   Client clientToBeUpdated = new Client(savedClient.getId(), "Mariana Smechera", savedClient.getUserName(), savedClient.getPassword(), savedClient.getLoyaltyPoints());
		   Client updatedClient = clientService.updateClient(clientToBeUpdated);

		   LocalDateTime dateTime = LocalDateTime.of(2023, Month.of(3), 19, 10, 0 , 0);
		   Appointment appointment = new Appointment(updatedClient, updatedEmployee, dateTime);
		   Appointment savedAppointment = appointmentService.addAppointment(appointment);
		   Appointment appointmentToBeUpdated = new Appointment(savedAppointment.getId(),updatedClient, updatedEmployee, LocalDateTime.of(2024, Month.of(3), 19, 10, 0 , 0));
		   Appointment updatedAppointment = appointmentService.updateAppointment(appointmentToBeUpdated);

		   //clientService.deleteClientById(updatedClient.getId());

		    */




			/*
			BeautyService beauty = new BeautyService("abc", new BigDecimal(120));
			BeautyService beautyService = beautyServiceRepository.save(beauty);

			Employee employee = new Employee("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus", "employee1", EmployeeType.MAKEUP_ARTIST);
			Employee savedE = employeeService.addEmployee(employee);

			Client client = new Client("Alexandra Dumitru", "alexandradumitru", "client1", 0);
			Client savedC = clientService.addClient(client);

	        //beautyServiceRepository.save(beautyService);

			LocalDateTime localDateTime = LocalDateTime.of(2023, Month.AUGUST, 1, 10,0);
			Appointment appointment = new Appointment(savedC, savedE, localDateTime);
			appointment.addBeautyService(beautyService);
			Appointment savedAppointment = appointmentService.addAppointment(appointment);

			//BeautyService savedBS = beautyServiceRepository.save(beautyService);

			Employee employeeToBeUpdated =  new Employee(savedE.getId(), "Mary Jane", UserType.EMPLOYEE, savedE.getUserName(), savedE.getPassword(), EmployeeType.MAKEUP_ARTIST);
			Employee updatedEmployee = employeeService.updateEmployee(employeeToBeUpdated);

			Client clientToBeUpdated = new Client(savedC.getId(), "Dora John", savedC.getUserName(), savedC.getPassword(), savedC.getLoyaltyPoints());
			Client updatedClient = clientService.updateClient(clientToBeUpdated);


			//Appointment appointment1 = appointmentService.getAppointmentByEmployeeAndDateTime(employee, localDateTime);
			Client unprC = (Client) Hibernate.unproxy(updatedClient);
			Appointment appointmentToBeUpdated = new Appointment(savedAppointment.getId(),(Client) Hibernate.unproxy(updatedClient), (Employee) Hibernate.unproxy(updatedEmployee), LocalDateTime.of(2024, Month.of(3), 29, 10, 0 , 0));
			Appointment updatedAppointment = appointmentService.updateAppointment(appointmentToBeUpdated);

			Client client2 = new Client("John Doe", "johnDoe", "1234", 100);
			Client savedClient2 = clientService.addClient(client2);
			LocalDateTime localDateTime1 = LocalDateTime.of(2023,Month.SEPTEMBER, 20, 9,30);
			Appointment appointment2 = new Appointment(savedClient2, updatedEmployee,localDateTime1);
			appointment2.addBeautyService(beautyService);

			appointmentService.addAppointment(appointment2);
            */




			User user = new User("Andreea Dragus", UserType.EMPLOYEE, "andreeadragus", "employee1");
			userService.addUser(user);

			Employee employee = new Employee("Anca Chetan", UserType.EMPLOYEE, "ancachetan", "employee2", EmployeeType.NAIL_TECH);
			Employee savedEmployee = employeeService.addEmployee(employee);

			Client client = new Client("Alexandra Dumitru", "alexandradumitru", "client1", 0);
			Client savedClient = clientService.addClient(client);

			BeautyService beautyService1 = new BeautyService("Simple gel nails",new BigDecimal(120), EmployeeType.NAIL_TECH);
			BeautyService beautyServiceSaved1 = beautyServiceRepository.save(beautyService1);

			BeautyService beautyService2 = new BeautyService("Slim gel nails", new BigDecimal(250), EmployeeType.NAIL_TECH);
			BeautyService beautyServiceSaved2 = beautyServiceRepository.save(beautyService2);

			LocalDateTime localDateTime = LocalDateTime.of(2023, Month.AUGUST, 1, 10,0);
			Appointment appointment = new Appointment(savedClient, savedEmployee, localDateTime);
			appointment.addBeautyService(beautyServiceSaved1);
			appointment.addBeautyService(beautyServiceSaved2);
	        Appointment savedAppointment = appointmentService.addAppointment(appointment);


			Employee employee2 = new Employee("Mary Jane", UserType.EMPLOYEE, "maryjane", "employee3", EmployeeType.HAIR_DRESSER);
			Employee savedEmployee2 = employeeService.addEmployee(employee2);


			Client client2 = new Client("Dora Johnes","dorajohnes", "client3", 0);
            Client savedClient2 = clientService.addClient(client2);

			LocalDateTime localDateTime2 = LocalDateTime.of(2023, Month.JUNE, 1, 14,0);
			Appointment appointment2 = new Appointment(savedClient2, savedEmployee, localDateTime2);
			appointment2.addBeautyService(beautyServiceSaved1);
			appointment2.addBeautyService(beautyServiceSaved2);
			Appointment savedAppointment2 = appointmentService.addAppointment(appointment2);

			LocalDateTime localDateTime3 = LocalDateTime.of(2023, Month.JULY, 1, 16,0);
			Appointment appointment3 = new Appointment(savedClient, savedEmployee2, localDateTime2);
			appointment3.addBeautyService(beautyServiceSaved1);
			Appointment savedAppointment3 = appointmentService.addAppointment(appointment3);

            User admin = new User("Administrator", UserType.ADMIN, "administrator", "admin1");
			User savedAdmin = userService.addUser(admin);



			//employeeService.deleteEmployeeById(savedE.getId());
			//clientService.deleteClientById(savedClient2.getId());
			//appointmentService.deleteAppointmentById(savedAppointment.getId());

		};
	}

}

package com.SoftwareDesign.BeautySalon.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender emailSender;


    @Override
    public void sendInvoice(String to, int attachmentId) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreplyglamhaven@gmail.com");
        helper.setTo(to);
        helper.setSubject("Appointment bill");
        helper.setText("We are happy you choose us again! Can't wait to see you next time");

        FileSystemResource file
                = new FileSystemResource(new File("Bill_number_" + attachmentId  + ".pdf"));
        helper.addAttachment("Bill_number_" + attachmentId + ".pdf", file);

        emailSender.send(message);
    }

    @Override
    public void sendSalesCode(String to, int attachmentId) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreplyglamhaven@gmail.com");
        helper.setTo(to);
        helper.setSubject("Loyalty Program");
        helper.setText("Thank you for being part of our society! Here you have a little gift.");

        FileSystemResource file
                = new FileSystemResource(new File("Sales_number_" + attachmentId  + ".pdf"));
        helper.addAttachment("Sales_number_" + attachmentId + ".pdf", file);

        emailSender.send(message);
    }

    @Override
    public void sendPasswordRecovery(String to, String temporaryPassword) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreplyglamhaven@gmail.com");
        helper.setTo(to);
        helper.setSubject("Password recovery");
        helper.setText("You have a request for password recovery. Here is tour temporary password: " + temporaryPassword);


        emailSender.send(message);
    }
}

package com.SoftwareDesign.BeautySalon.email;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface EmailService {
    void sendInvoice(String to, int attachmentId) throws MessagingException, FileNotFoundException;

    void sendSalesCode(String to, int attachmentId) throws MessagingException;

    void sendPasswordRecovery(String to, String temporaryPassword) throws MessagingException;
}

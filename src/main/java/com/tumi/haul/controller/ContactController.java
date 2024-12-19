package com.tumi.haul.controller;
import com.tumi.haul.service.emailservice.ContactForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public String sendEmail(@RequestBody ContactForm contactForm) {
        log.info("HERE:{}", contactForm);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("patrickmuigai4@gmail.com");
        // message.setTo("patrickmuigai4@gmail.com"); // Your email
        message.setSubject("Contact Form Submission from " + contactForm.getName());
        message.setText("Name: " + contactForm.getName() + "\nContact: " + contactForm.getContactInfo() + "\nMessage: " + contactForm.getMessage());

        mailSender.send(message);
        return "Message sent successfully!";
    }
}


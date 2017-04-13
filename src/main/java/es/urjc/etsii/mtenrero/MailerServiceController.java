package es.urjc.etsii.mtenrero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.MailSender;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


/**
 * Created by Adrian on 4/4/17.
 */

@RestController
public class MailerServiceController {

    final String fromEmail = "vetmanagerApp@gmail.com";
    final MailerResponse emailSentSuccesfully = new MailerResponse(true,"Your email was succesfully sent!");
    final MailerResponse emailNotValid = new MailerResponse(false,"The email address you provided isn't valid");


    @Autowired
    private JavaMailSender mail;

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity<MailerResponse> sendEmail(@RequestParam("email") String email,@RequestParam("subject") String subject, @RequestParam("body") String body) {
        if (!validAddress(email)) {
            return new ResponseEntity<>(this.emailNotValid, HttpStatus.BAD_REQUEST);
        }
        try {
            this.buildEmailAndSend(email,this.fromEmail,subject,body);
            return new ResponseEntity<>(this.emailSentSuccesfully, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            MailerResponse errorResponse = new MailerResponse(false,e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean validAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public synchronized void buildEmailAndSend(String toAddress, String fromAddress, String subject, String msgBody) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(msgBody);
        mail.send(message);
    }


}

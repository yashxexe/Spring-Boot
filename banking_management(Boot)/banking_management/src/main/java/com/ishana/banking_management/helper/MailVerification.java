package com.ishana.banking_management.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ishana.banking_management.dto.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailVerification {
  @Autowired
  JavaMailSender javaMailSender;
	public void sendMail(Customer  customer) {
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
		try {
			helper.setFrom("ayushgowdapubg@gmail.com");
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		try {
			helper.setTo(customer.getEmail());
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		try {
			helper.setText("Your OTP For email verification is"+customer.getOtp());
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		javaMailSender.send(mimeMessage);
	}
	
}

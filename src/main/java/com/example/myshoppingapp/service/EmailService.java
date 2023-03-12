package com.example.myshoppingapp.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final TemplateEngine templateEngine;

  public EmailService(JavaMailSender javaMailSender,
                      TemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.templateEngine = templateEngine;
  }

  public void sendRegistrationEmail(
      String userEmail,
      String userName
  ) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

    try {
      mimeMessageHelper.setFrom("admin@ShopLizt.com");
      mimeMessageHelper.setTo(userEmail);
      //TODO: i18n
      mimeMessageHelper.setSubject("Welcome to ShopLizt!");
      mimeMessageHelper.setText(generateEmailText(userName), true);

      javaMailSender.send(mimeMessageHelper.getMimeMessage());

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }

  }

  private String generateEmailText(String userName) {
    Context ctx = new Context();
    ctx.setLocale(Locale.getDefault());
    ctx.setVariable("userName", userName);

    return templateEngine.process("email/registration", ctx);
  }

}

package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);
    public enum  EMAIL_TYPE {TRELLO, TASK};

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailCreatorService mailCreatorService;

    private SimpleMailMessage createMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        if (!mail.getMailToCc().isEmpty()) {
            mailMessage.setCc(mail.getMailToCc());
        }
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        return mailMessage;
    }



    private MimeMessagePreparator createMimeTrelloMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailCreatorService.buildTrelloCardEmail(mail.getMessage()), true);
        };
    }

    private MimeMessagePreparator createMimeTasksMessage(final Mail mail, List<TaskDto> tasksList) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailCreatorService.dailyTaskInfoMail(mail.getMessage(), tasksList), true);
        };
    }

    public void sendTasksEmail(final Mail mail, List<TaskDto> tasksList) {
        LOGGER.info("Starting e-mail preparation...");
        try {
            MimeMessagePreparator tasksMailMessage = createMimeTasksMessage(mail, tasksList);
            javaMailSender.send(tasksMailMessage);
            LOGGER.info("E-mail send");
        } catch (MailException e) {
            LOGGER.error("Mail", mail, mail);
            LOGGER.error("Sending failed", e.getMessage(), e);
        }
    }


    public void sendTrelloEmail(final Mail mail) {
        LOGGER.info("Starting e-mail preparation...");
        try {
                MimeMessagePreparator mailMessage = createMimeTrelloMessage(mail);
            javaMailSender.send(mailMessage);
            LOGGER.info("E-mail send");
        } catch (MailException e) {
            LOGGER.error("Mail", mail, mail);
            LOGGER.error("Sending failed", e.getMessage(), e);
        }
    }
}

package com.sayone.obr.scheduler;

import com.sayone.obr.repository.DownloadRepository;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.security.AppProperties;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class EmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppProperties appProperties;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
//        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");

        if (subject.toLowerCase().contains("downloads")) {
            mostDownloads(mailProperties.getUsername(), recipientEmail);

        }
        else if(subject.toLowerCase().contains("users")) {
            newUsers(mailProperties.getUsername(), recipientEmail);
        }
    }

    private void mostDownloads(String fromEmail, String toEmail) {
        try {
            logger.info("Sending Email to {}", toEmail);
            List<Map<String,Object>> mostDownloads = downloadRepository.mostDownload();

            Context context = new Context();
            context.setVariable("mostDownloads", mostDownloads);
            String body = templateEngine.process("emails/mostDownload", context);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(appProperties.getSubjectDownload());
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(message);
        } catch (MessagingException ex) {
            logger.error("Failed to send email to {}", toEmail);
        }
    }

    private void newUsers(String fromEmail, String toEmail) {
        try {
            logger.info("Sending Email to {}", toEmail);
            List<Map<String,Object>> newUsers = userRepository.newUsers(String.valueOf(LocalDate.now()));
            Context context = new Context();
            context.setVariable("newUsers", newUsers);
            String body = templateEngine.process("emails/newUsers", context);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(appProperties.getSubjectNewUsers());
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(message);
        } catch (MessagingException ex) {
            logger.error("Failed to send email to {}", toEmail);
        }
    }
}

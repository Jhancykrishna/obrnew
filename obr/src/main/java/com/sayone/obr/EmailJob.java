package com.sayone.obr;

import com.sayone.obr.repository.UserRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class EmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private  TemplateEngine templateEngine;

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    UserRepository userRepository;



    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            Instant in = new Date().toInstant().minus(24, ChronoUnit.HOURS);
            Date date = Date.from(in);
            String recipientEmail = jobDataMap.getString("email");
            sendMail(mailProperties.getUsername(), recipientEmail, date);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

   private void sendMail(String fromEmail, String toEmail, Date date) {
        try {
            logger.info("Sending Email to {}", toEmail);
            List<Map<String, Object>> user= userRepository.findUsersByDate(date);
            MimeMessage message = mailSender.createMimeMessage();
            Context context = new Context();
            context.setVariable("newSignupUsers",user);
            String process = templateEngine.process("emails/emailjoblist", context);
            String subject = "List of new logged users";
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setSubject(subject);
            messageHelper.setText(process, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);
            mailSender.send(message);
        } catch (MessagingException ex) {
            logger.error("Failed to send email to {}", toEmail);
        }
    }
}


//            List<Map<String, Object>> loggedUsers=loggedUser.getNewUsersByDate(date);
////            String list =jobDataMap.getString("loggedUsers");
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            String loggedInUserStr = objectMapper.writeValueAsString(jobDataMap.get("loggedUsers"));
//            List<Object> list1 = objectMapper.readValue(loggedInUserStr, List.class);
//            List<Map<String,Object>> list2 = new ArrayList<>();
//            for(Object o:list1){
//                String objectStr= objectMapper.writeValueAsString(o);
//                Map <String,Object> mar = objectMapper.readValue(objectStr,Map.class);
//                list2.add(mar);
//                System.out.println(mar.get("first_name"));
//            }
//            System.out.println(loggedUsers);



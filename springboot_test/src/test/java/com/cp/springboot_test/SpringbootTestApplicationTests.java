package com.cp.springboot_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class SpringbootTestApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() {
        //一个简单的邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("你好！");
        simpleMailMessage.setText("谢谢你");
        simpleMailMessage.setTo("770047395@qq.com");
        simpleMailMessage.setFrom("770047395@qq.com");
        mailSender.send(simpleMailMessage);
    }

    @Test
    void contextLoads2() throws MessagingException {
        //一个复杂的邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);


        helper.setSubject("hhh");
        helper.setText("<p style='color:red'>asdlkjlasdj</p>",true);

        helper.setFrom("770047395@qq.com");
        helper.setTo("770047395@qq.com");

        mailSender.send(mimeMessage);
    }

}

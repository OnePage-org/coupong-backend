package com.onepage.coupong.infrastructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.naver.id}")
    private String id;
    @Value("${mail.naver.password}")
    private String password;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        /* SMTP 서버명 */
        javaMailSender.setHost("smtp.naver.com");
        /* 네이버 아이디 */
        javaMailSender.setUsername(id);
        /* 네이버 비밀번호 */
        javaMailSender.setPassword(password);
        /* SMTP 포트 */
        javaMailSender.setPort(465);
        /* 메일 인증서버 가져오기 */
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

    /* 메일 인증서버 정보 가져오기 */
    private Properties getMailProperties() {
        Properties properties = new Properties();
        /* 프로토콜 설정 */
        properties.setProperty("mail.transport.protocol", "smtp");
        /* SMTP 인증 */
        properties.setProperty("mail.smtp.auth", "true");
        /* SMTP starttless 사용 */
        properties.setProperty("mail.smtp.starttls.enable", "true");
        /* 디버그 사용 */
        properties.setProperty("mail.debug", "true");
        /* ssl 인증 서버 (smtp 서버명) */
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com");
        /* ssl 사용 */
        properties.setProperty("mail.smtp.ssl.enable", "true");

        return properties;
    }
}

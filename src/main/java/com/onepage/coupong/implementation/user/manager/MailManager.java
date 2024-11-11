package com.onepage.coupong.implementation.user.manager;

import com.onepage.coupong.business.user.dto.request.CertificationCheckReq;
import com.onepage.coupong.business.user.dto.request.EmailCertificationReq;
import com.onepage.coupong.implementation.user.CertificationException;
import com.onepage.coupong.implementation.user.enums.CertificationExceptionType;
import com.onepage.coupong.infrastructure.mail.exception.MailException;
import com.onepage.coupong.infrastructure.mail.exception.enums.MailExceptionType;
import com.onepage.coupong.jpa.user.Certification;
import com.onepage.coupong.persistence.user.CertificationRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailManager {

    /* MailConfig 에서 등록해둔 Bean을 autowired 해 사용하기 */
    private final JavaMailSender emailSender;
    private final CertificationRepository certificationRepository;

    @Value("${mail.naver.id}")
    private String from;

    public String createCertificationNumber() {

        /* 인증번호를 위한 임의적인 4자리 숫자 생성 메서드 */
        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }

    public MimeMessage createMessage(String to, String certificationNumber) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, to);

            /* 이메일 제목 */
            message.setSubject("[coupong] 회원가입 이메일 인증코드");

            /* 메일 내용 작성 */
            String mailMessage = "";
            mailMessage += "<h1 style='text-align: center;'>[coupong] 회원가입 인증메일</h1>";
            mailMessage += "<h3 style='text-align: center;'>인증코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>"
                    + certificationNumber + "</strong></h3>";

            /* 메일 내용, charset타입, subtype */
            message.setText(mailMessage, "utf-8", "html");

            /* 보내는 사람의 이메일 주소 */
            message.setFrom(from);
        } catch (MessagingException e) {
            throw new MailException(MailExceptionType.MAIL_SEND_ERROR);
        }
        return message;
    }

    public void sendMail(MimeMessage message) {
        emailSender.send(message);
    }

    public void saveCertificationNumber(EmailCertificationReq emailCertificationReq, String certificationNumber) {
        Certification certification = Certification.builder()
                .username(emailCertificationReq.getUsername())
                .email(emailCertificationReq.getEmail())
                .certification(certificationNumber)
                .build();
        certificationRepository.save(certification);
    }

    public boolean isAvailableCertification(CertificationCheckReq certificationCheckReq) {

        /* 해당 사용자에 대한 인증번호 정보가 없다면 에러 반환 */
        Certification certification = certificationRepository.findByUsername(certificationCheckReq.getUsername())
                .orElseThrow(() -> new CertificationException(CertificationExceptionType.CERTIFICATION_NOT_FOUND));

        /* 인증번호 검증을 위해 클라이언트가 작성한 인증번호가 다르다면 에러 반환 */
        if (!certification.getCertification().equals(certificationCheckReq.getCertification())) {
            throw new CertificationException(CertificationExceptionType.CERTIFICATION_UNAVAILABLE);
        }

        certificationRepository.delete(certification);

        return true;
    }
}

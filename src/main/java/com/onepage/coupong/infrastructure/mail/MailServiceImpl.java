package com.onepage.coupong.infrastructure.mail;

import com.onepage.coupong.user.dto.request.CheckEmailCertificationRequestDto;
import com.onepage.coupong.user.dto.request.EmailCertificationRequestDto;
import com.onepage.coupong.user.dto.response.ResponseDto;
import com.onepage.coupong.user.dto.response.CheckEmailCertificationResponseDto;
import com.onepage.coupong.user.dto.response.EmailCertificationResponseDto;
import com.onepage.coupong.user.domain.Certification;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

/* @@@@@@@@ MimeMessage 클래스에 대한 설명 필요 @@@@@@@@@@@@@@ */

@Service
@RequiredArgsConstructor
@Transactional
public class MailServiceImpl implements MailService {

    /* MailConfig 에서 등록해둔 Bean을 autowired 해 사용하기 */
    private final JavaMailSender emailSender;

    private final CertificationRepository certificationRepository;

    /* 사용자가 메일로 받을 인증번호 */
    private String key;

    @Value("${mail.naver.id}")
    private String id;

    /* 메일 발송
    * createMessage() 의 매개변수 to는 이메일 주소가 되고,
    * MimeMessage 객체 안에 전송할 메일의 내용을 담는다.
    * Bean으로 등록해둔 javaMail 객체를 사용해 이메일을 발송한다. */
    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> sendMessage(EmailCertificationRequestDto dto) throws Exception {

        /* 인증번호로 활용할 무작위의 4자리 숫자를 생성해서 key에 넣어준다. */
        key = RandomNumber.getCertificationNumber();

        /* 인증번호를 받는 사람의 이메일 주소 */
        String to = dto.getEmail();
        String username = dto.getUsername();

        /* to로 메일 발송 */
        MimeMessage message = createMessage(to);

        try {

            /* 메일로 보내주는 메서드 */
            emailSender.send(message);

            /* 이메일 인증 번호 정보 DB 저장
            * 해당 데이터는 인증 번호 검증에 사용한다. */
            Certification certification = new Certification(username, to, key);
            certificationRepository.save(certification);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    private static class RandomNumber {

        /* 인증번호를 위한 임의적인 4자리 숫자 생성 메서드 */
        public static String getCertificationNumber() {
            StringBuilder certificationNumber = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                certificationNumber.append((int) (Math.random() * 10));
            }

            return certificationNumber.toString();
        }
    }

    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);

        /* 이메일 제목 */
        message.setSubject("[coupong] 회원가입 이메일 인증코드");

        /* 메일 내용 작성 */
        String mailMessage = "";
        mailMessage += "<h1 style='text-align: center;'>[coupong] 회원가입 인증메일</h1>";
        mailMessage += "<h3 style='text-align: center;'>인증코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>"
                +  key + "</strong></h3>";

        /* 메일 내용, charset타입, subtype */
        message.setText(mailMessage, "utf-8", "html");

        /* 보내는 사람의 이메일 주소, 보내는 사람 이름 */
        message.setFrom(id);

        return message;
    }

    /* 사용자가 입력한 인증번호와 서버에서 생성한 인증번호를 비교하는 메서드 */
    @Override
    public ResponseEntity<? super CheckEmailCertificationResponseDto> verifyCode(CheckEmailCertificationRequestDto dto) {

        try {
            /* 해당 사용자에 대한 인증번호 정보가 없다면 에러 반환 */
            String code = dto.getCertification();
            Certification certification = certificationRepository.findCertificationByUsername(dto.getUsername());
            if (certification == null) {
                return CheckEmailCertificationResponseDto.certificationFailed();
            }

            /* 인증번호 검증을 위해 클라이언트가 작성한 인증번호가 다르다면 에러 반환 */
            if (!code.equals(certification.getCertification())) {
                return CheckEmailCertificationResponseDto.certificationFailed();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CheckEmailCertificationResponseDto.success();
    }
}

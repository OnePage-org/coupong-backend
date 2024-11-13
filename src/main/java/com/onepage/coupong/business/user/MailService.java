package com.onepage.coupong.business.user;

import com.onepage.coupong.business.user.dto.request.CertificationCheckReq;
import com.onepage.coupong.business.user.dto.request.EmailCertificationReq;
import com.onepage.coupong.implementation.user.manager.MailManager;
import com.onepage.coupong.presentation.user.MailUseCase;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService implements MailUseCase {

    private final MailManager mailManager;

    /* 메일 발송
     * createMessage() 의 매개변수 to는 이메일 주소가 되고,
     * MimeMessage 객체 안에 전송할 메일의 내용을 담는다.
     * Bean으로 등록해둔 javaMail 객체를 사용해 이메일을 발송한다. */
    @Override
    public boolean sendMessage(EmailCertificationReq emailCertificationReq) {

        /* 인증번호로 활용할 무작위의 4자리 숫자를 생성 */
        String certificationNumber = mailManager.createCertificationNumber();

        /* 수신자(to)에게 보낼 메시지 본문 작성 */
        MimeMessage message = mailManager.createMessage(emailCertificationReq.getEmail(), certificationNumber);

        /* 이메일 인증 번호 정보 DB 저장 해당 데이터는 인증 번호 검증에 사용한다. */
        mailManager.saveCertificationNumber(emailCertificationReq, certificationNumber);

        /* 메일 전송 */
        mailManager.sendMail(message);

        return true;
    }

    /* 사용자가 입력한 인증번호와 서버에서 생성한 인증번호를 비교하는 메서드 */
    @Override
    public boolean isAvailableCertification(CertificationCheckReq certificationCheckReq) {
        return mailManager.isAvailableCertification(certificationCheckReq.getUsername(), certificationCheckReq.getCertification());
    }
}

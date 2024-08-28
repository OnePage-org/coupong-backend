package com.onepage.coupong.sign.service.implement;

import com.onepage.coupong.sign.dto.request.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.request.SignUpRequestDto;
import com.onepage.coupong.sign.dto.response.ResponseDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.dto.response.auth.SignUpResponseDto;
import com.onepage.coupong.sign.entity.Certification;
import com.onepage.coupong.sign.entity.User;
import com.onepage.coupong.sign.repository.CertificationRepository;
import com.onepage.coupong.sign.repository.UserRepository;
import com.onepage.coupong.sign.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /* 아이디 중복 검사 확인 서비스 */
    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {

            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) {
                return IdCheckResponseDto.duplicated();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IdCheckResponseDto.success();
    }

    /* 회원가입 서비스 */
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {

            String username = dto.getUsername();
            boolean isExistUser = userRepository.existsByUsername(username);
            if (isExistUser) {
                return SignUpResponseDto.duplicated();
            }

            String email = dto.getEmail();
            String certificationNumber = dto.getCertification();
            Certification certification = certificationRepository.findCertificationByUsername(username);
            if (certification == null) {
                return ResponseDto.validationFailed();
            }

            boolean isMatched = certification.getEmail().equals(email)
                    && certification.getCertification().equals(certificationNumber);

            if (!isMatched) {
                return SignUpResponseDto.certificationFailed();
            }

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            User user = new User(dto);
            userRepository.save(user);

            certificationRepository.deleteByUsername(username);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }
}

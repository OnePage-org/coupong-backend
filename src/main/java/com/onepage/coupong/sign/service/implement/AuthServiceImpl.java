package com.onepage.coupong.sign.service.implement;

import com.onepage.coupong.sign.dto.request.IdCheckRequestDto;
import com.onepage.coupong.sign.dto.response.ResponseDto;
import com.onepage.coupong.sign.dto.response.auth.IdCheckResponseDto;
import com.onepage.coupong.sign.repository.UserRepository;
import com.onepage.coupong.sign.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

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
}

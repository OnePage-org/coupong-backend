package com.onepage.coupong.implementation.user.manager;

import com.onepage.coupong.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpManager {

    private final UserRepository userRepository;

    public boolean duplicateCheckId(String username) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        return true;
    }
}

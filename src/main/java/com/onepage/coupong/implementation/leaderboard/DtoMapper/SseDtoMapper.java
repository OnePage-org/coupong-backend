package com.onepage.coupong.implementation.leaderboard.DtoMapper;

import com.onepage.coupong.business.leaderboard.dto.SseEmitterDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseDtoMapper {

    public static SseEmitterDto toAddSseEmitter(SseEmitter sseEmitter) {
        return new SseEmitterDto(sseEmitter);
    }


}

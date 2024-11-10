package com.onepage.coupong.presentation.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilteringControllerResp {
    BAN_WORD("금칙어 해당"),
    ALLOW_WORD("금칙어 통과");

    private String message;
}

package com.onepage.coupong.implementation.chat;

import com.onepage.coupong.global.exception.CustomRuntimeException;
import com.onepage.coupong.implementation.chat.enums.ChatExceptionType;

public class ChatFilterException extends CustomRuntimeException {
    public ChatFilterException(ChatExceptionType message, Object... args) {super(String.valueOf(message), args);}

}

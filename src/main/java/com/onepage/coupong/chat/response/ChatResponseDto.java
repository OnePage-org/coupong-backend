package com.onepage.coupong.chat.response;

import com.onepage.coupong.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter @AllArgsConstructor
public class ChatResponseDto {

    private String status;
    private ChatMessage data;
    private String message;

    /*기본 생성자는 성공으로 설정*/
    public ChatResponseDto(ChatMessage data){
        this.status = "success";
        this.data = data;
        this.message = null;
    }

    /* 성공 */
    public static ResponseEntity<ChatResponseDto> successChat(ChatMessage data) {
        ChatResponseDto responsebody = new ChatResponseDto(data);
        return ResponseEntity.ok(responsebody);
    }

    /* 400 : no data */
    public static ResponseEntity<ChatResponseDto> noChat(ChatMessage data) {
        ChatResponseDto responsebody = new ChatResponseDto("error", data, "no data chatMessage");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsebody);
    }

    /* 413 : too long */
    public static ResponseEntity<ChatResponseDto> tooLongChat(ChatMessage data) {
        ChatResponseDto responsebody = new ChatResponseDto("error", data, "too long chatMessage data");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(responsebody);
    }
}

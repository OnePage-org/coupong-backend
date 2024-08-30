package com.onepage.coupong.common;

public interface ResponseMessage {

    /* 모든 경우에 발생하는 경우 */
    /* 성공 */
    String SUCCESS = "Success.";

    /* DB 에러 */
    String DATABASE_ERROR = "Database Error.";

    /* 사용자의 요구 사항에 대해 실패 */
    String VALIDATION_FAILED = "Validation Failed.";

    /* 회원가입 및 로그인 관련 */
    /* 아이디 중복 */
    String DUPLICATED_ID = "Duplicated Id.";

    /* 로그인 실패 */
    String SIGN_IN_FAILED = "Login information mismatch.";

    /* 인증 실패 */
    String CERTIFICATION_FAILED = "Certification Failed.";

    /* 메일 전송 실패 */
    String MAIL_FAILED = "Mail send Failed.";


}

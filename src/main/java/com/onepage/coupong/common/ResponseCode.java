package com.onepage.coupong.common;

public interface ResponseCode {

    /* 모든 경우에 발생하는 경우 */
    /* 성공 */
    String SUCCESS = "SU";

    /* DB 에러 */
    String DATABASE_ERROR = "DBE";

    /* 사용자의 요구 사항에 대해 실패 */
    String VALIDATION_FAILED = "VF";

    /* 회원가입 및 로그인 관련 */
    /* 아이디 중복 */
    String DUPLICATED_ID = "DI";

    /* 로그인 실패 */
    String SIGN_IN_FAILED = "SF";

    /* 인증 실패 */
    String CERTIFICATION_FAILED = "CF";

    /* 메일 전송 실패 */
    String MAIL_FAILED = "MF";

}

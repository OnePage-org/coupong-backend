package com.onepage.coupong.common;

public class RandomNumber {

    /* 인증번호를 위한 임의적인 4자리 숫자 생성 메서드 */
    public static String getCertificationNumber() {
        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }
}

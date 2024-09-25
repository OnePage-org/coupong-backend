package com.onepage.coupong.sign.common;

/* 굳이 common 패키지에 만들지 않고 이메일 전송 쪽에 둬도 되긴하는데,
* 혹시나 해당 랜덤번호를 만드는 메서드가 필요한 곳이 있을수도... 있으니 ? (추후에 위치 변경 가능) */
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

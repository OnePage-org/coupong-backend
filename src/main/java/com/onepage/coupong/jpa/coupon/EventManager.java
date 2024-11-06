package com.onepage.coupong.jpa.coupon;

import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class EventManager {
    private String couponName;
    private CouponCategory couponCategory;
    private LocalDateTime startTime;
    private int couponCount;
    private int publish_nums;  // 다중 이벤트 환경에서 당첨자 리스트 만들 때 아웃오브 인덱스 에러 막기 위해
    private int endNums = 0;  //이벤트 종료 조건 쿠폰 잔여 개수
    private Map<Object, Coupon> userCouponMap;

    public EventManager(String couponName, CouponCategory couponCategory, LocalDateTime startTime, int couponCount, int endNums) {
        this.couponName = couponName;
        this.couponCategory = couponCategory;
        this.startTime = startTime;
        this.couponCount = couponCount;
        this.publish_nums = couponCount;
        this.endNums = endNums;
        this.userCouponMap = new HashMap<>();
    }

    public synchronized void decreaseCouponCount() {
        couponCount--;
    }

    public boolean eventEnd() {
        return couponCount == endNums;
    }

    //userId long 으로 받으러 하면
    //java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Long (java.lang.String and java.lang.Long are in module java.base of loader 'bootstrap')
    public void addUserCoupon(String userId, Coupon coupon) {
        userCouponMap.put(userId, coupon);
    }

}

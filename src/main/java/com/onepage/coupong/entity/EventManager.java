package com.onepage.coupong.entity;

import com.onepage.coupong.entity.enums.CouponCategory;
import jdk.jfr.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class EventManager {
    private CouponCategory couponCategory;
    private int couponCount;
    private int endNums = 0;  //이벤트 종료 조건 쿠폰 잔여 개수
    private Map<Object, Coupon> userCouponMap;

    public EventManager(CouponCategory couponCategory, int couponCount, int endNums) {
        this.couponCategory = couponCategory;
        this.couponCount = couponCount;
        this.endNums = endNums;
        this.userCouponMap = new HashMap<>();
    }

    public EventManager(CouponCategory couponCategory, int couponCount) {
        this.couponCategory = couponCategory;
        this.couponCount = couponCount;
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

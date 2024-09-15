package com.onepage.coupong.entity;

import com.onepage.coupong.entity.enums.CouponCategory;
import jdk.jfr.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventManager {
    private CouponCategory couponCategory;
    private int couponCount;
    private int endNums = 0;  //이벤트 종료 조건 쿠폰 잔여 개수

    public EventManager(CouponCategory couponCategory, int couponCount, int endNums) {
        this.couponCategory = couponCategory;
        this.couponCount = couponCount;
        this.endNums = endNums;
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

}

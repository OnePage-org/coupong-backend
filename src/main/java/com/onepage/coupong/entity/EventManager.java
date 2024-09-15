package com.onepage.coupong.entity;

import jdk.jfr.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventManager {
    private EventType eventType;
    private int couponCount;
    private int endNums = 0;  //이벤트 종료 조건 쿠폰 잔여 개수

    public EventManager(EventType eventType, int couponCount, int endNums) {
        this.eventType = eventType;
        this.couponCount = couponCount;
        this.endNums = endNums;
    }

    public EventManager(EventType eventType, int couponCount) {
        this.eventType = eventType;
        this.couponCount = couponCount;
    }

    public synchronized void decreaseCouponCount() {
        couponCount--;
    }

    public boolean eventEnd() {
        return couponCount == endNums;
    }

}

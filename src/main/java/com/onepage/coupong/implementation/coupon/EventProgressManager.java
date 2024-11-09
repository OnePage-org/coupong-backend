package com.onepage.coupong.implementation.coupon;

import com.onepage.coupong.jpa.coupon.Coupon;
import com.onepage.coupong.jpa.coupon.CouponEvent;
import com.onepage.coupong.jpa.coupon.enums.CouponCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class EventProgressManager {
    private String couponName;
    private CouponCategory couponCategory;
    private LocalDateTime startTime;
    private int couponCount;
    private int publish_nums;
    private int endNums = 0;
    private Map<Object, Coupon> userCouponMap;

    private EventProgressManager(String couponName, CouponCategory couponCategory, LocalDateTime startTime, int couponCount, int endNums) {
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

    public void addUserCoupon(String userId, Coupon coupon) {
        userCouponMap.put(userId, coupon);
    }

    public static EventProgressManager of(CouponEvent couponEvent) {
        return new EventProgressManager(couponEvent.getName(), couponEvent.getCategory(), couponEvent.getDate(), couponEvent.getCoupon_publish_nums(), 0);
    }
}

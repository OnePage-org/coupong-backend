package com.onepage.coupong.coupon.domain;

import com.onepage.coupong.coupon.domain.enums.CouponCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CouponEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_event_id")
    private Long id;

    @Setter
    @Column(name = "coupon_event_name")
    private String name;

    @Setter  //테스트용
    @Column(name = "coupon_event_date")
    private LocalDateTime date;

    @Setter //테스트용
    private int coupon_publish_nums;

    @Column(name = "coupon_category")
    @Enumerated(EnumType.STRING)
    private CouponCategory category;

    @Setter //테스트용
    @Column(name = "coupon_event_duration")
    private String duration;

    @OneToMany(mappedBy = "couponEventId", fetch = FetchType.EAGER)  //Lazy로 하면 이벤트 세팅할때 에러가 남, 어차피 세팅은 막 전날 어유로울 때하는거니까 미리 리스트를 만들어 놓는게 나을 듯, 서비스 운영중에 뒤늦게 쿼리문 날리는 거보다.
    private List<Coupon> couponList;
}
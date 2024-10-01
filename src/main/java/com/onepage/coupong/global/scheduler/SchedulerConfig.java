package com.onepage.coupong.global.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // 어떤 수치가 적절한지, CPU 코어 개수 + 1이 무조건 옳은 것인가? 의논 필요
        scheduler.setThreadNamePrefix("CouponEventScheduler-");
        scheduler.initialize();
        return scheduler;
    }
}
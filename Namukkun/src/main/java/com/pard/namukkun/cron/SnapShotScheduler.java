package com.pard.namukkun.cron;

import com.pard.namukkun.Data;
import com.pard.namukkun.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapShotScheduler {
    private final PostService postService;

    @Scheduled(cron = "0 0 0 * * *") // 00시 00분 마다 실행
//    @Scheduled(cron = "30 * * * * *") // 테스트용 -> 매 30초 마다
    public void run() {
        Integer changeNum = postService.postCheck(Data.getNowDate());
        log.info("-----------------------------------------");
        log.info("CronBot operated. TIME : {}", Data.getNowDate());
        log.info("Chagne to DONE : {}", changeNum);
        log.info("-----------------------------------------");
    }
}
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
    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        postService.postCheck(Data.getNowDate());






    }
}
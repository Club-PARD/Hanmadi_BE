package com.pard.namukkun;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class httpsController {
    @GetMapping("")
    public Integer get() {
        return 200;
    }

    @PostMapping("")
    public Integer post() {
        return 200;
    }
}

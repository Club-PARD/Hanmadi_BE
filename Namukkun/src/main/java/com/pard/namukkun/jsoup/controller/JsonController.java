package com.pard.namukkun.jsoup.controller;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class JsonController {

    @PostMapping("/api/parse-html")
    public ResponseEntity<String> parseHTML(@RequestBody String htmlContent) {
        try {
            // HTML 파싱
            Document doc = Jsoup.parse(htmlContent);

            // 예시: <div> 태그 내의 모든 텍스트 추출
            Elements divElements = doc.select("div");
            for (Element div : divElements) {
                String text = div.text();
                System.out.println("Text in <div>: " + text);
            }

            // 예시: 이미지 태그 내의 src 속성 추출
            Elements imgElements = doc.select("img");
            for (Element img : imgElements) {
                String imageUrl = img.attr("src");
                System.out.println("Image URL: " + imageUrl);
            }

            return ResponseEntity.status(HttpStatus.OK).body("HTML 파싱 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTML 파싱 오류: " + e.getMessage());
        }
    }
}

package com.pard.namukkun.attachment.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.pard.namukkun.attachment.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController("/file")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    @Operation(summary = "파일등록",description = "파일 이름을 입력하고 파일음 첨부합니다.")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName") MultipartFile fileName) {
        return attachmentService.upload(fileName);
    }

    @GetMapping("/download")
    @Operation(summary = "파일 다운로드", description = "파일을 다운로드합니다. key값 : 파일 이름임")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        return attachmentService.download(fileName);
    }

    @GetMapping("/delete")
    @Operation(summary = "파일 삭제", description = "파일을 삭제합니다.")
    public String deleteFile(@RequestParam("fileName") String fileName) throws IOException {
        return attachmentService.delete(fileName);
    }

}
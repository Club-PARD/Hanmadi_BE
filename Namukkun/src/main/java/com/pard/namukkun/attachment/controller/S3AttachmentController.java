package com.pard.namukkun.attachment.controller;

import com.pard.namukkun.attachment.service.S3AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor

public class S3AttachmentController {

    private final S3AttachmentService s3AttachmentService;

    @GetMapping("/download")
    @Operation(summary = "첨부파일 다운로드", description = "첨부파일 url을 받아서 다운로드합니다.")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
        return s3AttachmentService.download(fileUrl);
    }

}
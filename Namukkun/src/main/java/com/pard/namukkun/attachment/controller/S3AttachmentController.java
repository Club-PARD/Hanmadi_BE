package com.pard.namukkun.attachment.controller;

import com.pard.namukkun.attachment.service.S3AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor

public class S3AttachmentController {

    private final S3AttachmentService s3AttachmentService;

    @GetMapping("/download")
    public ResponseEntity<UrlResource> downloadFile(@RequestParam("fileName") String fileName) {
        return s3AttachmentService.download(fileName);
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("fileName") String fileName) {
        return s3AttachmentService.delete(fileName);
    }
}
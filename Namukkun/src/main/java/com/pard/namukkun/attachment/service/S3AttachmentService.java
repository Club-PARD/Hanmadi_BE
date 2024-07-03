package com.pard.namukkun.attachment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class S3AttachmentService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseEntity<String> upload(MultipartFile file, String fileName) {
        try {
            String fileUrl= "https://" + bucket + "/test" +fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
            amazonS3Client.setObjectAcl(bucket,fileName, CannedAccessControlList.PublicRead);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<UrlResource> download(String fileName) {
        UrlResource urlResource = new UrlResource(amazonS3Client.getUrl(bucket,fileName));

        String contentDisposition = "attachment; filename=\"" + fileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    public String delete(String fileName) {
        try{
            amazonS3Client.deleteObject(bucket,fileName);
            return "delete success";
        } catch (AmazonS3Exception e) {
            return "fail delete error message : "+e.getMessage();
        }
    }

    public String getUrlWithFileName(String fileName) {
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }
}
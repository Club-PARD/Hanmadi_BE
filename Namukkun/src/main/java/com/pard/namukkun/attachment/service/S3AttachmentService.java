package com.pard.namukkun.attachment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

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

    public void uploadFile(File file, String fileName) {
        try {
            String contentType = getContentTypeByFileName(fileName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType); // 예시: 이미지 파일의 MIME 유형
            metadata.setContentLength(file.length());
            metadata.setHeader("Content-Disposition", "inline"); // 웹으로 표시되도록 변경

            PutObjectRequest request = new PutObjectRequest(bucket, fileName, file)
                    .withMetadata(metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead); // 권한 설정

            amazonS3Client.putObject(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 파일 확장자에 따라 MIME 유형을 결정하는 메서드
    private String getContentTypeByFileName(String fileName) {
        if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream"; // 기본적으로 이진 파일로 설정
        }
    }


    public ResponseEntity<ByteArrayResource> download(String fileUrl) throws IOException {
        try {
            // 파일명이 한글일때 영어일때 구분해서 디코딩하기 위함
            // URL을 URI로 변환
            URI uri = new URI(fileUrl);

            // URI에서 path부부능ㄹ 가져옴
            String path = uri.getPath();

            // 객체 키 추출
            String objectKey = extractObjectKey(path);
            log.info("키값 추출 완료 : " + objectKey);
            // S3 객체 다운로드 요청
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, objectKey));
            InputStream objectData = s3Object.getObjectContent();
            log.info("다운로드 요청 완료");
            // 객체 데이터를 바이트로 읽어옴
            byte[] bytes = objectData.readAllBytes();
            log.info("바이트 읽기 완료 : " + bytes.length);
            // S3객체에서 파일 이름 추출
            String fileName = objectKey.substring(objectKey.lastIndexOf("_") + 1);
            log.info("파일 이름 추출 : " + fileName);

            // 다운로드할 파일의 타입설정
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (fileName.toLowerCase().endsWith(".jpg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (fileName.toLowerCase().endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.toLowerCase().endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if (fileName.toLowerCase().endsWith(".txt")) {
                mediaType = MediaType.TEXT_PLAIN;
            } else if (fileName.toLowerCase().endsWith(".hwp")) {
                mediaType = MediaType.parseMediaType("application/haansofthwp");
            } else if (fileName.toLowerCase().endsWith(".doc")) {
                mediaType = MediaType.parseMediaType("application/msword");
            } else if (fileName.toLowerCase().endsWith(".docx")) {
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }
            log.info(mediaType.toString());

            // 응답 반환
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8").replace("+", "%20") + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // S3에 저장된 객체 URL에서 다른거 다 빼고 UUID_파일이름 만 추출하는 메서드
    public String extractObjectKey(String fileUrl) {
        int index = fileUrl.lastIndexOf("/"); // 마지막 / 의 인덱스를 찾음
        if(index != -1) {
            String objectKeyEncoded = fileUrl.substring(index + 1); // "/" 이거 다음부터 문자 추출
            return java.net.URLDecoder.decode(objectKeyEncoded, StandardCharsets.UTF_8);
        } else {
            // 잘못된 Url일경우 에러처리
            throw new IllegalArgumentException("Invalid file URL: " + fileUrl);
        }
    }

    // UUID_파일이름인 경우에 파일이름만 추출하는 메서드
    public String extractFileName(String originalFileName) {
        int index = originalFileName.indexOf("_");
        if (index != -1 && index < originalFileName.length() - 1) {
            return originalFileName.substring(index + 1);
        } else {
            // '_'가 없거나 마지막에 위치한 경우
            return originalFileName;
        }
    }


    // 파일 이름 받아서 첨부파일 삭제
    public ResponseEntity<?> deleteByName(String fileName){
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,fileName));
            log.info("delete file : " + fileName);
            return ResponseEntity.ok().build();
        } catch (AmazonS3Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // url 받아서 첨부파일 삭제
    public ResponseEntity<?> deleteByUrl(String fileUrl){
        String objectKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,objectKey));
            return ResponseEntity.ok().build();
        } catch (AmazonS3Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String getUrlWithFileName(String fileName) {
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }


    public File downloadFileFromS3(String objectUrl) throws IOException {
        try {
            // URL을 URI로 변환하여 객체 키 추출
            URI uri = new URI(objectUrl);
            String objectKey = extractObjectKey(uri.getPath());

            // 파일 이름만 추출
            String fileName = extractFileName(extractObjectKey(objectUrl));
            fileName = URLEncoder.encode(fileName,StandardCharsets.UTF_8.toString());

            // S3에서 객체 다운로드
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, objectKey));
            File tempFile = File.createTempFile(fileName,"");

            // S3 객체를 임시 파일에 복사
            FileUtils.copyInputStreamToFile(s3Object.getObjectContent(), tempFile);

            // 다운로드된 임시 파일 반환
            return tempFile;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 객체 URL", e);
        }
    }

    // 객체 Url로 S3 버킷에 파일이 있는지 여부 확인하는 메서드
    public boolean isObjectExist(String objectUrl) {
        try{
            ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(bucket, objectUrl);
            return objectMetadata != null;
        } catch (AmazonS3Exception e) {
            return false;
        }
    }

}
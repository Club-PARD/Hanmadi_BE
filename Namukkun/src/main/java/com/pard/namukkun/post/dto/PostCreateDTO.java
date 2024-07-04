package com.pard.namukkun.post.dto;

import com.pard.namukkun.image.dto.ImageCreateDTO;
import com.pard.namukkun.image.dto.ImageReadDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PostCreateDTO {
    private String title; // 제목
    private Integer postRegion; // 지역
    private Integer upCountPost; // 추천수
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과

    private List<String> fileName; // 첨부파일 url
    private List<ImageReadDTO> images; // Base64로 인코딩된 image들
    private List<ImageCreateDTO> imageCreateDTOS; // Base64로 인코딩된 image들

    private boolean isDone; // 작성 후 7일이 지난거
    private boolean isReturn; // 게시물 업로드 확인

    private Long userId; // 작성자의 Id값 (고유 아이디)
}

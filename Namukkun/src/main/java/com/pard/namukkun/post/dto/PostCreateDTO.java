package com.pard.namukkun.post.dto;

import com.pard.namukkun.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    private boolean isDone; // 제출완료 (작성 후 7일이 지난거)

    private Long userId; // 작성자의 Id값 (고유 아이디)
}

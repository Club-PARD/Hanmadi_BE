package com.pard.namukkun.post.dto;

import com.pard.namukkun.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostCreateDTO {
    private String title; // 제목
    private Integer postRegion; // 지역
    private Integer upCountPost; // 추천수
    private String problem; //문제정의
    private String solution; //해결방법
    private String benefit; // 기대효과
    private boolean isDone; // 제출완료 (작성 후 7일이 지난거)
    private Long userId; // 작성자의 Id값 (고유 아이디)
}

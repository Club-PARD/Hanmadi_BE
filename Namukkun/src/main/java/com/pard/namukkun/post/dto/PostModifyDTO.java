package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PostModifyDTO {
    private Long postId;
    private String title; // 제목
    private int postLocal; // 지역
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과
    private List<String> fileNames; // 파일이름들

    private boolean isReturn;

    public PostModifyDTO(Post post, List<String> fileNames) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.postLocal = post.getPostLocal();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isReturn = post.isReturn();
        this.fileNames = fileNames;
    }
}

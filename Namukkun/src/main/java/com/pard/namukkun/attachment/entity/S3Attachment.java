package com.pard.namukkun.attachment.entity;

import com.pard.namukkun.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class S3Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public S3Attachment(String fileUrl, Post post) {
        this.fileUrl = fileUrl;
        this.post = post;
    }

    public void setFileUrl(String fileUrl, Post post) {
        this.fileUrl = fileUrl;
        this.post = post;
    }
}

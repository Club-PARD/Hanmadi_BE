package com.pard.namukkun.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UpPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long upPostId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_Id")
    @JsonBackReference
    private User user;

    private Long postId;

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.pard.namukkun.comment.repo;

import com.pard.namukkun.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
//    public List<Comment> findAllByPostId(Long postId);

}

package com.pard.namukkun.postit.service;


import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.comment.repo.CommentRepo;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.postit.dto.PostItMoveDTO;
import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.postit.repo.PostItRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostItService {
    private final CommentRepo commentRepo;
    private final PostItRepo postItRepo;
    private final UserRepo userRepo;


    // 포스트잇 생성
    public void createPostIt(PostItCreateDTO dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        PostIt postIt = PostIt.toEntity(dto, user);
        postItRepo.save(postIt);
    }

    // 포스트잇 삭제
    public void deletePostIt(Long postItId) {
        postItRepo.deleteById(postItId);
    }

    // 포스트잇 이동
    public void movePostIt(PostItMoveDTO dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow();


        PostIt postIt = PostIt.toEntity(dto, user);
        postItRepo.save(postIt);
        postItRepo.save()

    }
}

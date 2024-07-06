package com.pard.namukkun.postit.service;


import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.comment.repo.CommentRepo;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
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
    private final PostRepo postRepo;


    // 포스트에 있는 포스트잇 개수 확인
    public Integer getPostPostItCounter(Long PostId) {
        return postRepo.findById(PostId).orElseThrow().getPostitCount();
    }

    // 포스트잇 생성
    public void createPostIt(PostItCreateDTO dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        Post post = postRepo.findById(dto.getPostId()).orElseThrow();
        Comment comment = commentRepo.findById(dto.getCommentId()).orElseThrow();

        // 포스트잇 내용
        String context = commentRepo.findById(dto.getCommentId()).orElseThrow().getContent();

        // 포스트잇 생성
        PostIt postIt = PostIt.toEntity(dto, user, comment, context);

        // 포스트잇 개수 추가
        post.setPostitCount(post.getPostitCount() + 1);

        // 저장
        postRepo.save(post);
        postItRepo.save(postIt);
    }

    // 포스트잇 삭제
    public void deletePostIt(Long postItId) {
        // 포스트잇 개수 감소

        PostIt postIt = postItRepo.findById(postItId).orElseThrow();
        Post post = postIt.getPost();
        post.setPostitCount(post.getPostitCount() - 1);
        postItRepo.deleteById(postItId);
    }

    // 포스트잇 이동
    public void movePostIt(PostItMoveDTO dto) {
        PostIt postIt = postItRepo.findById(dto.getId()).orElseThrow();

        // 이동
        postIt.updatePosition(dto.getX(), dto.getY(), dto.getZ());

        postItRepo.save(postIt);
    }

    // 섹션 (좌, 우 이동)
    public void moveSection(Long postItId, String section) {
        PostIt postIt = postItRepo.findById(postItId).orElseThrow();

        // 이동
        postIt.updateSection(section);

        postItRepo.save(postIt);
    }

    // 포스트잇 글쓴이 아이디 가져오기
    public Long getWriterIdByPostIdIt(Long postItId) {
        return postItRepo.findById(postItId).orElseThrow().getPost().getPostId();
    }
}

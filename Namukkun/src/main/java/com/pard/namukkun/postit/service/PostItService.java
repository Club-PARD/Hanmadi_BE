package com.pard.namukkun.postit.service;


import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.comment.repo.CommentRepo;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.postit.dto.PostItMoveDTO;
import com.pard.namukkun.postit.dto.PostItReadDTO;
import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.postit.repo.PostItRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostItService {
    private final CommentRepo commentRepo;
    private final PostItRepo postItRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;


    // 포스트에 있는 포스트잇 개수 확인
    public Integer getPostPostItCounter(Long postId) {
        Integer temp = postRepo.findById(postId).orElseThrow().getPostitCount();
        return temp;
    }

    // 포스트잇 생성
    public Long createPostIt(PostItCreateDTO dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow();
        Post post = postRepo.findById(dto.getPostId()).orElseThrow();
        Comment comment = commentRepo.findById(dto.getCommentId()).orElseThrow();

        // 포스트잇 내용
        String context = comment.getContent();

        // 포스트잇 생성
        PostIt postIt = new PostIt(dto, user, comment, post, context);

        comment.setPostIt(postIt);


        // 저장
        postItRepo.save(postIt);
        commentRepo.save(comment);

        // 포스트잇 개수 추가
        post.setPostitCount(post.getPostitCount() + 1);
        System.out.println(postIt.getComment().getId());
        postRepo.save(post);
        return postIt.getId();
    }

    // 포스트잇 삭제
    public Long deletePostIt(Long postItId) {
        // 포스트잇 개수 감소

        log.info("postitid : {}",postItId);
        PostIt postIt = postItRepo.findById(postItId).orElseThrow();
        Comment comment = commentRepo.findById(postIt.getComment().getId()).orElseThrow();

        log.info("comment id {}", comment.getId());

        Post post = postIt.getPost();
        post.setPostitCount(post.getPostitCount() - 1);
        postItRepo.deleteById(postItId);

        try {
            return postIt.getComment().getId();
        } catch (Exception Ignore) {
            return null;
        }
    }

    // 포스트잇 이동
    public void movePostIt(PostItMoveDTO dto) {
        PostIt postIt = postItRepo.findById(dto.getPostItId()).orElseThrow();

        // x 최대값 설정
        if (200 <= dto.getX()) dto.setX(200f);

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

    public Long getWriterIdByPostItId(Long postItId) {
        return postItRepo.findById(postItId).orElseThrow()
                .getPost().getUser().getUserId();
    }

    // 포스트아이디로 글쓴이 아이디 가져오기
    public Long getWriterIdByPostId(Long postId) {
        return postRepo.findById(postId).orElseThrow().getUser().getUserId();
    }

    // 포스트잇 읽기
    public List<PostItReadDTO> readAllByPostId(Long postId) {
        return postRepo.findById(postId).orElseThrow().getPostIts()
                .stream().map(PostItReadDTO::new).collect(Collectors.toList());

    }
}

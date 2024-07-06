package com.pard.namukkun.comment.service;

import com.pard.namukkun.comment.dto.CommentCreateDTO;
import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.comment.repo.CommentRepo;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.dto.UserUpListDTO;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    // 덧글 생성
    public Boolean createComment(Long postId, Long userId, CommentCreateDTO dto) {
        // dto 에 user id 세팅
        dto.setUserId(userId);

        // 포스트가 존재할때만 저장
        if (postRepo.findById(postId).isPresent()) {
            User user = userRepo.findById(userId).orElseThrow();
            Post post = postRepo.findById(postId).orElseThrow();
            commentRepo.save(Comment.toEntity(dto, user, post));
            // 생성됨
            return true;
        } else return false; // 포스트가 존재하지 않으면 false
    }

    // 포스트의 덧글 모두 가져오기
    public List<CommentReadDTO> readALlComment(Long postId) {
        List<Comment> comments = postRepo.findById(postId).orElseThrow().getComments();
        List<CommentReadDTO> list = comments.stream()
                .map(CommentReadDTO::new)
                .collect(Collectors.toList());
        return list;
    }

    // 덧글 삭제
    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    // 덧글 작성자 아이디 가져오기
    public Long getCommentWriterId(Long commentId) {
        Long commentWriterId = commentRepo.findById(commentId).orElseThrow().getUser().getUserId();
//        log.info(String.valueOf(commentWriterId));
        return commentWriterId;
    }

    // 좋아요 버튼
    public void upButton(Long commentId, Long userId) {
        // 좋아요할 유저
        User user = userRepo.findById(userId).orElseThrow();

        // 좋아요 될 덧글
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        // 유저의 좋아요 리스트 가져오기
        List<Long> upList = user.getUpCommentList();

        log.info(String.valueOf(upList.contains(commentId)));
        // 이미 좋아요를 눌렀다면
        if (upList.contains(comment.getId())) {
            comment.minUpCounter();
            upList.remove(comment.getId());

            // 좋아요 누루기
        } else {
            comment.addUpCounter();
            upList.add(comment.getId());
        }
//        log.info("{}", comment.getUpCounter());
//        log.info("{}", user.getUpList().toArray().length);
        //유저 리스트 업데이트
        user.updateUpCommentList(upList);
        userRepo.save(user);
    }

    public void takeComment(Long commentId, Boolean take) {
        Comment comment = commentRepo.findById(commentId).orElseThrow();
        comment.setIsTaken(take);
        commentRepo.save(comment);
        log.info("{}", comment.getIsTaken());
    }

    public UserUpListDTO getUserUpList(Long postId, Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserUpListDTO(user.getUpList());
    }
}
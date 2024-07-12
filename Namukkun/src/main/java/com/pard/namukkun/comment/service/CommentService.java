package com.pard.namukkun.comment.service;

import com.pard.namukkun.comment.dto.CommentCreateDTO;
import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.comment.repo.CommentRepo;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.postit.repo.PostItRepo;
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
    private final PostItRepo postItRepo;

    // 덧글 생성
    public Long createComment(Long postId, Long userId, CommentCreateDTO dto) {
        // dto 에 user id 세팅
        dto.setUserId(userId);

        // 포스트가 존재할때만 저장
        if (postRepo.findById(postId).isPresent()) {
            User user = userRepo.findById(userId).orElseThrow();
            Post post = postRepo.findById(postId).orElseThrow();
            Comment comment = Comment.toEntity(dto, user, post);
            commentRepo.save(comment);
            // 생성됨
            return comment.getId();
        } else return null;
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
        Comment comment = commentRepo.findById(commentId).orElseThrow();
        PostIt postIt = comment.getPostIt();

        // 연관성 제거
        if (postIt != null) postIt.setComment(null);
        comment.setPostIt(null);

        // 연관성 제거 저장
        commentRepo.save(comment);
        if (comment.getPostIt() != null) postItRepo.save(postIt);

        // 댓글 제거
        commentRepo.deleteById(commentId);
    }

    // 덧글 작성자 아이디 가져오기
    public Long getCommentWriterId(Long commentId) {
        Long commentWriterId = commentRepo.findById(commentId).orElseThrow().getUser().getUserId();
        return commentWriterId;
    }

    // 좋아요 버튼
    public void upButton(Long commentId, Long userId, Boolean up) {

        // 좋아요할 유저
        User user = userRepo.findById(userId).orElseThrow();

        // 좋아요 될 덧글
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        // 유저의 좋아요 리스트 가져오기
        List<Long> upList = user.getUpCommentList();

        Boolean isContaining = upList.contains(commentId);

        // 좋아요 클릭
        if (up && !isContaining) {
            comment.addUpCounter();
            upList.add(comment.getId());
        } else if (!up && isContaining) {
            comment.minUpCounter();
            upList.remove(comment.getId());
        }
        user.updateUpCommentList(upList);
        userRepo.save(user);
    }

    // 채택
    public void takeComment(Long commentId, Boolean take) {
        Comment comment = commentRepo.findById(commentId).orElseThrow();
        comment.setIsTaken(take);
        commentRepo.save(comment);
    }

    // 해당 개시글에서 유저가 좋아한 댓글의 아이디만 모아서 리스트 return
    public UserUpListDTO getUserUpList(Long postId, Long userId) {
        // 유저 찾기
        User user = userRepo.findById(userId).orElseThrow();

        // 리턴할 리스트
        List<Long> list = new ArrayList<>();

        //유저가 가지고 있는 좋아요 리스트
        List<Long> userList = user.getUpCommentList();

        // 덧글이 작성된 포스트 아이디
        Long commentPostId = 0L;

        // 저장된 아이디로 하나하나 검사
        for (Long commentId : userList) {
            try { // 덧글이 지워진 경우 대응
                Comment comment = commentRepo.findById(commentId).orElseThrow();
                commentPostId = comment.getPost().getPostId();
                if (postId.equals(commentPostId)) list.add(commentId);
            } catch (Exception ignored) {
                user.getUpCommentList().remove(commentId);
                userRepo.save(user);
            }
        }

        return new UserUpListDTO(list);
    }

    public List<Long> getUserCommentList(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();

        if (user.getComments() == null) return null;
        List<Long> list = user.getComments()
                .stream().map(Comment::getId).toList();
        return list;
    }
}
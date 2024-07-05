package com.pard.namukkun.comment.controller;

import com.pard.namukkun.comment.dto.CommentCreateDTO;
import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/post/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    // 덧글 생성
    @PostMapping("/comment")
    @Operation(summary = "덧글 생성", description = "포스트 아이디와 유저아이디(세션)로 덧글을 생성합니다." + "유저 아이디는 디버그용입니다.")
    public ResponseEntity<?> createComment(
            @RequestParam("postId") Long postId,
            @RequestParam(value = "userId", required = true /* 이후 체크 해야함 */) Long userId, // debug
            @RequestBody() CommentCreateDTO dto
    ) {
        // 권한 확인
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        commentService.createComment(postId, userId, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 덧글 수정 없음

    @GetMapping("/comment")
    @Operation(summary = "덧글 읽기", description = "포스트에 있는 덧글을 읽어옵니다.")
    public List<CommentReadDTO> readAllComment(@RequestParam("postId") Long postId) {
        return commentService.readALlComment(postId);
    }

    // 덧글 삭제
    @DeleteMapping("/comment")
    @Operation(summary = "덧글 삭제", description = "덧글아이디로 덧글을 삭제합니다" + "유저의 아이디와 작성자의 아이디가 다르다면 삭제되지 않습니다")
    public ResponseEntity<?> deleteComment(
            @RequestParam(value = "userid", required = false) Long userId, // debug
            @RequestParam(value = "commentId") Long commentId
    ) {
        // 권한 확인
        Long commentWriterId = commentService.getCommentWriterId(commentId);
        if (userId == null || !userId.equals(commentWriterId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 덧글 좋아요
    @PatchMapping("/comment/up")
    @Operation(summary = "덧글 좋아요", description = "좋아요가 증가됩니다." + "만약 유저가 이미 눌렀다면 좋아요가 취소 됩니다(감소됩니다)")
    public ResponseEntity<?> upButton(
            @RequestParam("commentId") Long commentId,
            @RequestParam("userId") Long userId// debug
    ) {
        // 로그인 되어있는지 확인
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        commentService.upButton(commentId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

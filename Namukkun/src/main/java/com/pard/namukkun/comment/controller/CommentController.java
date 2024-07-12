package com.pard.namukkun.comment.controller;

import com.pard.namukkun.comment.dto.CommentCreateDTO;
import com.pard.namukkun.comment.dto.CommentCreateInfoDTO;
import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.comment.service.CommentService;
import com.pard.namukkun.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/post/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 덧글 생성
    @PostMapping("")
    @Operation(summary = "덧글 생성", description = "포스트 아이디와 유저아이디(세션)로 덧글을 생성합니다." + "유저 아이디는 디버그용입니다.")
    public ResponseEntity<?> createComment(
            @RequestParam(value = "postid") Long postId,
            @RequestBody() CommentCreateDTO dto,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        log.info("[POST:/post/comment] userid = {}", userId);

        // 글이 존재하지 않음
        if (!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // 권한 확인
        if (userId == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Long id = commentService.createComment(postId, userId, dto);
        CommentCreateInfoDTO infoDTO = new CommentCreateInfoDTO(id);
        return new ResponseEntity<>(infoDTO, HttpStatus.OK);
    }

    // 덧글 수정 없음

    // 덧글 읽기 전체 허용
    @GetMapping("")
    @Operation(summary = "덧글 읽기", description = "포스트에 있는 덧글을 읽어옵니다.")
    public ResponseEntity<?> readAllComment(
            @RequestParam("postid") Long postId
    ) {
//        log.info("[Get:/post/comment] postId = {}", postId);

        if (!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            List<CommentReadDTO> list = commentService.readALlComment(postId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception Ignore) {
            return null;
        }

    }

//    // TODO 포스트쪽에서 가져갈 수 있도록 포스트쪽에서도 만들것
//    // 게시글에서 유저가 좋아요 한 아이디 리스트를 가져옵니다
//    @GetMapping("/user/comment-uplist")
//    @Operation(summary = "유저 좋아요 리스트", description = "포스트에 있는 덧글중 유저가 좋아요를 누를 리스트를 읽어옵니다.")
//    public UserUpListDTO readUserCommentUpList(
//            @RequestParam(value = "userid", required = false) Long userId,
//            @RequestParam("postid") Long postId
//    ) {
//        return commentService.getUserUpList(postId, userId);
//    }

    // 덧글 삭제
    @DeleteMapping("")
    @Operation(summary = "덧글 삭제", description = "덧글아이디로 덧글을 삭제합니다" + "유저의 아이디와 작성자의 아이디가 다르다면 삭제되지 않습니다")
    public ResponseEntity<?> deleteComment(
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestParam(value = "commentid") Long commentId
    ) {
        log.info("[Delete:/post/comment] uesrId = {} commentId = {}", userId, commentId);
        Long commentWriterId = commentService.getCommentWriterId(commentId);

        // 권한 확인
        if (userId == null || !userId.equals(commentWriterId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 덧글 좋아요
    @PatchMapping("/up")
    @Operation(summary = "덧글 좋아요", description = "좋아요가 증가됩니다." +
            "만약 유저가 이미 눌렀다면 좋아요가 취소 됩니다(감소됩니다)")
    public ResponseEntity<?> upButton(
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestParam("commentid") Long commentId,
            @RequestParam("up") Boolean up) {
        log.info("[Patch:/post/comment/up] userId = {}, commentId = {}", userId, commentId);

        if (userId == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        commentService.upButton(commentId, userId, up);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 덧글 채택
    @PatchMapping("/take")
    @Operation(summary = "덧글 채택", description = "덧글을 채택합니다")
    public ResponseEntity<?> takeComment(
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestParam("postid") Long postId,
            @RequestParam("commentid") Long commentId,
            @RequestParam("take") Boolean take
    ) {
        log.info("[Patch:/post/comment/take] userId = {}, postId = {}, commentId = {}, take = {}",
                userId, postId, commentId, take);

        // 포스트가 존재하지 않음
        if (!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // 권한 확인
        if (!postService.getWriterUserId(postId).equals(userId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        commentService.takeComment(commentId, take);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("list/mycomment")
    @Operation(summary = "유저의 댓글 리스트 받기", description = "리스트 형식으로 유저가 작성한 댓글의 아이디 리스트를 전송합니다")
    public ResponseEntity<?> myCommentList(
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
//        log.info("[Get:/post/comment/list/mycomment] userId = {}", userId);
        // 권한 없음
        if (userId == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        List<Long> commentIdList = commentService.getUserCommentList(userId);
        return new ResponseEntity<>(commentIdList, HttpStatus.OK);
    }
}

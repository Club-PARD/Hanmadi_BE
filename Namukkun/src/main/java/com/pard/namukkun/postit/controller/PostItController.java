package com.pard.namukkun.postit.controller;

import com.pard.namukkun.postit.dto.*;
import com.pard.namukkun.postit.service.PostItService;
import com.pard.namukkun.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("post/postit")
@RequiredArgsConstructor
@Slf4j
public class PostItController {
    private final PostItService postItService;
    private final UserService userService;

    // 덧글 선택하여 포스트잇으로 만들기
    @PostMapping("/create")
    @Operation(summary = "포스트잇 생성", description = "덧글의 내용을 포스트잇으로 생성합니다.")
    public ResponseEntity<?> selectCommentToPostIt(
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestBody PostItCreateDTO dto
    ) {
        // 권한 없음
        if (!postItService.getWriterIdByPostId(dto.getPostId()).equals(userId))
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);

        // 최대갯수 확인
        if (10 <= postItService.getPostPostItCounter(dto.getPostId()))
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);

        // 생성
        Long postId = postItService.createPostIt(dto);
        PostItCreateInfoDTO infoDTO = new PostItCreateInfoDTO(postId, userService.getUserInfoDTO(dto.getUserId()));
        return new ResponseEntity<>(infoDTO, HttpStatus.OK);
    }


    // 포스트잇 읽기
    @GetMapping("/read")
    @Operation(summary = "포스트잇 읽기", description = "포스트에 있는 모든 포스트잇의 내용을 읽어옵니다")
    public ResponseEntity<?> readPostIts(
            @RequestParam("postid") Long postId
    ) {
        // 권한 없이 가능

        List<PostItReadDTO> dtos = postItService.readAllByPostId(postId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 포스트잇 좌우 이동
    @PatchMapping("/sectionmove")
    @Operation(summary = "포스트잇 섹션 이동", description = "덧글의 섹션을 이동합니다." + "left, right 아니면 값을 받지 않습니다")
    public ResponseEntity<?> moveSectionPostIt(
//            @RequestParam("userid") Long userId, // debug
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestParam("postitid") Long postItId,
            @RequestParam("section") String section) {
        // 로그인 안됨
        if (userId == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (!userId.equals(postItService.getWriterIdByPostItId(postItId)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // 잘못된 입력
        if (!(section.equals("left") || section.equals("right")))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        postItService.moveSection(postItId, section);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 포스트잇 이동
    @PatchMapping("/move")
    @Operation(summary = "포스트잇 이동", description = "포스트잇의 위치를 이동시킵니다")
    public ResponseEntity<?> movePostIt(
            @RequestParam("userid") Long userId,
            @RequestBody() PostItMoveDTO dto
    ) {
        // 권한 없음
        if (!postItService.getWriterIdByPostId(dto.getPostId()).equals(userId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        postItService.movePostIt(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 포스트잇 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "포스트잇 제거", description = "포스트잇을 지웁니다")
    public ResponseEntity<?> deletePostIt(
            @SessionAttribute(name = "userid", required = false) Long userId,
//            @RequestParam("userid") Long userId,
            @RequestParam("postitid") Long postItId
    ) {

        // 권한 없음
        if (!Objects.equals(userId, postItService.getWriterIdByPostItId(postItId)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Long commentid = postItService.deletePostIt(postItId);
        PostItCommentDTO dto = new PostItCommentDTO(commentid);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

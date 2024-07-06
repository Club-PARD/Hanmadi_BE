package com.pard.namukkun.postit.controller;


import com.pard.namukkun.post.service.PostService;
import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.postit.dto.PostItMoveDTO;
import com.pard.namukkun.postit.service.PostItService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("post/postit")
@RequiredArgsConstructor
public class PostItController {
    private final PostItService postItService;
    private final PostService postService;

    // 덧글 선택하여 포스트잇으로 만들기
    @GetMapping("/create")
    @Operation(summary = "포스트잇 생성", description = "덧글의 내용을 포스트잇으로 생성합니다.")
    public ResponseEntity<?> selectCommentToPostIt(
            @RequestParam("userid") Long userid,
            PostItCreateDTO dto
    ) {
        // 권한 없음
        if (!postItService.getWriterIdByPostIdIt(dto.getPostId()).equals(userid))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        // 최대갯수 확인
        if (10 <= postItService.getPostPostItCounter(dto.getPostId()))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); // 생성 안됨

        // 생성
        postItService.createPostIt(dto);
        return new ResponseEntity<>(HttpStatus.CREATED); // 생성됨
    }


    // 포스트잇 수정 없음


    @PatchMapping("/sectionmove")
    @Operation(summary = "포스트잇 섹션 이동", description = "덧글의 섹션을 이동합니다." + "left, right 아니면 값을 받지 않습니다")
    public ResponseEntity<?> moveSectionPostIt(
            @RequestParam("userId") Long userId, // debug
            @RequestParam("postItId") Long postItId,
            @RequestParam("section") String section) {
        // 로그인 안됨
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!userId.equals(postItService.getWriterIdByPostIdIt(postItId)))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        // 잘못된 입력
        if (!(section.equals("left") || section.equals("right")))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        postItService.moveSection(postItId, section);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 포스트잇 이동
    @PostMapping("/move")
    @Operation(summary = "포스트잇 이동", description = "포스트잇의 위치를 이동시킵니다")
    public ResponseEntity<?> movePostIt(
            @RequestParam("userid") Long userId,
            @RequestBody() PostItMoveDTO dto
    ) {
        // 권한 없음
        if (!postItService.getWriterIdByPostIdIt(dto.getPostId()).equals(userId))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        postItService.movePostIt(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 포스트잇 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "포스트잇 제거", description = "포스트잇을 지웁니다")
    public ResponseEntity<?> deletePostIt(
            @RequestParam("userid") Long userId,
            @RequestParam("id") Long postItId
    ) {
        // 권한 없음
        if (!Objects.equals(userId, postItService.getWriterIdByPostIdIt(postItId)))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        // TODO post count --
        postItService.deletePostIt(postItId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

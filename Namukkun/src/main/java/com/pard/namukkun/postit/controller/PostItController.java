package com.pard.namukkun.postit.controller;


import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.postit.dto.PostItMoveDTO;
import com.pard.namukkun.postit.service.PostItService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post/postit")
@RequiredArgsConstructor
public class PostItController {
    private final PostItService postItService;

    // 덧글 선택하여 포스트잇으로 만들기
    @GetMapping("/create")
    @Operation(summary = "포스트잇 생성", description = "덧글의 내용을 포스트잇으로 생성합니다.")
    public ResponseEntity<?> selectCommentToPostIt(PostItCreateDTO dto) {
        // TODO post 에서 count 증가 해야함
        postItService.createPostIt(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);

        // 글쓴이 아니면 권한 없음 return 해야함
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }


    // 포스트잇 수정 없음


    // 포스트잇 이동
    @PostMapping("/move")
    @Operation(summary = "포스트잇 이동", description = "포스트잇의 위치를 이동시킵니다")
    public ResponseEntity<?> movePostIt(@RequestBody() PostItMoveDTO dto) {
        postItService.movePostIt(dto);
        return new ResponseEntity<>(HttpStatus.OK);
        // 글쓴이 아니면 권한 없음 return 해야함
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    // 포스트잇 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "포스트잇 제거", description = "포스트잇을 지웁니다")
    public ResponseEntity<?> deletePostIt(@RequestParam("id") Long postItId) {
        // TODO post count --
        postItService.deletePostIt(postItId);
        return new ResponseEntity<>(HttpStatus.OK);
        // 글쓴이 아니면 권한 없음 return 해야함
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}

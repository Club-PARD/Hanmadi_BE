package com.pard.namukkun.post.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import com.pard.namukkun.post.dto.*;
import com.pard.namukkun.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final S3AttachmentService s3AttachmentService;

    @PostMapping("/upload/post")
    @Operation(summary = "게시물 등록", description = "게시물 내용을 입력해서 게시물을 등록합니다.")
    public ResponseEntity<?> createPost(
            @RequestBody() PostCreateDTO postCreateDTO,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        // 권한 확인 - 로그인 되어있으면 가능함
        if (userId == null || !userId.equals(postCreateDTO.getUserId()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // Post에서 첨부파일을 제외한 데이터는 CreateDTO형태로 받고, 첨부파일은 List형태로 따로 받음
        return postService.createPost(postCreateDTO);
    }

    @PostMapping("/upload/temppost")
    @Operation(summary = "게시물 임시저장", description = "게시물을 임시저장합니다.")
    public ResponseEntity<?> createTempPost(
            @RequestBody() PostCreateDTO postCreateDTO,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        // 권한 확인 - 로그인 되어있으면 가능함
        if (userId == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return postService.saveTempPost(postCreateDTO);
    }

    @PostMapping(value = "/upload/file", consumes = {"multipart/form-data"})
    @Operation(summary = "첨부파일 첨부", description = "첨부파일을 첨부합니다.")
    public ResponseEntity<?> uploadFile(
            @SessionAttribute(name = "userid", required = false) Long userid,
            @RequestPart("files") List<MultipartFile> files
    ) {
        //권한 확인 - 로그인 되어있으면 가능함
        if (userid == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        ReturnFileNameDTO dto = postService.uploadAttachment(files);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/upload/img", consumes = {"multipart/form-data"})
    @Operation(summary = "이미지 첨부", description = "이미지 이름을 받아서 UUID를 앞에 붙인 이름을 반환합니다.")
    public ResponseEntity<?> uploadImg(
            @RequestParam("img") MultipartFile img,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        // 권한확인 - 로그인 되어있으면 가능함
        if (userId == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        log.info("{}\n{}", userId, img);
        return postService.uploadImg(img, userId);
    }

    @PostMapping("/delete/file")
    @Operation(summary = "첨부파일 삭제", description = "첨부파일 이름을 받아서 삭제합니다.")
    public ResponseEntity<?> deleteFile(
            @RequestParam("fileName") String fileName,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        // 권한확인 - 로그인 되어있으면 가능함
        if (userId == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return s3AttachmentService.deleteByName(fileName);
    }

    @GetMapping("/read/all")
    @Operation(summary = "모든 게시물을 읽습니다.")
    public ResponseEntity<?> findAllPost() {
        List<PostReadDTO> dtos = postService.readAllPosts();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/read/update")
    @Operation(summary = "수정할 게시물을 id를 통해서 읽습니다.")
    public ResponseEntity<?> findPostById(
            @RequestParam("id") Long id,  // 게시물 어이디
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        // 권한확인 - 글 작성자만 가능함
        if (userId == null || !userId.equals(postService.getWriterUserId(id)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        PostUpdateDTO dto = postService.findPostByIdUpdateVer(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/read")
    @Operation(summary = "게시물 id를 통해서 게시물을 읽습니다.")
    public ResponseEntity<?> findById(@RequestParam("id") Long id) {
        PostReadDTO dto = postService.findPostById(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/read/by-local/by-up-count")
    @Operation(summary = "선택된 지역 게시물중 추천순으로 나열합니다.")
    public ResponseEntity<?> findByUpCount(Integer localPageId) {
        List<PostReadDTO> postReadDTOS = postService.findByLocal(localPageId);

        List<PostReadDTO> dtos = postService.sortByUpCountPost(postReadDTOS);
        return new ResponseEntity(dtos, HttpStatus.OK);
    }

    @GetMapping("/read/by-local")
    @Operation(summary = "선택된 지역 게시물중 최신순으로 나열합니다.")
    public ResponseEntity<?> findByRecent(Integer localPageId) {
        List<PostReadDTO> postReadDTOS = postService.findByLocal(localPageId);
        List<PostReadDTO> dtos = postService.sortByRecentPost(postReadDTOS);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/read/by-Upcount")
    @Operation(summary = "게시물을 추천 높은순으로 정렬해 나열합니다.")
    public ResponseEntity<?> findByUpCount() {
        List<PostReadDTO> dtos = postService.findByUpCountPost();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PatchMapping("/update")
    @Operation(summary = "게시물을 수정합니다.", description = "이거 실행하기전에 /post/decreaseUpCount에 가서 파일 첨부먼저 하고 리턴값을" +
            "fileName에 넣어줘야합니다.")
    public ResponseEntity<?> updatePost(
            @RequestBody PostUpdateDTO postUpdateDTO,
            @RequestParam("postId") Long postId,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        if(!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // 권한확인 - 글 작성자만 가능
        if (userId == null || !userId.equals(postService.getWriterUserId(postId)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return postService.updatePost(postId, postUpdateDTO);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "게시물을 삭제합니다.")
    public ResponseEntity<?> deletePost(
            @RequestParam("postId") Long postId,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {

        if(!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // 권한확인 - 글 작성자만 가능
        if (userId == null || !userId.equals(postService.getWriterUserId(postId)))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return postService.deletePost(postId);
    }


    @PostMapping("/increase/UpCount")
    @Operation(summary = "게시물 채택", description = "postid : 채택한 게시물 id, state 추천 상태 true : 채택 / 미채택이면 그냥 없어짐")
    public ResponseEntity<?> increaeUpCount(
            @RequestParam("postId") Long postId,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {

        if(!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // 권한확인 - 로그인 되어있으면 가능
        if (userId == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return postService.IncreaseUpCountPost(postId, userId);
    }

    @PostMapping("/decrease/UpCount")
    @Operation(summary = "게시물 채택 취소", description = "postid : 채택한 게시물 id, state 추천 상태 true : 채택 / 미채택이면 그냥 없어짐")
    public ResponseEntity<?> decreaseUpCount(
            @RequestParam("postId") Long postId,
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {

        if(!postService.checkValid(postId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // 권한확인 - 로그인 되어있으면 가능
        if (userId == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return postService.decreaseUpCountPost(postId, userId);
    }
}
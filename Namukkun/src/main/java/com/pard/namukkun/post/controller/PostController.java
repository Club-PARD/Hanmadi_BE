package com.pard.namukkun.post.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pard.namukkun.attachment.service.S3AttachmentService;
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
    @Operation(summary = "게시물 등록", description = "게시물 내용을 입력합니다. " +
            "fileName : 첨부파일 이름 ex) text.png " +
            "/post를 통해서 게시물을 등록하고, /post/uploadfile로 가서 첨부파일을 업로드 해야지 객체 url로 첨부파일을 볼 수 있습니다.")
    public ResponseEntity<?> createPost(@RequestBody() PostCreateDTO postCreateDTO) {
        // Post에서 첨부파일을 제외한 데이터는 CreateDTO형태로 받고, 첨부파일은 List형태로 따로 받음
        return postService.createPost(postCreateDTO);
    }

    //-----------------------------------
    @PostMapping("/test")
    public ResponseEntity<?> test(){
        log.info("test");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //-----------------------------------

    @PostMapping("/upload/temppost")
    @Operation(summary = "게시물 임시저장", description = "게시물을 임시저장합니다. 임시저장 후에도 /post/uploadfile로 가서 첨부파일을 업로드 해야합니다.")
    public ResponseEntity<?> createTempPost(@RequestBody() PostCreateDTO postCreateDTO) {
        return postService.saveTempPost(postCreateDTO);
    }

    @PostMapping(value = "/upload/file", consumes = {"multipart/form-data"})
    @Operation(summary = "첨부파일 첨부", description = "첨부파일을 첨부합니다.")
    public ReturnFileNameDTO uploadFile(@RequestPart("files") List<MultipartFile> files) throws JsonProcessingException {
        return postService.uploadAttachment(files);
    }

    @PostMapping("/upload/img")
    @Operation(summary = "이미지 첨부", description = "이미지 이름을 받아서 UUID를 앞에 붙인 이름을 반환합니다.")
    public ResponseEntity<?> uploadImg(@RequestParam("img") MultipartFile img){
        return postService.uploadImg(img);
    }

    @PostMapping("/delete/file")
    @Operation(summary = "첨부파일 삭제", description = "첨부파일 이름을 받아서 삭제합니다.")
    public ResponseEntity<?> deleteFile(@RequestParam("fileName") String fileName) {
        return s3AttachmentService.deleteByName(fileName);
    }

    @GetMapping("/read/all")
    @Operation(summary = "모든 게시물을 읽습니다.")
    public List<PostReadDTO> findAllPost() {
        return postService.readAllPosts();
    }

    @GetMapping("/read/modify")
    @Operation(summary = "수정할 게시물을 id를 통해서 읽습니다.")
    public PostModifyDTO findPostById(@RequestParam("id") Long id) {
        return postService.findPostByIdmodifyVer(id);
    }

    @GetMapping("/read")
    @Operation(summary = "게시물 id를 통해서 게시물을 읽습니다.")
    public PostReadDTO findById(@RequestParam("id") Long id) {
        return postService.findPostById(id);
    }

    @GetMapping("/read/by-local/by-up-count")
    @Operation(summary = "선택된 지역 게시물중 추천순으로 나열합니다.")
    public List<PostReadDTO> findByUpCount(Integer localPageId) {
        List<PostReadDTO> postReadDTOS = postService.findByLocal(localPageId);
        return postService.sortByUpCountPost(postReadDTOS);
    }

    @GetMapping("/read/by-local")
    @Operation(summary = "선택된 지역 게시물중 최신순으로 나열합니다.")
    public List<PostReadDTO> findByRecent(Integer localPageId) {
        List<PostReadDTO> postReadDTOS = postService.findByLocal(localPageId);
        return postService.sortByRecentPost(postReadDTOS);
    }

    @GetMapping("/read/by-Upcount")
    @Operation(summary = "게시물을 추천 높은순으로 정렬해 나열합니다.")
    public List<PostReadDTO> findByUpCount(){
        return postService.findByUpCountPost();
    }

    @PatchMapping("/update")
    @Operation(summary = "게시물을 수정합니다.", description = "이거 실행하기전에 /post/decreaseUpCount에 가서 파일 첨부먼저 하고 리턴값을" +
            "fileName에 넣어줘야합니다.")
    public PostReadDTO updatePost(@RequestBody PostUpdateDTO postUpdateDTO, @RequestParam("postId") Long postId) {
        return postService.updatePost(postId, postUpdateDTO);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "게시물을 삭제합니다.")
    public ResponseEntity<?> deletePost(@RequestParam("postId") Long postId) {
        return postService.deletePost(postId);
    }


    @PostMapping("/increae/UpCount")
    @Operation(summary = "게시물 채택")
    public Integer increaeUpCount(@RequestParam("postId") Long postId, @RequestParam("userId") Long userId) {
        return postService.IncreaseUpCountPost(postId,userId);
    }

    @PostMapping("/decrease/UpCount")
    @Operation(summary = "게시물 채택 취소")
    public Integer decreaseUpCount(@RequestParam("postId") Long postId, @RequestParam("userId") Long userId) {
        return postService.decreaseUpCountPost(postId,userId);
    }

}

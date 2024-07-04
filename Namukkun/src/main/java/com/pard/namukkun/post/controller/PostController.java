package com.pard.namukkun.post.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;
    private final S3AttachmentService s3AttachmentService;

    @PostMapping("/uploadpost")
    @Operation(summary = "게시물 등록", description = "게시물 내용을 입력합니다. " +
            "fileName : 첨부파일 이름 ex) text.png " +
            "/post를 통해서 게시물을 등록하고, /post/uploadfile로 가서 첨부파일을 업로드 해야지 객체 url로 첨부파일을 볼 수 있습니다.")
    public String createPost(@RequestBody() PostCreateDTO postCreateDTO) {
        // Post에서 첨부파일을 제외한 데이터는 CreateDTO형태로 받고, 첨부파일은 List형태로 따로 받음
        return postService.createPost(postCreateDTO);
    }

    @PostMapping("/uploadtemppost")
    @Operation(summary = "게시물 임시저장", description = "게시물을 임시저장합니다. 임시저장 후에도 /post/uploadfile로 가서 첨부파일을 업로드 해야합니다.")
    public String createTempPost(@RequestBody() PostCreateDTO postCreateDTO) {
        return postService.saveTempPost(postCreateDTO);
    }

    @PostMapping(value = "/uploadfile", consumes = {"multipart/form-data"})
    @Operation(summary = "첨부파일 첨부", description = "첨부파일을 첨부합니다.")
    public String uploadFile(@RequestPart("files") List<MultipartFile> files) throws JsonProcessingException {
        return postService.uploadAttachment(files);
    }


    @GetMapping("/readall")
    @Operation(summary = "모든 게시물을 읽습니다.")
    public List<PostReadDTO> findAllPost() {
        return postService.readAllPosts();
    }

    @PatchMapping("/update/{postId}")
    @Operation(summary = "게시물을 수정합니다.", description = "이거 실행하면 기존의 S3 파일은 삭제되기 때문에" +
            " 다시 /uploadfile로 s3에 첨부파일을 업로드 해야합니다.")
    public PostReadDTO updatePost(@RequestBody PostCreateDTO postCreateDTO, @PathVariable("postId") Long postId) {
        return postService.updatePost(postId, postCreateDTO);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "게시물을 삭제합니다.")
    public String deletePost(@RequestParam("postId") Long postId) {
        return postService.deletePost(postId);
    }
}

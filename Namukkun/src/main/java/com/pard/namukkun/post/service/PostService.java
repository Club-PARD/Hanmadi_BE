package com.pard.namukkun.post.service;

import com.nimbusds.openid.connect.sdk.assurance.evidences.attachment.Attachment;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class PostService {

    @Autowired
    private final PostRepo postRepo;

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final S3AttachmentService s3AttachmentService;

    // PostCreateDTO 받아서 postDTO 생성
    @Transactional
    public void createPost(PostCreateDTO postCreateDTO) {

        // post 정보 저장할 때 User의 모든 정보 받을 필요 없이 User Id만 받고
        // UserId로 User 찾아서 저장한 뒤에 Post 생성후 save함.
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error creating post -> "+postCreateDTO.getUserId()));

        List<String> fileNames = postCreateDTO.getFileName();

        Post post = Post.toEntity(postCreateDTO, user);
        for(String fileName : fileNames) {
            String S3FileUrl = s3AttachmentService.getUrlWithFileName(fileName);
            post.addS3Attachment(S3FileUrl);
        }
        postRepo.save(post);
    }

    /*@Transactional
    public void addFileUrlToPost(String originalFileName,String fileName) {
        log.info("------------------------------------");
        List<Post> posts = postRepo.findByFileUrlsContainingFileName(originalFileName);
        for(Post post : posts) {
            for(String posturl : post.getFileUrls()) {
                if(posturl.contains(fileName)) {
                    post.getFileUrls().remove(posturl);
                    log.info(posturl);
                    post.getFileUrls().add(fileName);
                    log.info(fileName);
                }
            }
        }

    }*/

    public List<PostReadDTO> readAllPosts() {
        return postRepo.findAll()
                .stream()
                .map(post -> new PostReadDTO(post,
                        post.getS3Attachments().stream()
                                .map(S3AttachmentReadDTO::new)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }


    public String deletePost(Long postId) {
        postRepo.deleteById(postId);
        return "Post deleted";
    }

}

package com.pard.namukkun.post.service;

import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.dto.PostUpdateDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.entity.UpPost;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UpPostRepo;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final S3AttachmentService s3AttachmentService;
    private final UpPostRepo upPostRepo;


    /*// PostCreateDTO 받아서 postDTO 생성
    @Transactional
    public ResponseEntity<?> createPost(PostCreateDTO postCreateDTO) {

        // post 정보 저장할 때 User의 모든 정보 받을 필요 없이 User Id만 받고
        // UserId로 User 찾아서 저장한 뒤에 Post 생성후 save함.
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error creating post -> " + postCreateDTO.getUserId()));

        // s3attachment에 url 저장하기 위해서 filename을 받음
        List<String> fileNames = postCreateDTO.getFileName();

        Post post = Post.toEntity(postCreateDTO, user);
        post.setInitial(true, Data.getDeadLine(post.getPostTime()));

        // 파일 저장
        for (String fileName : fileNames) {
            String S3FileUrl = s3AttachmentService.getUrlWithFileName(fileName);
            post.addS3Attachment(S3FileUrl);
        }

        post = s3AttachmentService.saveS3File(fileNames, post);


        postRepo.save(post);

        // 이미지 저장
        for (ImageCreateDTO imageCreateDTO : postCreateDTO.getImageCreateDTOS()) {
            imageService.saveImage(imageCreateDTO, post);
        }

        saveImage(postCreateDTO,post);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/


    @Transactional
    // 게시물 임시저장
    public ResponseEntity<?> saveTempPost(PostCreateDTO postCreateDTO) {
        /*// User 불러옴
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error saving temp post -> " + postCreateDTO.getUserId()));

        // 만약 원래 temp post가 있다면 삭제
        if (user.getTempPost() != null) {
            Post exsitedPost = user.getTempPost();
            List<S3Attachment> exsitedS3Attachments = exsitedPost.getS3Attachments();

            // S3파일 삭제
            for (S3Attachment exsitedS3Attachment : exsitedS3Attachments) {
                s3AttachmentService.delete(exsitedS3Attachment.getFileUrl());
            }

            // 원래 있던 임시 게시물 삭제
            user.setTempPost(null);
            postRepo.delete(exsitedPost);
        }

        // 임시 게시물 생성
        Post tempPost = Post.toEntity(postCreateDTO, user);
        tempPost.setInitial(true, Data.getDeadLine(tempPost.getPostTime()));
        List<String> fileNames = postCreateDTO.getFileName();
        for (String fileName : fileNames) {
            String S3FileUrl = s3AttachmentService.getUrlWithFileName(fileName);
            tempPost.addS3Attachment(S3FileUrl);
        }

        s3AttachmentService.saveS3File(fileNames, tempPost);


        // 임시게시물 저장
        user.setTempPost(tempPost);
        postRepo.save(tempPost);
        saveImage(postCreateDTO,tempPost);
        userRepo.save(user);*/

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 모든 게시물 read
    public List<PostReadDTO> readAllPosts() {
        return postRepo.findAll()
                .stream()
                .map(post -> new PostReadDTO(post,
                        post.getS3Attachments().stream()
                                .map(S3AttachmentReadDTO::new)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    //post 업데이트 메서드

    public PostReadDTO updatePost(Long postId, PostUpdateDTO postUpdateDTO){
        Post post = postRepo.findById(postId).get(); //postId로 post find

        // 내용 넣어주기
        // 이거 한번에 뭉쳐놓기

        post.updatePost(postUpdateDTO.getTitle(),postUpdateDTO.getPostLocal(),postUpdateDTO.getUpCountPost()
        ,postUpdateDTO.getPostitCount(),postUpdateDTO.getProBackground(),postUpdateDTO.getSolution(),postUpdateDTO.getBenefit());

        // 기존에 있던 S3 파일 삭제
        List<S3Attachment> existS3Attachments = post.getS3Attachments();
        for (S3Attachment s3Attachments : existS3Attachments) {
            s3AttachmentService.delete(s3Attachments.getFileUrl());
        }

        List<String> fileNames = postUpdateDTO.getFileName();
        post.getS3Attachments().clear(); // 기존에 있던 url제거
        for (String fileName : fileNames) {
            String S3FileUrl = s3AttachmentService.getUrlWithFileName(fileName);
            post.addS3Attachment(S3FileUrl);
        }

        postRepo.save(post);

        return new PostReadDTO(post,
                post.getS3Attachments().stream()
                        .map(S3AttachmentReadDTO::new)
                        .collect(Collectors.toList()));
    }

    // postId로 찾아 삭제
    public ResponseEntity<?> deletePost(Long postId) {
        postRepo.deleteById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 첨부파일 업로드
    public List<String> uploadAttachment(List<MultipartFile> files) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString(); // 랜덤 string생성
                String fileName = uuid + "_" + file.getOriginalFilename();
                // uuid를 이름 앞에 붙여서 같은 이름으로 들어와도 중복저장이 되도록한다.
                //String fileName = file.getOriginalFilename();
                s3AttachmentService.upload(file, fileName);
                //postService.addFileUrlToPost(file.getOriginalFilename(),fileName);
                fileUrls.add(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrls;
    }


    //-----------------------------------

    // 포스트의 시간과 현재 시간을 비교하여
    public void postCheck(String presentTime) {

        // isDone 이 false 인 post 가져오기
        List<Post> posts = postRepo.findByIsDoneFalse();

        // 포메터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 서버타임 설정
        LocalDate serverTime = LocalDate.parse(presentTime, formatter);

        for (Post post : posts) {
            // 포스트의 시간 가져오기
            LocalDate postTime = LocalDate.parse(post.getPostTime(), formatter);
            // 포스트 시간에 7일을 더한 것이 서버 시간보다 이전이라면 = 7일이 지났다면
            if (serverTime.isBefore(postTime.plusDays(7))) post.setIsDone(true); // isdone -> true
        }
    }
    //-----------------------------------


    // 이미지 업로드
    public List<String> uploadImge(List<MultipartFile> files) {
        List<String> fileUrls = new ArrayList<>();
        try{
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + file.getOriginalFilename();
                s3AttachmentService.upload(file, fileName);
                fileUrls.add(s3AttachmentService.getUrlWithFileName(fileName));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return fileUrls;
    }

    //-----------------------------------------
    public Long getWriterUserId(Long postId) {
        return postRepo.findById(postId).orElseThrow().getUser().getUserId();
    }
    //-----------------------------------------


    // 채택하는 메서드
    public Integer IncreaseUpCountPost(Long postId, Long userId) {
        User user = returnUser(userId);

        Post post = returnPost(postId);
        post.increaseUpCountPost();
        postRepo.save(post);

        UpPost upPost = new UpPost();
        upPost.setPostId(postId);
        user.addUpPost(upPost);

        upPostRepo.save(upPost);

        return post.getUpCountPost();
    }

    // 채택 취소하는 메서드
    @Transactional
    public Integer decreaseUpCountPost(Long postId, Long userId) {
        Post post = returnPost(postId);
        post.decreaseUpCountPost();
        postRepo.save(post);

        User user = returnUser(userId);
        List<UpPost> upPosts = user.getUpPosts();
        for(UpPost upPost : upPosts) {
            if(upPost.getPostId().equals(postId)) {
                upPostRepo.deleteById(upPost.getUpPostId());
                user.getUpPosts().remove(upPost);
                userRepo.save(user);
                break;
            }
        }
        return post.getUpCountPost();
    }

    // postId를 받아서 post를 리턴하는 메서드
    public Post returnPost(Long postId) {
        return postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post -> "+postId));
    }

    // userId를 받아서 user를 리턴하는 메서드
    public User returnUser(Long userId){
        return userRepo.findById(userId).orElseThrow(()
                -> new RuntimeException("Error can't find user -> "+userId));
    }

    public List<PostReadDTO> sortByUpCountPost(List<PostReadDTO> postReadDTOS){
        return postReadDTOS.stream()
                .sorted(Comparator.comparingInt(PostReadDTO::getUpCountPost).reversed())
                .collect(Collectors.toList());
    }

    // 유저 아이디에 있는 지역 번호에 따라서 해당 지역을 나오게함
    public List<PostReadDTO> findByLocal(Integer localPageId) {
        List<Post> posts = postRepo.findByPostLocal(localPageId);
        return posts.stream()
                .map(post -> new PostReadDTO(post,
                        post.getS3Attachments().stream()
                                .map(S3AttachmentReadDTO::new)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    // 게시물 최신순으로 정렬하는 메서드
    public List<PostReadDTO> sortByRecentPost(List<PostReadDTO> postReadDTOS){
        return postReadDTOS.stream()
                .sorted(Comparator.comparing(PostReadDTO::getPostTime).reversed())
                .collect(Collectors.toList());
    }

    // 게시물 채택수별로 정렬하는 메서드
    public List<PostReadDTO> findByUpCountPost(){
        List<PostReadDTO> postReadDTOS = readAllPosts();
        return sortByUpCountPost(postReadDTOS);
    }

    public ResponseEntity<?> createPost(PostCreateDTO postCreateDTO) {
        log.info("서비스 들어옴");
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error creating post -> "+postCreateDTO.getUserId()));
        try {
            // HTML 파싱
            String proBackgroundHtml = postCreateDTO.getProBackground();
            Document probackgroundDocument = Jsoup.parse(proBackgroundHtml);
            String probackgroundText = probackgroundDocument.body().text();
            System.out.println(probackgroundText);

            String solutionHtml = postCreateDTO.getSolution();
            Document solutionDocument = Jsoup.parse(solutionHtml);
            String solutionText = solutionDocument.body().text();

            String benefitHtml = postCreateDTO.getBenefit();
            Document benefitDocument = Jsoup.parse(benefitHtml);
            String benefitText = benefitDocument.body().text();

            // s3attachment에 url 저장하기 위해서 filename을 받음
            List<String> fileNames = postCreateDTO.getFileName();

            Post post = Post.toEntity(postCreateDTO,probackgroundText,solutionText,benefitText, user);
            post.setInitial(true, Data.getDeadLine(post.getPostTime()));

            // 파일 저장
            post = s3AttachmentService.saveS3File(fileNames, post);

            postRepo.save(post);

            /*// 예시: <div> 태그 내의 모든 텍스트 추출
            Elements divElements = doc.select("div");
            for (Element div : divElements) {
                String text = div.text();
                System.out.println("Text in <div>: " + text);
            }

            // 예시: 이미지 태그 내의 src 속성 추출
            Elements imgElements = doc.select("img");
            for (Element img : imgElements) {
                String imageUrl = img.attr("src");
                System.out.println("Image URL: " + imageUrl);
            }*/

            return ResponseEntity.status(HttpStatus.OK).body("HTML 파싱 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTML 파싱 오류: " + e.getMessage());
        }
    }
}

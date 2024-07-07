package com.pard.namukkun.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.post.dto.*;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final S3AttachmentService s3AttachmentService;

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private Map<String, Path> tempStorage = new HashMap<>();


    // PostCreateDTO 받아서 postDTO 생성
    @Transactional
    public ResponseEntity<?> createPost(PostCreateDTO postCreateDTO) {
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error creating post -> "+postCreateDTO.getUserId()));
        try{
            Post post = makePost(postCreateDTO,user,"create");
            List<String> fileNames = postCreateDTO.getFileNames();
            post.setInitial(true,Data.getDeadLine(post.getPostTime()));

            for (String fileName : fileNames)
                post.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));

            postRepo.save(post);
            tempStorage.clear();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTML 파싱 오류: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    // HTML에서 텍스트와 이미지를 골라서 파싱하는 메서드
    private String parseHtml(String html) {
        Document document = Jsoup.parse(html);
        StringBuilder sb = new StringBuilder();

        catchNode(document.body().childNodes(), sb);

        return sb.toString();
    }

    private void catchNode(List<Node> nodes, StringBuilder sb) {
        for(Node node : nodes) {
            log.info("node : "+node);
            if(node instanceof TextNode) {
                sb.append(((TextNode) node).text());
                log.info("택스트"+node);
            } else if(node instanceof Element) {
                Element element = (Element) node;
                log.info("엘리먼트: " + element);

                if(element.tagName().equals("img")) { // 이미지 일 때
                    try{
                        String fileName = URLDecoder.decode(element.attr("src"),StandardCharsets.UTF_8.toString()); // 파일 이름 저장 (tempStorage key값)
                        String UUIDFileName = UUID.randomUUID()+"_"+fileName;
                        log.info("fileName : " + fileName);
                        if(tempStorage.containsKey(fileName)) {

                            Path tempFilePath = tempStorage.get(fileName); // 해당 이름으로 저장된 이미지 불러옴

                            if(tempFilePath != null) { // 만약 저장소에 있으면

                                log.info("img save : "+fileName);
                                s3AttachmentService.uploadFile(tempFilePath.toFile(),UUIDFileName); // s3에 저장
                                sb.append("[이미지: ").append(s3AttachmentService.getUrlWithFileName(UUIDFileName)).append("]"); // stringbuilder에 추가

                            }else log.warn("임시 파일이 null입니다: " + fileName);

                        } else log.warn("임시 저장소에 파일이 존재하지 않습니다:" + fileName);

                    } catch (Exception e) {
                        log.error("이미지 업로드 중 오류 발생: " + e.getMessage(), e);
                    }
                } else {
                    catchNode(element.childNodes(), sb);
                }
            }
        }
    }

    // HTML 파싱하는 메서드
    public Post makePost(PostCreateDTO postCreateDTO, User user, String version) {
        // proBackground 파싱
        String proBackgroundHtml = postCreateDTO.getProBackground();
        String proBackgroundText = parseHtml(proBackgroundHtml);

        // solution 파싱
        String solutionHtml = postCreateDTO.getSolution();
        String solutionText = parseHtml(solutionHtml);

        // benefit 파싱
        String benefitHtml = postCreateDTO.getBenefit();
        String benefitText = parseHtml(benefitHtml);

        if(version.equals("create")) return Post.toEntity(postCreateDTO,proBackgroundText,solutionText,benefitText,user,true);
        else if(version.equals("temp")) return Post.toEntity(postCreateDTO,proBackgroundText,solutionText,benefitText,user,false);
        return null;
    }


    @Transactional
    // 게시물 임시저장
    public ResponseEntity<?> saveTempPost(PostCreateDTO postCreateDTO) {
        // User 불러옴
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error saving temp post -> " + postCreateDTO.getUserId()));

        // 만약 원래 temp post가 있다면 삭제
        if (user.getTempPost() != null) {
            Post exsitedPost = user.getTempPost();
            List<S3Attachment> exsitedS3Attachments = exsitedPost.getS3Attachments();

            // S3파일 삭제
            for (S3Attachment exsitedS3Attachment : exsitedS3Attachments) {
                s3AttachmentService.deleteByUrl(exsitedS3Attachment.getFileUrl());
            }

            // 원래 있던 임시 게시물 삭제
            user.setTempPost(null);
            postRepo.delete(exsitedPost);
        }

        // 임시 게시물 생성
        Post tempPost = makePost(postCreateDTO,user,"temp");

        List<String> fileNames = postCreateDTO.getFileNames();
        tempPost.setInitial(true,Data.getDeadLine(tempPost.getPostTime()));

        for (String fileName : fileNames)
            tempPost.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));

        postRepo.save(tempPost);

        // 임시게시물 저장
        user.setTempPost(tempPost);
        postRepo.save(tempPost);
        userRepo.save(user);

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

    public PostReadDTO updatePost(Long postId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepo.findById(postId).get(); //postId로 post find

        post.updatePost(postUpdateDTO.getTitle(),postUpdateDTO.getPostLocal(),postUpdateDTO.getProBackground(),
                postUpdateDTO.getSolution(),postUpdateDTO.getBenefit());


        List<String> fileNames = postUpdateDTO.getFileName();
        post.getS3Attachments().clear(); // 기존에 있던 url제거
        for (String fileName : fileNames) {
            post.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));
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
    public ReturnFileNameDTO uploadAttachment(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString(); // 랜덤 string생성
                // uuid를 이름 앞에 붙여서 같은 이름으로 들어와도 중복저장이 되도록한다.
                String fileName = uuid + "_" + file.getOriginalFilename();

                // 파일 저장
                s3AttachmentService.upload(file, fileName);

                // UUID가 붙은 파일 이름을 List에 저장
                fileNames.add(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("save file : "+fileNames);
        return new ReturnFileNameDTO(fileNames);
    }

    // 이미지 업로드
    public ResponseEntity<?> uploadImg(MultipartFile file) {
        // 임시 저장소에 파일 저장
        try {
            // 임시 저장소에 저장될 키값, 저장될 이미지 이름
            String originalFilename = file.getOriginalFilename();
            String fileId = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            Path tempFilePath = Paths.get(TEMP_DIR, fileId);

            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            tempStorage.put(fileId, tempFilePath);
            log.info("Temp storage contains: " + tempStorage.keySet());

            ImgDTO imgDTO = new ImgDTO(originalFilename);
            log.info("Img 저장 : " + imgDTO.getImg());

            return ResponseEntity.status(HttpStatus.CREATED).body(imgDTO);
        } catch (IOException e) {
            log.error("Error storing temp image file: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing temp image file");
        }
    }

    // 포스트의 시간과 현재 시간을 비교하여
    public Integer postCheck(String presentTime) {
        Integer counter = 0;

        // isDone 이 false 인 post 가져오기
        List<Post> posts = postRepo.findByIsDoneFalse();

        // 포메터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 서버타임 설정
        LocalDate serverTime = LocalDate.parse(presentTime, formatter);

        for (Post post : posts) {
            // 포스트의 시간 가져오기
            LocalDate postTime = LocalDate.parse(post.getPostTime(), formatter);

            // 포스트 시간에 7일을 더한 것이 서버 시간보다 이전이라면 = 7일이 지났다면
            if (serverTime.isAfter(postTime.plusDays(7))) {
                post.setIsDone(true); // isdone -> true
                counter = counter + 1;
                postRepo.save(post);
            }
        }
        return counter;
    }


    //-----------------------------------------
    public Long getWriterUserId(Long postId) {
        return postRepo.findById(postId).orElseThrow().getUser().getUserId();
    }
    //-----------------------------------------


    // 채택하는 메서드
    @Transactional
    public List<UpCountInfoDTO> IncreaseUpCountPost(Long postId, Long userId) {
        User user = returnUser(userId);
        //--------------------------------------
        List<Long> list = user.getUpPostList();
        list.add(postId);
        user.updateUpPostList(list);
        userRepo.save(user);
        //--------------------------------------
        Post post = returnPost(postId);
        post.increaseUpCountPost();
        postRepo.save(post);

        return makeUpCountInfoDTO(list);
    }

    // 채택 취소하는 메서드
    @Transactional
    public List<UpCountInfoDTO> decreaseUpCountPost(Long postId, Long userId) {
        User user = returnUser(userId);
        //--------------------------------------
        List<Long> list = user.getUpPostList();
        list.remove(postId);
        user.updateUpPostList(list);
        userRepo.save(user);
        //--------------------------------------
        Post post = returnPost(postId);
        post.decreaseUpCountPost();
        postRepo.save(post);

        return makeUpCountInfoDTO(list);
    }

    public List<UpCountInfoDTO> makeUpCountInfoDTO(List<Long> list){
        // UpCountInfoDTO 객체 리스트 생성
        List<UpCountInfoDTO> upCountInfoDTOList = new ArrayList<>();

        // 각 사용자 upPostList 항목과 게시글의 upCount를 DTO에 추가
        for (Long upPostId : list) {
            Post upPost = postRepo.findById(upPostId).orElseThrow();
            upCountInfoDTOList.add(new UpCountInfoDTO(upPostId,true,upPost.getUpCountPost()));
        }

        return upCountInfoDTOList;
    }

    // postId를 받아서 post를 리턴하는 메서드
    public Post returnPost(Long postId) {
        return postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post -> " + postId));
    }

    // userId를 받아서 user를 리턴하는 메서드
    public User returnUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(()
                -> new RuntimeException("Error can't find user -> " + userId));
    }

    public List<PostReadDTO> sortByUpCountPost(List<PostReadDTO> postReadDTOS) {
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
    public List<PostReadDTO> sortByRecentPost(List<PostReadDTO> postReadDTOS) {
        return postReadDTOS.stream()
                .sorted(Comparator.comparing(PostReadDTO::getPostTime).reversed())
                .collect(Collectors.toList());
    }

    // 게시물 채택수별로 정렬하는 메서드
    public List<PostReadDTO> findByUpCountPost() {
        List<PostReadDTO> postReadDTOS = readAllPosts();
        return sortByUpCountPost(postReadDTOS);
    }
    public PostReadDTO findPostById(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(()
        -> new RuntimeException("Error can't find post -> "+postId));
        List<S3Attachment> s3Attachments = post.getS3Attachments();

        List<S3AttachmentReadDTO> s3AttachmentReadDTOS = s3Attachments.stream()
                .map(s3Attachment -> new S3AttachmentReadDTO(s3Attachment))
                .collect(Collectors.toList());
        return new PostReadDTO(post,s3AttachmentReadDTOS);
    }

    public PostModifyDTO findPostByIdmodifyVer(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post -> "+postId));
        List<S3Attachment> s3Attachments = post.getS3Attachments();
        List<String> fileNames = new ArrayList<>();
        for (S3Attachment s3Attachment : s3Attachments) {
            log.info(s3Attachment.getFileUrl());
            log.info(s3AttachmentService.extractObjectKey(s3Attachment.getFileUrl()));
            fileNames.add(s3AttachmentService.extractObjectKey(s3Attachment.getFileUrl()));
        }
        return new PostModifyDTO(post,fileNames);
    }
}

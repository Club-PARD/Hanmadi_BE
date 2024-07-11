package com.pard.namukkun.post.service;

import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.attachment.service.S3AttachmentService;
import com.pard.namukkun.img.dto.ImgDTO;
import com.pard.namukkun.img.entity.Img;
import com.pard.namukkun.img.repo.ImgRepo;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import com.pard.namukkun.post.dto.*;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import com.pard.namukkun.user.service.UserService;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final S3AttachmentService s3AttachmentService;
    private final ImgRepo imgRepo;

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private final UserService userService;
    private Map<String, Path> tempStorage = new HashMap<>();


    // PostCreateDTO 받아서 postDTO 생성
    public ResponseEntity<?> createPost(PostCreateDTO postCreateDTO) {
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error find user -> " + postCreateDTO.getUserId()));
//        try {
            Post post = makePost(postCreateDTO, user);

            log.info("post created");

            List<String> fileNames = postCreateDTO.getFileNames();
            post.setInitial(true, Data.getDeadLine(post.getPostTime()));

            for (String fileName : fileNames)
                post.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));

            postRepo.save(post);
            return ResponseEntity.ok(post.getPostId());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTML 파싱 오류: " + e.getMessage());
//        }
    }


    // HTML 파싱하는 메서드
    public Post makePost(PostCreateDTO postCreateDTO, User user) {
        // proBackground 파싱
        String proBackgroundHtml = postCreateDTO.getProBackground();
        String proBackgroundText = parseHtml(proBackgroundHtml, user);

        // solution 파싱
        String solutionHtml = postCreateDTO.getSolution();
        String solutionText = parseHtml(solutionHtml, user);

        // benefit 파싱
        String benefitHtml = postCreateDTO.getBenefit();
        String benefitText = parseHtml(benefitHtml, user);



        // -------------- S3에 쓸모없는 이미지 저장된거 삭제 처리로직 (임시 폐기) --------//
        // 리스트에 남은 이미지들은 S3에서 삭제한다.
//        List<Img> imgs = user.getImgs();
//        List<Img> tempImgs = new ArrayList<>(imgs);
//        for (Img img : imgs) {
//            s3AttachmentService.deleteByUrl(img.getImgUrl());
//            tempImgs.remove(img);
//            userRepo.save(user);
//            log.info("이미지 삭제 완료: " + img.getImgUrl());
//        }
//        user.setImgs(tempImgs);
//        userRepo.save(user);
//        log.info("background: " + proBackgroundText);
//        log.info("solution: " + solutionText);
//        log.info("benefit: " + benefitText);

        List<Img> tempImg = new ArrayList<>(user.getImgs());
        for(int i=0; i<tempImg.size(); i++) {
            Img toBeDeletedImg = tempImg.get(i);
            s3AttachmentService.deleteByUrl(toBeDeletedImg.getImgUrl());
            user.deleteImg(toBeDeletedImg);
            imgRepo.delete(toBeDeletedImg);
        }
        userRepo.save(user);
        return Post.toEntity(postCreateDTO, proBackgroundText, solutionText, benefitText, user);
    }

    // HTML에서 텍스트와 이미지를 골라서 파싱하는 메서드
    private String parseHtml(String html, User user) {
        Document document = Jsoup.parse(html);
        StringBuilder sb = new StringBuilder();

        catchNode(document.body().childNodes(), sb, user);

        return sb.toString();
    }

    private void catchNode(List<Node> nodes, StringBuilder sb, User user) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            log.info("node : " + node);
            if (node instanceof TextNode) {
                sb.append(((TextNode) node).text());
                log.info("텍스트: " + node);
            } else if (node instanceof Element) {
                Element element = (Element) node;
                log.info("엘리먼트: " + element);

                if (element.tagName().equals("img")) {
                    String postImgName = element.attr("src");
                    String decodedPostImgName = URLDecoder.decode(postImgName, StandardCharsets.UTF_8);
                    // 프론트에서 받은 이미지 이름을 인코딩된 형태로 저장함
                    List<Img> imgs = user.getImgs();
                    List<Img> tempImgs = new ArrayList<>(imgs);

                    for (Img img : tempImgs) {
                        String decodedImgUrl = URLDecoder.decode(img.getImgUrl(), StandardCharsets.UTF_8);
                        if (decodedImgUrl.contains(decodedPostImgName)) {
                            sb.append("[이미지: ").append(img.getImgUrl()).append("]");
                            user.deleteImg(img);
                            imgRepo.delete(img);
                        }
                    }
                    userRepo.save(user);

                } else if (element.tagName().equals("br")) { // <br> 태그 처리
                    sb.append("\n");
                } else if (element.tagName().equals("p")) { // <p> 태그 처리
                    if (i > 0 && nodes.get(i - 1) instanceof Element && ((Element) nodes.get(i - 1)).tagName().equals("/p")) {
                        sb.append("\n"); // 이전 노드가 </p> 태그인 경우 개행 추가
                    }
                    catchNode(element.childNodes(), sb, user);
                    sb.append("\n"); // <p> 태그가 끝날 때 개행 추가
                } else {
                    // 글씨체와 글씨 굵기 처리
                    boolean isBold = element.tagName().equals("strong") || element.hasClass("ql-size-bold");
                    String sizeClass = element.className().contains("ql-size-") ? element.className() : "";

                    String sizeTag = "";
                    if (sizeClass.contains("ql-size-small")) {
                        sizeTag = "[small]";
                    } else if (sizeClass.contains("ql-size-large")) {
                        sizeTag = "[large]";
                    } else if (sizeClass.contains("ql-size-huge")) {
                        sizeTag = "[huge]";
                    } else {
                        sizeTag = "[normal]";
                    }

                    if (isBold) {
                        sb.append("[bold]");
                    }
                    sb.append(sizeTag);

                    catchNode(element.childNodes(), sb, user);

                    // 태그 닫기
                    if (!sizeTag.equals("[normal]")) {
                        sb.append(sizeTag.replace("[", "[/"));
                    }
                    if (isBold) {
                        sb.append("[/bold]");
                    }
                }
            }
        }
    }

    @Transactional
    // 게시물 임시저장
    public ResponseEntity<?> saveTempPost(PostCreateDTO postCreateDTO) {
        // User 불러옴
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error find user -> " + postCreateDTO.getUserId()));

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

        try {
            // 임시 게시물 생성
            Post tempPost = makePost(postCreateDTO, user);
            List<String> fileNames = postCreateDTO.getFileNames();
            tempPost.setInitial(false, Data.getDeadLine(tempPost.getPostTime()));

            for (String fileName : fileNames)
                tempPost.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));

            postRepo.save(tempPost);

            // 임시게시물 저장
            user.setTempPost(tempPost);
            postRepo.save(tempPost);
            userRepo.save(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTML 파싱 오류: " + e.getMessage());
        }
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

    @Transactional
    //post 수정 메서드
    public ResponseEntity<?> updatePost(Long postId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post: " + postId)); //postId로 post find

        User user = userRepo.findById(post.getUser().getUserId()).orElseThrow(()
                -> new RuntimeException("Error can't find user"));
        // 수정된 HTML 코드를 TEXT형태로 저장 및 수정된 이미지 확인해서 저장 및 삭제.

        // 제안배경 파싱
        String proBackgroundHtml = postUpdateDTO.getProBackground();
        String proBackgroundText = parseHtml(proBackgroundHtml, user);

        // solution 파싱
        String solutionHtml = postUpdateDTO.getSolution();
        String solutionText = parseHtml(solutionHtml, user);

        // benefit 파싱
        String benefitHtml = postUpdateDTO.getBenefit();
        String benefitText = parseHtml(benefitHtml, user);

        try {
            post.updatePost(postUpdateDTO, proBackgroundText, solutionText, benefitText);
        } catch (Exception e) {
            log.warn("post update error: " + e.getMessage());
        }

        // S3에 있는 첨부파일 삭제 및 저장은 글 쓰는 단계에서 이루어지기 때문에 지금은
        // post에 저장된 file이름을 통해서 s3attachmentDTO에 저장해준다.
        List<String> fileNames = postUpdateDTO.getFileNames();

        if (post.getS3Attachments() != null)
            post.getS3Attachments().clear(); // 기존에 있던 url제거
        for (String fileName : fileNames) {
            post.addS3Attachment(s3AttachmentService.getUrlWithFileName(fileName));
        }

        userRepo.save(user);
        postRepo.save(post);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    // postId로 찾아 삭제
    public ResponseEntity<?> deletePost(Long postId) {
        try {
            Optional<Post> optionalPost = postRepo.findById(postId);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                try {
                    Optional<User> optionalUser = userRepo.findById(post.getUser().getUserId());
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        user.setTempPost(null);
                        postRepo.delete(post);
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find user");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find user");
                }
            } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find post");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find post");
        }
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
        log.info("save file : " + fileNames);
        return new ReturnFileNameDTO(fileNames);
    }

    // 이미지 업로드
    @Transactional
    public ResponseEntity<?> uploadImg(MultipartFile file, Long userId) {
        // S3에 이미지 저장
        Optional<User> optionalUser = userRepo.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't find user");
        }
        User user = optionalUser.get();
        String originalImgName = file.getOriginalFilename();
        String UUIDImgName = UUID.randomUUID() + "_" + originalImgName;
        s3AttachmentService.upload(file, UUIDImgName);
        String imgUrl = s3AttachmentService.getUrlWithFileName(UUIDImgName);

        // ImgDTO에 Url 저장
        try {
            Img img = Img.toEntity(user, imgUrl);
            imgRepo.save(img);
            log.info("Img saved successfully", img);
            user.addImg(img);
            userRepo.save(user);
            return ResponseEntity.ok("S3 upload succeed.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("S3 upload failed.");
        }
    }

    @Transactional
    // 포스트의 시간과 현재 시간을 비교하여
    public Integer postCheck(String presentTime) {
        Integer counter = 0;

        // isDone 이 false 인 post 가져오기
        List<Post> posts = postRepo.findByIsDoneFalse();

        // 포메터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Data.timeFormatString);

        // 서버타임 설정
        LocalDate serverTime = LocalDate.parse(presentTime, formatter);

        for (Post post : posts) {
            // 포스트의 시간 가져오기
            LocalDate postTime = LocalDate.parse(post.getPostTime(), formatter);

            post.setInitial(post.isReturn(), Data.getDeadLine(post.getPostTime()));
            if (Integer.parseInt(post.getDeadLine()) < 0) {
                post.setInitial(post.isReturn(), Long.valueOf("0"));
            }
            // 7일이 지났다면
            if (serverTime.isAfter(postTime.plusDays(7))) {
                post.setIsDone(true); // isdone -> true
                counter = counter + 1;
            }

            postRepo.save(post);
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
    public ResponseEntity<?> IncreaseUpCountPost(Long postId, Long userId) {
        User user = returnUser(userId);

        List<Long> list = user.getUpPostList();
        if (list.contains(postId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 채택 중복" + postId);
        }
        list.add(postId);
        user.updateUpPostList(list);
        userRepo.save(user);

        Post post = returnPost(postId);
        post.increaseUpCountPost();
        postRepo.save(post);

        return ResponseEntity.ok(makeUpCountInfoDTO(list, user));
    }

    // 채택 취소하는 메서드
    @Transactional
    public ResponseEntity<?> decreaseUpCountPost(Long postId, Long userId) {
        User user = returnUser(userId);

        List<Long> list = user.getUpPostList();
        if (!list.contains(postId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 채택 한적이 없음" + postId);
        }
        list.remove(postId);
        user.updateUpPostList(list);
        userRepo.save(user);

        Post post = returnPost(postId);
        post.decreaseUpCountPost();
        postRepo.save(post);

        return ResponseEntity.ok(makeUpCountInfoDTO(list, user));
    }

    public List<UpCountInfoDTO> makeUpCountInfoDTO(List<Long> list, User user) {
        // UpCountInfoDTO 객체 리스트 생성
        List<UpCountInfoDTO> upCountInfoDTOList = new ArrayList<>();
        // 새로 저장될 채택 게시물 Id 저장할 리스트
        List<Long> tempList = new ArrayList<>();

        // 각 사용자 upPostList 항목과 게시글의 upCount를 DTO에 추가
        for (Long upPostId : list) {
            Optional<Post> optionalPost = postRepo.findById(upPostId); // Id로 포스트를 찾음
            if (optionalPost.isPresent()) { // 만약 포스트가 있으면 (삭제되지 않았으면) DTO 생성
                Post upPost = optionalPost.get();
                upCountInfoDTOList.add(new UpCountInfoDTO(upPostId, true, upPost.getUpCountPost()));
                tempList.add(upPostId);
            }
        }
        user.updateUpPostList(tempList);
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
                .sorted(Comparator.comparing(PostReadDTO::getSortTime).reversed())
                .collect(Collectors.toList());
    }

    // 게시물 채택수별로 정렬하는 메서드
    public List<PostReadDTO> findByUpCountPost() {
        List<PostReadDTO> postReadDTOS = readAllPosts();
        return sortByUpCountPost(postReadDTOS);
    }

    public PostReadDTO findPostById(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post -> " + postId));
        List<S3Attachment> s3Attachments = post.getS3Attachments();

        List<S3AttachmentReadDTO> s3AttachmentReadDTOS = s3Attachments.stream()
                .map(s3Attachment -> new S3AttachmentReadDTO(s3Attachment))
                .collect(Collectors.toList());
        return new PostReadDTO(post, s3AttachmentReadDTOS);
    }

    // 수정할 게시물 정보 넘겨주기
    public PostUpdateDTO findPostByIdUpdateVer(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("Error can't find post -> " + postId));
        List<S3Attachment> s3Attachments = post.getS3Attachments();
        List<String> fileNames = new ArrayList<>();

        for (S3Attachment s3Attachment : s3Attachments) {
            log.info(s3Attachment.getFileUrl());
            log.info(s3AttachmentService.extractObjectKey(s3Attachment.getFileUrl()));
            fileNames.add(s3AttachmentService.extractObjectKey(s3Attachment.getFileUrl()));
        }
        return new PostUpdateDTO(post, fileNames);
    }
}

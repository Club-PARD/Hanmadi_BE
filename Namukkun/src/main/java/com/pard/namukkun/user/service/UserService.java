package com.pard.namukkun.user.service;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.user.dto.*;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;

    // 유저 생성
    public void createUser(UserCreateDTO dto) {
        userRepo.save(User.toEntity(dto));
    }

//    public void findby
//
//    public List<UserReadDTO> findAllUser() {
//        return userRepo.findAll()
//                .stream()
//                .map(UserReadDTO::new)
//                .collect(Collectors.toList());
//    }

    // kakao oauth 아이디로 user 정보 가져오기
    public UserReadDTO findUserByOauth(Long id) {
        return new UserReadDTO(userRepo.findByOauthID(id));
    }

    // userinfo 수정
    public void updateUser(UserUpdateDTO dto) {
        User user = userRepo.findById(dto.getId()).orElseThrow();
        user.updateUserinfo(dto.getNickName(), dto.getLocal(), dto.getProfileImage());
        userRepo.save(user);
    }

    // 유저 삭제
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    // 가입 한적 있는지 검사
    public Boolean checkSigned(Long oauthID) {
        return userRepo.existsByOauthID(oauthID);
    }
//
//    public UserPostDTO getUserPosts(Long id) {
//        User user = userRepo.findById(id).orElseThrow();
//        UserPostDTO dto = new UserPostDTO();
//        dto.setId(id);
//        if (user.getPosts().isEmpty()) dto.setPosts(null);
//        else dto.setPosts(user.getPosts().stream().map(PostReadDTO::new).toList());
//
//        if (user.getTempPost() == null) dto.setTempPost(null);
//        else dto.setTempPost(new PostReadDTO(user.getTempPost()));
//
//        return dto;
//    }

    // 유저 정보 전달
    public UserInfoDTO getUserInfo(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserInfoDTO(user.getUserId(), user.getNickName(), user.getLocal(), user.getProfileImage());
    }

    // 유저 상세 정보 전달
    public UserReadDTO getUserInfoAll(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserReadDTO(
                user.getUserId(),
                user.getLocal(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileImage(),
                new PostReadDTO(user.getTempPost()),
                user.getPosts().stream().map(PostReadDTO::new).toList()
        );
    }
}

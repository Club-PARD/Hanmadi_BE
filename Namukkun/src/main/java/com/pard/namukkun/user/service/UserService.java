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

    // 유저 정보 전달
    public UserLoginInfoDTO getUserLoginInfoDTO(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserLoginInfoDTO(user.getNickName(), user.getLocal(), user.getProfileImage(), user.getUpPostList(), user.getUpCommentList());
    }
    // 유저 정보 전달
    public UserInfoDTO getUserInfoDTO(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserInfoDTO(user.getNickName(), user.getLocal(), user.getProfileImage());
    }

    // 유저 상세 정보 전달
    public UserReadDTO getUserInfoAll(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return new UserReadDTO(
                user.getUserId(),
                new UserInfoDTO(user.getNickName(), user.getLocal(), user.getProfileImage()),
                user.getEmail(),
                new PostReadDTO(user.getTempPost()),
                user.getPosts().stream().map(PostReadDTO::new).toList()
        );
    }
}

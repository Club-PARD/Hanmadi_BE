package com.pard.namukkun.user.service;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.user.dto.*;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;

    public void createUser(UserCreateDTO dto) {
        userRepo.save(User.toEntity(dto));
    }

//    public void findby

    public List<UserReadDTO> findAllUser() {
        return userRepo.findAll()
                .stream()
                .map(UserReadDTO::new)
                .collect(Collectors.toList());
    }

    public UserReadDTO findUserByOauth(Long id) {
        return new UserReadDTO(userRepo.findByOauthID(id));
    }


    public void updateUserNickname(UserUpdateDTO dto) throws Exception {
        Long id = dto.getId();
        User user = userRepo.findById(dto.getId())
                .orElseThrow(() -> new Exception("User not found"));
        user.updateNickName(dto.getNickName());
        userRepo.save(user);
    }



    public void updateUserLocal(UserUpdateDTO dto) {
        Long userId = dto.getId();
        User user = userRepo.findById(userId).get();
        user.updateLocal(dto.getLocal());
        userRepo.save(user);
    }


    public void deleteUser(UserDeleteDTO dto) {
        userRepo.deleteById(dto.getId());
    }

    // 가입 한적 있는지 검사
    public Boolean checkSigned(Long oauthID) {
        return userRepo.existsByOauthID(oauthID);
    }

    public UserPostDTO getUserPosts(Long id) throws Exception {
        User user = userRepo.findById(id).orElseThrow();
        UserPostDTO dto = new UserPostDTO();
        dto.setId(id);
        if (user.getPosts().isEmpty()) dto.setPosts(null);
        else dto.setPosts(user.getPosts().stream().map(PostReadDTO::new).toList());

        if (user.getTempPost() == null) dto.setTempPost(null);
        else dto.setTempPost(new PostReadDTO(user.getTempPost()));


        return dto;

    }
}

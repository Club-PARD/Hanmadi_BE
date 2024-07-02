package com.pard.namukkun.user.service;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserDeleteDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import com.pard.namukkun.user.dto.UserUpdateDTO;
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

    public List<UserReadDTO> findAllUser(){
        return userRepo.findAll()
                .stream()
                .map(UserReadDTO::new)
                .collect(Collectors.toList());
    }


    public void updateUserNickname(UserUpdateDTO dto) throws Exception {
        Long id = dto.getId();
        User user = userRepo.findById(dto.getId())
                .orElseThrow(() -> new Exception("User not found"));
        user.updateNickName(dto.getNickName());
        userRepo.save(user);
    }


    public void deleteUser(UserDeleteDTO dto){
        userRepo.deleteById(dto.getId());
    }
}

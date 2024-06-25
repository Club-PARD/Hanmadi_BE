package com.pard.namukkun.user.service;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
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

    public void createUser(UserCreateDTO userCreateDTO) {
        userRepo.save(User.toEntity(userCreateDTO));
    }

    public List<UserReadDTO> findAllUser(){
        return userRepo.findAll()
                .stream()
                .map(user -> new UserReadDTO(user))
                .collect(Collectors.toList());
    }

}

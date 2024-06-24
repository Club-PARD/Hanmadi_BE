package com.pard.namukkun.user.service;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepo userRepo;

    public void createUser(UserCreateDTO userCreateDTO) {
        userRepo.save(User.toEntity(userCreateDTO));
    }

}

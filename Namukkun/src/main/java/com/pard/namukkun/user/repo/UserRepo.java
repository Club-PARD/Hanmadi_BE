package com.pard.namukkun.user.repo;

import com.pard.namukkun.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    public User findByOauthID(Long oauthID);

//    public Optional<User> findByOauthID(Long oauthId);
    public boolean existsByOauthID(Long oauthId);
}

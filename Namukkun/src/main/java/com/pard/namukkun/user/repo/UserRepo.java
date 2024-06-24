package com.pard.namukkun.user.repo;

import com.pard.namukkun.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}

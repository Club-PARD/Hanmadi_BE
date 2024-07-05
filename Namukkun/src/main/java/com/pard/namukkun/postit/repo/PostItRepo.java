package com.pard.namukkun.postit.repo;

import com.pard.namukkun.postit.entity.PostIt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostItRepo extends JpaRepository<PostIt, Long> {
}

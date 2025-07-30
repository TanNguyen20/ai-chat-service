package com.tannguyen.ai.repository;

import com.tannguyen.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUsernameIn(List<String> usernames);
}

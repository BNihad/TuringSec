package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByActivationToken(String token);

    boolean existsByUsername(String username);
}

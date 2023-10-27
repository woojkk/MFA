package com.woojkk.mfa.repository;

import com.woojkk.mfa.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  UserEntity findByUsername(String username);
}

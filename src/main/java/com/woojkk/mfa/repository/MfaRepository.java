package com.woojkk.mfa.repository;

import com.woojkk.mfa.data.entity.MfaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaRepository extends JpaRepository<MfaEntity, Long> {
  MfaEntity findByUsername(String username);
}

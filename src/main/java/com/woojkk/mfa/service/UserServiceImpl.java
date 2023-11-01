package com.woojkk.mfa.service;

import com.woojkk.mfa.data.entity.UserEntity;
import com.woojkk.mfa.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserEntity getUser(UserEntity userEntity) {
    return userRepository.findByUsername(userEntity.getUsername());
  }
}

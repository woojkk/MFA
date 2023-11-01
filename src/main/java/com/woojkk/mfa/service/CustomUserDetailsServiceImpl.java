package com.woojkk.mfa.service;

import com.woojkk.mfa.data.dto.CustomUserDetails;
import com.woojkk.mfa.data.entity.UserEntity;
import com.woojkk.mfa.exception.OtpNotApproveException;
import com.woojkk.mfa.util.OTPUtil;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

  private final UserService userService;
  private final MfaService mfaService;
  private String otp;

  public CustomUserDetailsServiceImpl(UserService userService, MfaService mfaService) {
    this.userService = userService;
    this.mfaService = mfaService;
  }

  @Override
  public UserDetails loadUserByUsername(String username, String otp)
      throws UsernameNotFoundException, OtpNotApproveException {

    this.otp = otp;

    if (otp != null) {
      String secretKey = mfaService.getMfaSecretKey(username).getSecretKey();
      if (!OTPUtil.checkCode(otp, secretKey)) {
        throw new OtpNotApproveException("OTP number didn't approve. Please check again.");
      }
    }
    return loadUserByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userEntity = userService.getUser(UserEntity.builder()
        .username(username)
        .build());

    if (userEntity == null) {
      throw new UsernameNotFoundException("The user not exist. Please check again.");
    }

    CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder();
    customUserDetailsBuilder
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .authorities(
            Arrays.stream(userEntity.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()))
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .enabled(true);

    return customUserDetailsBuilder.build();
  }
}

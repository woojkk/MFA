package com.woojkk.mfa.service;

import com.woojkk.mfa.exception.OtpNotApproveException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
  UserDetails loadUserByUsername(String username, String otp) throws UsernameNotFoundException, OtpNotApproveException;
}

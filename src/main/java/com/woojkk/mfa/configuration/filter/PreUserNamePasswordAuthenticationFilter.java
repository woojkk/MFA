package com.woojkk.mfa.configuration.filter;

import com.woojkk.mfa.data.dto.MfaDto;
import com.woojkk.mfa.data.entity.UserEntity;
import com.woojkk.mfa.service.MfaService;
import com.woojkk.mfa.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class PreUserNamePasswordAuthenticationFilter implements Filter {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserService userService;
  private final MfaService mfaService;

  public PreUserNamePasswordAuthenticationFilter(BCryptPasswordEncoder passwordEncoder, UserService userService, MfaService mfaService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.mfaService = mfaService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    if(httpServletRequest.getServletPath().equals("/prelogin") && httpServletRequest.getMethod().equals("POST")){
      String username = httpServletRequest.getParameter("username");
      String password = httpServletRequest.getParameter("password");
      UserEntity userEntity = userService.getUser(UserEntity.builder().username(username).build());

      String encodePassword = passwordEncoder.encode(password);
      userEntity.setPassword(encodePassword);


      if(Optional.ofNullable(userEntity).isPresent() && Optional.ofNullable(userEntity.getUsername()).isPresent()){
        if((passwordEncoder).matches(password, userEntity.getPassword())){
          httpServletRequest.getSession().setAttribute("username", username);
          httpServletRequest.getSession().setAttribute("password", password);
          MfaDto mfaDto = mfaService.getMfa(username);
          if(Optional.ofNullable(mfaDto).isPresent() && Optional.ofNullable(mfaDto.getSecretKey()).isPresent()){

            httpServletRequest.getSession().setAttribute("mfa", true);
            httpServletResponse.sendRedirect("/mfactor");
          }else{
            httpServletRequest.getSession().setAttribute("mfa", false);
            httpServletResponse.sendRedirect("/purelogin");
          }
        }else{
          ((HttpServletResponse) response).sendRedirect("/logout");
        }
      }else{
        ((HttpServletResponse) response).sendRedirect("/logout");
      }

    }else{
      chain.doFilter(request, response);
    }
  }
}

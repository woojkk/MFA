package com.woojkk.mfa.configuration.handler;

import com.woojkk.mfa.data.dto.CustomUserDetails;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    String failureUrl = "/login?error=invalid";

    this.setDefaultFailureUrl(failureUrl);
    request.getSession().removeAttribute("username");
    request.getSession().removeAttribute("password");
    request.getSession().removeAttribute("mfa");
    super.onAuthenticationFailure(request, response, exception);
  }
}

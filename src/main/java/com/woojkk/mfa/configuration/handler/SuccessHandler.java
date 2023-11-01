package com.woojkk.mfa.configuration.handler;

import com.woojkk.mfa.data.dto.CustomUserDetails;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException, ServletException {
    String successUrl = "/main";

    request.getSession().removeAttribute("username");
    request.getSession().removeAttribute("password");
    request.getSession().removeAttribute("mfa");

    this.setDefaultTargetUrl(successUrl);

    HttpSession httpSession = request.getSession();
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    customUserDetails.setPassword(null);
    httpSession.setAttribute("userInfo", customUserDetails);

    super.onAuthenticationSuccess(request, response, authentication);
  }
}

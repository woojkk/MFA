package com.woojkk.mfa.configuration.provider;

import com.woojkk.mfa.exception.OtpNotApproveException;
import com.woojkk.mfa.service.CustomUserDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
  private PasswordEncoder passwordEncoder;
  private volatile String userNotFoundEncodedPassword;
  private CustomUserDetailsService userDetailsService;
  private UserDetailsPasswordService userDetailsPasswordService;

  public CustomDaoAuthenticationProvider() {
    setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
  }

  public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService) {
    this();
    setUserDetailsService(userDetailsService);
  }

  @Override
  @SuppressWarnings("deprecation")
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    if (authentication.getCredentials() == null) {
      this.logger.debug("Failed to authenticate since no credentials provided");
      throw new BadCredentialsException(this.messages.getMessage(
          "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
    String presentedPassword = authentication.getCredentials().toString();
    if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
      this.logger.debug("Failed to authenticate since password does not match stored value");
      throw new BadCredentialsException(this.messages.getMessage(
          "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
  }
  @Override
  protected void doAfterPropertiesSet() throws Exception {
    Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
  }

  @Override
  protected UserDetails retrieveUser(String username,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    prepareTimingAttackProtection();

    try {

      String otp = (String) authentication.getDetails();
      UserDetails loadedUser;
      if (otp == null) {
        loadedUser = this.getUserDetailsService().loadUserByUsername(username);
      } else {
        loadedUser = ((CustomUserDetailsService)this.getUserDetailsService()).loadUserByUsername(username, otp);
      }

      if (loadedUser == null) {
        throw new InternalAuthenticationServiceException(
            "UserDetailsService returned null, which is an interface contract violation");
      }
      return loadedUser;
    } catch (OtpNotApproveException | UsernameNotFoundException e) {
      mitigateAgainstTimingAttack(authentication);
      throw e;
    } catch (InternalAuthenticationServiceException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalAuthenticationServiceException(e.getMessage(), e);
    }
  }

  @Override
  protected Authentication createSuccessAuthentication(Object principal,
      Authentication authentication, UserDetails user) {
    boolean upgradeEncoding = this.userDetailsPasswordService != null &&
        this.passwordEncoder.upgradeEncoding(user.getPassword());
    if (upgradeEncoding) {
      String presentedPassword = authentication.getCredentials().toString();
      String newPassword = this.passwordEncoder.encode(presentedPassword);
      user = this.userDetailsPasswordService.updatePassword(user, newPassword);
    }
    return super.createSuccessAuthentication(principal, authentication, user);
  }

  private void prepareTimingAttackProtection() {
    if (this.userNotFoundEncodedPassword == null) {
      this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }
  }

  private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
    if (authentication.getCredentials() != null) {
      String presentedPassword = authentication.getCredentials().toString();
      this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
    }
  }


  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
    this.passwordEncoder = passwordEncoder;
    this.userNotFoundEncodedPassword = null;
  }

  protected PasswordEncoder getPasswordEncoder() {
    return this.passwordEncoder;
  }

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = (CustomUserDetailsService) userDetailsService;
  }

  protected UserDetailsService getUserDetailsService() {
    return this.userDetailsService;
  }

  public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
    this.userDetailsPasswordService = userDetailsPasswordService;
  }
}

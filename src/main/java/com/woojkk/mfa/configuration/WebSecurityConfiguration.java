package com.woojkk.mfa.configuration;

import com.woojkk.mfa.configuration.filter.CustomUsernamePasswordAuthenticationFilter;
import com.woojkk.mfa.configuration.filter.PreUserNamePasswordAuthenticationFilter;
import com.woojkk.mfa.configuration.handler.FailureHandler;
import com.woojkk.mfa.configuration.handler.LogoutSucceedHandler;
import com.woojkk.mfa.configuration.handler.SuccessHandler;
import com.woojkk.mfa.configuration.provider.CustomDaoAuthenticationProvider;
import com.woojkk.mfa.service.CustomUserDetailsService;
import com.woojkk.mfa.service.MfaService;
import com.woojkk.mfa.service.UserService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
@EnableJpaRepositories(basePackages = {"com.woojkk.mfa.repository"})
@EntityScan(basePackages = {"com.woojkk.mfa.data"}, basePackageClasses = {Jsr310Converters.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;
  private final String permitAllUrl = "/login,/,/prelogin,/mfactor,/purelogin";
  private final UserService userService;
  private final MfaService mfaService;

  public WebSecurityConfiguration(CustomUserDetailsService customUserDetailsService, UserService userService, MfaService mfaService) {
    this.userDetailsService = customUserDetailsService;
    this.userService = userService;
    this.mfaService = mfaService;
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/il8n/**")
        .antMatchers("/sql/**")
        .antMatchers("/css/**")
        .antMatchers("/js/**")
        .antMatchers("/images/**");
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(
        new PreUserNamePasswordAuthenticationFilter(passwordEncoder(), userService, mfaService),
        UsernamePasswordAuthenticationFilter.class);
    http.addFilterAt(customUsernamePasswordAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class);
    http.authenticationProvider(customDaoAuthenticationProvider());

    http.cors().and()
        .headers().frameOptions().sameOrigin()
        .and().authorizeRequests().antMatchers(permitAllUrl.split(",")).permitAll()
        .and().formLogin().loginPage("/login").successHandler(new SuccessHandler()).failureHandler(new FailureHandler())
        .and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSucceedHandler()).invalidateHttpSession(false).permitAll()
        .and().authorizeRequests().anyRequest().authenticated();

    http.csrf().disable();
  }

  private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(this.authenticationManagerBean());
    customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SuccessHandler());
    customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new FailureHandler());

    return customUsernamePasswordAuthenticationFilter;
  }

  private CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
    CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider(userDetailsService);
    customDaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return customDaoAuthenticationProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }
}

package com.woojkk.mfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = ("com.woojkk.mfa"))
public class MfaApplication {

  public static void main(String[] args) {
    SpringApplication.run(MfaApplication.class, args);
  }

}

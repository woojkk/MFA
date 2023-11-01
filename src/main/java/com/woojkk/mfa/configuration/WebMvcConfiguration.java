package com.woojkk.mfa.configuration;


import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Bean(name = "jsonView")
  public MappingJackson2JsonView mappingJackson2JsonView() {
    MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
    mappingJackson2JsonView.setContentType("application/json;charset=UTF-8");
    return mappingJackson2JsonView;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/sql/**").addResourceLocations("classpath:/static/");
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
    registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    WebMvcConfigurer.super.configureMessageConverters(converters);
  }
}

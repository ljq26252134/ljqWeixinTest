package org.ljq.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class AuthenticationInterceptorConfig extends WebMvcConfigurationSupport {
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor());
	}
	
	@Bean
	public AuthenticationInterceptor authenticationInterceptor(){
		return new AuthenticationInterceptor();
	}
}

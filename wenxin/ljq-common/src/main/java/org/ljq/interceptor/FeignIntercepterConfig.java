package org.ljq.interceptor;

import org.ljq.common.security.JwtTokenUtil;
import org.ljq.common.security.SecurityConfigProperties;
import org.ljq.context.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FeignIntercepterConfig implements RequestInterceptor {

	@Autowired
	JwtTokenUtil jwtFactory;

	@Autowired
	SecurityConfigProperties tokenProperties;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		log.info("%s设置远程调用的用户凭证：%s,%s", requestTemplate, UserHolder.getUser(), UserHolder.getUserName());
		String token = jwtFactory.createToken(UserHolder.getUser(), UserHolder.getUserName());
		requestTemplate.header(tokenProperties.getHeader(), token);
	}
}
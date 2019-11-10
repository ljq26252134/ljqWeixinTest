package org.ljq.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljq.common.security.Constant.AuthConstant;
import org.ljq.common.security.JwtTokenUtil;
import org.ljq.common.security.SecurityConfigProperties;
import org.ljq.common.security.UserToken;
import org.ljq.context.UserHolder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 单体权限拦截器
 * 
 * @author Yaky
 *
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	SecurityConfigProperties config;

	@Autowired
	JwtTokenUtil jwtTokenFactory;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		List<String> ignores = config.getIgnores();
		if (ignores != null && !ignores.isEmpty()) {
			for (String ignore : ignores) {
				AntPathMatcher antMatcher = new AntPathMatcher();
				antMatcher.setCaseSensitive(false);
				if (antMatcher.match(ignore, request.getRequestURI())) {
					return true;
				}
			}
		}
		UserToken userToken = jwtTokenFactory.parseToken(request);
		if (userToken == null) {
			return super.preHandle(request, response, handler);
		} else {
			UserHolder.setUser(userToken.getUserCode());
			UserHolder.setUserName(userToken.getUsername());
			MDC.put(AuthConstant.SessionKey, UserHolder.getUser());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserHolder.remove();
		MDC.remove(AuthConstant.SessionKey);
		super.afterCompletion(request, response, handler, ex);
	}

}

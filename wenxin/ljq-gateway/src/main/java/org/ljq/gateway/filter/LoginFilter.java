package org.ljq.gateway.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ljq.common.security.JwtTokenUtil;
import org.ljq.common.security.SecurityConfigProperties;
import org.ljq.common.security.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日
 */

public class LoginFilter extends ZuulFilter {

	@Autowired
	SecurityConfigProperties config;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	public boolean shouldFilter() {

		List<String> ignores = config.getIgnores();
		if (ignores != null && !ignores.isEmpty()) {
			for (String ignore : ignores) {
				AntPathMatcher antMatcher = new AntPathMatcher();
				antMatcher.setCaseSensitive(false);
				RequestContext ctx = RequestContext.getCurrentContext();
				HttpServletRequest request = ctx.getRequest();
				if (antMatcher.match(ignore, request.getRequestURI())) {
					return false;
				}
			};
		}
		RequestContext context = RequestContext.getCurrentContext();
		if (!context.sendZuulResponse()) {
			System.out.println("无登录记录");
			return false;
		}
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		System.out.println("解析token");
		UserToken userToken = jwtTokenUtil.parseToken(request);
		if (userToken != null) {
			System.out.println("解析token成功");
			return null;
		}
		context.setSendZuulResponse(false);
		context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
		context.setResponseBody(null);
		context.set("isSuccess", false);
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}

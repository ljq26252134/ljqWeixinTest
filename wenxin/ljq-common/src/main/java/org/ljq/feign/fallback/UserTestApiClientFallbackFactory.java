package org.ljq.feign.fallback;

import org.ljq.common.security.UserToken;
import org.ljq.common.web.ApiResponse;
import org.ljq.common.web.ApiResponseBuilder;
import org.ljq.feign.UserTestApi;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */
@Component
public class UserTestApiClientFallbackFactory implements FallbackFactory<UserTestApi> {

	@Override
	public UserTestApi create(Throwable cause) {
		// TODO Auto-generated method stub
		return new UserTestApi() {

			@Override
			public ApiResponse<UserToken> findUserToken(String userCode) {
				// TODO Auto-generated method stub
				return ApiResponseBuilder.ok(new UserToken().setUsername("获取失败 "));
			}

		};
	}
}

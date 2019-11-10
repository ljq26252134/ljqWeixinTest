package org.ljq.feign;

import org.ljq.common.security.UserToken;
import org.ljq.common.web.ApiResponse;
import org.ljq.feign.fallback.UserTestApiClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */
@FeignClient(value = "${feign.client.fuwuB}", fallbackFactory = UserTestApiClientFallbackFactory.class)
public interface UserTestApi {
	/*
	 * 从下一个服务 获取token
	 * 
	 * @param userCode
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fuwuB-Api/findUserToken")
	public ApiResponse<UserToken> findUserToken(@RequestParam("userCode") String userCode);
}

package org.fuwuB.web;

import org.ljq.common.security.UserToken;
import org.ljq.common.web.ApiResponse;
import org.ljq.common.web.ApiResponseBuilder;
import org.ljq.common.web.BaseController;
import org.ljq.context.UserHolder;
import org.ljq.feign.UserTestApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 
* @author 作者 liangjq: 
* @version 创建时间：2019年11月9日  
*/

@RestController
@RequestMapping("/fuwuB-Api")
public class FuwuBApiController extends BaseController implements UserTestApi {

	@Override
	@RequestMapping("/findUserToken")
	public ApiResponse<UserToken> findUserToken(@RequestParam String userCode) {
		System.out.println(userCode);
		System.out.println(UserHolder.getUser());
		System.out.println(UserHolder.getUserName());
		return ApiResponseBuilder.ok(new UserToken().setUsername(UserHolder.getUserName()));
	}

}

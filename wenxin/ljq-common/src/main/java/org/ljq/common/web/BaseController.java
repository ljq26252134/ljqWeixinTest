package org.ljq.common.web;

import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日
 */

@Slf4j
public class BaseController {

	@ExceptionHandler(Exception.class)
	public ApiResponse<String> exceptionHandler(Exception exception) {
		StringBuilder msg = new StringBuilder(200);
		msg.append("系统异常：" + exception.getMessage());
		log.error("【系统异常】-" + exception.getMessage(), exception);

		log.info("【业务异常】-" + msg.toString());
		return ApiResponseBuilder.wrap(300, msg.toString());
	}

}

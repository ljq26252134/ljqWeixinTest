
package org.ljq.common.web;

import org.apache.commons.lang.StringUtils;

/**
 * 返回对象构造器与包装器
 */
public class ApiResponseBuilder {

	private ApiResponseBuilder() {
	}

	public static <E> ApiResponse<E> wrap(int code, String message, E o) {
		return new ApiResponse<>(code, message, o);
	}

	public static <E> ApiResponse<E> wrap(int code, String message) {
		return wrap(code, message, null);
	}

	public static <E> ApiResponse<E> wrap(int code) {
		return wrap(code, null);
	}

	public static <E> ApiResponse<E> wrap(Exception e) {
		return new ApiResponse<>(ApiResponse.ERROR_CODE, e.getMessage());
	}

	public static <E> E unWrap(ApiResponse<E> ApiResponse) {
		return ApiResponse.getResult();
	}

	public static <E> ApiResponse<E> illegalArgument() {
		return wrap(ApiResponse.ILLEGAL_ARGUMENT_CODE_, ApiResponse.ILLEGAL_ARGUMENT_MESSAGE);
	}

	public static <E> ApiResponse<E> error() {
		return wrap(ApiResponse.ERROR_CODE, ApiResponse.ERROR_MESSAGE);
	}

	public static <E> ApiResponse<E> error(String message) {
		return wrap(ApiResponse.ERROR_CODE, StringUtils.isBlank(message) ? ApiResponse.ERROR_MESSAGE : message);
	}

	public static <E> ApiResponse<E> ok() {
		return new ApiResponse<>();
	}

	public static <E> ApiResponse<E> tip() {
		return wrap(ApiResponse.SUCCESS_CODE, ApiResponse.SUCCESS_MESSAGE);
	}

	public static <E> ApiResponse<E> ok(E o) {
		return new ApiResponse<>(ApiResponse.SUCCESS_CODE, ApiResponse.SUCCESS_MESSAGE, o);
	}
}

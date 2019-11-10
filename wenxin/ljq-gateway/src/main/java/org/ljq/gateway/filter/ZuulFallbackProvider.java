package org.ljq.gateway.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.ljq.common.web.ApiResponse;
import org.ljq.common.web.ApiResponseBuilder;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.exception.HystrixTimeoutException;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日
 */

@Component
public class ZuulFallbackProvider implements FallbackProvider {

	@Override
	public String getRoute() {
		// TODO Auto-generated method stub
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		if (cause instanceof HystrixTimeoutException) {
			return response(HttpStatus.GATEWAY_TIMEOUT);
		}
		return response(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ClientHttpResponse response(final HttpStatus httpStatus) {
		return new ClientHttpResponse() {

			@Override
			public InputStream getBody() throws IOException {
				ApiResponse<String> response = ApiResponseBuilder.wrap(200, "微服务不能正常提供服务，请稍后");
				return new ByteArrayInputStream(JSON.toJSONString(response, false).getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return httpStatus;
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return httpStatus.value();
			}

			@Override
			public String getStatusText() throws IOException {
				return httpStatus.getReasonPhrase();
			}

			@Override
			public void close() {

			}

		};
	}

}

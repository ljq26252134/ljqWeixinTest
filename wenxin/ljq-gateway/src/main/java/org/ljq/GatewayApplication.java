package org.ljq;
import org.ljq.gateway.filter.LoginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日
 */

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"org.ljq"})
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public LoginFilter tokenFilter() {
		return new LoginFilter();
	}

	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowCredentials(true);
		c.addAllowedOrigin("*");
		c.addAllowedHeader("*");
		c.setMaxAge(18000L);
		c.addAllowedMethod(HttpMethod.GET);
		c.addAllowedMethod(HttpMethod.DELETE);
		c.addAllowedMethod(HttpMethod.OPTIONS);
		c.addAllowedMethod(HttpMethod.PATCH);
		c.addAllowedMethod(HttpMethod.POST);
		c.addAllowedMethod(HttpMethod.PUT);
		s.registerCorsConfiguration("/**", c);
		return null;

	}
}

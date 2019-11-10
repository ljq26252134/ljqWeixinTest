package org.weixinA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月7日
 */
@EnableFeignClients(basePackages = { "org.ljq.feign" })
@SpringBootApplication(scanBasePackages = {"org.ljq","org.weixinA"})
public class WeixinAApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeixinAApplication.class, args);
	}
}

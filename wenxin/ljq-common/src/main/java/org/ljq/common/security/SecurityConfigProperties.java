package org.ljq.common.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日
 */
@ConfigurationProperties(prefix = "security")
@Configuration
@Data
public class SecurityConfigProperties {

	private long expseconds;

	private String secret;
	
	private int allcssends;
	
	private String header;
	
    private List<String> ignores;

}

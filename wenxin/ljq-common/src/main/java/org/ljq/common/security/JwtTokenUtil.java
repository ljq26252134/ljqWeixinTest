package org.ljq.common.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.ljq.common.security.Constant.AuthConstant;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月6日 JWT工具类
 */
@Data
@Slf4j
@Component
public class JwtTokenUtil {

	private final SecurityConfigProperties config;

	public JwtTokenUtil(SecurityConfigProperties config) {
		this.config = config;
	}

	public String createToken(String userCode, String userName) {
		return Jwts.builder().setSubject(userCode).setIssuedAt(new Date()).claim(AuthConstant.UserCode, userCode)
				.claim(AuthConstant.UserName, userName)
				.setExpiration(new Date(System.currentTimeMillis() + config.getExpseconds() * 1000))
				.signWith(SignatureAlgorithm.HS256, config.getSecret()).compact();
	}

	public UserToken parseUserToken(String token) throws Exception {
		try {
			Claims claims = Jwts.parser().setAllowedClockSkewSeconds(config.getAllcssends())
					.setSigningKey(config.getSecret()).parseClaimsJws(token).getBody();
			return new UserToken().setUserCode(claims.getSubject())
					.setUsername((String) claims.get(AuthConstant.UserName));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public UserToken parseToken(HttpServletRequest request) {
		try {
			String token = request.getHeader(config.getHeader());
			if (token != null && !token.isEmpty()) {
				return parseUserToken(token);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
		String userCode = "aaaaa";
		String userName = "ljq";
		String e = Jwts.builder().setSubject(userCode).setIssuedAt(new Date()).claim(AuthConstant.UserCode, userCode)
				.claim(AuthConstant.UserName, userName)
				.setExpiration(new Date(System.currentTimeMillis() + 180000 * 1000))
				.signWith(SignatureAlgorithm.HS256, "ljqStr").compact();
		System.out.println(e);
				
		
	}

}

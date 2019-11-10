package org.ljq.common.security;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserToken implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户代码
	 */
	private String userCode;

	private String token;

	private String username;

}

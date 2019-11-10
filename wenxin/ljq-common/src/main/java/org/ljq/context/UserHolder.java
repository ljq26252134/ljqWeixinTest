package org.ljq.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class UserHolder {

	public static ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<Map<String, Object>>();

	public static void set(String key, Object value) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			threadLocal.set(map);
		}
		map.put(key, value);
	}

	public static Object get(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			threadLocal.set(map);
		}
		return map.get(key);
	}

	public static Object remove(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map != null) {
			return map.remove(key);
		}
		return null;
	}

	public static void remove() {
		threadLocal.remove();
	}

	public static void setUser(String userCode) {
		set(AuthConstant.UserCode, userCode);
	}

	public static void setUserName(String userName) {
		set(AuthConstant.UserName, userName);
	}

	public static Map<String, Object> getCopyContext() {
		return new HashMap<String, Object>(
				Optional.ofNullable(threadLocal.get()).orElse(new HashMap<String, Object>()));
	}

	/**
	 * 设置上下文内容
	 * 
	 * @param usrContext
	 */
	public static void setContext(Map<String, Object> usrContext) {
		threadLocal.set(usrContext);
	}

	public static String getUser() {
		return Optional.ofNullable((String) get(AuthConstant.UserCode)).orElse("");
	}

	public static String getUserName() {
		return Optional.ofNullable((String) get(AuthConstant.UserName)).orElse("");
	}

}

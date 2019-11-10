package org.weixinA.config.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class MD5Kit {

	public static String md5(String text) {
		String encodeStr = DigestUtils.md5Hex(text);
		return encodeStr;
	}

	public static boolean verify(String text, String md5) {
		String md5Text = md5(text);
		if (md5Text.equalsIgnoreCase(md5)) {
			return true;
		}
		return false;
	}

	public static String md5(String text, String key) {
		String encodeStr = DigestUtils.md5Hex(text + key);
		return encodeStr;
	}

	public static boolean verify(String text, String key, String md5) {
		String md5Text = md5(text, key);
		if (md5Text.equalsIgnoreCase(md5)) {
			return true;
		}
		return false;
	}
}
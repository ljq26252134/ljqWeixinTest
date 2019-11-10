package org.weixinA.weather;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.weixinA.config.util.JsonUtils;
import org.weixinA.config.util.WeixinAConstants;

import com.alibaba.fastjson.JSONObject;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月8日
 */

public class WeatherConfig {
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 1.根据城市查询天气
	public static String getRequest1(WxMpXmlMessage wxMessage) {
		String result = null;
		String url = "http://op.juhe.cn/onebox/weather/query";// 请求接口地址
		Map<String,String> params = new HashMap<>();// 请求参数
		params.put("cityname", wxMessage.getContent());// 要查询的城市，如：温州、上海、北京
		params.put("key", WeixinAConstants.weatherAppKey);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "json");// 返回数据的格式,xml或json，默认json

		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.parseObject(result);
			if (object.getInteger("error_code") == 0) {
				JSONObject object2 = object.getJSONObject("result");
				JSONObject object3 = object2.getJSONObject("data").getJSONObject("realtime");
				System.out.println(JsonUtils.toJson(object3));
				return JsonUtils.toJson(object3).replace("date", "日期").replace("city_name", "城市")
						.replace("moon", "月份").replace("week", "星期").replace("weather", "天气")
						.replace("temperature", "温度").replace("humidity", "湿度");
			} else {
				System.out.println(object.get("error_code") + ":" + object.get("reason"));
				return JsonUtils.toJson(object.get("error_code") + ":" + object.get("reason"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "未知错误";
	}

	/**
	 *
	 * @param strUrl 请求地址
	 * @param params 请求参数
	 * @param method 请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map<String,String> params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, String> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}

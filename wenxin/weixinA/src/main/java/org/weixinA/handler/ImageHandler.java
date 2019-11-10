package org.weixinA.handler;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.weixinA.config.util.WeixinAConstants;

import com.baidu.aip.ocr.AipOcr;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */

@Component
public class ImageHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		// 初始化一个AipOcr
		AipOcr client = new AipOcr(WeixinAConstants.APP_ID, WeixinAConstants.API_KEY, WeixinAConstants.SECRET_KEY);
		// 调用接口
		String path = wxMessage.getPicUrl();
		org.json.JSONObject res = client.basicGeneralUrl(path, new HashMap<String, String>());
		System.out.println(res.toString());
		StringBuilder sb = new StringBuilder();
		JSONArray jarrs = res.getJSONArray("words_result");
		for (int j = 0; j < jarrs.length(); j++) {
			org.json.JSONObject next = jarrs.getJSONObject(j);
			sb.append(next.getString("words"));
		}
		return WxMpXmlOutMessage.TEXT().content(sb.toString()).fromUser(wxMessage.getToUser())
				.toUser(wxMessage.getFromUser()).build();
	}

}

package org.weixinA.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.weixinA.builder.TextBuilder;
import org.weixinA.config.util.JsonUtils;
import org.weixinA.config.util.WeixinAConstants;
import org.weixinA.weather.WeatherConfig;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */
@Component
public class MsgHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}
		// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
					&& weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			} else if (StringUtils.startsWithAny(wxMessage.getContent(), "登录")) {
				String url = weixinService.oauth2buildAuthorizationUrl(String
						.format("%s://%s/weixinA/wx/redirect/%s/greet", "http", WeixinAConstants.extraUrl, weixinService.getWxMpConfigStorage().getAppId()),
						WxConsts.OAuth2Scope.SNSAPI_USERINFO, null); 
				String re = "点击<a href=\""+url+"\">这里</a>登录";
				return WxMpXmlOutMessage.TEXT().content(re)
						.fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
			}else if (StringUtils.isNotEmpty(wxMessage.getContent())) {// 获取城市天气
				return WxMpXmlOutMessage.TEXT().content(WeatherConfig.getRequest1(wxMessage))
						.fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
			}  else {
				return WxMpXmlOutMessage.TEXT().content("哈哈哈哈").fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		// TODO 组装回复消息
		String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);

		return new TextBuilder().build(content, wxMessage, weixinService);

	}

}

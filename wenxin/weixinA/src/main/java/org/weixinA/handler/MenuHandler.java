package org.weixinA.handler;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月9日
 */
@Component
public class MenuHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {
		String msg = String.format("type:%s, event:%s, key:%s", wxMessage.getMsgType(), wxMessage.getEvent(),
				wxMessage.getEventKey());
		System.out.println();
		if (EventType.VIEW.equals(wxMessage.getEvent())) {
			return null;
		} else if (EventType.CLICK.equals(wxMessage.getEvent()) && wxMessage.getEventKey().equals("QRCODE")) {
			try {
				WxMpQrCodeTicket wxMpQrCodeTicket = weixinService.getQrcodeService().qrCodeCreateTmpTicket(6, 3600);
				WxMpXmlOutNewsMessage.Item e = new WxMpXmlOutNewsMessage.Item();
				e.setTitle("二维码");
				String url = weixinService.getQrcodeService().qrCodePictureUrl(wxMpQrCodeTicket.getTicket());
				e.setUrl(url);
				e.setPicUrl(url);
				return WxMpXmlOutMessage.NEWS().addArticle(e).fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			} catch (WxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return WxMpXmlOutMessage.TEXT().content(msg).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
				.build();
	}

}

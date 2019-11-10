package org.weixinA.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月8日
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wx")
public class ControllerFacade {

	private final WxMpService wxService;
	private final WxMpMessageRouter messageRouter;

	@GetMapping(value = "/facade/{appid}", produces = "text/plain;charset=utf-8")
	public String test(@PathVariable String appid, @RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "nonce", required = false) String nonce,
			@RequestParam(name = "echostr", required = false) String echostr) {
		log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

		if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
			throw new IllegalArgumentException("请求参数非法，请核实!");
		}
		WxMpConfigStorage e1 = wxService.getWxMpConfigStorage();
		System.out.println(e1.getAppId());
		if (!this.wxService.switchover(appid)) {
			throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
		}

		if (wxService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}
		return "test";
	}

	@PostMapping(value = "/facade/{appid}", produces = "application/xml; charset=UTF-8")
	public String post(@PathVariable String appid, @RequestBody String requestBody,
			@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce, @RequestParam("openid") String openid,
			@RequestParam(name = "encrypt_type", required = false) String encType,
			@RequestParam(name = "msg_signature", required = false) String msgSignature) {
		log.info(
				"\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
						+ " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
				openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

		if (!this.wxService.switchover(appid)) {
			throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
		}

		if (!wxService.checkSignature(timestamp, nonce, signature)) {
			throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
		}

		String out = null;
		if (encType == null) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.route(inMessage);//路由分发微信处理
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toXml();
		} else if ("aes".equalsIgnoreCase(encType)) {
			// aes加密的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
					timestamp, nonce, msgSignature);
			log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
		}
		System.out.println(out);
		// log.debug("\n组装回复信息：{}", out);
		return out;
	}

	private WxMpXmlOutMessage route(WxMpXmlMessage message) {
		try {
			return this.messageRouter.route(message);
		} catch (Exception e) {
			log.error("路由消息时出现异常！", e);
		}

		return null;
	}
}

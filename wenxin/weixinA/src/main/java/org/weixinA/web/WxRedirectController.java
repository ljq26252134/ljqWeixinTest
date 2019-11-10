package org.weixinA.web;

import org.ljq.common.security.JwtTokenUtil;
import org.ljq.common.security.UserToken;
import org.ljq.context.AuthConstant;
import org.ljq.context.UserHolder;
import org.ljq.feign.UserTestApi;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.enums.TicketType;

/**
 * @author 作者 liangjq:
 * @version 创建时间：2019年11月8日
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/redirect/{appid}")
public class WxRedirectController {
	private final WxMpService wxService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserTestApi userTestApi;

	@RequestMapping(value = "/greet", produces = "text/plain;charset=utf-8")
	public String greetUser(@PathVariable String appid, @RequestParam String code) {
		if (!this.wxService.switchover(appid)) {
			throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
		}
		WxMpUser user = null;
		try {
			WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
			user = wxService.oauth2getUserInfo(accessToken, null);// 这里获取到用户信息，则登录成功
			// 把用户信息设置在 上下文中 用feign获取 返回（说明feign调用能带着登录信息）
			UserToken userToken = new UserToken();
			userToken.setUserCode(code);
			userToken.setUsername("ljq");
			userToken.setToken(jwtTokenUtil.createToken(userToken.getUserCode(), userToken.getUsername()));
			UserHolder.setUser(userToken.getUserCode());
			UserHolder.setUserName(userToken.getUsername());
			MDC.put(AuthConstant.SessionKey, UserHolder.getUser());
			String name = userTestApi.findUserToken(userToken.getUserCode()).getResult().getUsername();
			if (name.equals("ljq")) {
				System.out.println("获取登录用户+" + user.getNickname() + "----成功从下一个服务中获取上下文" + name);
				return "获取登录用户+" + user.getNickname() + "----成功从下一个服务中获取上下文" + name;
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
			return "用户获取失败";
		}
		System.out.println("获取登录用户+" + user.getNickname());
		return "获取登录用户+" + user.getNickname();
	}

	@RequestMapping("/ticket")
	public String ticket() {
		String ticket = "";
		try {
			WxMpQrCodeTicket wxMpQrCodeTicket = wxService.getQrcodeService().qrCodeCreateTmpTicket(6, 3600);
			ticket = wxMpQrCodeTicket.getTicket();
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ticket;
	}

}

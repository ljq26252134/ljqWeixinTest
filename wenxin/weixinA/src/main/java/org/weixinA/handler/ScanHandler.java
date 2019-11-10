package org.weixinA.handler;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.weixinA.builder.TextBuilder;

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
public class ScanHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理
    	return new TextBuilder().build("尚未实现扫码事件处理", wxMpXmlMessage, wxMpService);
    }
}

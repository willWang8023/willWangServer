package com.will.wang.wechatPay.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.will.wang.utils.XMLUtils;
import com.will.wang.wechatPay.service.WeChatPayService;
import com.will.wang.wechatPay.utils.WeChatUtils;


@Controller
@RequestMapping("/weChatPay")
public class WeChatPayController {
	
	@Autowired
	private WeChatPayService weChatPayService;
	@Autowired
	private WeChatUtils weChatUtils;
	@Autowired
	private XMLUtils XMLUtils;
	
	
	@RequestMapping("/testWeChatPay")
	public String testWeChatPay(HttpServletRequest request) {
		Double realAmount = 0.00;
		//订单来源ip
		String bill_create_ip = request.getRemoteAddr();
		String realAmountStr = request.getParameter("realAmount");
		if(StringUtils.isEmpty(realAmountStr)) {
			realAmount = 0.01;//此处应该返回页面错误信息
		}
		
		String mweb_url = weChatPayService.testWeChatPay(bill_create_ip, realAmount);
		
		return mweb_url;
	}
	
	@RequestMapping("/weChatPayResult")
	public void weChatPayResult(HttpServletRequest request,HttpServletResponse response) {
		try {
			//将微信的返回参数封装成map
			Map<String, Object> reqData = weChatUtils.getWeChatReplay(request);
			
			String returnCode = String.valueOf(reqData.get("return_code"));
	        String resultCode = String.valueOf(reqData.get("result_code"));
	        if (WeChatUtils.SUCCESS.equals(returnCode) && WeChatUtils.SUCCESS.equals(resultCode)) {
	            boolean signatureValid = weChatPayService.orderQuery(reqData);
	            if (signatureValid) {
	                // TODO 业务处理
	
	                Map<String, String> responseMap = new HashMap<>(2);
	                responseMap.put("return_code", "SUCCESS");
	                responseMap.put("return_msg", "OK");
	                String responseXml = XMLUtils.doMapToXML(responseMap);
	
	                response.setContentType("text/xml");
	                response.getWriter().write(responseXml);
	                response.flushBuffer();
	            }
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

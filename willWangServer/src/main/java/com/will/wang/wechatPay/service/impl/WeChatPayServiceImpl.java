package com.will.wang.wechatPay.service.impl;

import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.will.wang.utils.WeChatPropertyConfig;
import com.will.wang.utils.XMLUtils;
import com.will.wang.utils.http.HttpClient;
import com.will.wang.wechatPay.service.WeChatPayService;
import com.will.wang.wechatPay.utils.WeChatUtils;

@Service
public class WeChatPayServiceImpl implements WeChatPayService {
	
	Log log = LogFactory.getLog(WeChatPayServiceImpl.class);

	@Autowired
	private WeChatPropertyConfig weChatPropertyConfig;
	@Autowired
	private HttpClient httpRequest;
	@Autowired
	private XMLUtils XMLUtils;
	@Autowired
	private WeChatUtils weChatUtils;
	
	public boolean orderQuery(Map<String, Object> reqData) {
		String xmlBody = weChatUtils.packageOrderQueryXml(reqData);
		//查询订单
		CloseableHttpResponse response = httpRequest.doPost(weChatPropertyConfig.getOrderquery(), xmlBody);
		int status = response.getStatusLine().getStatusCode();
		try {
			if(status == HttpClient.SUCCESS) {
				//开始解析这个返回结果，取到需要的东西
				String httpResult = httpRequest.getRequestResult(response);
				 // 过滤
				httpResult = httpResult.replaceAll("<!\\[CDATA\\[|]]>", "");
	            Map<String, Object> map = XMLUtils.doXMLParse(httpResult);
	            String return_code = String.valueOf(map.get("return_code"));
	            if("SUCCESS".equals(return_code)) {
	            		//此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
	                    if (map.get("openid") != null && map.get("trade_type") !=null) {
	                        String total_fee = String.valueOf(map.get("total_fee"));
	                        String order_total_fee = String.valueOf(map.get("total_fee"));
	                        if (Integer.parseInt(order_total_fee) >= Integer.parseInt(total_fee)) {
	                            return true;
	                        }
	                    }
	            }
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public String testWeChatPay(String bill_create_ip, Double realAmount) {
		String mweb_url = null;
		try {
			//产生订单，插入数据库状态为支付中
			
			// 将订单参数封装之后给微信
			String prePayXml = weChatUtils.packageParam(bill_create_ip, realAmount);
//			String prePayXml = "<xml>" + 
//					"<appid>wxd2cd1be31b64c572</appid>" + 
//					"<body>党费缴纳</body>" + 
//					"<mch_id>1270161801</mch_id>" + 
//					"<nonce_str>CFAAE21CC5EA9EEF9BBA69C7D4E8710A</nonce_str>" + 
//					"<notify_url>http://test4.csservice.cn/mapi/wxPayResult.action</notify_url>" + 
//					"<out_trade_no>297e4b7d64d9bcbd0164d9bea2600005</out_trade_no>" + 
//					"<scene_info>{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"http://test4.csservice.cn\",\"wap_name\":\"dangfeijiaona\"}}</scene_info>" + 
//					"<sign>A25A80A121F0121046F57727E20ECC7B</sign>" + 
//					"<spbill_create_ip>0:0:0:0:0:0:0:1</spbill_create_ip>" + 
//					"<total_fee>0</total_fee>" + 
//					"<trade_type>MWEB</trade_type>" + 
//					"</xml>";
			
			CloseableHttpResponse response = httpRequest.doPost(weChatPropertyConfig.getUnifiedorder(), prePayXml);
			int status = response.getStatusLine().getStatusCode();
			if(status == HttpClient.SUCCESS) {
				//开始解析这个返回结果，取到需要的东西
				String httpResult = httpRequest.getRequestResult(response);
				 // 过滤
				httpResult = httpResult.replaceAll("<!\\[CDATA\\[|]]>", "");
	            System.out.println("5---获取预支付回话url和调起支付接口参数========="+prePayXml+"============================");
	            Map<String, Object> map = XMLUtils.doXMLParse(httpResult);
	            String return_code = String.valueOf(map.get("return_code"));
	            if("SUCCESS".equals(return_code)) {
	            	//解析mweb_url
	            	mweb_url = String.valueOf(map.get("mweb_url"));
//		            mweb_url = Jsoup.parse(httpResult).select("mweb_url").html();
		            mweb_url = mweb_url.replaceAll("&amp;", "&");
		            System.out.println("6---获取预支付回话mweb_url========"+mweb_url+"---------------------------------------");
		            System.out.println(httpResult);
	            }else {
	            	log.error(map.get("return_msg"));
	            }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return mweb_url;
		
	}

	
	
}

package com.will.wang.wechatPay.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.will.wang.utils.MD5Util;
import com.will.wang.utils.UUIDUtils;
import com.will.wang.utils.WeChatPropertyConfig;
import com.will.wang.utils.XMLUtils;

@Component
public class WeChatUtils {
	
	//支付状态
	public static final String SUCCESS= "SUCCESS";
	public static final String FAIL= "FAIL";
	
	@Autowired
	private WeChatPropertyConfig weChatPropertyConfig;
	@Autowired
	private XMLUtils XMLUtils;
	
	public boolean checkWeChatSign() {
		String url = weChatPropertyConfig.getAccessTocken();
		
		return true;
	}

	
	
	public String packageOrderQueryXml(Map<String, Object> map) {
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", String.valueOf(map.get("appid")));
		treeMap.put("mch_id", String.valueOf(map.get("mch_id")));
		treeMap.put("transaction_id", String.valueOf(map.get("transaction_id")));
		treeMap.put("nonce_str", UUIDUtils.createUUID());
//		treeMap.put("out_trade_no", this.out_trade_no);
		
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + weChatPropertyConfig.getMerchantKey());
		String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		System.out.println("008---查询订单参数sign签名打印============="+sign);
		treeMap.put("sign", sign);

		String xmlStr = XMLUtils.doMapToXML(treeMap);
		return xmlStr;
	}
	
	/**
	 * 封装需要提交给微信的参数
	 * 此处参数再实际使用中最好传入一个订单类，根据订单参数封装提交给微信的数据
	 * @param bill_create_ip
	 * @param amount
	 * @return
	 */
	public String packageParam(String bill_create_ip, Double amount) {
		String prePayXml = null;
		try {
			//订单id
			String out_trade_no  = UUIDUtils.createUUID();
			//获取配置信息，appId，商户id，商户名称，商户key，回调地址
			String appid = weChatPropertyConfig.getAppid();
			String merchantId = weChatPropertyConfig.getMerchantId();
			String merchantKey = weChatPropertyConfig.getMerchantKey();
			String merchantName = weChatPropertyConfig.getMerchantName();
			String notifyUrl = weChatPropertyConfig.getNotifyUrl();
					
			TreeMap<String, String> treeMap = new TreeMap<String, String>();
			treeMap.put("appid", appid);
			treeMap.put("mch_id", merchantId);// 设置商户号
			treeMap.put("nonce_str", UUIDUtils.createUUID());//随机数
			treeMap.put("body", URLEncoder.encode("测试支付", "UTF-8"));//商品描述  
			treeMap.put("out_trade_no", out_trade_no);//商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一,详细说明
//			treeMap.put("total_fee", String.valueOf((int)Math.floor(amount*100)));// 商品总金额,以分为单位，古放大100倍向下取整、
			treeMap.put("total_fee", String.valueOf(amount));
			treeMap.put("spbill_create_ip", bill_create_ip);
			treeMap.put("notify_url", notifyUrl);//通知回调地址
			treeMap.put("trade_type", "MWEB");//H5支付
			String sceneinfo = "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"http://test4.csservice.cn\",\"wap_name\":\"dangfeijiaona\"}}";
			treeMap.put("scene_info",sceneinfo);
			treeMap.put("openid", null);
			treeMap.put("attach", URLEncoder.encode("测试微信支付", "UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (String key : treeMap.keySet()) {
				sb.append(key).append("=").append(treeMap.get(key)).append("&");
			}
			sb.append("key=" + merchantKey);
			String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
			treeMap.put("sign", sign);
			StringBuilder xml = new StringBuilder();
			xml.append("<xml>\n");
			for (Map.Entry<String, String> entry : treeMap.entrySet()) {
					xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
			}
			xml.append("</xml>");
			System.out.println(xml.toString());
			
			prePayXml = new String(xml.toString().getBytes("UTF-8"), "ISO-8859-1");
				//调用微信接口
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return prePayXml;
	}

	/**
	 * 获取微信回调数据
	 * @param request
	 * @return
	 */
	public Map<String, Object> getWeChatReplay(HttpServletRequest request){
		Map<String, Object> map = null;
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");
			map = XMLUtils.doXMLParse(result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}

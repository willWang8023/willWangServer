package com.will.wang.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:config/wechatConfig.properties")
@ConfigurationProperties(prefix = "wechat")
public class WeChatPropertyConfig {
	public String appid;
	public String merchantId;
	public String merchantName;
	public String merchantKey;
	public String notifyUrl;
	public String unifiedorder;
	public String orderquery;
	public String accessTocken;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getUnifiedorder() {
		return unifiedorder;
	}
	public void setUnifiedorder(String unifiedorder) {
		this.unifiedorder = unifiedorder;
	}
	public String getOrderquery() {
		return orderquery;
	}
	public void setOrderquery(String orderquery) {
		this.orderquery = orderquery;
	}
	public String getAccessTocken() {
		return accessTocken;
	}
	public void setAccessTocken(String accessTocken) {
		this.accessTocken = accessTocken;
	}
}

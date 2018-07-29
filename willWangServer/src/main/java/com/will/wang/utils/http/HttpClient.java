package com.will.wang.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class HttpClient {
	
	public static final int SUCCESS = 200;
	
	CloseableHttpClient httpClient = null;
	CloseableHttpResponse response = null;
	
	private static class HttpClientHolder{
        private static HttpClient instance=new HttpClient();
    }
    private HttpClient(){
    }
    public static HttpClient getInstance(){
        return HttpClientHolder.instance;
    }
	
	public String doGet(String uri){
		HttpGet httpGet = new HttpGet(uri);
		return sendHttpGet(httpGet);
	}
	
	/**
	 * 发送Get请求
	 * @param uri
	 * @param map
	 * @return
	 */
	public String doGet(String uri, Map<String, Object> map){
		 List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		 for(Map.Entry<String,Object> entry : map.entrySet()){
			 parameters.add(new BasicNameValuePair(entry.getKey(),String.valueOf(entry.getValue())));
		 }
		HttpGet httpGet = new HttpGet(uri);
		String param = null;
		try{
			param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			//build get uri with params
			httpGet.setURI(new URIBuilder(httpGet.getURI().toString() + "?" + param).build());
		}catch(Exception e){
			e.printStackTrace();
		}
		return sendHttpGet(httpGet);
	}
	
	/**
	 * 无参POST请求
	 * @param uri
	 * @return
	 */
	public CloseableHttpResponse doPost(String uri){
		HttpPost httpPost = new HttpPost(uri);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送post请求，参数用map接收
	 * @param url 地址
	 * @param map 参数
	 * @return 返回值
	 */
	public CloseableHttpResponse doPost(String url,Map<String,String> map) {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for(Map.Entry<String,String> entry : map.entrySet()){
			pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sendHttpPost(post);
	}
	
	/**
	 * POST发送xml文件
	 * @param uri
	 * @param reqXml
	 * @return
	 */
	public CloseableHttpResponse doPost(String uri, String reqXml){
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader("Content-Type", "application/xml");
		StringEntity entity = null;
		try{
			entity = new StringEntity(reqXml, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		//http post with xml data
		httpPost.setEntity(entity);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 
	 * @param uri
	 * @param map
	 * @return
	 */
	public String doPut(String uri, Map<String, Object> map){
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		 for(Map.Entry<String,Object> entry : map.entrySet()){
			 parameters.add(new BasicNameValuePair(entry.getKey(),String.valueOf(entry.getValue())));
		 }
		
		HttpPut httpPut = new HttpPut(uri);
		String param = null;
		try{
			param = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			httpPut.setURI(new URIBuilder(httpPut.getURI().toString() + "?" + param).build());
		}catch(Exception e){
			e.printStackTrace();
		}
		return sendHttpPut(httpPut);
	}
	
	private CloseableHttpResponse sendHttpPost(HttpPost httpPost){
		
//		HttpEntity entity = null;
//		String responseContent = null;
		try{
			httpClient = HttpClients.createDefault();
//			httpPost.setConfig(config);
			response = httpClient.execute(httpPost);
//			int status = response.getStatusLine().getStatusCode();
//			if(status == 200) {//支付成功
//				entity = response.getEntity();
//				responseContent = EntityUtils.toString(entity, "UTF-8");
//			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	private String sendHttpGet(HttpGet httpGet){
//		CloseableHttpClient httpClient = null;
//		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try{
			httpClient = HttpClients.createDefault();
//			httpGet.setConfig(config);
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			try{
//				if(response != null)
//					response.close();
//				if(httpClient != null)
//					httpClient.close();
//			}catch(IOException e){
//				e.printStackTrace();
//			}
		}
		return responseContent;
	}
	
	
	private String sendHttpPut(HttpPut httpPut){
//		CloseableHttpClient httpClient = null;
//		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try{
			httpClient = HttpClients.createDefault();
//			httpPut.setConfig(config);
			response = httpClient.execute(httpPut);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return responseContent;
	}
	
	public String getRequestResult(CloseableHttpResponse response) {
		HttpEntity entity = null;
		String responseContent = null;
		int status = response.getStatusLine().getStatusCode();
		if(status == HttpClient.SUCCESS) {//支付成功
			entity = response.getEntity();
			try {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}finally {
				try{
					if(response != null)
						response.close();
					if(httpClient !=null)
						httpClient.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}

}

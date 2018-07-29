package com.will.wang.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

@Component
public class XMLUtils {

	/**
	 * Map转xml字符串
	 * @param params
	 * @return
	 */
	public String doMapToXML(Map<String,String> params) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : params.entrySet()) {
				xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
		}
		xml.append("</xml>");
		return null;
	}
	
	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public Map<String, Object> doXMLParse(String strxml) throws IOException{
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		try {
			// 创建saxReader对象  
	        SAXReader reader = new SAXReader();  
	        // 通过read方法读取一个文件 转换成Document对象  
	        Document document = reader.read(new ByteArrayInputStream(strxml.getBytes("UTF-8")));  
	        //获取根节点元素对象  
	        Element node = document.getRootElement();
	        m = parse(node, m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
	
	
	private Map<String, Object> parse(Element node, Map<String, Object> m) {  
		m.put(node.getName(), node.getTextTrim());
        // 当前节点下面子节点迭代器  
        Iterator<Element> it = node.elementIterator();  
        // 遍历  
        while (it.hasNext()) {  
            // 获取某个子节点对象  
            Element e = it.next();  
            // 对子节点进行遍历  
            parse(e,m);  
        }  
        return m;
    }  
	
	public static void main(String[] args) throws IOException {
		String testXML = "<xml><return_code>FAIL</return_code>\r\n" + 
				"<return_msg>invalid total_fee</return_msg>\r\n" + 
				"</xml>";
		XMLUtils utils = new XMLUtils();
		Map<String, Object> result = utils.doXMLParse(testXML);
	}
	
}

package com.oumuv.cas.runmain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
 
public class HttpClientUtil {
 
	  public static boolean sendPost(String url,String sessionId) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	        	if(url==null||url.isEmpty()){
	        		System.out.println("URL为空！");
	        		return false;
	        	}
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
//	            conn.setRequestProperty("accept", "*/*");
//	            conn.setRequestProperty("Cookie", cookie);
//	            conn.setRequestProperty("connection", "Keep-Alive");
//	            conn.setRequestProperty("user-agent",
//	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//	            
//	            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
	            conn.setDoOutput(true);  
//	            conn.setRequestMethod("GET");  
	            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");  
//	            conn.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");  
	            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");  
	            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");  
	            conn.setRequestProperty("Connection", "keep-alive");  
	            conn.setRequestProperty("Cookie", sessionId);  
	            conn.setRequestProperty("Host", "192.168.99.230:8443");
//	            conn.setRequestProperty("Referer", referer);  
	            conn.setRequestProperty("User-Agent",   
	                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");  
	            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	            // 建立实际的连接  
	            conn.connect();  
	            // 获取所有响应头字段  
	            Map<String, List<String>> map = conn.getHeaderFields();
	            boolean flag = false;
	            String content = "";
	            for (String key : map.keySet()) {
//	                System.out.println(key + "--->" + map.get(key));
	            	 content += map.get(key) == null?"":map.get(key).toString().trim();
	                if(content.indexOf("200")>0){
	                	flag = true;
	                	break;
	                }
	            }  
	            in = new BufferedReader(  
	                    new InputStreamReader(conn.getInputStream(),"utf-8"));
	            String line;
				String s = in.readLine();
				while ((line = in.readLine()) != null) {
	                result += line;  
	            }  
	            if(result!=null&&result.indexOf("errcode\":\"0000")>0){return true;}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	            }
	        }
	        return false;
	    } 
}
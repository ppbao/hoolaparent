package com.unisharing.hoola.hoolacommon.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;

public class HoolaSendSmsUtil {

	private static String key = "084545912546a5261d797d65fccbfa12";// 产品密钥
	private static String r = "json";// 返回格式

	public static void sendSms(String mob, String sendContent, String p) {

		try {
			String sendContentUtf8 = java.net.URLEncoder.encode(sendContent,
					"utf-8");
			HttpClient client = new HttpClient();
			String str = "http://www.tui3.com/api/send/?k=" + key + "&r=" + r
					+ "&p=" + p + "&t=" + mob + "&c=" + sendContentUtf8;
			HttpMethod method = new GetMethod(str);
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				System.out.println(tmp); // 这里打印出来的内容是
											// utf8格式，如果在windows中输出，请自行处理
			}

			method.releaseConnection();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String strg[]) {
		sendSms("13666698085", "尊敬的会员,您的注册验证码是138438,感谢您使用Hoola客户端！", "1");
	}

}

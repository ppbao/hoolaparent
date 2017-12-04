package com.unisharing.hoola.hoolacommon.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HoolaUtils {

	public static Map<Character, List<String>> badwordMap = new HashMap<Character, List<String>>();

	/**
	 * 以日期作为文件夹
	 * 
	 * @return
	 */
	public static String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 生成唯一的文件名 规则： 用户ID + 时间戳
	 * 
	 * @param uid
	 * @return
	 */
	public static String getFileName(String uid) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		return uid + timestamp;
	}

	public static void writeFile(File file, byte[] bytes) throws Exception {
		if (file != null) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			RandomAccessFile videoRandom = null;
			videoRandom = new RandomAccessFile(file, "rw");
			videoRandom.write(bytes);
			videoRandom.close();
		}

	}

	public static String getIp() {
		try {
			return InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	public static String getRamdCode() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();

		while (sb.length() < 4) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String md5File(MultipartFile file  ) throws IOException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(file.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Map<Character, List<String>> wordSetToMap(
			Set<String> sensitiveWordSet) {
		badwordMap.clear();
		for (String s : sensitiveWordSet) {
			char c = s.charAt(0);
			List<String> strs = badwordMap.get(c);
			if (strs == null) {
				strs = new ArrayList<String>();
				badwordMap.put(c, strs);
			}
			strs.add(s);
		}
		return badwordMap;
	}

	public static String filter(String src, Map<Character, List<String>> wordMap) {
		if (src == null) {
			return null;
		}
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			String find = null;
			if (wordMap.containsKey(c)) {
				List<String> words = wordMap.get(c);
				for (String s : words) {
					String temp = src.substring(i,
							(s.length() <= (src.length() - i)) ? i + s.length()
									: i);
					if (s.equals(temp)) {
						find = s;
						break;
					}
				}
			}
			if (find != null) {
				strb.append("***");
				i += (find.length() - 1);
			} else {
				strb.append(c);
			}
		}
		return strb.toString();
	}

	public static String hasSensitiveWord(String src,
			Map<Character, List<String>> wordMap) {
		if (src == null) {
			return null;
		}
		String find = null;
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);

			if (wordMap.containsKey(c)) {
				List<String> words = wordMap.get(c);
				for (String s : words) {
					String temp = src.substring(i,
							(s.length() <= (src.length() - i)) ? i + s.length()
									: i);
					if (s.equals(temp)) {
						find = s;
						break;
					}
				}
			}
			if (find != null) {
				break;
			}
		}
		return find;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}

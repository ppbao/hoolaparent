package com.unisharing.hoola.hoolacommon.utils;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HoolaSecurity {

	private static String PASSWORD_PERFIX = "duanqu";

	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encodePassword(String password) {
		return toMd5(password + PASSWORD_PERFIX);
	}

	public static String encodeToken(String email) {
		if (email != null) {
			return toMd5(email + System.currentTimeMillis());
		} else {
			return toMd5(UUID.randomUUID().toString()
					+ System.currentTimeMillis());
		}

	}

	private static String toMd5(String password) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			byte[] byteDigest = md5.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getHash(InputStream fis) throws Exception {
		byte[] buffer = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			md5.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(md5.digest());
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out
				.println(HoolaSecurity
						.toMd5("http://online-duanqu-video-hd.oss.aliyuncs.com/20130714/41b94213-6c5f-4f0d-9266-e0d08927716a.mp4"));
	}
}

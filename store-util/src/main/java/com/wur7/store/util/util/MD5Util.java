package com.wur7.store.util.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {
	
	public static final String md5(String res) {
		char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
		try {
			byte[] e = res.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(e);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			
			for(int dd = 0; dd < j; ++dd) {
				byte byte0 = md[dd];
				str[k++] = hexDigits[byte0 >>> 4 & 15];
				str[k++] = hexDigits[byte0 & 15];
			}
			
			String var11 = new String(str);
			return var11;
		} catch (Exception var10) {
			return null;
		}
	}
	
	/**
	 * * 使用md5的算法进行加密
	 * 
	 * @param plainText
	 *            加密字段
	 * @param charset
	 *            字符集
	 * @param isUpper
	 *            是否大写
	 * @return 加密后字段
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(String plainText, String charset, boolean isUpper) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes(charset));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("没有字符集！");
		}
		if (isUpper) {
			return byteToHex(secretBytes).toUpperCase();
		}
		return byteToHex(secretBytes);
	}

	private static String byteToHex(byte[] inbuf) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < inbuf.length; i++) {
			String byteStr = Integer.toHexString(inbuf[i] & 0xFF);
			if (byteStr.length() != 2)
				strBuf.append('0').append(byteStr);
			else {
				strBuf.append(byteStr);
			}
		}
		return new String(strBuf);
	}

	public static String getMd5(String srcSignString, String key, String charset) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(srcSignString.getBytes(charset));

			StringBuilder result = new StringBuilder();
			byte[] temp;
			temp = md5.digest(key.getBytes(charset));
			for (byte b : temp) {
				result.append(Integer.toHexString((0x000000ff & b) | 0xffffff00).substring(6));
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

package org.dewdrop.steamhelper.util.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.dewdrop.steamhelper.util.bean.CodeMsgBean;
import org.dewdrop.steamhelper.util.exception.SteamHelperException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {
	
	private static final String caPath = "D:/IT/jdk/1.8/jre/lib/security/";				// JDK信任库位置
	private static final String crtPath = "E:/steamcommunity_302/";						// cert存放位置
	private static final String crtName = "steamcommunity.crt";						// cert存放位置
	private static final String caKeyStore = "cacerts";									// JDK 信任库文件路径
	private static final String caPassword = "changeit";								// CA根证书生成密码
	private static final String clientCertPath = "C:/Users/L.zhen/.keystore";			// 客户端证书文件路径
	private static final String clientCertPassword = "dewdrop123456";					// 客户端证书密码
	private  static SSLSocketFactory sslFactory;
	
	public static CodeMsgBean get(String url, Map<String, Object> param, boolean onlyCheck) throws Exception {
		if (StringUtil.isBlank(url) || !url.startsWith("http")) {
			throw new SteamHelperException("错误的url请求:" + url);
		}
		if (url.startsWith("https")) {
			return httpsGet(url, param, onlyCheck);
		}
		return httpGet(url, param, onlyCheck);
	}
	
	public static CodeMsgBean post(String url, Object param) throws Exception {
		if (StringUtil.isBlank(url) || !url.startsWith("http")) {
			throw new SteamHelperException("错误的url请求:" + url);
		}
		if (url.startsWith("https")) {
			return httpsPost(url, param);
		}
		return httpPost(url, param);
	}
	
	private static CodeMsgBean httpGet(String requestUrl, Map<String, Object> param, boolean onlyCheck) throws Exception {
		StringBuilder builder = new StringBuilder();
		if (param != null) {
			builder.append("?");
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		HttpURLConnection conn = null;
		CodeMsgBean responseBody = new CodeMsgBean();
		try {
			URL url = new URL(requestUrl + builder.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			// 从conn中提取结果
			int code = conn.getResponseCode();
			responseBody.setCode(code);
			if (!onlyCheck) {
				BufferedReader reader = null;
				if (code == 200) {
					log.info("http GET url请求成功, url={}", requestUrl);
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					log.info("http GET url请求失败, responseCode={}, url={}", code, requestUrl);
					reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				StringBuilder result = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				responseBody.setMsg(result.toString());
				reader.close();
			}
			conn.disconnect();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseBody;
	}
	
	private static CodeMsgBean httpsGet(String requestUrl, Map<String, Object> param, boolean onlyCheck) throws Exception {
		StringBuilder builder = new StringBuilder();
		if (param != null) {
			builder.append("?");
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		HttpsURLConnection conn = null;
		CodeMsgBean responseBody = new CodeMsgBean();
		try {
			URL url = new URL(requestUrl + builder.toString());
			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setSSLSocketFactory(initSSL());
			conn.connect();
			// 从conn中提取结果
			int code = conn.getResponseCode();
			responseBody.setCode(code);
			if (!onlyCheck) {
				BufferedReader reader = null;
				if (code == 200) {
					log.info("https GET url请求成功, url={}", requestUrl);
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					log.info("https GET url请求失败, responseCode={}, url={}", code, requestUrl);
					reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				StringBuilder result = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				responseBody.setMsg(result.toString());
				reader.close();
			}
			conn.disconnect();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseBody;
	}
	
	private static CodeMsgBean httpPost(String requestUrl, Object param) throws Exception {
		String json = JsonUtil.toJSONString(param);
		OutputStreamWriter wr = null;
		HttpURLConnection conn = null;
		CodeMsgBean responseBody = new CodeMsgBean();
		try {
			URL url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json);
			wr.close();
			conn.connect();
			// 从conn中提取结果
			int code = conn.getResponseCode();
			responseBody.setCode(code);
			BufferedReader reader = null;
			if (code == 200) {
				log.info("http POST url请求成功, url={}", requestUrl);
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				log.info("http POST url请求失败, responseCode={}, url={}", code, requestUrl);
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			StringBuilder buffer = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			responseBody.setMsg(buffer.toString());
			reader.close();
			conn.disconnect();
		} finally {
			if (wr != null) {
				wr.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseBody;
	}
	
	private static CodeMsgBean httpsPost(String requestUrl, Object param) throws Exception {
		String json = JsonUtil.toJSONString(param);
		OutputStreamWriter wr = null;
		CodeMsgBean responseBody = new CodeMsgBean();
		HttpsURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setSSLSocketFactory(initSSL());
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json);
			wr.close();
			conn.connect();
			// 从conn中提取结果
			int code = conn.getResponseCode();
			responseBody.setCode(code);
			BufferedReader reader = null;
			if (code == 200) {
				log.info("https POST url请求成功, url={}", requestUrl);
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				log.info("https POST url请求失败, responseCode={}, url={}", code, requestUrl);
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			StringBuilder buffer = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			responseBody.setMsg(buffer.toString());
			reader.close();
			conn.disconnect();
		} finally {
			if (wr != null) {
				wr.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseBody;
	}
	
	
	
	//  创建一个客户端证书 SSLSocketFactory
	private static SSLSocketFactory initSSL() throws Exception {
		if (sslFactory == null) {
			// 加载证书之前，先处理keytool
			initKey();
			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManager[] tm = {new MyX509TrustManager()};
			KeyStore trustStore = KeyStore.getInstance("JKS");
			// 加载客户端证书
			FileInputStream clientInputStream = new FileInputStream(clientCertPath);
			trustStore.load(clientInputStream, clientCertPassword.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
			kmf.init(trustStore, clientCertPassword.toCharArray());
			sslContext.init(kmf.getKeyManagers(), tm, new SecureRandom());
			sslFactory = sslContext.getSocketFactory();
		}
		return sslFactory;
	}
	
	// 创建JDK 信任库，证书管理器类
	private static class MyX509TrustManager implements X509TrustManager {
		private final X509TrustManager sunJSSEX509TrustManager;
		MyX509TrustManager() throws Exception {
			KeyStore ks = KeyStore.getInstance("JKS");
			// 获取CA证书
			FileInputStream caInputStream = new FileInputStream(caPath + caKeyStore);
			ks.load(caInputStream, caPassword.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
			tmf.init(ks);
			TrustManager[] tms = tmf.getTrustManagers();
			for (TrustManager tm : tms) {
				if (tm instanceof X509TrustManager) {
					sunJSSEX509TrustManager = (X509TrustManager) tm;
					return;
				}
			}
			throw new Exception("Couldn't not initialize");
		}
		
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				sunJSSEX509TrustManager.checkClientTrusted(x509Certificates, s);
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				sunJSSEX509TrustManager.checkServerTrusted(x509Certificates, s);
		}
		
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return sunJSSEX509TrustManager.getAcceptedIssuers();
		}
	}
	
	private static void initKey() {
		try {
			delete();
			create();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void delete() throws IOException, InterruptedException {
		String cmd = "keytool -delete -alias mykey -keystore " + caPath + caKeyStore + " -storepass " + caPassword;
		Process ps = Runtime.getRuntime().exec(cmd);
		ps.waitFor();
	}
	
	private static void create() throws IOException, InterruptedException {
		String cmd = "keytool -importcert -file " + crtPath + crtName + " -keystore " + caPath +  caKeyStore + " -storepass " + caPassword + " -alias mykey -noprompt";
		Process ps = Runtime.getRuntime().exec(cmd);
		ps.waitFor();
	}
	
	private static boolean today() throws IOException {
		String cmd1 = "keytool -list -alias mykey -keystore " + caPath + caKeyStore + " -storepass " + caPassword;
		String todayStr = DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD);
		boolean result = false;
		Process ps = null;
		String line;
		try {
			ps = Runtime.getRuntime().exec(cmd1);
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			while ((line = br.readLine()) != null) {
				if (line.contains(todayStr)) {
					result = true;
					break;
				}
			}
			ps.waitFor();
			br.close();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}

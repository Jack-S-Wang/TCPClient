package com.dascom.operation.utils;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@Component
public class HttpClientUtils {
	
	
	
	

	private static final Logger logger = LogManager.getLogger(HttpClientUtils.class);

	static CloseableHttpClient client = null;
	static CloseableHttpResponse response = null;

	// 设置连接超时
	private static RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000)
			.setConnectTimeout(5000).setSocketTimeout(5000).build();

	/**
	 * 封装请求头
	 * 
	 * @param headersParam
	 *            请求头参数,json格式
	 * @param httpMethod
	 */
	public static void packageHeader(String headersParam, HttpRequestBase httpMethod) {
		// 将json字符串转为map
		Map params = new HashMap();
		params = JSON.parseObject(headersParam);
		if (params != null) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * httpclient get请求
	 * 
	 * @param url
	 * @param headerPara
	 * @return
	 */
	public static Map<String, Object> doGet(String url, String headerPara) {
		/*
		 * CloseableHttpClient client = null; CloseableHttpResponse response = null;
		 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			HttpEntity entity = response.getEntity();
			String resultLine = EntityUtils.toString(entity, "UTF-8");
			resultMap.put("resultLine", resultLine);
			resultMap.put("requestUrl", url);
		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {
				response.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return resultMap;
	}

	/**
	 * httpclient Post请求
	 * 
	 * @param url
	 * @param body
	 *            参数
	 * @param headerParam
	 *            请求头
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> doPost(String url, String body, String headerParam) {

		/*
		 * CloseableHttpClient client = null; CloseableHttpResponse response = null;
		 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			// 设置请求头
			packageHeader(headerParam, post);
			post.setEntity(new StringEntity(body));
			// 获取返回信息
			response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			HttpEntity entity = response.getEntity();

			String resultLine = EntityUtils.toString(entity, "UTF-8");
			resultMap.put("resultLine", resultLine);
			resultMap.put("requestUrl", url);
		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}

	/**
	 * httpclient Delete请求
	 * 
	 * @param 请求URL
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> doDelete(String url, String headerParam) {
		/*
		 * CloseableHttpClient client = null; CloseableHttpResponse response = null;
		 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpDelete delete = new HttpDelete(url);
			
			// 设置请求头
			packageHeader(headerParam, delete);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(50000)
					.build();
			delete.setConfig(requestConfig);
			response = client.execute(delete);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			String resultLine = null;
			if (statusCode <= 400) {
				resultLine = "删除成功";
			} else {
				resultLine = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			resultMap.put("resultLine", resultLine);
			resultMap.put("requestUrl", url);

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}

	/**
	 * httpclient Patch请求
	 * 
	 * @param 请求url
	 * @param jason参数
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> doPatch(String url, String jsonPara, String headerParam) {
		/*
		 * CloseableHttpClient client = null; CloseableHttpResponse response = null;
		 */
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpPatch patch = new HttpPatch(url);
			patch.setConfig(requestConfig);
			// 设置请求头
			packageHeader(headerParam, patch);
			// 设置json参数
			patch.setEntity(new StringEntity(jsonPara));
			response = client.execute(patch);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			String resultLine = null;
			if (statusCode <= 400) {
				resultLine = "修改成功";
			} else {
				resultLine = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			resultMap.put("resultLine", resultLine);
			resultMap.put("requestUrl", url);

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}

	/**
	 * 邮箱警报
	 * 
	 * @param interfaceName
	 * @param code
	 * @param resultLine
	 */
	/*public static void sendEmail(String emailUrl, String interfaceName, int code, String resultLine) {

		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(emailUrl);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			// 测试
			
			 * builder.addTextBody("sender", "qnm123456"); builder.addTextBody("password",
			 * "qnm123456Gzds1130");
			 
			builder.addTextBody("sender", "e10001");
			builder.addTextBody("password", "Gzds1130");
			builder.addTextBody("recipient", "522267533@qq.com");
			builder.addTextBody("subject", "请求地址:" + interfaceName,
					ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8));
			builder.addTextBody("content", "----错误信息---- 错误码：" + code + ",错误内容：" + resultLine,
					ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8));
			Charset charset = Charset.forName("UTF-8");
			builder.setCharset(charset);
			
			HttpEntity entity = builder.build();

			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			logger.info("----邮件已发送----");
		} catch (ClientProtocolException e) {
			logger.error("----调用client错误----" + e.getStackTrace().toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("----调用IO错误----" + e.getStackTrace().toString());
			e.printStackTrace();
		}
	}*/

	
	/**
	 * 钉钉报警
	 * @param dingdingUrl
	 * @param resultMap
	 * @param requestUrl
	 */
	public static void sendDingding( String dingdingUrl ,Map<String, Object> resultMap, String requestUrl) {

		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(dingdingUrl);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000)
					.build();
			post.setConfig(requestConfig);

			JSONObject jsonParam = new JSONObject();
			int statusCode = (int) resultMap.get("statusCode"); // http错误码
			String resultLine = resultMap.get("resultLine").toString(); // 错误信息

			String content = "请求地址:" + requestUrl + "  ----错误信息---- 错误码：" + statusCode + "  ,错误内容：" + resultLine;
			jsonParam.put("content", content);
			jsonParam.put("msgtype", "text");
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(code<400) {
				logger.info("----钉钉信息已发送----");
			}else {
				logger.error("----发送失败，错误原因----"+result);
			}
		} catch (ClientProtocolException e) {
			logger.error("----调用client错误----"+e.getStackTrace().toString());
		} catch (IOException e) {
			logger.error("----调用IO错误----"+e.getStackTrace().toString());
		}
	}

}

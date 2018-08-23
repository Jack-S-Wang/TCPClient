package com.dascom.operation.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
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

import com.alibaba.fastjson.JSON;

public class HttpClientUtils {

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
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			HttpEntity entity = response.getEntity();
			String resultLine = EntityUtils.toString(entity, "UTF-8");
			resultMap.put("resultLine", resultLine);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			// 设置请求头
			packageHeader(headerParam, post);
			post.setEntity(new StringEntity(body));
			// ִ获取返回信息
			response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put("statusCode", statusCode);
			HttpEntity entity = response.getEntity();
			String resultLine = EntityUtils.toString(entity, "UTF-8");
			resultMap.put("resultLine", resultLine);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
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

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			client = HttpClients.createDefault();
			HttpPatch patch = new HttpPatch(url);
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

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

}

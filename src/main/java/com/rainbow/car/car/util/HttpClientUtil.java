package com.rainbow.car.car.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 基于HTTP Client连接池的工具类
 * @author huangzhuodong
 */
public class HttpClientUtil {
  private static PoolingHttpClientConnectionManager pool;
  private static String UTF_8 = "UTF-8";

  private static void init() {
    if (pool == null) {
      pool = new PoolingHttpClientConnectionManager();
      pool.setMaxTotal(50);
      pool.setDefaultMaxPerRoute(5);
    }
  }

  private static CloseableHttpClient getHttpClient() {
    init();
    return HttpClients.custom().setConnectionManager(pool).build();
  }

  public static String getForString(String url) {
    HttpGet httpGet = new HttpGet(url);
    return executeAndGetStringResponseData(httpGet,true);
  }

  public static String getForString(String url, Map<String, Object> params) {
    URIBuilder uriBuilder = new URIBuilder();
    uriBuilder.setPath(url);
    ArrayList<NameValuePair> pairs = covertMapToParameterFormat(params);
    uriBuilder.setParameters(pairs);
    HttpGet httpGet = null;
    try {
      httpGet = new HttpGet(uriBuilder.build());
    } catch (URISyntaxException e) {

      e.printStackTrace();
      return null;
    }
    return executeAndGetStringResponseData(httpGet,true);
  }

  public static String getForString(String url, Map<String, Object> headers, Map<String, Object> params)
      throws URISyntaxException {
    URIBuilder ub = new URIBuilder();
    ub.setPath(url);
    ArrayList<NameValuePair> pairs = covertMapToParameterFormat(params);
    ub.setParameters(pairs);
    HttpGet httpGet = new HttpGet(ub.build());
    for (Map.Entry<String, Object> param : headers.entrySet()) {
      httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
    }
    return executeAndGetStringResponseData(httpGet,false);
  }

  public static String postForString(String url) {
    HttpPost httpPost = new HttpPost(url);
    return executeAndGetStringResponseData(httpPost,true);
  }

  public static String postForString(String url,String json) {
    HttpPost httpPost = new HttpPost(url);
    StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
    entity.setContentEncoding("UTF-8");
    entity.setContentType("application/json");
    httpPost.setEntity(entity);

    return executeAndGetStringResponseData(httpPost,true);
  }

  public static String postForString(String url, Map<String, Object> params) throws UnsupportedEncodingException {
    HttpPost httpPost = new HttpPost(url);
    ArrayList<NameValuePair> pairs = covertMapToParameterFormat(params);
    httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

    return executeAndGetStringResponseData(httpPost,true);
  }

  public static String postForString(String url, Map<String, Object> headers, Map<String, Object> params)
      throws UnsupportedEncodingException {
    HttpPost httpPost = new HttpPost(url);
    for (Map.Entry<String, Object> param : headers.entrySet()) {
      httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
    }
    ArrayList<NameValuePair> pairs = covertMapToParameterFormat(params);
    httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));


    return executeAndGetStringResponseData(httpPost,false);
  }

  private static ArrayList<NameValuePair> covertMapToParameterFormat(Map<String, Object> params) {
    ArrayList<NameValuePair> pairs = new ArrayList<>();
    for (Map.Entry<String, Object> param : params.entrySet()) {
      pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
    }
    return pairs;
  }

  private static String executeAndGetStringResponseData(HttpRequestBase request,
      boolean withCommonHeader) {
    CloseableHttpClient httpClient = getHttpClient();
    try {
      if(withCommonHeader){
        addCommonHeader(request);
      }
      return httpClient.execute(request,new StringBodyResponseHandler());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void addCommonHeader(HttpRequestBase request){
    request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
  }

  /**
   * 取出String时使用的处理类
   */
  static class StringBodyResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response)
        throws ClientProtocolException, IOException {
      String rs = null;
      HttpEntity entity = response.getEntity();
      if(entity != null){
        rs = EntityUtils.toString(entity);
      }
      return rs;
    }
  }
}
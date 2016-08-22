/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyun.api.gateway.demo.util;

import com.aliyun.api.gateway.demo.constant.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Http工具类
 */
public class HttpUtil {
    /**
     * HTTP GET
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpGet(String url, Map<String, String> headers, String appKey, String appSecret,
                                       int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.GET, url, null, signHeaderPrefixList);

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpGet get = new HttpGet(encodeUrl(url));

        for (Map.Entry<String, String> e : headers.entrySet()) {
            get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return httpClient.execute(get);
    }

    /**
     * HTTP POST表单
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param formParam 表单参数
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, Map<String, String> formParam,
                                        String appKey, String appSecret, int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_FORM);

        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, formParam, signHeaderPrefixList);

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(formParam);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return httpClient.execute(post);
    }

    /**
     * Http POST 字符串
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param body 字符串请求体
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, String body, String appKey,
                                        String appSecret, int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, Constants.ENCODING));

        }
        httpClient = wrapClient(httpClient);
        return httpClient.execute(post);
    }

    /**
     * HTTP POST 字节数组
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param bytes 字节数组请求体
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, byte[] bytes, String appKey,
                                        String appSecret, int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bytes != null) {
            post.setEntity(new ByteArrayEntity(bytes));

        }
        httpClient = wrapClient(httpClient);
        return httpClient.execute(post);
    }

    /**
     * HTTP PUT 字符串
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param body 字符串请求体
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPut(String url, Map<String, String> headers, String body, String appKey,
                                       String appSecret, int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.PUT, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPut put = new HttpPut(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return httpClient.execute(put);
    }

    /**
     * HTTP PUT字节数组
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param bytes 字节数组请求体
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPut(String url, Map<String, String> headers, byte[] bytes, String appKey,
                                       String appSecret, int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.PUT, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPut put = new HttpPut(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bytes != null) {
            put.setEntity(new ByteArrayEntity(bytes));

        }

        return httpClient.execute(put);
    }

    /**
     * HTTP DELETE
     *
     * @param url http://host+path+query
     * @param headers Http头
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param timeout 超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpDelete(String url, Map<String, String> headers, String appKey, String appSecret,
                                          int timeout, List<String> signHeaderPrefixList)
            throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.DELETE, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpDelete delete = new HttpDelete(encodeUrl(url));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return httpClient.execute(delete);
    }

    /**
     * 构建FormEntity
     * 
     * @param formParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, String> formParam)
            throws UnsupportedEncodingException {
        if (formParam != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : formParam.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, formParam.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.ENCODING);
            formEntity.setContentType(ContentType.CONTENT_TYPE_FORM);
            return formEntity;
        }

        return null;
    }

    /**
     * 初始化基础Header
     *
     * @param headers Http头
     * @param appKey APP KEY
     * @param appSecret APP密钥
     * @param method Http方法
     * @param requestAddress http://host+path+query
     * @param formParam 表单参数
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 基础Header
     * @throws MalformedURLException
     */
    private static Map<String, String> initialBasicHeader(Map<String, String> headers, String appKey, String appSecret,
                                                          String method, String requestAddress, Map formParam,
                                                          List<String> signHeaderPrefixList)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        URL url = new URL(requestAddress);
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(url.getPath())) {
            stringBuilder.append(url.getPath());
        }
        if (StringUtils.isNotBlank(url.getQuery())) {
            stringBuilder.append("?");
            stringBuilder.append(url.getQuery());
        }

        headers.put(HttpHeader.HTTP_HEADER_USER_AGENT, Constants.USER_AGENT);
        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_SIGNATURE,
                SignUtil.sign(method, stringBuilder.toString(), headers, formParam, appSecret, signHeaderPrefixList));

        return headers;
    }

    /**
     * 读取超时时间
     * 
     * @param timeout
     * @return
     */
    private static int getTimeout(int timeout) {
        if (timeout == 0) {
            return Constants.DEFAULT_TIMEOUT;
        }

        return timeout;
    }

    /**
     * 对Query参数进行编码
     * 
     * @param url
     * @return
     */
    private static String encodeUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }

        try {
            URL tmpUrl = new URL(url);
            StringBuilder sb = new StringBuilder();
            sb.append(tmpUrl.getProtocol());
            if (StringUtils.isNotBlank(tmpUrl.getProtocol())) {
                sb.append("://");
            }
            sb.append(tmpUrl.getHost());
            if (StringUtils.isNotBlank(tmpUrl.getPath())) {
                sb.append(tmpUrl.getPath());
            }
            if (StringUtils.isNotBlank(tmpUrl.getQuery())) {
                sb.append("?");

                boolean flag = false;
                for (String s : tmpUrl.getQuery().split("\\&")) {
                    if (flag) {
                        sb.append("&");
                    }

                    flag = true;
                    String key = s.split("\\=")[0];
                    String value = s.split("\\=")[1];
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, Constants.ENCODING));
                }
            }

            return sb.toString();
        } catch (MalformedURLException e) {
            return url;
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    //以下是wrapClient方法
    /**
     * 获取可信任https链接，以避免不受信任证书出现peer not authenticated异常
     *
     * @param base
     * @return
     */
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                                               String string) {
                }
                public void checkServerTrusted(X509Certificate[] xcs,
                                               String string) {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

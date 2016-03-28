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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.aliyun.api.gateway.demo.constant.Constants;
import com.aliyun.api.gateway.demo.constant.HttpHeader;
import com.aliyun.api.gateway.demo.constant.SystemHeader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

/**
 * 签名工具
 */
public class SignUtil {

    /**
     * 计算签名
     *
     * @param method               HttpMethod
     * @param url                  Path+Query
     * @param headers              Http头
     * @param formParamMap         POST表单参数
     * @param secret               APP密钥
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 签名后的字符串
     */
    public static String sign(String method, String url, Map<String, String> headers, Map formParamMap, String secret, List<String> signHeaderPrefixList) {
        try {
            Mac hmacSha256 = Mac.getInstance(Constants.HMAC_SHA256);
            byte[] keyBytes = secret.getBytes(Constants.ENCODING);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, Constants.HMAC_SHA256));

            return new String(Base64.encodeBase64(hmacSha256.doFinal(buildStringToSign(headers, url, formParamMap, method, signHeaderPrefixList)
                    .getBytes(Constants.ENCODING))), Constants.ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建待签名字符串
     *
     * @param headers              Http头
     * @param url                  Path+Query
     * @param formParamMap         POST表单参数
     * @param method
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 签名字符串
     */
    private static String buildStringToSign(Map<String, String> headers, String url, Map formParamMap, String method, List<String> signHeaderPrefixList) {
        StringBuilder sb = new StringBuilder();

        sb.append(method.toUpperCase()).append(Constants.LF);
        if (headers.get(HttpHeader.HTTP_HEADER_ACCEPT) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_ACCEPT));
        }
        sb.append(Constants.LF);
        if (headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5));
        }
        sb.append(Constants.LF);
        if (headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE));
        }
        sb.append(Constants.LF);
        if (headers.get(HttpHeader.HTTP_HEADER_DATE) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_DATE));
        }
        sb.append(Constants.LF);
        sb.append(buildHeaders(headers, signHeaderPrefixList));
        sb.append(buildResource(url, formParamMap));

        return sb.toString();
    }

    /**
     * 构建待签名Path+Query+FormParams
     *
     * @param url          Path+Query
     * @param formParamMap POST表单参数
     * @return 待签名Path+Query+FormParams
     */
    private static String buildResource(String url, Map formParamMap) {
        if (url.contains("?")) {
            String path = url.split("\\?")[0];
            String queryString = url.split("\\?")[1];
            url = path;
            if (formParamMap == null) {
                formParamMap = new HashMap();
            }
            if (StringUtils.isNotBlank(queryString)) {
                for (String query : queryString.split("\\&")) {
                    String key = query.split("\\=")[0];
                    String value = "";
                    if (query.split("\\=").length == 2) {
                        value = query.split("\\=")[1];
                    }
                    if (formParamMap.get(key) == null) {
                        formParamMap.put(key, value);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(url);

        if (formParamMap != null && formParamMap.size() > 0) {
            sb.append('?');

            //参数Key按字典排序
            Map<String, String> sortMap = new TreeMap<String, String>();
            sortMap.putAll(formParamMap);

            int flag = 0;
            for (Map.Entry<String, String> e : sortMap.entrySet()) {
                if (flag != 0) {
                    sb.append('&');
                }

                flag++;
                String key = e.getKey();
                String val = e.getValue();

                if (val == null || ((val instanceof String) && StringUtils.isBlank(val))) {
                    sb.append(key);
                } else {
                    sb.append(key).append("=").append(val);
                }
            }
        }

        return sb.toString();
    }

    /**
     * 构建待签名Http头
     *
     * @param headers              请求中所有的Http头
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 待签名Http头
     */
    private static String buildHeaders(Map<String, String> headers, List<String> signHeaderPrefixList) {
        Map<String, String> headersToSign = new TreeMap<String, String>();

        if (headers != null) {
            StringBuilder signHeadersStringBuilder = new StringBuilder();

            int flag = 0;
            for (Map.Entry<String, String> header : headers.entrySet()) {
                if (isHeaderToSign(header.getKey(), signHeaderPrefixList)) {
                    if (flag != 0) {
                        signHeadersStringBuilder.append(",");
                    }
                    flag++;
                    signHeadersStringBuilder.append(header.getKey());
                    headersToSign.put(header.getKey(), header.getValue());
                }
            }

            headers.put(SystemHeader.X_CA_SIGNATURE_HEADERS, signHeadersStringBuilder.toString());
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> e : headersToSign.entrySet()) {
            sb.append(e.getKey()).append(':').append(e.getValue()).append(Constants.LF);
        }

        return sb.toString();
    }

    /**
     * Http头是否参与签名
     * return
     */
    private static boolean isHeaderToSign(String headerName, List<String> signHeaderPrefixList) {
        if (StringUtils.isBlank(headerName)) {
            return false;
        }

        if (headerName.startsWith(Constants.CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
            return true;
        }

        if (signHeaderPrefixList != null) {
            for (String signHeaderPrefix : signHeaderPrefixList) {
                if (headerName.startsWith(signHeaderPrefix)) {
                    return true;
                }
            }
        }

        return false;
    }
}

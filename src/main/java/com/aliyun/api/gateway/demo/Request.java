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
package com.aliyun.api.gateway.demo;

import com.aliyun.api.gateway.demo.enums.Method;

import java.util.List;
import java.util.Map;

/**
 * Request
 */
public class Request {

    public Request() {
    }

    public Request(Method method, String url, String appKey, String appSecret, int timeout) {
        this.method = method;
        this.url = url;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.timeout = timeout;
    }

    /**
     * （必选）请求方法
     */
    private Method method;

    /**
     * （必选）Host+Path+Query
     */
    private String url;

    /**
     * （必选)APP KEY
     */
    private String appKey;

    /**
     * （必选）APP密钥
     */
    private String appSecret;

    /**
     * （必选）超时时间,单位毫秒,设置零默认使用com.aliyun.apigateway.demo.constant.Constants.DEFAULT_TIMEOUT
     */
    private int timeout;

    /**
     * （可选） HTTP头
     */
    private Map<String, String> headers;

    /**
     * （可选）表单参数
     */
    private Map<String, String> formBody;

    /**
     * （可选）字符串Body体
     */
    private String stringBody;

    /**
     * （可选）字节数组类型Body体
     */
    private byte[] bytesBody;

    /**
     * （可选）自定义参与签名Header前缀
     */
    private List<String> signHeaderPrefixList;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getFormBody() {
        return formBody;
    }

    public void setFormBody(Map<String, String> formBody) {
        this.formBody = formBody;
    }

    public String getStringBody() {
        return stringBody;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
    }

    public byte[] getBytesBody() {
        return bytesBody;
    }

    public void setBytesBody(byte[] bytesBody) {
        this.bytesBody = bytesBody;
    }

    public List<String> getSignHeaderPrefixList() {
        return signHeaderPrefixList;
    }

    public void setSignHeaderPrefixList(List<String> signHeaderPrefixList) {
        this.signHeaderPrefixList = signHeaderPrefixList;
    }
}

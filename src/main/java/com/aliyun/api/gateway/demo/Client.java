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

import com.aliyun.api.gateway.demo.util.HttpUtil;
import org.apache.http.HttpResponse;

/**
 * Client
 */
public class Client {
    /**
     * 发送请求
     *
     * @param request request对象
     * @return HttpResponse
     * @throws Exception
     */
    public static HttpResponse execute(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                return HttpUtil.httpGet(request.getUrl(), request.getHeaders(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case POST_FORM:
                return HttpUtil.httpPost(request.getUrl(), request.getHeaders(), request.getFormBody(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case POST_STRING:
                return HttpUtil.httpPost(request.getUrl(), request.getHeaders(), request.getStringBody(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case POST_BYTES:
                return HttpUtil.httpPost(request.getUrl(), request.getHeaders(), request.getBytesBody(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case PUT_STRING:
                return HttpUtil.httpPut(request.getUrl(), request.getHeaders(), request.getStringBody(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case PUT_BYTES:
                return HttpUtil.httpPut(request.getUrl(), request.getHeaders(), request.getBytesBody(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            case DELETE:
                return HttpUtil.httpDelete(request.getUrl(), request.getHeaders(), request.getAppKey(), request.getAppSecret(), request.getTimeout(), request.getSignHeaderPrefixList());
            default:
                throw new IllegalArgumentException(String.format("unsupported method:%s", request.getMethod()));
        }
    }
}

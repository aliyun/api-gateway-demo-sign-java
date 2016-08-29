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

import com.aliyun.api.gateway.demo.constant.Constants;
import com.aliyun.api.gateway.demo.constant.ContentType;
import com.aliyun.api.gateway.demo.constant.HttpHeader;
import com.aliyun.api.gateway.demo.constant.HttpSchema;
import com.aliyun.api.gateway.demo.enums.Method;
import com.aliyun.api.gateway.demo.util.MessageDigestUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用示例
 * 请替换APP_KEY,APP_SECRET,HOST,CUSTOM_HEADERS_TO_SIGN_PREFIX为真实配置
 */
public class Demo {
    //APP KEY
    private final static String APP_KEY = "app_key";
    // APP密钥
    private final static String APP_SECRET = "app_secret";
    //API域名
    private final static String HOST = "api_host";
    //自定义参与签名Header前缀（可选,默认只有"X-Ca-"开头的参与到Header签名）
    private final static List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();

    static {
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add("Custom");
    }

    /**
     * HTTP GET
     *
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        //请求URL
        String url = "/demo/get?qk1=qv2&qkn=qvn";

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        headers.put("CustomHeader", "demo");

        Request request = new Request(Method.GET, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP POST 表单
     *
     * @throws Exception
     */
    @Test
    public void postForm() throws Exception {
        //请求URL
        String url = "/demo/post/form";

        Map<String, String> bodyParam = new HashMap<String, String>();
        bodyParam.put("FormParamKey", "FormParamValue");

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");

        Request request = new Request(Method.POST_FORM, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setFormBody(bodyParam);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP POST 字符串
     *
     * @throws Exception
     */
    @Test
    public void postString() throws Exception {
        //请求URL
        String url = "/demo/post/string";
        //Body内容
        String body = "demo string body content";

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(body));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        Request request = new Request(Method.POST_STRING, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setStringBody(body);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP POST 字节数组
     *
     * @throws Exception
     */
    @Test
    public void postBytes() throws Exception {
        //请求URL
        String url = "/demo/post/bytes";
        //Body内容
        byte[] bytesBody = "demo bytes body content".getBytes(Constants.ENCODING);

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(bytesBody));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        Request request = new Request(Method.POST_BYTES, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setBytesBody(bytesBody);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP PUT 字符串
     *
     * @throws Exception
     */
    @Test
    public void putString() throws Exception {
        //请求URL
        String url = "/demo/put/string";
        //Body内容
        String body = "demo string body content";

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(body));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        Request request = new Request(Method.PUT_STRING, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setStringBody(body);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP PUT 字节数组
     *
     * @throws Exception
     */
    @Test
    public void putBytesBody() throws Exception {
        //请求URL
        String url = "/demo/put/bytes";
        //Body内容
        byte[] bytesBody = "demo bytes body content".getBytes(Constants.ENCODING);

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(bytesBody));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);

        Request request = new Request(Method.PUT_BYTES, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
        request.setBytesBody(bytesBody);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * HTTP DELETE
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        //请求URL
        String url = "/demo/delete";

        Map<String, String> headers = new HashMap<String, String>();
        //（必填）根据期望的Response内容类型设置
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");

        Request request = new Request(Method.DELETE, HttpSchema.HTTP + HOST + url, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);
        request.setHeaders(headers);
        request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

        //调用服务端
        HttpResponse response = Client.execute(request);

        print(response);
    }

    /**
     * 打印Response
     *
     * @param response
     * @throws IOException
     */
    private void print(HttpResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(response.getStatusLine().getStatusCode()).append(Constants.LF);
        for (Header header : response.getAllHeaders()) {
            sb.append(MessageDigestUtil.iso88591ToUtf8(header.getValue())).append(Constants.LF);
        }
        sb.append(readStreamAsStr(response.getEntity().getContent())).append(Constants.LF);
        System.out.println(sb.toString());
    }

    /**
     * 将流转换为字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readStreamAsStr(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WritableByteChannel dest = Channels.newChannel(bos);
        ReadableByteChannel src = Channels.newChannel(is);
        ByteBuffer bb = ByteBuffer.allocate(4096);

        while (src.read(bb) != -1) {
            bb.flip();
            dest.write(bb);
            bb.clear();
        }
        src.close();
        dest.close();

        return new String(bos.toByteArray(), Constants.ENCODING);
    }
}

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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.aliyun.api.gateway.demo.constant.Constants;
import org.apache.commons.codec.binary.Base64;

/**
 * 消息摘要工具
 */
public class MessageDigestUtil {
    /**
     * 先进行MD5摘要再进行Base64编码获取摘要字符串
     *
     * @param inStr
     * @return
     */
    public static String base64AndMD5(String inStr) {
        if (inStr == null) {
            throw new IllegalArgumentException("inStr can not be null");
        }
        return base64AndMD5(toBytes(inStr));
    }

    /**
     * 先进行MD5摘要再进行Base64编码获取摘要字符串
     *
     * @return
     */
    public static String base64AndMD5(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can not be null");
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(bytes);
            final Base64 base64 = new Base64();
            final byte[] enbytes = base64.encode(md.digest());
            return new String(enbytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("unknown algorithm MD5");
        }
    }

    /**
     * String转换为字节数组
     *
     * @param str
     * @return
     */
    private static byte[] toBytes(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(Constants.ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

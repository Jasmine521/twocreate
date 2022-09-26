package com.example.twocreate.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * 生成 SHA-256 加密的字符串
     *
     * @param input 输入字符串
     * @return
     */
    public static String sha256(String input) {
        return sha256(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 SHA-256 加密的字符串
     *
     * @param input 输入字节数组
     * @return 十六进制字符串
     */
    public static String sha256(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(input);
        byte[] digest = md.digest();
        return ByteUtil.toHexString(digest);
    }

    /**
     * HMAC-SHA256加密
     * @param data
     * @param key
     * @return 字节数组
     */
    public static byte[] hmacSha256AsBytes(byte[] data, byte[] key) {
        SecretKey skey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(skey);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        mac.update(data);
        return mac.doFinal();
    }

    /**
     * HMAC-SHA256加密
     * @param data
     * @param key
     * @return 十六进制字符串
     */
    public static String hmacSha256(byte[] data,byte[] key){
        return ByteUtil.toHexString(hmacSha256AsBytes(data,key));
    }

    /**
     * HMAC-SHA256加密
     * @param data
     * @param key
     * @return
     */
    public static String hmacSha256(String data,String key){
        return hmacSha256(data.getBytes(StandardCharsets.UTF_8),key.getBytes(StandardCharsets.UTF_8));
    }
}

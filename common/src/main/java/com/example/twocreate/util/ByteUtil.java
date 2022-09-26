package com.example.twocreate.util;

public class ByteUtil {
    /**
     * 转换字节为十六进制字符串，全小写
     * @param b 输入字节
     * @return 十六进制字符串
     */
    public static String toHexString(byte[] b){
        StringBuilder sb = new StringBuilder(b.length*2);
        for(byte x : b){
            int hi = (x & 0xf0) >> 4;
            int lo = x & 0x0f;
            sb.append(HEX_CHARS[hi]);
            sb.append(HEX_CHARS[lo]);
        }
        return sb.toString().trim();
    }
    /**
     * Convert byte to hex string (all lower-case).
     *
     * @param b Input bytes.
     * @return Hex string.
     */
    public static String toHex(byte b) {
        int hi = (b & 0xf0) >> 4;
        int lo = b & 0x0f;
        char[] cs = { HEX_CHARS[hi], HEX_CHARS[lo] };
        return new String(cs);
    }

    private static final String HEX_STRING = "0123456789abcdef";
    private static final char[] HEX_CHARS = HEX_STRING.toCharArray();
}

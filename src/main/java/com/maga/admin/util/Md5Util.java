package com.maga.admin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author zhangsl
 * @date 2018-01-24
 */
public class Md5Util {


    public static final String MD5(final String s) {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        final char[] str;
        final byte[] btInput = s.getBytes();
        try {
            final MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            final byte[] md = mdInst.digest();
            final int j = md.length;
            str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                final byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String digestWidthMD5(final String value) {
        final byte[] btKey = new byte[value.getBytes().length + 1];
        System.arraycopy(value.getBytes(), 0, btKey, 0, value.getBytes().length);
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        md.update(btKey);
        final byte[] btDigest = md.digest();
        return new String(btDigest);
    }
}

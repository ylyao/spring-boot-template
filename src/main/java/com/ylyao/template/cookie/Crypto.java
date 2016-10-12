package com.ylyao.template.cookie;

/**
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public interface Crypto {

    /**
     * 加密
     *
     * @param rawString
     *
     * @return
     */
    public String encrypt(String rawString);

    /**
     * 解密
     *
     * @param encryptedString 被加密的字符串
     *
     * @return
     */
    public String decrypt(String encryptedString);
}

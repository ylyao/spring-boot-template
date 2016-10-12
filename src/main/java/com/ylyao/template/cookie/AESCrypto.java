package com.ylyao.template.cookie;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * AES对称加密解密
 *
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public class AESCrypto implements Crypto {

    private static final Logger logger = LoggerFactory.getLogger(AESCrypto.class);

    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING";

    private static final String ENCODING = "UTF-8";

    private String KEY = "1029384756qbvcy1";

    // MUST 16 bytes
    private String initVector = "JSONQLIOAVOELBY1";


    @Override
    public String encrypt(String rawString) {
        String result = rawString;
        try {
            Cipher cipher = getEncryptCipher();
            result = Base64.encodeBase64String(cipher.doFinal(rawString.getBytes("utf-8")));
        } catch (CryptoException e) {
            logger.error("aes encrypto exception, use raw string {}", e);
        } catch (BadPaddingException e) {
            logger.error("aes encrypto exception, use raw string BadPaddingException {}", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("aes encrypto exception, use raw string IllegalBlockSizeException {}", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("aes encrypto exception, use raw string UnsupportedEncodingException {}", e);
        }
        return result;
    }

    @Override
    public String decrypt(String encryptedString) {
        String result = encryptedString;
        try {
            Cipher cipher = getDecryptCipher();
            result = new String(cipher.doFinal(Base64.decodeBase64(encryptedString)), ENCODING);
        } catch (CryptoException e) {
            logger.error("aes encrypto exception, use raw string {}", e);
        } catch (BadPaddingException e) {
            logger.error("aes encrypto exception, use raw string BadPaddingException {}", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("aes encrypto exception, use raw string IllegalBlockSizeException {}", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("aes encrypto exception, use raw string UnsupportedEncodingException {}", e);
        }
        return result;
    }

    protected Cipher getEncryptCipher() throws CryptoException {
        try {
            SecretKeySpec e = new SecretKeySpec(this.KEY.getBytes(ENCODING), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes(ENCODING));
            cipher.init(Cipher.ENCRYPT_MODE, e, iv);
            return cipher;
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    protected Cipher getDecryptCipher() throws CryptoException {
        try {
            SecretKeySpec e = new SecretKeySpec(this.KEY.getBytes(ENCODING), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, e, iv);
            return cipher;
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    public static void main(String[] args) {
        //        String key = "1029384756qbvcy1"; // 128 bit key
        //        String initVector = "JSONQLIOAVOELBY1"; // 16 bytes IV
        //
        //        System.out.println(decrypt(key, initVector,
        //                encrypt(key, initVector, "Hello中国人 World")));
        Crypto crypto = new AESCrypto();
        String raw = "123456";

        String encrypted = crypto.encrypt(raw);
        String decrypted = crypto.decrypt("3cCw/4roidztPoTupXn8lA==");
        System.out.printf("原始字符串\t：\t%s \n加密结果\t\t：\t%s\n解密结果\t\t：\t%s\n", raw, encrypted, decrypted);
    }
}

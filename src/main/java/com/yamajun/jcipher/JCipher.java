package com.yamajun.jcipher;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.yamajun.jcipher.annotation.CipherMe;
import com.yamajun.jcipher.annotation.Ciphered;
import com.yamajun.jcipher.exception.JCipherException;
import com.yamajun.jcipher.exception.JCipherInvalidKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.Strings;

public class JCipher {
    private static final Logger logger = LogManager.getLogger(JCipher.class);

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String DEFAULT_HASH_ALGORITHM = "SHA-1";
    private static final String DEFAULT_SYMETRIC_ALGORITHM = "AES";

    public JCipher(String secret) throws JCipherInvalidKey {
        if(Strings.isBlank(secret)){
            String errorMsg = "The key can't be empty";
            logger.error(errorMsg);
            throw new JCipherInvalidKey(errorMsg);
        }

        initCipher(secret);
    }

    private void initCipher(String secret) throws JCipherInvalidKey {
        logger.debug("Initializing JCipher");
        byte[] key;
        try {
            key = (secret).getBytes(DEFAULT_ENCODING);

            try {
                MessageDigest sha = MessageDigest.getInstance(DEFAULT_HASH_ALGORITHM);
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16); // use only first 128 bit

                SecretKeySpec secretKeySpec = new SecretKeySpec(key, DEFAULT_SYMETRIC_ALGORITHM);

                encryptCipher = Cipher.getInstance(DEFAULT_SYMETRIC_ALGORITHM);
                encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                decryptCipher = Cipher.getInstance(DEFAULT_SYMETRIC_ALGORITHM);
                decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
                String errorMsg = "Unknown error while initializing JCipher";
                logger.error(errorMsg);
            }

        } catch (UnsupportedEncodingException e) {
            String errorMsg = String.format("Error while parsing the key to <%s>; Are you using a correct key?", e);
            logger.error(errorMsg);
            throw new JCipherInvalidKey(errorMsg);
        }
    }

    public String encryptString(String plainText) throws JCipherException {
        byte[] plainTextByte = plainText.getBytes();
        byte[] encryptedByte;

        try {
            encryptedByte = encryptCipher.doFinal(plainTextByte);
            return Base64.encodeBase64String(encryptedByte);

        } catch (IllegalBlockSizeException | BadPaddingException e) {
            String errorMsg = String.format("Error while ciphering the String: <%s>", e);
            logger.error(errorMsg);
            throw new JCipherException(errorMsg);
        }
    }

    public String decryptString(String encryptedText) throws JCipherException {
        try {
            byte[] encryptedTextByte = Base64.decodeBase64(encryptedText);
            byte[] decryptedByte = decryptCipher.doFinal(encryptedTextByte);
            return new String(decryptedByte);
        } catch(BadPaddingException | IllegalBlockSizeException e) {
            String errorMsg = String.format("Error while deciphering the String: <%s>", e);
            logger.error(errorMsg);
            throw new JCipherException(errorMsg);
        }
    }

    public void encryptObject(Object object) throws Exception{
        executeCipher(object, true);
    }

    public void decryptObject(Object object) throws Exception{
        executeCipher(object, false);
    }

    private void executeCipher(Object object, boolean encryptOrDecrypt) throws JCipherException {
        if(object.getClass().isAnnotationPresent(Ciphered.class)) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(CipherMe.class)) {
                    if (field.getType().isAssignableFrom(String.class)) {
                        try {
                            String fieldValue = (String) field.get(object);
                            if (encryptOrDecrypt) {
                                logger.info(String.format("Ciphering param <%s>", field.getName()));
                                field.set(object, encryptString(fieldValue));
                            } else {
                                logger.info(String.format("Deciphering param <%s>", field.getName()));
                                field.set(object, decryptString(fieldValue));
                            }
                        } catch (IllegalAccessException ignored){
                            logger.info(String.format("The param <%s> is private", field.getName()));
                        }
                    }
                }
            }
        }
    }
}

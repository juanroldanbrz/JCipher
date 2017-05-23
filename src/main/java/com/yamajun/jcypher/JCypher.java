package com.yamajun.jcypher;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class JCypher {
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public JCypher(String secret){
        initCipher(secret);
    }

    private void initCipher(String secret) {
        try {
            byte[] key = (secret).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);

            key = Arrays.copyOf(key, 16); // use only first 128 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String encryptString(String plainText)
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        byte[] encryptedByte = encryptCipher.doFinal(plainTextByte);
        return Base64.encode(encryptedByte);
    }

    public String decryptString(String encryptedText)
            throws Exception {
        byte[] encryptedTextByte = Base64.decode(encryptedText);
        byte[] decryptedByte = decryptCipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }

    public void encryptObject(Object object) throws Exception{
        executeCipher(object, true);
    }

    public void decryptObject(Object object) throws Exception{
        executeCipher(object, false);
    }

    private void executeCipher(Object object, boolean encryptOrDecrypt) throws Exception {
        if(object.getClass().isAnnotationPresent(Ciphered.class)) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(CipherMe.class)) {
                    if (field.getType().isAssignableFrom(String.class)) {
                        String fieldValue = (String) field.get(object);
                        if (encryptOrDecrypt) {
                            field.set(object, encryptString(fieldValue));
                        } else {
                            field.set(object, decryptString(fieldValue));
                        }
                    }
                }
            }
        }
    }
}

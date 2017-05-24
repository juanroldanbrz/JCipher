package com.yamajun.jcipher;

import com.yamajun.jcipher.exception.JCipherInvalidKey;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JCipherTest {

    private JCipher cipher;

    @Before
    public void setUp() throws JCipherInvalidKey {
        cipher = new JCipher("Random_password");
    }

    @Test(expected = NumberFormatException.class)
    public void testCipherNumber_NumberIsEncrypted() throws Exception {
        String numberToCipher = String.valueOf(1234);
        String ciphered = cipher.encryptString(numberToCipher);
        Integer.parseInt(ciphered);
    }

    @Test
    public void testCipherNumber_NumberIsDecrypted() throws Exception {
        String numberToCipher = String.valueOf(1234);
        String ciphered = cipher.encryptString(numberToCipher);
        String plainText = cipher.decryptString(ciphered);
        Integer.parseInt(plainText);
    }

    @Test
    public void testCipherClass_NumberIsDecrypted() throws Exception {
        CipherMeTest objectToCipher = new CipherMeTest();
        cipher.encryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertNotEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());
    }

    @Test
    public void testCipherClass_NumberIsEncrypted() throws Exception {
        CipherMeTest objectToCipher = new CipherMeTest();
        cipher.encryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertNotEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());

        cipher.decryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());
    }

    @Test(expected = JCipherInvalidKey.class)
    public void testEmptyKey() throws JCipherInvalidKey {
        new JCipher("");
    }
}

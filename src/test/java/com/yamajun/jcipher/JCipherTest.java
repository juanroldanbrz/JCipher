package com.yamajun.jcipher;

import com.yamajun.jcipher.exception.JCipherInvalidKey;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JCipherTest {

    private JCipher cypher;

    @Before
    public void setUp() throws JCipherInvalidKey {
        cypher = new JCipher("Random_password");
    }

    @Test(expected = NumberFormatException.class)
    public void testCipherNumber_NumberIsEncrypted() throws Exception {
        String numberToCipher = String.valueOf(1234);
        String ciphered = cypher.encryptString(numberToCipher);
        Integer.parseInt(ciphered);
    }

    @Test
    public void testCipherNumber_NumberIsDecrypted() throws Exception {
        String numberToCipher = String.valueOf(1234);
        String ciphered = cypher.encryptString(numberToCipher);
        String plainText = cypher.decryptString(ciphered);
        Integer.parseInt(plainText);
    }

    @Test
    public void testCipherClass_NumberIsDecrypted() throws Exception {
        CipherMeTest objectToCipher = new CipherMeTest();
        cypher.encryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertNotEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());
    }

    @Test
    public void testCipherClass_NumberIsEncrypted() throws Exception {
        CipherMeTest objectToCipher = new CipherMeTest();
        cypher.encryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertNotEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());

        cypher.decryptObject(objectToCipher);
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToNOTCipher());
        assertEquals(CipherMeTest.INITIAL_NUMBER, objectToCipher.getNumberToCipher());
    }

    @Test(expected = JCipherInvalidKey.class)
    public void testEmptyKey() throws JCipherInvalidKey {
        new JCipher("");
    }
}

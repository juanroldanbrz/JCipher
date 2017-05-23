package com.yamajun.jcypher;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JCypherTest {

    private JCypher cypher;

    @Before
    public void setUp(){
        cypher = new JCypher("Random_password");
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
}

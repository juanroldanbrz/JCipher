package com.yamajun.jcypher;

import com.yamajun.jcypher.annotation.CipherMe;
import com.yamajun.jcypher.annotation.Ciphered;

@Ciphered
public class CipherMeTest {
    public static final String INITIAL_NUMBER = "20";

    @CipherMe
    private String numberToCipher = INITIAL_NUMBER;

    private String numberToNOTCipher = INITIAL_NUMBER;

    public String getNumberToCipher() {
        return numberToCipher;
    }

    public void setNumberToCipher(String numberToCipher) {
        this.numberToCipher = numberToCipher;
    }

    public String getNumberToNOTCipher() {
        return numberToNOTCipher;
    }

    public void setNumberToNOTCipher(String numberToNOTCipher) {
        this.numberToNOTCipher = numberToNOTCipher;
    }
}

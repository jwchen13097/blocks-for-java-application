package org.firefly.provider.cryptography;

import org.apache.commons.codec.DecoderException;
import org.firefly.provider.cryptography.exception.CryptographyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CryptographyDemo {
    public static void main(String[] args) throws IOException, CryptographyException, DecoderException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String operate;
        do {
            System.out.println("Chose an operation: 1) generates a key pair. 2) encrypts files. 3) decrypts files.");
            operate = reader.readLine();
        } while (!"1".equals(operate) && !"2".equals(operate) && !"3".equals(operate));

        if ("1".equals(operate)) {
            CryptographyUtil.generateKeyPair("./keypair/rsa.public", "./keypair/rsa.private");
        } else if ("2".equals(operate)) {
            CryptographyUtil.encryptDirectory("./keypair/rsa.public", "./target");
        } else {
            CryptographyUtil.decryptDirectory("./keypair/rsa.private", "./target");
        }
    }
}

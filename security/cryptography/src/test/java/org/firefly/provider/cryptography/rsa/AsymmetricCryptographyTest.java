package org.firefly.provider.cryptography.rsa;

import org.firefly.provider.cryptography.exception.CryptographyException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public class AsymmetricCryptographyTest {
    private static AsymmetricCryptography asymmetricCryptography;
    private static byte[] publicKey;
    private static byte[] privateKey;

    @Before
    public void setUp() throws Exception {
        asymmetricCryptography = new RsaAsymmetricCryptography();
        KeyPair keyPair = asymmetricCryptography.generateKeyPair();
        publicKey = keyPair.getPublicKey();
        privateKey = keyPair.getPrivateKey();
    }

    @After
    public void tearDown() {
        privateKey = null;
        publicKey = null;
        asymmetricCryptography = null;
    }

    @Test
    public void testEncryptWithPublicKey() throws CryptographyException {
        byte[] plainBytes = "123!&kjWdfYYL?".getBytes(StandardCharsets.UTF_8);

        byte[] encryptedBytes = asymmetricCryptography.encryptWithPublicKey(plainBytes, publicKey);
        byte[] decryptedBytes = asymmetricCryptography.decryptWithPrivateKey(encryptedBytes, privateKey);

        assertArrayEquals(plainBytes, decryptedBytes);
    }

    @Test
    public void testEncryptWithPrivateKey() throws CryptographyException {
        byte[] plainBytes = "3!&jWdfYYL?".getBytes(StandardCharsets.UTF_8);

        byte[] encryptedBytes = asymmetricCryptography.encryptWithPrivateKey(plainBytes, privateKey);
        byte[] decryptedBytes = asymmetricCryptography.decryptWithPublicKey(encryptedBytes, publicKey);

        assertArrayEquals(plainBytes, decryptedBytes);
    }

    @Test
    public void testSign() throws CryptographyException {
        byte[] message = "fj123!&kWfY".getBytes(StandardCharsets.UTF_8);

        byte[] signature = asymmetricCryptography.sign(message, privateKey);
        boolean verify = asymmetricCryptography.verify(message, signature, publicKey);

        Assert.assertTrue(verify);
    }
}

package org.firefly.provider.cryptography.des;

import org.firefly.provider.cryptography.exception.CryptographyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public class SymmetricCryptographyTest {
    private static SymmetricCryptography symmetricCryptography;
    private static byte[] key;

    @Before
    public void setUp() throws Exception {
        symmetricCryptography = new DesSymmetricCryptography();
        key = symmetricCryptography.generateKey();
    }

    @After
    public void tearDown() {
        key = null;
        symmetricCryptography = null;
    }

    @Test
    public void shouldReturnTheSameOriginalTextAfterEncryptAndDecrypt() throws CryptographyException {
        byte[] plainBytes = "123!&kjWdfYYL?".getBytes(StandardCharsets.UTF_8);

        byte[] encryptedBytes = symmetricCryptography.encrypt(plainBytes, key);
        byte[] decryptedBytes = symmetricCryptography.decrypt(encryptedBytes, key);

        assertArrayEquals(plainBytes, decryptedBytes);
    }
}

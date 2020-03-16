package org.firefly.provider.cryptography.des;

import org.firefly.provider.cryptography.exception.CryptographyException;

public interface SymmetricCryptography {
    byte[] generateKey() throws CryptographyException;

    byte[] encrypt(byte[] plainBytes, byte[] key) throws CryptographyException;

    byte[] decrypt(byte[] cipherBytes, byte[] key) throws CryptographyException;
}

package org.firefly.provider.cryptography.rsa;

import org.firefly.provider.cryptography.exception.CryptographyException;

public interface AsymmetricCryptography {
    KeyPair generateKeyPair() throws CryptographyException;

    byte[] encryptWithPublicKey(byte[] plainBytes, byte[] publicKey) throws CryptographyException;

    byte[] encryptWithPrivateKey(byte[] plainBytes, byte[] privateKey) throws CryptographyException;

    byte[] decryptWithPublicKey(byte[] cipherBytes, byte[] publicKey) throws CryptographyException;

    byte[] decryptWithPrivateKey(byte[] cipherBytes, byte[] privateKey) throws CryptographyException;

    byte[] sign(byte[] message, byte[] privateKey) throws CryptographyException;

    boolean verify(byte[] message, byte[] signature, byte[] publicKey) throws CryptographyException;
}

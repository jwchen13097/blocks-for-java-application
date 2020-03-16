package org.firefly.provider.cryptography.rsa;

import org.apache.commons.codec.binary.Hex;
import org.firefly.provider.cryptography.exception.CryptographyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaAsymmetricCryptography implements AsymmetricCryptography {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_ALGORITHM_NOT_AVAILABLE = String.format(
            "The %s cryptographic algorithm is not available in the environment", RSA_ALGORITHM);
    private static final int RSA_KEY_LENGTH = 1024;

    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String SHA256_ALGORITHM_NOT_AVAILABLE = String.format(
            "The %s cryptographic algorithm is not available in the environment", SHA256_ALGORITHM);

    private static final String SHA256_WITH_RSA_ALGORITHM = "SHA256withRSA";
    private static final String SHA256_WITH_RSA_ALGORITHM_NOT_AVAILABLE = String.format(
            "The %s cryptographic algorithm is not available in the environment", SHA256_WITH_RSA_ALGORITHM);

    @Override
    public KeyPair generateKeyPair() throws CryptographyException {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(RSA_ALGORITHM_NOT_AVAILABLE, e);
        }

        keyPairGenerator.initialize(RSA_KEY_LENGTH);
        java.security.KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new KeyPair(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
    }

    @Override
    public byte[] encryptWithPublicKey(byte[] plainBytes, byte[] publicKey) throws CryptographyException {
        Key key = getKey(publicKey, false);
        Cipher cipher = getCipher();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to encrypt plain bytes %s", Hex.encodeHexString(plainBytes)), e);
        }
    }

    private Key getKey(byte[] bytesKey, boolean isPrivateKey) throws CryptographyException {
        KeyFactory keyFactory = getKeyFactory();

        try {
            if (isPrivateKey) {
                return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytesKey));
            } else {
                return keyFactory.generatePublic(new X509EncodedKeySpec(bytesKey));
            }
        } catch (InvalidKeySpecException e) {
            throw new CryptographyException(String.format(
                    "Fails to get key from %s", Hex.encodeHexString(bytesKey)), e);
        }
    }

    private KeyFactory getKeyFactory() throws CryptographyException {
        try {
            return KeyFactory.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(RSA_ALGORITHM_NOT_AVAILABLE, e);
        }
    }

    private Cipher getCipher() throws CryptographyException {
        try {
            return Cipher.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(RSA_ALGORITHM_NOT_AVAILABLE, e);
        } catch (NoSuchPaddingException e) {
            throw new CryptographyException(String.format("Getting cipher of %s goes wrong", RSA_ALGORITHM), e);
        }
    }

    @Override
    public byte[] encryptWithPrivateKey(byte[] plainBytes, byte[] privateKey) throws CryptographyException {
        Key key = getKey(privateKey, true);
        Cipher cipher = getCipher();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to encrypt plain bytes %s", Hex.encodeHexString(plainBytes)), e);
        }
    }

    @Override
    public byte[] decryptWithPublicKey(byte[] cipherBytes, byte[] publicKey) throws CryptographyException {
        Key key = getKey(publicKey, false);
        Cipher cipher = getCipher();

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to decrypt cipher bytes %s", Hex.encodeHexString(cipherBytes)), e);
        }
    }

    @Override
    public byte[] decryptWithPrivateKey(byte[] cipherBytes, byte[] privateKey) throws CryptographyException {
        Key key = getKey(privateKey, true);
        Cipher cipher = getCipher();

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to decrypt cipher bytes %s", Hex.encodeHexString(cipherBytes)), e);
        }
    }

    @Override
    public byte[] sign(byte[] message, byte[] privateKey) throws CryptographyException {
        byte[] messageDigest = getMessageDigest(message);
        Signature signatureProvider = getSignature();
        PrivateKey key = (PrivateKey) getKey(privateKey, true);

        try {
            signatureProvider.initSign(key);
        } catch (InvalidKeyException e) {
            throw new CryptographyException(String.format(
                    "Fails to initialize signing with key %s", Hex.encodeHexString(privateKey)), e);
        }

        try {
            signatureProvider.update(messageDigest);
            return signatureProvider.sign();
        } catch (SignatureException e) {
            throw new CryptographyException(String.format(
                    "Fails to sign message %s", Hex.encodeHexString(message)), e);
        }
    }

    private byte[] getMessageDigest(byte[] message) throws CryptographyException {
        MessageDigest messageDigestProvider = getMessageDigest();
        messageDigestProvider.update(message);
        return messageDigestProvider.digest();
    }

    private MessageDigest getMessageDigest() throws CryptographyException {
        try {
            return MessageDigest.getInstance(SHA256_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(SHA256_ALGORITHM_NOT_AVAILABLE, e);
        }
    }

    private Signature getSignature() throws CryptographyException {
        try {
            return Signature.getInstance(SHA256_WITH_RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(SHA256_WITH_RSA_ALGORITHM_NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean verify(byte[] message, byte[] signature, byte[] publicKey) throws CryptographyException {
        byte[] messageDigest = getMessageDigest(message);
        Signature signatureProvider = getSignature();
        PublicKey key = (PublicKey) getKey(publicKey, false);

        try {
            signatureProvider.initVerify(key);
        } catch (InvalidKeyException e) {
            throw new CryptographyException(String.format(
                    "Fails to initialize verification with key %s", Hex.encodeHexString(publicKey)), e);
        }

        try {
            signatureProvider.update(messageDigest);
            return signatureProvider.verify(signature);
        } catch (SignatureException e) {
            throw new CryptographyException(String.format("Fails to verify signature %s " +
                    "against message %s", Hex.encodeHexString(signature), Hex.encodeHexString(message)), e);
        }
    }
}

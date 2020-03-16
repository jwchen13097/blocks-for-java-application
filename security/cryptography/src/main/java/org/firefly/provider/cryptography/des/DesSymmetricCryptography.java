package org.firefly.provider.cryptography.des;

import org.apache.commons.codec.binary.Hex;
import org.firefly.provider.cryptography.exception.CryptographyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DesSymmetricCryptography implements SymmetricCryptography {
    private static final String DES_ALGORITHM = "DES";
    private static final String DES_ALGORITHM_NOT_AVAILABLE = String.format(
            "The %s cryptographic algorithm is not available in the environment", DES_ALGORITHM);

    @Override
    public byte[] generateKey() throws CryptographyException {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(DES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(DES_ALGORITHM_NOT_AVAILABLE, e);
        }

        keyGenerator.init(56);
        SecretKey generateKey = keyGenerator.generateKey();
        return generateKey.getEncoded();
    }

    @Override
    public byte[] encrypt(byte[] plainBytes, byte[] key) throws CryptographyException {
        byte[] filledPlainBytes = fill(plainBytes);
        SecretKey secretKey = getSecretKey(key);
        Cipher cipher = getCipher();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(filledPlainBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to encrypt plain bytes %s", Hex.encodeHexString(plainBytes)), e);
        }
    }

    /**
     * Fill data with some bytes from 1 to 8, letting it be of 8 bytes
     * times and the last byte is number of filling bytes. The filled
     * data is parsed by {@link #parse}.
     *
     * @param data original data
     * @return filled data
     */
    private byte[] fill(byte[] data) {
        int dataLength = data.length;
        if (dataLength > Integer.MAX_VALUE - 8) {
            throw new IllegalArgumentException("Data to be filled is too big");
        }

        int fillingByteNumber = 8 - dataLength % 8;
        byte[] fillingBytes = new byte[fillingByteNumber];
        fillingBytes[fillingByteNumber - 1] = (byte) fillingByteNumber;

        byte[] filledData = new byte[dataLength + fillingByteNumber];
        System.arraycopy(data, 0, filledData, 0, dataLength);
        System.arraycopy(fillingBytes, 0, filledData, dataLength, fillingByteNumber);
        return filledData;
    }

    private SecretKey getSecretKey(byte[] key) throws CryptographyException {
        SecretKeyFactory secretKeyFactory = getSecretKeyFactory();

        try {
            DESKeySpec desKeySpec = new DESKeySpec(key);
            return secretKeyFactory.generateSecret(desKeySpec);
        } catch (InvalidKeyException | InvalidKeySpecException e) {
            throw new CryptographyException(String.format(
                    "Fails to get secret key from %s", Hex.encodeHexString(key)), e);
        }
    }

    private SecretKeyFactory getSecretKeyFactory() throws CryptographyException {
        try {
            return SecretKeyFactory.getInstance(DES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(DES_ALGORITHM_NOT_AVAILABLE, e);
        }
    }

    private Cipher getCipher() throws CryptographyException {
        try {
            return Cipher.getInstance(DES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(DES_ALGORITHM_NOT_AVAILABLE, e);
        } catch (NoSuchPaddingException e) {
            throw new CryptographyException(String.format("Getting cipher of %s goes wrong", DES_ALGORITHM), e);
        }
    }

    @Override
    public byte[] decrypt(byte[] cipherBytes, byte[] key) throws CryptographyException {
        SecretKey secretKey = getSecretKey(key);
        Cipher cipher = getCipher();
        byte[] decryptedBytes;

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedBytes = cipher.doFinal(cipherBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptographyException(String.format(
                    "Fails to decrypt cipher bytes %s", Hex.encodeHexString(cipherBytes)), e);
        }

        return parse(decryptedBytes);
    }

    /**
     * Parse filled data. The original data is from {@link #fill}.
     *
     * @param data filled data
     * @return original data
     */
    private byte[] parse(byte[] data) {
        int dataLength = data.length;

        int fillingByteNumber = data[dataLength - 1];
        byte[] originalData = new byte[dataLength - fillingByteNumber];
        System.arraycopy(data, 0, originalData, 0, originalData.length);
        return originalData;
    }
}

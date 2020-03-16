package org.firefly.provider.cryptography;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.firefly.provider.cryptography.des.DesSymmetricCryptography;
import org.firefly.provider.cryptography.des.SymmetricCryptography;
import org.firefly.provider.cryptography.exception.CryptographyException;
import org.firefly.provider.cryptography.rsa.AsymmetricCryptography;
import org.firefly.provider.cryptography.rsa.KeyPair;
import org.firefly.provider.cryptography.rsa.RsaAsymmetricCryptography;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CryptographyUtil {
    private static final AsymmetricCryptography ASYMMETRIC_CRYPTOGRAPHY = new RsaAsymmetricCryptography();
    private static final SymmetricCryptography SYMMETRIC_CRYPTOGRAPHY = new DesSymmetricCryptography();

    private static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    private static final DateTimeFormatter DATETIME_PATTERN = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    private static final int DATETIME_FORMAT_LENGTH = DATETIME_FORMAT.length();

    private CryptographyUtil() {
    }

    public static void generateKeyPair(
            String publicKeyFilePath, String privateKeyFilePath) throws IOException, CryptographyException {
        KeyPair keyPair = ASYMMETRIC_CRYPTOGRAPHY.generateKeyPair();
        String publicKey = Hex.encodeHexString(keyPair.getPublicKey());
        String privateKey = Hex.encodeHexString(keyPair.getPrivateKey());

        writeKey(publicKeyFilePath, publicKey);
        writeKey(privateKeyFilePath, privateKey);
    }

    private static void writeKey(String filePath, String key) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(new File(filePath))));

        writer.write(key);
        writer.flush();
        writer.close();
    }

    public static void encryptDirectory(
            String publicKeyFile, String targetDirectory) throws IOException, CryptographyException, DecoderException {
        byte[] publicKey = readKey(publicKeyFile);

        byte[] key = SYMMETRIC_CRYPTOGRAPHY.generateKey();
        byte[] cipherKey = ASYMMETRIC_CRYPTOGRAPHY.encryptWithPublicKey(key, publicKey);

        encryptDirectory(key, cipherKey, new File(targetDirectory));
    }

    private static byte[] readKey(String keyFile) throws IOException, DecoderException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(keyFile))));
        String hexKey = reader.readLine();
        return Hex.decodeHex(hexKey);
    }

    private static void encryptDirectory(
            byte[] key, byte[] cipherKey, File targetDirectory) throws IOException, CryptographyException {
        File[] targetFiles = targetDirectory.listFiles();
        if (targetFiles == null) {
            return;
        }

        for (File targetFile : targetFiles) {
            if (targetFile.isFile()) {
                encryptFile(key, cipherKey, targetFile);
            } else {
                encryptDirectory(key, cipherKey, targetFile);
            }
        }
    }

    private static void encryptFile(
            byte[] key, byte[] cipherKey, File targetFile) throws IOException, CryptographyException {
        InputStream inputStream = new FileInputStream(targetFile);
        OutputStream outputStream = new FileOutputStream(
                new File(targetFile.getAbsolutePath() + "." + LocalDateTime.now().format(DATETIME_PATTERN)));

        outputStream.write(toBytes(cipherKey.length));
        outputStream.write(cipherKey);

        byte[] segment = new byte[2048];
        int segmentLength;
        do {
            segmentLength = inputStream.read(segment);
            if (segmentLength != -1) {
                byte[] temp = new byte[segmentLength];
                System.arraycopy(segment, 0, temp, 0, segmentLength);
                byte[] encryptedSegment = SYMMETRIC_CRYPTOGRAPHY.encrypt(temp, key);
                outputStream.write(toBytes(encryptedSegment.length));
                outputStream.write(encryptedSegment);
            }
        } while (segmentLength != -1);

        outputStream.close();
        inputStream.close();
        targetFile.delete();
    }

    private static byte[] toBytes(int length) {
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte) ((length >> 8 >> 8 >> 8) & 0xff);
        lengthBytes[1] = (byte) ((length >> 8 >> 8) & 0xff);
        lengthBytes[2] = (byte) ((length >> 8) & 0xff);
        lengthBytes[3] = (byte) ((length) & 0xff);
        return lengthBytes;
    }

    public static void decryptDirectory(
            String privateKeyFile, String targetDirectory) throws IOException, DecoderException, CryptographyException {
        byte[] privateKey = readKey(privateKeyFile);

        decryptDirectory(privateKey, new File(targetDirectory));
    }

    private static void decryptDirectory(
            byte[] privateKey, File targetDirectory) throws IOException, CryptographyException {
        File[] targetFiles = targetDirectory.listFiles();
        if (targetFiles == null) {
            return;
        }

        for (File targetFile : targetFiles) {
            if (targetFile.isFile()) {
                decryptFile(privateKey, targetFile);
            } else {
                decryptDirectory(privateKey, targetFile);
            }
        }
    }

    private static void decryptFile(
            byte[] privateKey, File targetFile) throws IOException, CryptographyException {
        InputStream inputStream = new FileInputStream(targetFile);
        OutputStream outputStream = new FileOutputStream(
                targetFile.getPath().substring(0, targetFile.getPath().length() - DATETIME_FORMAT_LENGTH));

        int cipherKeyLength = readLength(inputStream);
        byte[] cipherKey = new byte[cipherKeyLength];
        if (inputStream.read(cipherKey) != cipherKeyLength) {
            throw new IOException("Reading des key fails");
        }

        byte[] key = ASYMMETRIC_CRYPTOGRAPHY.decryptWithPrivateKey(cipherKey, privateKey);
        int segmentLength;
        do {
            segmentLength = readLength(inputStream);
            if (segmentLength != -1) {
                byte[] segment = new byte[segmentLength];
                if (inputStream.read(segment) != segmentLength) {
                    throw new IOException("Reading data fails");
                }
                outputStream.write(SYMMETRIC_CRYPTOGRAPHY.decrypt(segment, key));
            }
        } while (segmentLength != -1);

        outputStream.close();
        inputStream.close();
        targetFile.delete();
    }

    private static int readLength(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[4];
        if (inputStream.read(bytes) == 4) {
            return new BigInteger(1, bytes).intValue();
        }
        return -1;
    }
}

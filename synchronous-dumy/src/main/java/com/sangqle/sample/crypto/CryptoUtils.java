package com.sangqle.sample.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class CryptoUtils {

    private static final String PRIVATE_KEY_FILE = "/Users/sanglq3/ZTE/MPTeam/id_rsa";
    private static final String PUBLIC_KEY_FILE = "/Users/sanglq3/ZTE/MPTeam/id_rsa.pub";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public CryptoUtils() throws Exception {
        byte[] publicKeyBytes = readKeyFile(PUBLIC_KEY_FILE);

        privateKey = getPrivateKey(PRIVATE_KEY_FILE, "RSA");
        publicKey = getPublicKey(publicKeyBytes);
    }

    private byte[] readKeyFile(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PrivateKey getPrivateKey(String filename, String algorithm) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder builder = new StringBuilder();
        boolean inKey = false;
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (!inKey) {
                if (line.startsWith("-----BEGIN ") &&
                    line.endsWith(" PRIVATE KEY-----")) {
                    inKey = true;
                }
                continue;
            } else {
                if (line.startsWith("-----END ") &&
                    line.endsWith(" PRIVATE KEY-----")) {
                    inKey = false;
                    break;
                }
                builder.append(line.trim());
            }
        }
        byte[] encodedKey = Base64.getDecoder().decode(builder.toString());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm, "BC");
        return keyFactory.generatePrivate(keySpec);
    }


    private PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    public void generateKeyFiles() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        Files.write(Paths.get(PRIVATE_KEY_FILE), privateKey.getEncoded());
        Files.write(Paths.get(PUBLIC_KEY_FILE), publicKey.getEncoded());
    }

    public byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return encryptedData;
    }

    public String decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedData = cipher.doFinal(data);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        CryptoUtils cryptoUtils = new CryptoUtils();
        byte[] helloWorlds = cryptoUtils.encrypt("Hello world");
        // assuming the `signature` variable is a byte array
        String base64String = Base64.getEncoder().encodeToString(helloWorlds);
        System.out.println(base64String);
    }
}

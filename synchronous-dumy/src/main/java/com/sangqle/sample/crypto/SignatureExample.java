package com.sangqle.sample.crypto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.util.*;

public class SignatureExample {

    private static void writeKeysToFile(PrivateKey privateKey, PublicKey publicKey, String privateKeyFile, String publicKeyFile) throws IOException {
        // Write the private key to file
        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write(privateKey.getEncoded());
        }

        // Write the public key to file
        try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
            fos.write(publicKey.getEncoded());
        }
    }

    private static void writeKeysToFile() throws NoSuchAlgorithmException {
        // Generate a new key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Convert the private key to Base64 encoded string
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Write the private key to file
        try {
            Files.write(Paths.get("private_key.txt"), privateKeyString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Convert the public key to Base64 encoded string
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // Write the public key to file
        try {
            Files.write(Paths.get("public_key.txt"), publicKeyString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Keys written to files");
    }

    private static PrivateKey readPrivateKeyFromFile(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyBytes));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private static PublicKey readPublicKeyFromFile(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyBytes));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    private static boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(data);
        return verifier.verify(signature);
    }
    public static void main(String[] args) throws Exception {
        // Generate a public-private key pair
//        writeKeysToFile();
        PrivateKey privateKey = readPrivateKeyFromFile("private_key.txt");
        PublicKey publicKey = readPublicKeyFromFile("public_key.txt");

        String message = "Hello, world!";
        byte[] signature = sign(message.getBytes(StandardCharsets.UTF_8), privateKey);

        // Verify the signature using the public key
        boolean verified = verify(message.getBytes(StandardCharsets.UTF_8), signature, publicKey);
        System.err.println("Signature verified: " + verified);


    }
}

package com.sangqle.sample.crypto;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.stream.Collectors;

public class SignatureExample {

    @Deprecated
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

    private static void writeKeysToFile(String publicKeyFilePath, String privateKeyFilePath) throws NoSuchAlgorithmException, IOException {
        // Generate a new key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Convert the private key to PEM format
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] pkcs8Encoded = privateKey.getEncoded();
        PemObject pemObject = new PemObject("PRIVATE KEY", pkcs8Encoded);
        StringWriter privateKeyWriter = new StringWriter();
        JcaPEMWriter pemPrivateKeyWriter = new JcaPEMWriter(privateKeyWriter);
        pemPrivateKeyWriter.writeObject(pemObject);
        pemPrivateKeyWriter.close();

        // Write the private key to file
        File privateKeyFile = new File(privateKeyFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(privateKeyFile))) {
            writer.write(privateKeyWriter.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Convert the public key to PEM format
        PublicKey publicKey = keyPair.getPublic();
        StringWriter publicKeyWriter = new StringWriter();
        JcaPEMWriter pemPublicKeyWriter = new JcaPEMWriter(publicKeyWriter);
        pemPublicKeyWriter.writeObject(publicKey);
        pemPublicKeyWriter.close();

        // Write the public key to file
        File publicKeyFile = new File(publicKeyFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(publicKeyFile))) {
            writer.write(publicKeyWriter.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static PublicKey readPublicKeyFromFile(String filename) throws Exception {
        String publicKeyPEM = "";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("-----")) {
                publicKeyPEM += line.trim();
            }
        }
        reader.close();

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private static PrivateKey readPrivateKeyFromFile(String filename) throws Exception {
        String privateKeyPEM = "";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("-----")) {
                privateKeyPEM += line.trim();
            }
        }
        reader.close();
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
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

    public static boolean verifyAndPrint(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data);
        boolean verified = sig.verify(signature);
        if (verified) {
            String message = new String(data, StandardCharsets.UTF_8);
            System.err.println("Signature verified, message: " + message);
        } else {
            System.err.println("Signature not verified");
        }
        return verified;
    }

    public static String buildHash(Map<String, Object> data) throws NoSuchAlgorithmException {
        // Sort the keys in the map
        String[] keys = data.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // Concatenate the values in sorted order
        String content = Arrays.stream(keys)
            .map(k -> {
                Object value = data.get(k);
                if (value instanceof Map) {
                    try {
                        return buildHash((Map<String, Object>) value);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return String.valueOf(value);
                }
            })
            .collect(Collectors.joining());

        // Calculate the SHA-256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

        // Convert the hash to a hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {
        writeKeysToFile("public_key.pem", "private_key.pem");
        // Generate a public-private key pair
        PublicKey publicKey = readPublicKeyFromFile("public_key.pem");
        PrivateKey privateKey = readPrivateKeyFromFile("private_key.pem");

        String message = buildHash(new HashMap<String, Object>() {{
            put("a", 1);
            put("b", 2);
            put("c", 3);
        }});

        System.err.println("Message " + message);
        byte[] signature = sign(message.getBytes(StandardCharsets.UTF_8), privateKey);

        String signatureString = Base64.getEncoder().encodeToString(signature);
        System.out.println("Signature: " + signatureString);

        // Verify the signature using the public key
        boolean verified = verifyAndPrint(message.getBytes(StandardCharsets.UTF_8), signature, publicKey);
        System.err.println("Signature verified: " + verified);
    }
}

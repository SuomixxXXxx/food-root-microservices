package org.chiches.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Configuration
public class RSAKeyConfig {

    private static final String PRIVATE_KEY_FILE = "private_key.pem";
    private static final String PUBLIC_KEY_FILE = "public_key.pem";

    @Bean
    public KeyPair keyPair() throws Exception {
        if (Files.exists(Path.of(PRIVATE_KEY_FILE)) && Files.exists(Path.of(PUBLIC_KEY_FILE))) {
            return loadKeyPair();
        } else {
            KeyPair keyPair = generateKeyPair();
            saveKeyPair(keyPair);
            return keyPair;
        }
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    private void saveKeyPair(KeyPair keyPair) throws IOException {
        try (FileWriter writer = new FileWriter(PRIVATE_KEY_FILE)) {
            byte[] privKeyBytes = keyPair.getPrivate().getEncoded();
            writer.write("-----BEGIN PRIVATE KEY-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privKeyBytes));
            writer.write("\n-----END PRIVATE KEY-----\n");
        }

        try (FileWriter writer = new FileWriter(PUBLIC_KEY_FILE)) {
            byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(pubKeyBytes));
            writer.write("\n-----END PUBLIC KEY-----\n");
        }
    }

    private KeyPair loadKeyPair() throws Exception {
        String privateKeyPem = Files.readString(Path.of(PRIVATE_KEY_FILE))
                .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] privBytes = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(keySpec);

        String publicKeyPem = Files.readString(Path.of(PUBLIC_KEY_FILE))
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] pubBytes = Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubBytes);
        PublicKey publicKey = kf.generatePublic(pubKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}

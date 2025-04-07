package com.pi.utils.security;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(KeyGenerator.class);

    private static final Integer KEY_SIZE = 2048;
    private static final String FILE_PUBLIC = "/public.pub";
    private static final String FILE_PRIVATE = "/private.key";
    private static final String PATH = "core/src/main/resources";

    public static void main(String[] args) {
        if (!keysExist()) {
            var pair = generateKeyPairRSA();
            createFolderResourcesIfNotExist();
            generateFileWithPublicKey(pair.getPublic());
            generateFileWithPrivateKey(pair.getPrivate());
            LOG.debug("Generated RSA keys in " + PATH);
        } else {
            LOG.info("Keys already exist, no need to generate new ones.");
        }
    }

    private static KeyPair generateKeyPairRSA() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(KEY_SIZE);
            return keyPairGen.generateKeyPair();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            var alert = new CustomAlert(SystemCodeEnum.C002PI);
            throw GlobalException.builder().alert(alert).status(500).build();
        }
    }

    private static void createFolderResourcesIfNotExist() {
        File dir = new File(PATH);
        if (!dir.exists()) dir.mkdirs();
    }

    private static void generateFileWithPublicKey(PublicKey key) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
            String publicKeyPEM = convertToPEMFormat("PUBLIC KEY", Base64.getEncoder().encodeToString(x509EncodedKeySpec.getEncoded()));

            try (FileOutputStream fos = new FileOutputStream(PATH + FILE_PUBLIC)) {
                fos.write(publicKeyPEM.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            var alert = new CustomAlert(SystemCodeEnum.C002PI);
            throw GlobalException.builder().alert(alert).status(500).build();
        }
    }

    private static void generateFileWithPrivateKey(PrivateKey key) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key.getEncoded());
            String privateKeyPEM = convertToPEMFormat("PRIVATE KEY", Base64.getEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded()));

            try (FileOutputStream fos = new FileOutputStream(PATH + FILE_PRIVATE)) {
                fos.write(privateKeyPEM.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            var alert = new CustomAlert(SystemCodeEnum.C002PI);
            throw GlobalException.builder().alert(alert).status(500).build();
        }
    }

    private static String convertToPEMFormat(String keyType, String base64EncodedKey) {
        return "-----BEGIN " + keyType + "-----\n" +
                base64EncodedKey.replaceAll("(.{64})", "$1\n") + // Divide a cada 64 caracteres
                "\n-----END " + keyType + "-----\n";
    }

    // Verifica se as chaves já existem
    private static boolean keysExist() {
        File publicKeyFile = new File(PATH + FILE_PUBLIC);
        File privateKeyFile = new File(PATH + FILE_PRIVATE);
        return publicKeyFile.exists() && privateKeyFile.exists();
    }
}

package com.ibm.service.config;

import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.model.Secret;
import com.ibm.service.cloud.SecretsManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Configuration
public class SslConfig {

  private static final Logger LOG = LoggerFactory.getLogger(SslConfig.class);

  @Value("${server.ssl.trust-store-password}")
  private String trustStorePassword;

  @Value("${server.ssl.key-store-password}")
  private String keyStorePassword;

  private final Properties.IBMCloud ibmCloud;

  private final SecretsManagerService secretManagerService;

  public SslConfig(Properties properties, SecretsManagerService secretManagerService) {
    this.ibmCloud = properties.getIbmCloud();
    this.secretManagerService = secretManagerService;
  }

  @Bean("trustore")
  public void setUpTrustStoreForApplication() {
    Secret trustStoreSecret = secretManagerService.getSecret(ibmCloud.getTrustStoreSecretId());

    loadKeyStore(trustStoreSecret, "trustStore", "IBMTrustStore", trustStorePassword);
  }

  @Bean("keystore")
  public void setUpKeyStoreForApplication() {
    Secret keyStoreSecret = secretManagerService.getSecret(ibmCloud.getKeyStoreSecretId());

    loadKeyStore(keyStoreSecret, "keyStore", "IBMKeyStore", keyStorePassword);
  }

  private void loadKeyStore(Secret secret, String kind, String fileName, String password) {
    byte[] bytes = Base64.getDecoder().decode(secret.getPayload());

    try {
      // Create a path to the file within the keystore directory
      Path path = Paths.get("/tmp/keystore").resolve(fileName + ".jks");

      // Create the keystore directory if it does not exist
      if (!Files.exists(path.getParent())) {
        Files.createDirectories(path.getParent());
      }

      Files.write(path, bytes);
      LOG.debug("File written to: {}", path);

      System.setProperty("javax.net.ssl." + kind, path.toString());
      System.setProperty("javax.net.ssl." + kind + "Password", password);

      LOG.info("KeyStore {} was loaded successfully", secret.getName());
    } catch (IOException ex) {
      LOG.error("loadKeyStore: ", ex);
    }
  }
}

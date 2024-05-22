package com.ibm.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerSslBundle;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Overwritting autoconfiguration from application.yaml to be able to pull secrets from Secrets
 * Manager and start web server after Keystores are set.
 */
@Configuration
public class ServerConfig {
  @Value("${server.port}")
  private Integer serverPort;

  @Value("${server.ssl.enabled}")
  private boolean sslEnabled;

  @Value("${server.ssl.key-store}")
  private String keyStore;

  @Value("${server.ssl.key-store-password}")
  private String keyStorePassword;

  @Value("${server.ssl.key-alias}")
  private String keyAlias;

  @Value("${server.ssl.key-store-type}")
  private String keyStoreType;

  @Value("${server.ssl.key-store-provider}")
  private String keyStoreProvider;

  @Value("${server.ssl.trust-store")
  private String trustStore;

  @Value("${server.ssl.trust-store-password}")
  private String trustStorePassword;

  @Value("${server.ssl.trust-store-type}")
  private String trustStoreType;

  @Value("${server.ssl.trust-store-provider}")
  private String trustStoreProvider;

  @Bean
  @DependsOn({"keystore", "trustore"})
  public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers(
        connector -> {
          connector.setSecure(sslEnabled);
          connector.setScheme(sslEnabled ? "https" : "http");
          connector.setProperty("clientAuth", "false");
          connector.setProperty("sslProtocol", "TLS");
          connector.setProperty("SSLEnabled", String.valueOf(sslEnabled));
          connector.setPort(serverPort);
        });

    Ssl sslConfig = new Ssl();
    sslConfig.setKeyStorePassword(keyStorePassword);
    sslConfig.setKeyAlias(keyAlias);
    sslConfig.setKeyStore(keyStore);
    sslConfig.setKeyStoreProvider(keyStoreProvider);
    sslConfig.setKeyStoreType(keyStoreType);
    sslConfig.setTrustStore(trustStore);
    sslConfig.setTrustStorePassword(trustStorePassword);
    sslConfig.setTrustStoreType(trustStoreType);
    sslConfig.setTrustStoreProvider(trustStoreProvider);

    SslBundle sslBundle = WebServerSslBundle.get(sslConfig);
    SslBundles sslBundles = new DefaultSslBundleRegistry(keyAlias, sslBundle);

    factory.setSslBundles(sslBundles);

    return factory;
  }
}

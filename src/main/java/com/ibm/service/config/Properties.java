package com.ibm.service.config;

import java.util.Arrays;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class Properties {
  private final WebSecurity webSecurity;

  private final IBMCloud ibmCloud;

  public Properties() {
    this.webSecurity = new WebSecurity();
	this.ibmCloud = new IBMCloud();
  }

  @Validated
  public static class WebSecurity {
    private String corsAllowedOrigins;

    public List<String> getCorsAllowedOrigins() {
      return Arrays.asList(corsAllowedOrigins.split(","));
    }

    public void setCorsAllowedOrigins(String corsAllowedOrigins) {
      this.corsAllowedOrigins = corsAllowedOrigins;
    }
  }

  @Validated
  public static class IBMCloud {
	  @NotEmpty
	  private String apiKey;

	  @NotEmpty
	  private String secretsManagerServiceUrl;

	  @NotEmpty
	  private String keyStoreSecretId;

	  @NotEmpty
	  private String trustStoreSecretId;

	  public String getApiKey() {
		  return apiKey;
	  }

	  public void setApiKey(String apiKey) {
		  this.apiKey = apiKey;
	  }

	  public String getSecretsManagerServiceUrl() {
		  return secretsManagerServiceUrl;
	  }

	  public void setSecretsManagerServiceUrl(String secretsManagerServiceUrl) {
		  this.secretsManagerServiceUrl = secretsManagerServiceUrl;
	  }

	  public String getKeyStoreSecretId() {
		  return keyStoreSecretId;
	  }

	  public void setKeyStoreSecretId(String keyStoreSecretId) {
		  this.keyStoreSecretId = keyStoreSecretId;
	  }

	  public String getTrustStoreSecretId() {
		  return trustStoreSecretId;
	  }

	  public void setTrustStoreSecretId(String trustStoreSecretId) {
		  this.trustStoreSecretId = trustStoreSecretId;
	  }
  }

  public WebSecurity getWebSecurity() {
    return webSecurity;
  }

	public IBMCloud getIbmCloud() {
		return ibmCloud;
	}
}

package com.ibm.service.config;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.SecretsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretsManagerConfig {

	private final Properties.IBMCloud ibmCloud;

	public SecretsManagerConfig(Properties properties) {
		this.ibmCloud = properties.getIbmCloud();
	}

	@Bean
	public Authenticator authenticator() {
		return new IamAuthenticator.Builder()
			.apikey(ibmCloud.getApiKey())
			.build();
	}

	@Bean
	public SecretsManager secretsManager() {
		return new SecretsManager("Secrets Manager Service", authenticator());
	}
}

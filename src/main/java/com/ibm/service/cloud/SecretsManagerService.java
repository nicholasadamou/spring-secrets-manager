package com.ibm.service.cloud;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.model.Secret;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.SecretsManager;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.model.GetSecretOptions;
import com.ibm.service.config.Properties;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SecretsManagerService {
	private final Properties.IBMCloud ibmCloud;
	private final SecretsManager secretsManager;

	public SecretsManagerService(Properties properties, SecretsManager secretsManager) {
		this.ibmCloud = properties.getIbmCloud();
		this.secretsManager = secretsManager;
	}

	@PostConstruct
	public void init() {
		secretsManager.setServiceUrl(ibmCloud.getSecretsManagerServiceUrl());
	}

	public Secret getSecret(String id) {
		GetSecretOptions getSecretOptions = new GetSecretOptions.Builder()
			.id(id)
			.build();

		Response<Secret> response = secretsManager.getSecret(getSecretOptions).execute();

		return response.getResult();
	}
}

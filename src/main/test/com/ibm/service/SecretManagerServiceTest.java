package com.ibm.service;

import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v2.model.Secret;
import com.ibm.service.cloud.SecretsManagerService;
import com.ibm.service.config.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SecretManagerServiceTest {
	@Mock
	private Properties properties;

	@Mock
	private Properties.IBMCloud ibmCloud;

	@InjectMocks
	private SecretsManagerService secretManagerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(properties.getIbmCloud()).thenReturn(ibmCloud);
	}

	@Test
	void getTrustStoreSecret() {
		// Given
		String id = ibmCloud.getTrustStoreSecretId();

		// When
		Secret secret = secretManagerService.getSecret(id);

		// Then
		assertNotNull(secret);
	}

	@Test
	void getKeyStoreSecret() {
		// Given
		String id = ibmCloud.getKeyStoreSecretId();

		// When
		Secret secret = secretManagerService.getSecret(id);

		// Then
		assertNotNull(secret);
	}
}

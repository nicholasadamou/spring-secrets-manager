# Spring Secrets Manager

_Spring Secrets Manager_ is an innovative project that leverages the power of Spring Boot v3 along with IBM Cloud Secrets Manager for secure keystore retrieval and seamless integration. This repository provides an efficient solution for building, testing, and deploying secure, production-grade applications using the latest Spring Boot v3 framework.

## Features

- Demonstrates the usage of Spring Boot v3 framework for building robust applications.
- Utilizes IBM Cloud Secrets Manager for the secure management of keystores, thereby enhancing application security.
- Provides patterns for easy retrieval and integration of the keystores within the Spring Boot application.
- Includes comprehensive code snippets, detailed configuration steps, and best practices for managing secrets and application configurations.
- Offers a scalable structure that supports the seamless expansion of functionalities.

## Who can benefit?

- Java developers looking for secure keystore management solutions.
- Organizations aiming to strengthen their application's security.
- Anyone interested in implementing IBM Cloud Secrets Manager with Spring Boot v3.
- The Spring Secrets Manager repository is an excellent starting point for understanding the integration between Spring Boot v3 and IBM Cloud Secrets Manager. Its focus on security and efficient management of secrets in production-grade applications makes it a valuable asset for developers and organizations alike.

## Certificate Management

We make use of IBM Cloud Secret Manager to securely store our keystore files. The application retrieves the keystore files from the Secret Manager and loads them into the Java system properties.

### `SecretManagerConfig`

The `SecretManagerConfig` class is located in the package `com.ibm.service.config`. It is a configuration class in a Spring Boot application that handles the setup and configuration of the IBM Cloud Secrets Manager.

It's annotated with `@Configuration` to indicate that it's a source of bean definitions for the application context. In other words, this class is where we define our beans, i.e., objects that are managed by Spring and can be used elsewhere in our application.

The class has two key responsibilities:

1. It authenticates to IBM Cloud by creating an instance of `IamAuthenticator` which uses an API key to authenticate the Secrets Manager service.
2. It initializes and provides an instance of `SecretsManager` service, which is used to manage secrets on IBM Cloud.

#### Method Documentation

`SecretManagerConfig(Properties properties)`: This is the constructor that initializes the `ibmCloud` property with IBM Cloud configuration obtained from the provided `properties` object.

`public Authenticator authenticator()`: This method is marked as a `@Bean` to let Spring know that it's a bean producer. It creates an instance of `IamAuthenticator`, which uses an API key to authenticate with IBM Cloud.

`public SecretsManager secretsManager()`: This method is also marked as a `@Bean`. It creates and returns an instance of `SecretsManager`, which will be used by other parts of the application to interact with the Secrets Manager service on IBM Cloud.

#### Usage

This class is not intended to be used directly. Instead, it provides necessary beans (`Authenticator` and `SecretsManager`) that Spring can inject into other parts of the application where they're required.

This ensures that the IBM Cloud Secrets Manager is correctly set up and ready to use throughout the application, providing a secure and efficient way to manage your application's secrets.

### `SecretManagerService`

The `SecretManagerService` class resides in the `com.ibm.service.cloud` package. This class is defined as a Spring `@Service`, indicating it's a class designed to hold business logic or to act as a service layer in the application. It handles the operations related to retrieving secrets from the IBM Cloud Secrets Manager.

#### Method Documentation

`SecretManagerService(Properties properties, SecretsManager secretsManager)`: This is the constructor that initializes the `ibmCloud` and `secretsManager` properties. The `properties` object provides configuration for IBM Cloud, while `secretsManager` is the instance of the Secrets Manager service.

`public void init()`: This method is annotated with `@PostConstruct`, which means it's executed after the class has been instantiated and dependency injection has been completed. It sets the service URL for the `SecretsManager`.

`public Secret getSecret(String id)`: This method retrieves a secret from IBM Cloud Secrets Manager using the secret ID. It constructs `GetSecretOptions` with the provided ID, sends a request to the Secrets Manager service, and returns the retrieved `Secret`.

#### Usage

This class is used to interact with IBM Cloud Secrets Manager. The `SecretManagerService` class's `getSecret(String id)` method can be used anywhere within your application where a secret needs to be retrieved from the IBM Cloud Secrets Manager.

An example of its use can be found in the `SslConfig` class, which retrieves the truststore and keystore secrets from the Secrets Manager.

### `SslConfig`

This class configures SSL for the application. It uses `SecretManagerService` to retrieve KeyStore data from the Secret Manager.

The `setUpTrustStoreForApplication` and `setUpKeyStoreForApplication` methods retrieve the truststore and keystore secrets respectively from the Secret Manager. They use the `loadKeyStore` method to load the keystores into the Java system properties.

In the `loadKeyStore` method:

- The secret's payload is retrieved and decoded from Base64.
- A temporary file with the ".jks" extension is created to hold the KeyStore data.
- The decoded KeyStore data is written to the file.
- The file's path is set as the value of the `javax.net.ssl.trustStore` or `javax.net.ssl.keyStore` system property (depending on the kind of KeyStore).
- The password is set as the value of the `javax.net.ssl.trustStorePassword` or `javax.net.ssl.keyStorePassword` system property.
- As a result, the application's SSL context is set up with the KeyStores retrieved from the Secret Manager.

## Development

### Requirements

* [Git](https://git-scm.com/)
* [Maven](https://maven.apache.org/)
* [Java](https://www.java.com/) - version 17 or higher
* [docker](https://www.docker.com/)

### Configuration

Please configure a `.env` file in the root of the project.

The example `.env` file can be found here: [.env.example](.env.example).

### Utilizing Docker

Bring down the application using the following command:

```bash
docker compose down --remove-orphans
```

Build the application using the following command:

```bash
docker compose build
```

Start the application using the following command:

```bash
docker compose up -d
```

View the logs of the running container:

```bash
docker logs -f spring-secrets-manager
```

### Tests

The `SecretManagerServiceTest` class resides in the `com.ibm.service` package. It is designed to write integration tests for `SecretsManagerService` which interacts with the IBM Cloud Secrets Manager.

This class uses the Mockito framework for mocking dependencies. Mockito allows you to create and configure mock objects. Using Mockito simplifies the development of tests for classes with external dependencies significantly.

### Method Documentation

`setUp()`: Annotated with `@BeforeEach`, this method sets up the test environment before each test execution. It initializes mocks and configures the properties mock to return `ibmCloud` when `getIbmCloud()` is called.

`getTrustStoreSecret()`: This test method verifies the retrieval of the trust store secret from IBM Cloud Secrets Manager. It asserts that the returned `Secret` object is not null.

`getKeyStoreSecret()`: This test method checks the retrieval of the key store secret from IBM Cloud Secrets Manager. It also asserts that the returned `Secret` object is not null.


### Usage

This class is designed for testing purposes. In a TDD (Test Driven Development) or BDD (Behavior Driven Development) approach, it helps to ensure that the implemented business logic in the `SecretsManagerService` class is working as expected.

To run these tests, you can right-click on the class in IntelliJ IDEA and select **Run 'SecretManagerServiceTest'**. Make sure you have a proper testing environment set up with necessary dependencies installed (like JUnit and Mockito) and that your IDE (like IntelliJ IDEA) is properly configured to run tests.

Note: In these test methods, the actual interaction with IBM Cloud Secrets Manager is mocked, and the `Secret` object is not actually retrieved. In a real testing scenario, you would need valid credentials and an existing secret with the provided ID.

## License

The code is available under the [MIT license](LICENSE).
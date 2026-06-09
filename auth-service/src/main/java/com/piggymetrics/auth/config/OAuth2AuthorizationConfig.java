package com.piggymetrics.auth.config;

import com.piggymetrics.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.UUID;

@Configuration
public class OAuth2AuthorizationConfig {

    @Autowired
    private Environment env;

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient browserClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("ui")
                .redirectUri("http://localhost:4000/login/oauth2/code/browser")
                .build();

        RegisteredClient accountService = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("account-service")
                .clientSecret("{noop}" + env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("server")
                .build();

        RegisteredClient statisticsService = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("statistics-service")
                .clientSecret("{noop}" + env.getProperty("STATISTICS_SERVICE_PASSWORD"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("server")
                .build();

        RegisteredClient notificationService = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("notification-service")
                .clientSecret("{noop}" + env.getProperty("NOTIFICATION_SERVICE_PASSWORD"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("server")
                .build();

        return new InMemoryRegisteredClientRepository(browserClient, accountService, statisticsService, notificationService);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}

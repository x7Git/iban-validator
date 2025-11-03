package com.acme.ibanvalidator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class CseConnectorConfig {

    @Bean
    public RestClient ibanRestClient(
            @Value("${iban.rest.timeout}") int timeoutMillis,
            @Value("${iban.controller.basepath}") String cseControllerBasepath) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMillis);
        factory.setReadTimeout(timeoutMillis);

        return RestClient.builder().requestFactory(factory).baseUrl(cseControllerBasepath).build();
    }
}

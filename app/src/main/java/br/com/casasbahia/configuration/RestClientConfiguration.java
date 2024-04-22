package br.com.casasbahia.configuration;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration
{
    private static final Duration TIMEOUT = Duration.ofSeconds( 10 );

    @Bean
    public RestClient beanRestClient()
    {
        final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout( TIMEOUT )
            .setReadTimeout( TIMEOUT )
            .build();
        return RestClient.create( restTemplate );
    }
}

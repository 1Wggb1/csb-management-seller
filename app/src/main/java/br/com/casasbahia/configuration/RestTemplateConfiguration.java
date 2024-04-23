package br.com.casasbahia.configuration;

import java.time.Duration;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration
{
    private static final Duration TIMEOUT = Duration.ofSeconds( 5 );

    @Bean
    public RestTemplate restTemplateBean(
        final RestTemplateBuilderConfigurer configurer )
    {
        // should be used configure to pass traceId to another service
        // https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.micrometer-tracing.propagating-traces
        return configurer.configure( new RestTemplateBuilder() )
            .setConnectTimeout( TIMEOUT )
            .setReadTimeout( TIMEOUT )
            .build();
    }
}

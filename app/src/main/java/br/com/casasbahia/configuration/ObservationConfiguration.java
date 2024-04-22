package br.com.casasbahia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration
public class ObservationConfiguration
{
    @Bean
    public ObservedAspect observedAspect(
        final ObservationRegistry observationRegistry )
    {
        return new ObservedAspect( observationRegistry );
    }
}
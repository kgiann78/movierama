package com.constantine.movierama.configuration;

import com.constantine.movierama.domain.User;
import com.constantine.movierama.utils.SortedBy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class MovieramaConfiguration {

    @Bean
    @Scope(
            value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public User user() {
        return new User();
    }

    @Bean
    public Integer moviesByUserId() {
        return 0;
    }

    @Bean
    public SortedBy sortedBy() {
        return SortedBy.DATE;
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }
}

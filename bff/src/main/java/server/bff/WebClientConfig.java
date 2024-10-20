package server.bff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client(OAuth2AuthorizedClientManager authorizedClientManager) {

        return new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    }

    @Bean
    public WebClient webClient(ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client) {

        oauth2Client.setDefaultOAuth2AuthorizedClient(true);

        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }
}
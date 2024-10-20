package server.bff;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class BffController {

    private final WebClient webClient;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping
    public Mono<ResponseEntity<String>> hello() {

        return webClient.get()
                .uri("http://localhost:8080/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }

    @GetMapping("/access-token")
    public String index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

        return authorizedClient.getAccessToken().getTokenValue();
    }

    @GetMapping
    public String refreshAccessToken() {

    }
}

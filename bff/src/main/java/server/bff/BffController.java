package server.bff;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class BffController {

    private final WebClient webClient;

    @GetMapping
    public Mono<ResponseEntity<String>> hello() {
        return webClient.get()
                .uri("http://localhost:8080/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }
}

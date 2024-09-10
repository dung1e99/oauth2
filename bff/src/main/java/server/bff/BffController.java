package server.bff;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController("/")
@RequiredArgsConstructor
public class BffController {

    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> hello() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return restTemplate.exchange("http://localhost:8080", HttpMethod.GET, entity, String.class);
    }
}

package server.bff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class BffController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}

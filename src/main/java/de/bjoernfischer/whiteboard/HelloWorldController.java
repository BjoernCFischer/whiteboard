package de.bjoernfischer.whiteboard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("/hello")
public class HelloWorldController {

    @GetMapping()
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!");
    }

    @GetMapping("/{name}")
    public Mono<String> helloSomeone(@PathVariable String name) {
        return Mono.just(String.format("Hello %s!", name));
    }
}

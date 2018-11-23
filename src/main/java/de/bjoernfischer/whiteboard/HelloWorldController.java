package de.bjoernfischer.whiteboard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/hello")
public class HelloWorldController {

    @GetMapping()
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!");
    }

    @GetMapping("/{name}")
    public Mono<String> helloSomeone(@PathVariable String name) {
        return Mono.just(String.format("Hello %s!", name));
    }

    @GetMapping("/gangoffour")
    public Flux<String> helloGoF() {
        return Flux.just("Hello Erich Gamma!\n", "Hello Richard Helm!\n", "Hello Ralph Johnson!\n", "Hello John Vlissides!\n");
    }
}

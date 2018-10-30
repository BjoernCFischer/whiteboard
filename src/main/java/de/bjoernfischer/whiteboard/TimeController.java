package de.bjoernfischer.whiteboard;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RestController
public class TimeController {

    @GetMapping("/")
    public Mono<String> root() {
        return Mono.just("Hello World!");
    }

    @GetMapping("/time")
    public Flux<ServerSentEvent<String>> getTime() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(data -> ServerSentEvent.<String>builder()
                .event("time")
                .id(data.toString())
                .data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());
    }
}

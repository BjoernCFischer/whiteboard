package de.bjoernfischer.whiteboard.controller;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
public class WhiteboardController {

    private final ReactiveRedisOperations<String, WhiteboardMessage> whiteboardOps;

    WhiteboardController(ReactiveRedisOperations<String, WhiteboardMessage> whiteboardOps) {
        this.whiteboardOps = whiteboardOps;
    }

    @GetMapping
    public Mono<String> index() {
        return Mono.just("This is the index page of the Whiteboard42 app. If you lost your watch, try the endpoint /time.");
    }

    @GetMapping(path = "/time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getTime(ServerHttpResponse response) {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        return Flux.interval(Duration.ofSeconds(1))
            .map(data -> ServerSentEvent.<String>builder()
                .event("time")
                .id(data.toString())
                .data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());
    }

    @GetMapping("/messagesnosse")
    public Flux<WhiteboardMessage> getMessagesNoSSE() {
        return whiteboardOps.keys("*").flatMap(whiteboardOps.opsForValue()::get);
    }

    @GetMapping(path = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getMessages(ServerHttpResponse response) {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<WhiteboardMessage> whiteboardMessageFlux = whiteboardOps.keys("*").flatMap(whiteboardOps.opsForValue()::get);

        return Flux.zip(interval, whiteboardMessageFlux, (t, m) -> ServerSentEvent.<String>builder()
            .event("message")
            .id(m.getId())
            .data(String.format("%s: %s", m.getTimestamp(), m.getMessage()))
            .build()
        );
    }

    @PostMapping("/addmessage")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ServerResponse> addMessage(@RequestBody String string) {
        Mono.just(string)
            .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
            .flatMap(whiteboardMessage -> whiteboardOps.opsForValue().set(whiteboardMessage.getId(), whiteboardMessage))
            .subscribe(System.out::println);

        return ok().build();
    }

    @DeleteMapping("/clearmessages")
    public Mono<ServerResponse> clearMessages() {
        // FIXME
        return ok().build();
    }
}

package de.bjoernfischer.whiteboard.controller;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import de.bjoernfischer.whiteboard.repository.WhiteboardMessageRepository;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@RestController
public class WhiteboardController {

    private static final int HEARTBEAT_INTERVAL = 10;

    private final WhiteboardMessageRepository repository;
    private final ReactiveMongoOperations operations;

    WhiteboardController(WhiteboardMessageRepository repository, ReactiveMongoOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    @GetMapping("/")
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
        return repository.findAll();
    }

    @GetMapping(path = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getMessages() {
        Flux<ServerSentEvent<String>> heartbeat = Flux.interval(Duration.ofSeconds(HEARTBEAT_INTERVAL))
            .map(t -> ServerSentEvent.<String>builder()
                .event("heartbeat")
                .id(String.valueOf(t))
                .data(String.format("Heartbeat number %d", t))
                .build()
            );

        Flux<ServerSentEvent<String>> data = repository.findByTimestampGreaterThan(new Date())
            .map(m -> ServerSentEvent.<String>builder()
                .event("message")
                .id(m.getId())
                .data(m.getMessage())
                .build()
            );

        return Flux.merge(heartbeat, data);
    }

    @PostMapping("/addmessage")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WhiteboardMessage> addMessage(@RequestBody String string) {
        return Mono.just(string)
            .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
            .flatMap(repository::save);
    }

    @DeleteMapping("/clearmessages")
    public Mono<ResponseEntity<Void>> clearMessages() {
        return operations.collectionExists(WhiteboardMessage.class)
            .flatMap(exists -> exists ? repository.deleteAll() : Mono.just(exists))
            .then(Mono.just("Neues Whiteboard ist eröffnet!")
                .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
                .flatMap(repository::save))
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}

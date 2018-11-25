package de.bjoernfischer.whiteboard;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

import java.util.Date;
import java.util.UUID;

@Component
public class WhiteboardInitializer {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, WhiteboardMessage> whiteboardOps;

    public WhiteboardInitializer(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, WhiteboardMessage> whiteboardOps) {
        this.factory = factory;
        this.whiteboardOps = whiteboardOps;
    }

    @PostConstruct
    public void loadData() {
        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
                Flux.just("Neues Whiteboard ist eröffnet!")
                        .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
                        .flatMap(whiteboardMessage -> whiteboardOps.opsForValue().set(whiteboardMessage.getId(), whiteboardMessage)))
                .thenMany(whiteboardOps.keys("*")
                        .flatMap(whiteboardOps.opsForValue()::get))
                .subscribe(System.out::println);
    }
}
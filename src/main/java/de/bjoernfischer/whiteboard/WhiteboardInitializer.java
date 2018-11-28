package de.bjoernfischer.whiteboard;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import de.bjoernfischer.whiteboard.repository.WhiteboardMessageRepository;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.Date;
import java.util.UUID;

@Component
public class WhiteboardInitializer {

    private final WhiteboardMessageRepository repository;
    private final ReactiveMongoOperations operations;

    public WhiteboardInitializer(WhiteboardMessageRepository repository, ReactiveMongoOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    @PostConstruct
    public void saveSystemInitMessage() {
        Mono.just("Whiteboard Application neu gestartet!")
            .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
            .flatMap(repository::save)
            .thenMany(repository.findAll())
            .map(m -> m.getTimestamp() + ": " + m.getMessage())
            .subscribe(System.out::println);
    }
}
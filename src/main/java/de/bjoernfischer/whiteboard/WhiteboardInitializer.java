package de.bjoernfischer.whiteboard;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import de.bjoernfischer.whiteboard.repository.WhiteboardMessageRepository;
import org.springframework.data.mongodb.core.CollectionOptions;
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
    public void loadData() {

//        operations.collectionExists(WhiteboardMessage.class)
//            .flatMap(exists -> exists ? operations.dropCollection(WhiteboardMessage.class) : Mono.just(exists))
//            .flatMap(o -> operations.createCollection(WhiteboardMessage.class, CollectionOptions.empty().capped().size(1024 * 1024)))
//            .then()
//            .block();

        Mono.just("Neues Whiteboard ist eröffnet!")
            .map(message -> new WhiteboardMessage(UUID.randomUUID().toString(), message, new Date()))
            .flatMap(repository::save)
            .thenMany(repository.findAll())
            .map(m -> m.getTimestamp() + ": " + m.getMessage())
            .subscribe(System.out::println);
    }
}
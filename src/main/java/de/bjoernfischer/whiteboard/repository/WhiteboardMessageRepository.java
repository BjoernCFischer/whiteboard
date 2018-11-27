package de.bjoernfischer.whiteboard.repository;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface WhiteboardMessageRepository extends ReactiveMongoRepository<WhiteboardMessage, String> {

    @Tailable
    Flux<WhiteboardMessage> findByTimestampGreaterThan(Date date);
}

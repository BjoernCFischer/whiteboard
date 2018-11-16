package de.bjoernfischer.whiteboard.db.api;

import de.bjoernfischer.whiteboard.db.lib.LoremIpsum;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class WhiteboardDBConnector {

    private LoremIpsum loremIpsum = new LoremIpsum();
    private Random rand = new Random();

    public Flux<String> getMessages() {
        return Flux.interval(Duration.ofSeconds(5)).map(i -> loremIpsum.getWords(rand.nextInt(50), rand.nextInt(50)));
    }
}

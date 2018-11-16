package de.bjoernfischer.whiteboard.db.configuration;

import de.bjoernfischer.whiteboard.db.api.WhiteboardDBConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WhiteboardDBConfiguration {

    @Bean
    public WhiteboardDBConnector getConnector() {
        return new WhiteboardDBConnector();
    }
}

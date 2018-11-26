package de.bjoernfischer.whiteboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class StaticContentRouteConfiguration {

    @Bean
    RouterFunction<ServerResponse> staticResourceRouter(){
        return resources("/**", new ClassPathResource("static/"));
    }
}

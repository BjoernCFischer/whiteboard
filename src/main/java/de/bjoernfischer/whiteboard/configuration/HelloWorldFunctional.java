package de.bjoernfischer.whiteboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.BodyInserters.*;

@Configuration
public class HelloWorldFunctional {

    HandlerFunction<ServerResponse> helloWorld =
        request -> ServerResponse.ok().body(BodyInserters.fromObject("Hello Functional World!"));

    HandlerFunction<ServerResponse> helloSomeone =
        request -> ok().body(fromObject(String.format("Hello Functional %s!", request.pathVariable("name"))));

    HandlerFunction<ServerResponse> helloGoF =
        request -> ok()
            .contentType(TEXT_HTML)
            .body(
                fromPublisher(
                    Flux.just("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides")
                        .map(guy -> String.format("Hello %s!<br>", guy)), String.class
            )
        );

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(GET("/hellofunc"), helloWorld)
            .andRoute(GET("/hellofunc/gangoffour"), helloGoF)
            .andRoute(GET("/hellofunc/{name}"), helloSomeone);
    }
}

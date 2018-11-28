package de.bjoernfischer.whiteboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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

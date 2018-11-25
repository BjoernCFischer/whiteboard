package de.bjoernfischer.whiteboard.configuration;

import de.bjoernfischer.whiteboard.model.WhiteboardMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    ReactiveRedisOperations<String, WhiteboardMessage> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<WhiteboardMessage> serializer = new Jackson2JsonRedisSerializer<>(WhiteboardMessage.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, WhiteboardMessage> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, WhiteboardMessage> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}

package club.ss220.port.redis.config;

import club.ss220.port.redis.listener.IngameNewsRedisListener;
import club.ss220.port.redis.presentation.RedisIngamePost;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("ss220.redis.news")
public class IngameNewsRedisConfig {

    @NotBlank
    private String topic;
    @Nullable
    private Duration publishDelay;

    @Bean
    public MessageListenerAdapter newsListenerAdapter(IngameNewsRedisListener delegate) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(delegate, "onNewsPublished");
        adapter.setSerializer(new Jackson2JsonRedisSerializer<>(RedisIngamePost.class));
        return adapter;
    }

    @Bean
    public MessageListenerConfig newsListenerConfig(MessageListenerAdapter newsListenerAdapter) {
        return new MessageListenerConfig(newsListenerAdapter, List.of(new PatternTopic(topic)));
    }
}

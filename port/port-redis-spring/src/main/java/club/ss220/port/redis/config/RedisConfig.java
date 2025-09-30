package club.ss220.port.redis.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("ss220.integration.redis")
public class RedisConfig {

    private boolean enabled;

    @Bean
    @ConditionalOnBooleanProperty(value = "ss220.integration.redis.enabled")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    @ConditionalOnBooleanProperty(value = "ss220.integration.redis.enabled")
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   List<MessageListenerConfig> listenerConfigs) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        listenerConfigs.forEach(cfg -> {
            container.addMessageListener(cfg.listener(), cfg.topics());
            List<Topic> topics = new ArrayList<>(cfg.topics());
            Optional<Class<?>> delegateClass = Optional.ofNullable(cfg.listener().getDelegate()).map(Object::getClass);
            if (delegateClass.isEmpty()) {
                log.error("Listener delegate is null, topics: {}", topics);
                return;
            }
            log.info("Subscribed {} to topics: {}", delegateClass.get().getSimpleName(), topics);
        });
        return container;
    }
}

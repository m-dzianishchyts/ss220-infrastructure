package club.ss220.port.redis.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Collection;

public record MessageListenerConfig(@NotNull MessageListenerAdapter listener, @NotNull Collection<Topic> topics) {
}

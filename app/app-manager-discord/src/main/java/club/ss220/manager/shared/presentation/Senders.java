package club.ss220.manager.shared.presentation;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class Senders {

    @Nullable
    private final Long dispatchChannelId;

    public Senders(@Nullable @Value("${logging.discord.dispatchChannelId:#{null}}") Long dispatchChannelId) {
        this.dispatchChannelId = dispatchChannelId;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AllowedMentionsPolicy {
        NONE(Collections.emptySet()),
        NOTIFIABLE(Set.of(Message.MentionType.ROLE, Message.MentionType.USER)),
        ALL(EnumSet.allOf(Message.MentionType.class));

        private final Set<Message.MentionType> allowedMentions;
    }

    public void sendEmbedEphemeral(IReplyCallback interaction, MessageEmbed embed) {
        send(interaction, null, embed, true, AllowedMentionsPolicy.NONE);
    }

    public void sendEmbed(IReplyCallback interaction, MessageEmbed embed) {
        send(interaction, null, embed, false, AllowedMentionsPolicy.NONE);
    }

    public void sendMessageEphemeral(IReplyCallback interaction, String content) {
        send(interaction, content, null, true, AllowedMentionsPolicy.NONE);
    }

    public void sendMessage(IReplyCallback interaction, String content) {
        send(interaction, content, null, false, AllowedMentionsPolicy.NONE);
    }

    public void send(IReplyCallback interaction, @Nullable String content, @Nullable MessageEmbed embed,
                     boolean ephemeral, AllowedMentionsPolicy policy) {
        if (content == null && embed == null) {
            throw new IllegalArgumentException("Either content or embed must be provided");
        }

        MessageSender sender = createSender(interaction)
                .ephemeral(ephemeral)
                .allowedMentions(policy.getAllowedMentions());
        Optional.ofNullable(content).ifPresent(sender::content);
        Optional.ofNullable(embed).ifPresent(sender::addEmbed);
        sender.queue();
    }

    private MessageSender createSender(IReplyCallback interaction) {
        return interaction.isAcknowledged()
                ? new AcknowledgedMessageSender(interaction.getHook())
                : new ReplyMessageSender(interaction);
    }

    @Getter
    @Setter(onMethod = @__(@CanIgnoreReturnValue))
    @Accessors(fluent = true)
    private abstract static class MessageSender {

        protected boolean ephemeral;
        @NotNull
        protected Collection<Message.MentionType> allowedMentions = Collections.emptySet();
        @Nullable
        protected String content;
        @NotNull
        protected final List<MessageEmbed> embeds = new ArrayList<>();

        abstract void queue();

        @CanIgnoreReturnValue
        public MessageSender addEmbed(MessageEmbed embed) {
            this.embeds.add(embed);
            return this;
        }
    }

    @RequiredArgsConstructor
    private static class AcknowledgedMessageSender extends MessageSender {

        private final InteractionHook hook;

        @Override
        public void queue() {
            WebhookMessageCreateAction<Message> action = content != null
                    ? hook.sendMessage(content).addEmbeds(embeds)
                    : hook.sendMessageEmbeds(embeds);
            action.setAllowedMentions(allowedMentions).queue();
        }
    }

    @RequiredArgsConstructor
    private static class ReplyMessageSender extends MessageSender {

        private final IReplyCallback interaction;

        @Override
        public void queue() {
            ReplyCallbackAction action = content != null
                    ? interaction.reply(content).addEmbeds(embeds)
                    : interaction.replyEmbeds(embeds);
            action.setEphemeral(ephemeral).setAllowedMentions(allowedMentions).queue();
        }
    }

    public void sendUncaughtExceptionReport(JDA jda, MessageEmbed messageEmbed, String stacktrace) {
        if (dispatchChannelId == null) {
            log.warn("Error dispatch channel is not configured! Error report will not be sent");
            return;
        }

        TextChannel textChannelById = jda.getTextChannelById(dispatchChannelId);
        if (textChannelById == null) {
            log.error("Error dispatch channel is not found! Error report will not be sent");
            return;
        }

        String fileName = "stacktrace_" + LocalDateTime.now() + ".txt";
        try (FileUpload stacktraceFile = FileUpload.fromData(stacktrace.getBytes(StandardCharsets.UTF_8), fileName)) {
            textChannelById.sendMessageEmbeds(messageEmbed)
                    .addFiles(stacktraceFile)
                    .setAllowedMentions(Collections.emptyList())
                    .queue();

            log.debug("Sent error report");
        } catch (Exception e) {
            log.error("Error sending error report", e);
        }
    }
}

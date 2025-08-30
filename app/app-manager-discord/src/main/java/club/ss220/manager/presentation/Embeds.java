package club.ss220.manager.presentation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.SequencedMap;

@Component
public class Embeds {

    public MessageEmbed info(String title, String message) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(message)
                .setColor(UiConstants.COLOR_INFO)
                .build();
    }

    public MessageEmbed error(String message) {
        return new EmbedBuilder()
                .setTitle("Ошибка")
                .setDescription(message)
                .setColor(UiConstants.COLOR_ERROR)
                .build();
    }

    public MessageEmbed uncaughtException(String message, SequencedMap<String, Object> context) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Отчет об ошибке");
        embed.setDescription(message);
        context.forEach((key, value) -> embed.addField(key, String.valueOf(value), false));
        embed.setFooter("К сообщению прикреплен файл со стеком вызовов.");
        embed.setColor(UiConstants.COLOR_ERROR);
        return embed.build();
    }
}

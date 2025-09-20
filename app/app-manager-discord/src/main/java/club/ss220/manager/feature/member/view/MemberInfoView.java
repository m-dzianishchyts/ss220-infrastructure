package club.ss220.manager.feature.member.view;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameCharacterData;
import club.ss220.core.shared.MemberData;
import club.ss220.core.shared.PlayerData;
import club.ss220.core.shared.RoleCategory;
import club.ss220.core.shared.UserData;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.GameBuildStyle;
import club.ss220.manager.shared.presentation.UiConstants;
import dev.freya02.jda.emojis.unicode.Emojis;
import io.github.freya022.botcommands.api.components.SelectMenus;
import io.github.freya022.botcommands.api.components.event.StringSelectEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static club.ss220.manager.feature.member.controller.MemberInfoController.MemberInfoContext;

@Component
public class MemberInfoView extends BasicView {

    private final SelectMenus selectMenus;

    public MemberInfoView(Embeds embeds, Formatters formatters, SelectMenus selectMenus) {
        super(embeds, formatters);
        this.selectMenus = selectMenus;
    }

    public MessageCreateData buildInitialMessage(User viewer, MemberInfoContext context,
                                                 Consumer<StringSelectEvent> onBuildSelected) {
        MessageEmbed embed = createMemberInfoEmbed(context);
        ActionRow buildSelectMenu = createBuildSelectMenu(viewer, context, onBuildSelected);

        return new MessageCreateBuilder().setEmbeds(embed).setComponents(buildSelectMenu).build();
    }

    public MessageEditData buildUpdateMessage(User viewer, MemberInfoContext context,
                                              Consumer<StringSelectEvent> onBuildSelected) {
        return MessageEditData.fromCreateData(buildInitialMessage(viewer, context, onBuildSelected));
    }

    private ActionRow createBuildSelectMenu(User viewer, MemberInfoContext context,
                                            Consumer<StringSelectEvent> onSelected) {
        Set<GameBuild> availableBuilds = context.getMember().gameInfo().keySet();
        GameBuild selectedBuild = context.getSelectedBuild();

        List<SelectOption> options = availableBuilds.stream()
                .map(build -> {
                    GameBuildStyle style = GameBuildStyle.fromName(build.getName());
                    return SelectOption.of(style.getName(), build.name()).withEmoji(style.getEmoji());
                })
                .toList();

        StringSelectMenu selectMenu = selectMenus.stringSelectMenu()
                .ephemeral()
                .constraints(constraints -> constraints.addUsers(viewer))
                .bindTo(onSelected)
                .setPlaceholder("Игровой билд")
                .addOptions(options)
                .setDefaultValues(selectedBuild.name())
                .build();

        return ActionRow.of(selectMenu);
    }

    private MessageEmbed createMemberInfoEmbed(MemberInfoContext context) {
        MemberData member = context.getMember();
        GameBuild selectedBuild = context.getSelectedBuild();
        boolean isConfidential = context.isConfidential();

        UserData userData = member.userData();
        PlayerData player = member.gameInfo().get(selectedBuild);
        String description = "**Discord:** " + User.fromId(userData.discordId()).getAsMention() + "\n"
                             + "**CKEY:** " + userData.ckey() + "\n\n"
                             + createPlayerInfoBlock(player, isConfidential);

        List<MessageEmbed.Field> fields = List.of(
                playerExpField(player),
                playerCharactersField(player.characters())
        );

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Информация о пользователе " + userData.id());
        embed.setDescription(description);
        embed.getFields().addAll(fields);
        if (isConfidential) {
            embed.setFooter("Осторожно, сообщение содержит конфиденциальные данные.");
        }
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    private String createPlayerInfoBlock(PlayerData player, boolean isConfidential) {
        String info = "**Ранг:** " + player.lastAdminRank() + "\n"
                      + "**Стаж:** " + player.getKnownFor().toDays() + " дн.\n"
                      + "**BYOND создан:** " + formatters.formatDate(player.byondJoinDate()) + "\n"
                      + "**Первый вход:** " + formatters.formatDateTime(player.firstSeenDateTime()) + "\n"
                      + "**Последний вход:** " + formatters.formatDateTime(player.lastSeenDateTime()) + "\n";
        if (isConfidential) {
            info += "**IP:** ||" + player.ip().getHostAddress() + "||\n"
                    + "**CID:** ||" + player.computerId() + "||\n";
        }
        return info;
    }

    private MessageEmbed.Field playerExpField(PlayerData player) {
        Duration livingExp = player.exp().getForRole(RoleCategory.LIVING).orElseThrow();
        Duration ghostExp = player.exp().getForRole(RoleCategory.GHOST).orElseThrow();
        Duration totalExp = livingExp.plus(ghostExp);

        String title = "Время в игре: " + totalExp.toHours() + " ч.";
        String value = player.exp().getAll().entrySet().stream()
                .filter(e -> !e.getKey().equals(RoleCategory.IGNORE))
                .map(e -> playerExpLine(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        return new MessageEmbed.Field(title, value, true);
    }

    private String playerExpLine(RoleCategory category, Duration exp) {
        return UiConstants.SPACE_FILLER.repeat(category.getLevel())
               + category.getFormattedName() + ": " + exp.toHours() + " ч.";
    }

    private MessageEmbed.Field playerCharactersField(@Nullable List<GameCharacterData> characters) {
        if (characters == null) {
            String value = Emojis.CONSTRUCTION.getFormatted() + " Пока недоступно.";
            return new MessageEmbed.Field("Персонажи: 0", value, true);
        }

        String title = "Персонажи: " + characters.size();
        String value = characters.stream()
                .map(this::playerCharacterLine)
                .collect(Collectors.joining("\n"));
        return new MessageEmbed.Field(title, value, true);
    }

    private String playerCharacterLine(GameCharacterData character) {
        String ageFormat = "{0, plural, one{# год} few{# года} many{# лет} other{# лет}}.";
        return "`%02d` %s\n%s %s %s".formatted(
                character.slot(), character.realName(),
                getGenderEmoji(character.gender()).getFormatted(),
                character.species().getName(),
                formatters.formatPlural(ageFormat, character.age()));
    }

    private Emoji getGenderEmoji(GameCharacterData.Gender gender) {
        return switch (gender) {
            case MALE -> Emojis.MALE_SIGN;
            case FEMALE -> Emojis.FEMALE_SIGN;
            case PLURAL -> Emojis.PARKING;
            case OTHER -> Emojis.HELICOPTER;
        };
    }
}

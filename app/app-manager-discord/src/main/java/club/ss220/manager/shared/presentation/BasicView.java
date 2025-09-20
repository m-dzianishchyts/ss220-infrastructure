package club.ss220.manager.shared.presentation;

import club.ss220.manager.shared.MemberTarget;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BasicView {

    protected final Embeds embeds;
    protected final Formatters formatters;

    public MessageEmbed renderMemberNotFound(MemberTarget target) {
        return embeds.error("Пользователь " + target.getDisplayString() + " не найден.");
    }
}

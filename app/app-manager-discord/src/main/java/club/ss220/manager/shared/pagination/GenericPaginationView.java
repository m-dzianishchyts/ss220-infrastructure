package club.ss220.manager.shared.pagination;

import io.github.freya022.botcommands.api.components.SelectMenus;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenericPaginationView {

    private final SelectMenus selectMenus;

    public <T> void renderFirst(InteractionHook hook, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        User viewer = hook.getInteraction().getUser();
        MessageCreateData data = buildMessage(viewer, ctx, renderer);
        hook.sendMessage(data).queue();
    }

    public <T> void update(InteractionHook hook, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        User viewer = hook.getInteraction().getUser();
        MessageEditData data = MessageEditData.fromCreateData(buildMessage(viewer, ctx, renderer));
        hook.editOriginal(data).queue();
    }

    private <T> MessageCreateData buildMessage(User viewer, PaginatedContext<T> ctx,
                                               PageRenderer<T> renderer) {
        MessageEmbed embed = renderer.render(ctx);
        ActionRow pagesSelect = buildPagesSelect(viewer, ctx, renderer);
        List<ActionRow> rows = new ArrayList<>(List.of(pagesSelect));
        renderer.enrichComponents(ctx, rows);
        return new MessageCreateBuilder().setEmbeds(embed).setComponents(rows).build();
    }

    private <T> ActionRow buildPagesSelect(User viewer, PaginatedContext<T> ctx, PageRenderer<T> renderer) {
        List<SelectOption> options = buildPageOptions(ctx);
        StringSelectMenu menu = selectMenus.stringSelectMenu()
                .ephemeral()
                .constraints(c -> c.addUsers(viewer))
                .bindTo(event -> ctx.controller().onPageSelected(event, ctx, renderer))
                .addOptions(options)
                .setDefaultValues(String.valueOf(ctx.page()))
                .build();
        return ActionRow.of(menu);
    }

    private <T> List<SelectOption> buildPageOptions(PaginatedContext<T> ctx) {
        int startPage = Math.max(0, ctx.startPage());
        int endPage = Math.clamp(ctx.totalPages() - 1, 0, ctx.endPage());
        List<SelectOption> list = new ArrayList<>(Math.max(0, endPage - startPage + 1));
        for (int i = startPage; i <= endPage; i++) {
            int itemStart = i * ctx.pageSize() + 1;
            int itemEnd = Math.min(itemStart + ctx.pageSize() - 1, ctx.totalItems());
            list.add(SelectOption.of("#" + itemStart + " - #" + itemEnd, String.valueOf(i)));
        }
        return list;
    }
}

package club.ss220.manager.feature.blacklist.view;

import club.ss220.manager.presentation.Embeds;
import club.ss220.manager.presentation.Formatters;
import org.springframework.stereotype.Component;

@Component
public class BlacklistSimpleView extends BlacklistView {

    public BlacklistSimpleView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }
}

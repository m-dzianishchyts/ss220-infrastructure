package club.ss220.manager.feature.whitelist.view;

import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import org.springframework.stereotype.Component;

@Component
public class WhitelistSimpleView extends WhitelistView {

    public WhitelistSimpleView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }
}

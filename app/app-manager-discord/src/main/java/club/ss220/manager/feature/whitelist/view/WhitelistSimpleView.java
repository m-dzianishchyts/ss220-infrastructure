package club.ss220.manager.feature.whitelist.view;

import club.ss220.manager.presentation.Embeds;
import club.ss220.manager.presentation.Formatters;
import club.ss220.manager.presentation.Senders;
import org.springframework.stereotype.Component;

@Component
public class WhitelistSimpleView extends WhitelistView {

    public WhitelistSimpleView(Embeds embeds, Senders senders, Formatters formatters) {
        super(embeds, senders, formatters);
    }
}

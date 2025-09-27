package club.ss220.manager.feature.staff.controller;

import club.ss220.core.application.GetOnlineStaffListUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineStaffStatusData;
import club.ss220.manager.feature.staff.view.StaffView;
import club.ss220.manager.shared.presentation.Senders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class StaffController {

    private final StaffView view;
    private final GetOnlineStaffListUseCase getOnlineStaffListUseCase;
    private final GameConfig gameConfig;
    private final Senders senders;

    public void showOnlineStaff(IReplyCallback interaction) {
        interaction.deferReply().queue();

        List<GameServerData> servers = gameConfig.getSupportedServers();
        Map<GameServerData, List<OnlineStaffStatusData>> onlineStaff = getOnlineStaffListUseCase.execute(servers);
        senders.sendEmbed(interaction, view.renderOnlineStaff(onlineStaff));
        log.debug("Displayed online staff for {} servers", onlineStaff.size());
    }
}

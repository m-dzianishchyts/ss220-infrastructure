package club.ss220.manager.feature.staff.controller;

import club.ss220.core.application.GetOnlineStaffListUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineStaffStatusData;
import club.ss220.manager.feature.staff.view.StaffView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
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

    public void showOnlineStaff(InteractionHook hook) {
        try {
            List<GameServerData> servers = gameConfig.getServers();
            Map<GameServerData, List<OnlineStaffStatusData>> onlineStaff = getOnlineStaffListUseCase.execute(servers);
            view.renderOnlineStaff(hook, onlineStaff);

            log.debug("Displayed online staff for {} servers", onlineStaff.size());
        } catch (Exception e) {
            throw new RuntimeException("Error displaying online staff", e);
        }
    }
}

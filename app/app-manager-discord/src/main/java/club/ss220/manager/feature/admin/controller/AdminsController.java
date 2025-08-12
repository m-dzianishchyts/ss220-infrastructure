package club.ss220.manager.feature.admin.controller;

import club.ss220.core.application.GetOnlineAdminsListUseCase;
import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.OnlineAdminStatusData;
import club.ss220.manager.feature.admin.view.AdminsView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AdminsController {

    private final AdminsView view;
    private final GetOnlineAdminsListUseCase getAllAdminsList;
    private final GameConfig gameConfig;

    public void showOnlineAdmins(InteractionHook hook) {
        try {
            List<GameServerData> servers = gameConfig.getServers();
            Map<GameServerData, List<OnlineAdminStatusData>> onlineAdmins = getAllAdminsList.execute(servers);
            view.renderOnlineAdmins(hook, onlineAdmins);

            log.debug("Displayed online admins for {} servers", onlineAdmins.size());
        } catch (Exception e) {
            throw new RuntimeException("Error displaying online admins", e);
        }
    }
}

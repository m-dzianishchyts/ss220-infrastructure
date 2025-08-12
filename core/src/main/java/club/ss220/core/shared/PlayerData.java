package club.ss220.core.shared;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.ToString;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PlayerData(
        @NotNull
        GameBuild gameBuild,
        @NotNull
        @NotBlank
        String ckey,
        @NotNull
        @PastOrPresent
        LocalDate byondJoinDate,
        @NotNull
        @PastOrPresent
        LocalDateTime firstSeenDateTime,
        @NotNull
        @PastOrPresent
        LocalDateTime lastSeenDateTime,
        @NotNull
        @ToString.Exclude
        InetAddress ip,
        @NotNull
        @NotBlank
        @ToString.Exclude
        String computerId,
        @NotNull
        @NotBlank
        String lastAdminRank,
        @NotNull
        PlayerExperienceData exp,
        @Nullable // TODO: Update this when bandastation will store game characters in a database.
        List<GameCharacterData> characters
) {

    public Duration getKnownFor() {
        return Duration.between(firstSeenDateTime, lastSeenDateTime);
    }
}

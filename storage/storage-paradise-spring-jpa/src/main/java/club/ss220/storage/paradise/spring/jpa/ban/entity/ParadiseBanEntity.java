package club.ss220.storage.paradise.spring.jpa.ban.entity;

import club.ss220.core.shared.BanData;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Table(name = "ban")
@Getter
@ToString
public class ParadiseBanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "serverip")
    private String serverAddress;

    @NotNull
    @Column(name = "server_id")
    private String serverId;

    @Nullable
    @Column(name = "ban_round_id")
    private Integer roundId;

    @NotNull
    @Column(name = "ckey")
    private String ckey;

    @NotNull
    @Column(name = "a_ckey")
    private String adminCkey;

    @ToString.Exclude
    @NotNull
    @Column(name = "reason")
    private String reason;

    @Nullable
    @Column(name = "job")
    private String role;

    @NotNull
    @Column(name = "bantime")
    private LocalDateTime banDateTime;

    @NotNull
    @Column(name = "duration")
    private Integer duration;

    @NotNull
    @Column(name = "expiration_time")
    private LocalDateTime expirationDateTime;

    @Nullable
    @Column(name = "edits")
    private String editHistory;

    @Nullable
    @Column(name = "unbanned_ckey")
    private String unbanAdminCkey;

    @Nullable
    @Column(name = "unbanned_datetime")
    private LocalDateTime unbanDateTime;

    @NotNull
    @Formula("case"
             + " when bantype like '%admin%' then 'ADMIN'"
             + " when job is not null and job != '' then 'ROLE'"
             + " else 'NORMAL'"
             + " end")
    @Enumerated(EnumType.STRING)
    private BanData.BanType banType;
}

package club.ss220.storage.bandastation.spring.jpa.ban.entity;

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
public class BandaStationBanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "bantime")
    private LocalDateTime banDateTime;

    @NotNull
    @Column(name = "server_ip", columnDefinition = "INT UNSIGNED")
    private Long serverIp;

    @NotNull
    @Column(name = "server_port", columnDefinition = "SMALLINT UNSIGNED")
    private Integer serverPort;

    @Nullable
    @Column(name = "round_id")
    private Integer roundId;

    @Nullable
    @Column(name = "role")
    private String role;

    @Nullable
    @Column(name = "expiration_time")
    private LocalDateTime expirationDateTime;

    @NotNull
    @Column(name = "applies_to_admins")
    private Boolean appliesToAdmins;

    @ToString.Exclude
    @NotNull
    @Column(name = "reason")
    private String reason;

    @Nullable
    @Column(name = "ckey")
    private String ckey;

    @NotNull
    @Column(name = "a_ckey")
    private String adminCkey;

    @Nullable
    @Column(name = "edits")
    private String edits;

    @Nullable
    @Column(name = "unbanned_ckey")
    private String unbanAdminCkey;

    @Nullable
    @Column(name = "unbanned_datetime")
    private LocalDateTime unbanDateTime;

    @NotNull
    @Formula("case"
             + " when applies_to_admins = 1 then 'ADMIN'"
             + " when role is not null and role != '' and role != 'server' then 'ROLE'"
             + " else 'NORMAL'"
             + " end")
    @Enumerated(EnumType.STRING)
    private BanData.BanType banType;
}

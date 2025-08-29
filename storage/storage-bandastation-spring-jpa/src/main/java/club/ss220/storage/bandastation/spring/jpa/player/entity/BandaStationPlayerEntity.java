package club.ss220.storage.bandastation.spring.jpa.player.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "player")
@Getter
public class BandaStationPlayerEntity {

    @Id
    @Column(name = "ckey")
    private String ckey;

    @Column(name = "accountjoindate")
    private LocalDate byondJoinDate;

    @Column(name = "firstseen")
    private LocalDateTime firstSeen;

    @Column(name = "lastseen")
    private LocalDateTime lastSeen;

    @Column(name = "ip", columnDefinition = "INT UNSIGNED")
    private long ip;

    @Column(name = "computerid")
    private String computerId;

    @Column(name = "lastadminrank")
    private String lastAdminRank;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_time", joinColumns = @JoinColumn(name = "ckey"))
    @MapKeyColumn(name = "job")
    @Column(name = "minutes", columnDefinition = "INT UNSIGNED")
    private Map<String, Long> roleTime;
}

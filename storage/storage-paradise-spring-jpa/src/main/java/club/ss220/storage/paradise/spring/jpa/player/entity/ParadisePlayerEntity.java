package club.ss220.storage.paradise.spring.jpa.player.entity;

import club.ss220.storage.paradise.spring.jpa.character.entity.ParadiseCharacterEntity;
import club.ss220.storage.paradise.spring.jpa.admin.entity.AdminEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "player")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ParadisePlayerEntity {

    private static final String DEFAULT_RANK = "Игрок";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "ckey")
    private String ckey;

    @Column(name = "byond_date")
    private LocalDate byondJoinDate;

    @Column(name = "firstseen")
    private LocalDateTime firstSeen;

    @Column(name = "lastseen")
    private LocalDateTime lastSeen;

    @Column(name = "ip")
    private String ip;

    @Column(name = "computerid")
    private String computerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ckey", referencedColumnName = "ckey", insertable = false, updatable = false)
    private AdminEntity admin;

    @Column(name = "exp")
    private String exp;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ckey", referencedColumnName = "ckey")
    private List<ParadiseCharacterEntity> characters;

    public String getLastAdminRank() {
        return Optional.ofNullable(admin).map(AdminEntity::getAdminRankName).orElse(DEFAULT_RANK);
    }
}

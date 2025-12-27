package club.ss220.storage.paradise.spring.jpa.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ckey")
    private String ckey;

    @Column(name = "display_rank")
    private String displayRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissions_rank")
    private AdminRankEntity rank;

    public String getAdminRankName() {
        if (displayRank != null && !displayRank.isBlank()) {
            return displayRank;
        }
        return rank != null ? rank.getName() : null;
    }
}

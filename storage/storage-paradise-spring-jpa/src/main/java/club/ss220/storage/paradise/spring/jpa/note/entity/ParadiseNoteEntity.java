package club.ss220.storage.paradise.spring.jpa.note.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@ToString
public class ParadiseNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ckey", nullable = false)
    private String ckey;

    @Column(name = "notetext", columnDefinition = "TEXT")
    private String text;

    @Column(name = "adminckey", nullable = false)
    private String adminCkey;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "server", nullable = false)
    private String serverId;

    @Column(name = "round_id")
    private Integer roundId;

    @Column(name = "edits")
    private String editHistory;

    @Column(name = "automated", columnDefinition = "tinyint unsigned", nullable = false)
    private Boolean automated;

    @Column(name = "deleted", columnDefinition = "tinyint", nullable = false)
    private Boolean deleted;

    @Column(name = "deletedby")
    private String deletedCkey;
}

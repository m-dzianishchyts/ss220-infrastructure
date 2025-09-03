package club.ss220.storage.bandastation.spring.jpa.note.entity;

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
@Table(name = "messages")
@Getter
@ToString
public class BandaStationMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "targetckey", nullable = false)
    private String ckey;

    @Column(name = "adminckey", nullable = false)
    private String adminCkey;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "server_ip", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long serverIp;

    @Column(name = "server_port", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer serverPort;

    @Column(name = "edits")
    private String editHistory;

    @Column(name = "round_id")
    private Integer roundId;

    @Column(name = "deleted", columnDefinition = "tinyint unsigned", nullable = false)
    private Boolean deleted;

    @Column(name = "deleted_ckey")
    private String deletedCkey;
}

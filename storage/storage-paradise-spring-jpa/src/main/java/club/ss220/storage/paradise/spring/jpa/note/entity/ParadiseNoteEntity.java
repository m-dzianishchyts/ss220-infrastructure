package club.ss220.storage.paradise.spring.jpa.note.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
public class ParadiseNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ckey", nullable = false)
    private String ckey;

    @Column(name = "notetext", columnDefinition = "TEXT")
    private String noteText;

    @Column(name = "adminckey", nullable = false)
    private String adminCkey;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "server")
    private String server;

    @Column(name = "round_id")
    private Integer roundId;
}

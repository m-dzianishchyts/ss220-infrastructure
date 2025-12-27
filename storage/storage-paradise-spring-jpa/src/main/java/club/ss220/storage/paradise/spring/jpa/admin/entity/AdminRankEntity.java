package club.ss220.storage.paradise.spring.jpa.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_ranks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminRankEntity {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;
}

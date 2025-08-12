package club.ss220.storage.paradise.spring.jpa.character.entity;

import club.ss220.core.shared.GameCharacterData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "characters")
@NoArgsConstructor
@AllArgsConstructor
public class ParadiseCharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "ckey")
    private String ckey;
    
    @Column(name = "slot")
    private Integer slot;
    
    @Column(name = "real_name")
    private String realName;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "age")
    private Short age;
    
    @Column(name = "species")
    private GameCharacterData.Species species;
}

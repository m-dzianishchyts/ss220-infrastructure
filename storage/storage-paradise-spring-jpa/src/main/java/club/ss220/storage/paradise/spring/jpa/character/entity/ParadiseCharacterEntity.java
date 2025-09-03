package club.ss220.storage.paradise.spring.jpa.character.entity;

import club.ss220.core.shared.GameCharacterData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "characters")
@Getter
@ToString
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

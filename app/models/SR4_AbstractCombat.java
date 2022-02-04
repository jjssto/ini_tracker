package models;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
@Table( name = "sr4_combat")
public class SR4_AbstractCombat {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="combat", cascade = CascadeType.ALL)
    private List<SR4_CharRecord> charas;

    @Column( name ="combat_desc" )
    private String description;
}

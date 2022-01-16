package models;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public class AbstractCombat {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="combat", cascade = CascadeType.ALL)
    private List<CharRecord> charas;

    @Column( name ="combat_desc" )
    private String description;
}

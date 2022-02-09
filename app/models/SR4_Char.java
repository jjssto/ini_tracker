package models;

import javax.persistence.*;

@Entity
@Table( name="sr4_char" )
public class SR4_Char {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column( name = "id" )
    private Integer id;

    @Column( name = "char_name")
    private String name;

    @Column( name = "s_boxes")
    private Integer sBoxes = 0;

    @Column( name = "p_boxes")
    private Integer pBoxes = 0;

    @Column( name = "ini")
    private Integer ini = 0;

    @Column( name = "reaction")
    private Integer reaction = 0;

    @Column( name = "intuition")
    private Integer intuition = 0;

    @Column( name = "pc" )
    private boolean pc;

    public SR4_Char() {
        this.ini = 1;
        this.name = "";
        this.sBoxes = 1;
        this.pBoxes = 1;
        this.reaction = 1;
        this.intuition = 1;
        this.pc = false;
    }

    public Integer getId(){
        return id;
    }
    public void setId( Integer id ){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName( String name ){
        this.name = name;
    }

    public Integer getSBoxes() {
        return sBoxes;
    }
    public void setSBoxes( Integer sBoxes ) {
        this.sBoxes = sBoxes;
    }

    public Integer getPBoxes() {
        return pBoxes;
    }
    public void setPBoxes( Integer pBoxes ) {
        this.pBoxes = pBoxes;
    }

    public void setPc( boolean pc ) {
        this.pc = pc;
    }
    public boolean getPc() {
        return pc;
    }
    public Integer getIni() {
        return ini;
    }
    public void setIni( Integer ini ) {
        this.ini = ini;
    }
    public Integer getReaction() {
        return reaction;
    }
    public void setReaction( Integer reaction ) {
        this.reaction = reaction;
    }
    public Integer getIntuition() {
        return intuition;
    }
    public void setIntuition( Integer intuition ) {
        this.intuition = intuition;
    }
}
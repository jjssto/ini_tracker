package models.coc;

import javax.persistence.*;

@Entity
public class CocChar {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    private String name;

    private short strength;
    private short constitution;
    private short size;
    private short dexterity;
    private short appearance;
    private short intelligence;
    private short power;
    private short education;

    public CocChar( String name ) {

        this.name = name;
        strength = 10;
        constitution = 10;
        size = 10;
        dexterity = 10;
        appearance = 10;
        intelligence = 10;
        power = 10;
        education = 10;
    }

    public CocChar() {
        this( null );
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public short getStrength() {
        return strength;
    }

    public void setStrength( short strength ) {
        this.strength = strength;
    }

    public short getConstitution() {
        return constitution;
    }

    public void setConstitution( short constitution ) {
        this.constitution = constitution;
    }

    public short getSize() {
        return size;
    }

    public void setSize( short size ) {
        this.size = size;
    }

    public short getDexterity() {
        return dexterity;
    }

    public void setDexterity( short dexterity ) {
        this.dexterity = dexterity;
    }

    public short getAppearance() {
        return appearance;
    }

    public void setAppearance( short appearance ) {
        this.appearance = appearance;
    }

    public short getIntelligence() {
        return intelligence;
    }

    public void setIntelligence( short intelligence ) {
        this.intelligence = intelligence;
    }

    public short getPower() {
        return power;
    }

    public void setPower( short power ) {
        this.power = power;
    }

    public short getEducation() {
        return education;
    }

    public void setEducation( short education ) {
        this.education = education;
    }
}

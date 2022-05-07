package models.coc;

import javax.persistence.*;

//@Entity
public class CocCharSheet {
 //   @Id
  //  @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

   // @OneToOne
    //@JoinColumn( name = "char_id" )
    private CocChar chara;

    private short accounting;
    private short acting;
    private short animalHandling;
    private short anthropology;
    private short appraise;
    private short archeology;
    private short artillery;
    private short astronomy;
    private short axe;
    private short biology;
    private short botany;
    private short bow;
    private short brawl;
    private short chainsaw;
    private short charm;
    private short chemistry;
    private short climb;
    private short computerUse;
    private short creditRating;
    private short cryptography;
    private short cthulhuMythos;
    private short demolitions;
    private short disguise;
    private short diving;
    private short dodge;
    private short driveAuto;
    private short electricalRepair;
    private short electronics;
    private short engineering;
    private short fastTalk;
    private short fineArts;
    private short firstAid;
    private short flail;
    private short flamethrower;
    private short forensics;
    private short forgery;
    private short garrote;
    private short geology;
    private short handgun;
    private short heavyWeapons;
    private short history;
    private short hypnosis;
    private short intimidate;
    private short jump;
    private short ownLanguage;
    private short law;
    private short libraryUse;
    private short listen;
    private short locksmith;
    private short machineGun;
    private short mathematics;
    private short mechanicalRepair;
    private short medicine;
    private short meteorology;
    private short naturalWorld;
    private short navigate;
    private short occult;
    private short operateHeavyMachinery;
    private short persuade;
    private short pharmacy;
    private short photography;
    private short physics;
    private short pilot;
    private short psychoanalysis;
    private short psychology;
    private short readLips;
    private short ride;
    private short rifle;
    private short shotgun;
    private short sleightOfHand;
    private short spear;
    private short spotHidden;
    private short stealth;
    private short submachineGun;
    private short surival;
    private short sword;
    private short swim;
    private short throwing;
    private short track;
    private short whip;
    private short zoology;

    public CocCharSheet( CocChar chara ) {

       this.chara = chara;
       accounting = 5;
       acting = 5;
       animalHandling = 5;
       anthropology = 1;
       appraise = 5;
       archeology = 1;
       artillery = 1;
       astronomy = 1;
       axe = 15;
       biology = 1;
       botany = 1;
       bow = 15;
       brawl = 25;
       chainsaw = 10;
       charm = 15;
       chemistry = 1;
       climb = 20;
       computerUse = 5;
       creditRating = 0;
       cryptography = 1;
       cthulhuMythos = 0;
       demolitions = 1;
       disguise = 5;
       diving = 1;
       dodge = (short) ((short) chara.getDexterity() / 2);
       driveAuto = 20;
       electricalRepair = 10;
       electronics = 1;
       engineering = 1;
       fastTalk = 5;
       fineArts = 5;
       firstAid = 30;
       flail = 10;
       flamethrower = 10;
       forensics = 1;
       forgery = 5;
       garrote = 15;
       geology = 1;
       handgun = 20;
       heavyWeapons = 10;
       history = 5;
       hypnosis = 01;
       intimidate = 15;
       jump = 20;
       ownLanguage = chara.getEducation();
       law = 5;
       libraryUse = 20;
       listen = 20;
       locksmith = 1;
       machineGun = 10;
       mathematics = 10;
       mechanicalRepair = 10;
       medicine = 1;
       meteorology = 1;
       naturalWorld = 10;
       navigate = 10;
       occult = 5;
       operateHeavyMachinery = 1;
       persuade = 10;
       pharmacy = 1;
       photography = 5;
       physics = 1;
       pilot = 1;
       psychoanalysis = 1;
       psychology = 10;
       readLips = 1;
       ride = 5;
       rifle = 25;
       shotgun = 25;
       sleightOfHand = 10;
       spear = 20;
       spotHidden = 25;
       stealth = 20;
       submachineGun = 15;
       surival = 10;
       sword = 20;
       swim = 20;
       throwing = 20;
       track = 10;
       whip = 5;
       zoology = 1;
    }

    public CocCharSheet() {
        this( null );
    }


    public CocChar getChara() {
        return chara;
    };

    public void setChara( CocChar chara ) {
        this.chara = chara;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public short getAccounting() {
        return accounting;
    }

    public void setAccounting( short accounting ) {
        this.accounting = accounting;
    }

    public short getActing() {
        return acting;
    }

    public void setActing( short acting ) {
        this.acting = acting;
    }

    public short getAnimalHandling() {
        return animalHandling;
    }

    public void setAnimalHandling( short animalHandling ) {
        this.animalHandling = animalHandling;
    }

    public short getAnthropology() {
        return anthropology;
    }

    public void setAnthropology( short anthropology ) {
        this.anthropology = anthropology;
    }

    public short getAppraise() {
        return appraise;
    }

    public void setAppraise( short appraise ) {
        this.appraise = appraise;
    }

    public short getArcheology() {
        return archeology;
    }

    public void setArcheology( short archeology ) {
        this.archeology = archeology;
    }

    public short getArtillery() {
        return artillery;
    }

    public void setArtillery( short artillery ) {
        this.artillery = artillery;
    }

    public short getAstronomy() {
        return astronomy;
    }

    public void setAstronomy( short astronomy ) {
        this.astronomy = astronomy;
    }

    public short getAxe() {
        return axe;
    }

    public void setAxe( short axe ) {
        this.axe = axe;
    }

    public short getBiology() {
        return biology;
    }

    public void setBiology( short biology ) {
        this.biology = biology;
    }

    public short getBotany() {
        return botany;
    }

    public void setBotany( short botany ) {
        this.botany = botany;
    }

    public short getBow() {
        return bow;
    }

    public void setBow( short bow ) {
        this.bow = bow;
    }

    public short getBrawl() {
        return brawl;
    }

    public void setBrawl( short brawl ) {
        this.brawl = brawl;
    }

    public short getChainsaw() {
        return chainsaw;
    }

    public void setChainsaw( short chainsaw ) {
        this.chainsaw = chainsaw;
    }

    public short getCharm() {
        return charm;
    }

    public void setCharm( short charm ) {
        this.charm = charm;
    }

    public short getChemistry() {
        return chemistry;
    }

    public void setChemistry( short chemistry ) {
        this.chemistry = chemistry;
    }

    public short getClimb() {
        return climb;
    }

    public void setClimb( short climb ) {
        this.climb = climb;
    }

    public short getComputerUse() {
        return computerUse;
    }

    public void setComputerUse( short computerUse ) {
        this.computerUse = computerUse;
    }

    public short getCreditRating() {
        return creditRating;
    }

    public void setCreditRating( short creditRating ) {
        this.creditRating = creditRating;
    }

    public short getCryptography() {
        return cryptography;
    }

    public void setCryptography( short cryptography ) {
        this.cryptography = cryptography;
    }

    public short getCthulhuMythos() {
        return cthulhuMythos;
    }

    public void setCthulhuMythos( short cthulhuMythos ) {
        this.cthulhuMythos = cthulhuMythos;
    }

    public short getDemolitions() {
        return demolitions;
    }

    public void setDemolitions( short demolitions ) {
        this.demolitions = demolitions;
    }

    public short getDisguise() {
        return disguise;
    }

    public void setDisguise( short disguise ) {
        this.disguise = disguise;
    }

    public short getDiving() {
        return diving;
    }

    public void setDiving( short diving ) {
        this.diving = diving;
    }

    public short getDodge() {
        return dodge;
    }

    public void setDodge( short dodge ) {
        this.dodge = dodge;
    }

    public short getDriveAuto() {
        return driveAuto;
    }

    public void setDriveAuto( short driveAuto ) {
        this.driveAuto = driveAuto;
    }

    public short getElectricalRepair() {
        return electricalRepair;
    }

    public void setElectricalRepair( short electricalRepair ) {
        this.electricalRepair = electricalRepair;
    }

    public short getElectronics() {
        return electronics;
    }

    public void setElectronics( short electronics ) {
        this.electronics = electronics;
    }

    public short getEngineering() {
        return engineering;
    }

    public void setEngineering( short engineering ) {
        this.engineering = engineering;
    }

    public short getFastTalk() {
        return fastTalk;
    }

    public void setFastTalk( short fastTalk ) {
        this.fastTalk = fastTalk;
    }

    public short getFineArts() {
        return fineArts;
    }

    public void setFineArts( short fineArts ) {
        this.fineArts = fineArts;
    }

    public short getFirstAid() {
        return firstAid;
    }

    public void setFirstAid( short firstAid ) {
        this.firstAid = firstAid;
    }

    public short getFlail() {
        return flail;
    }

    public void setFlail( short flail ) {
        this.flail = flail;
    }

    public short getFlamethrower() {
        return flamethrower;
    }

    public void setFlamethrower( short flamethrower ) {
        this.flamethrower = flamethrower;
    }

    public short getForensics() {
        return forensics;
    }

    public void setForensics( short forensics ) {
        this.forensics = forensics;
    }

    public short getForgery() {
        return forgery;
    }

    public void setForgery( short forgery ) {
        this.forgery = forgery;
    }

    public short getGarrote() {
        return garrote;
    }

    public void setGarrote( short garrote ) {
        this.garrote = garrote;
    }

    public short getGeology() {
        return geology;
    }

    public void setGeology( short geology ) {
        this.geology = geology;
    }

    public short getHandgun() {
        return handgun;
    }

    public void setHandgun( short handgun ) {
        this.handgun = handgun;
    }

    public short getHeavyWeapons() {
        return heavyWeapons;
    }

    public void setHeavyWeapons( short heavyWeapons ) {
        this.heavyWeapons = heavyWeapons;
    }

    public short getHistory() {
        return history;
    }

    public void setHistory( short history ) {
        this.history = history;
    }

    public short getHypnosis() {
        return hypnosis;
    }

    public void setHypnosis( short hypnosis ) {
        this.hypnosis = hypnosis;
    }

    public short getIntimidate() {
        return intimidate;
    }

    public void setIntimidate( short intimidate ) {
        this.intimidate = intimidate;
    }

    public short getJump() {
        return jump;
    }

    public void setJump( short jump ) {
        this.jump = jump;
    }

    public short getOwnLanguage() {
        return ownLanguage;
    }

    public void setOwnLanguage( short ownLanguage ) {
        this.ownLanguage = ownLanguage;
    }

    public short getLaw() {
        return law;
    }

    public void setLaw( short law ) {
        this.law = law;
    }

    public short getLibraryUse() {
        return libraryUse;
    }

    public void setLibraryUse( short libraryUse ) {
        this.libraryUse = libraryUse;
    }

    public short getListen() {
        return listen;
    }

    public void setListen( short listen ) {
        this.listen = listen;
    }

    public short getLocksmith() {
        return locksmith;
    }

    public void setLocksmith( short locksmith ) {
        this.locksmith = locksmith;
    }

    public short getMachineGun() {
        return machineGun;
    }

    public void setMachineGun( short machineGun ) {
        this.machineGun = machineGun;
    }

    public short getMathematics() {
        return mathematics;
    }

    public void setMathematics( short mathematics ) {
        this.mathematics = mathematics;
    }

    public short getMechanicalRepair() {
        return mechanicalRepair;
    }

    public void setMechanicalRepair( short mechanicalRepair ) {
        this.mechanicalRepair = mechanicalRepair;
    }

    public short getMedicine() {
        return medicine;
    }

    public void setMedicine( short medicine ) {
        this.medicine = medicine;
    }

    public short getMeteorology() {
        return meteorology;
    }

    public void setMeteorology( short meteorology ) {
        this.meteorology = meteorology;
    }

    public short getNaturalWorld() {
        return naturalWorld;
    }

    public void setNaturalWorld( short naturalWorld ) {
        this.naturalWorld = naturalWorld;
    }

    public short getNavigate() {
        return navigate;
    }

    public void setNavigate( short navigate ) {
        this.navigate = navigate;
    }

    public short getOccult() {
        return occult;
    }

    public void setOccult( short occult ) {
        this.occult = occult;
    }

    public short getOperateHeavyMachinery() {
        return operateHeavyMachinery;
    }

    public void setOperateHeavyMachinery( short operateHeavyMachinery ) {
        this.operateHeavyMachinery = operateHeavyMachinery;
    }

    public short getPersuade() {
        return persuade;
    }

    public void setPersuade( short persuade ) {
        this.persuade = persuade;
    }

    public short getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy( short pharmacy ) {
        this.pharmacy = pharmacy;
    }

    public short getPhotography() {
        return photography;
    }

    public void setPhotography( short photography ) {
        this.photography = photography;
    }

    public short getPhysics() {
        return physics;
    }

    public void setPhysics( short physics ) {
        this.physics = physics;
    }

    public short getPilot() {
        return pilot;
    }

    public void setPilot( short pilot ) {
        this.pilot = pilot;
    }

    public short getPsychoanalysis() {
        return psychoanalysis;
    }

    public void setPsychoanalysis( short psychoanalysis ) {
        this.psychoanalysis = psychoanalysis;
    }

    public short getPsychology() {
        return psychology;
    }

    public void setPsychology( short psychology ) {
        this.psychology = psychology;
    }

    public short getReadLips() {
        return readLips;
    }

    public void setReadLips( short readLips ) {
        this.readLips = readLips;
    }

    public short getRide() {
        return ride;
    }

    public void setRide( short ride ) {
        this.ride = ride;
    }

    public short getRifle() {
        return rifle;
    }

    public void setRifle( short rifle ) {
        this.rifle = rifle;
    }

    public short getShotgun() {
        return shotgun;
    }

    public void setShotgun( short shotgun ) {
        this.shotgun = shotgun;
    }

    public short getSleightOfHand() {
        return sleightOfHand;
    }

    public void setSleightOfHand( short sleightOfHand ) {
        this.sleightOfHand = sleightOfHand;
    }

    public short getSpear() {
        return spear;
    }

    public void setSpear( short spear ) {
        this.spear = spear;
    }

    public short getSpotHidden() {
        return spotHidden;
    }

    public void setSpotHidden( short spotHidden ) {
        this.spotHidden = spotHidden;
    }

    public short getStealth() {
        return stealth;
    }

    public void setStealth( short stealth ) {
        this.stealth = stealth;
    }

    public short getSubmachineGun() {
        return submachineGun;
    }

    public void setSubmachineGun( short submachineGun ) {
        this.submachineGun = submachineGun;
    }

    public short getSurival() {
        return surival;
    }

    public void setSurival( short surival ) {
        this.surival = surival;
    }

    public short getSword() {
        return sword;
    }

    public void setSword( short sword ) {
        this.sword = sword;
    }

    public short getSwim() {
        return swim;
    }

    public void setSwim( short swim ) {
        this.swim = swim;
    }

    public short getTrack() {
        return track;
    }

    public void setTrack( short track ) {
        this.track = track;
    }

    public short getWhip() {
        return whip;
    }

    public void setWhip( short whip ) {
        this.whip = whip;
    }

    public short getZoology() {
        return zoology;
    }

    public void setZoology( short zoology ) {
        this.zoology = zoology;
    }

    public short getThrowing() {
        return throwing;
    }

    public void setThrowing( short throwing ) {
        this.throwing = throwing;
    }
}

package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import java.util.ArrayList;

public class Player {
    private int ID;
    private City city;
    private ArrayList<BaseBuildings> buildings;
    private BaseBot bot;
    private BaseCharacter character;
    private int gold;
    private String name;
    private int score;
    private boolean crowned;
    private boolean killed;
    private boolean haveCitadel;
    private boolean finishFirst;
    private ArrayList<Player> gamePlayers;

    public Player(BaseBot bot, int ID) {
        this.ID = ID;
        this.bot = bot;
        this.city = new City();
        this.buildings = new ArrayList<>();
        this.gold = 0;
        name = bot.getName();
        this.score = 0;
        this.crowned = false;
        this.haveCitadel = false;
        this.finishFirst = false;
        this.killed = false;
    }

    public void setGamePlayers(ArrayList<Player> players){
        this.gamePlayers = players;
    }

    public ArrayList<Player> getGamePlayers(){
        return gamePlayers;
    }

    public void setCity(City city){
        this.city = city;
    }

    public BaseBot getBot(){
        return this.bot;
    }

    public BaseCharacter getCharacter(){
        return this.character;
    }

    void setScore(){
        this.score = 0;
        BaseBuildings courDesMiracles = getCourDesMiracles();
        if(courDesMiracles!=null){
            String newColor = this.getBot().courDesMiraclesDecision();
            courDesMiracles.setType(newColor);
            city.getBuildings().remove(courDesMiracles);
            city.build(courDesMiracles);
        }
        for (BaseBuildings building: city.getBuildings()) {
            this.score += building.getVictoryPoints();
        }
        if (finishFirst && haveCitadel) this.score += 4;
        if (!finishFirst && haveCitadel) this.score +=2;
        if (city.bonusDifferentBuildings()) this.score += 3;
    }
    int getScore(){
        return this.score;
    }

    public City getCity(){return this.city;}

    public int getID(){return this.ID;}

    public void setCharacter(BaseCharacter character) {
        this.character = character;
    }

    public String getName(){return this.name;}


    public void setGold(int newGold){
        this.gold = newGold;
    }

    public int getGold(){
        return  this.gold;
    }

    public void addGold(int goldToAdd){this.gold += goldToAdd;}


    public boolean useBuilding(BaseBuildings building){
        city.build(building);
        gold -= building.getPrice();
        return buildings.remove(building);
    }


    /**
     * @author FRANCIS Anas
     * @return le b??timent biblioth??que si le joueur l'a dans sa cit??, null sinon.
     */
    BibliothequeBuilding getBibliotheque() {
        return this.city.getBibliotheque();
    }
    ManufactureBuilding getManufacture() {
        return this.city.getManufacture();
    }
    LaboratoireBuilding getLaboratoire() {return this.city.getLaboratoire();}
    ObservatoireBuilding getObservatoire() {return this.city.getObservatoire();}

    private BaseBuildings getCourDesMiracles() {
        return this.city.getCourDesMiracles();
    }
    public WonderBuilding getCimetiere() {
        return this.city.getCimetiere();
    }
    public WonderBuilding getEcoleDeMagie() {
        return this.city.getEcoleDeMagie();
    }

    public void resetAllUsedWonder(){
        for(BaseBuildings building : this.getBuildingsBuilt()){
            if(building instanceof WonderBuilding ) {
                ((WonderBuilding) building).resetBuilding();
            }
        }
    }


    /**
     * @author DOMINGUEZ Lucas
     * \brief Sert ?? piocher N batiments (pas sur si ??a doit ??tre ici -> trop de resp)
     * @param deckBuildings La pioche de b??timents
     * @param n Le nombre de b??timents qu'il faut piocher
     */
    public ArrayList<BaseBuildings> drawNBuildings (ArrayList<BaseBuildings> deckBuildings, int n) {
        ArrayList<BaseBuildings> builingsDrawned = new ArrayList<>(); //liste des batiments pioch??s
        for (int i = 0; i < n; i++) {
            if (deckBuildings.size() > 0) { // si le deck n'est pas vide :
                builingsDrawned.add(deckBuildings.remove(0)); //on pioche et retire du deck
            }
        }
        return builingsDrawned;
    }
    /**
     * @author DOMINGUEZ Lucas
     * \brief Sert ?? remettre un batiment au fond d'un paquet
     * @param building Le b??timent a ins??rer au fond du paquet
     * @param deckBuildings La pioche de b??timents o?? l'on ins??re la carte
     */
    public void putInDeckBuildings (BaseBuildings building, ArrayList<BaseBuildings> deckBuildings){
        deckBuildings.add(building);
    }
    /**
     * \brief M??thode qui affecte au joueur une nouvelle liste de b??timents
     */
    public void setBuildingsOfThisPlayer(ArrayList<BaseBuildings> newBuildings){buildings = newBuildings;}

    /**
     * \brief M??thode qui retourne la liste des b??timents du joueur
     */
    public ArrayList<BaseBuildings> getBuildingsOfThisPlayer(){return buildings;}

    /**
     * \brief M??thode qui informe si le joueur est couronn?? ou non
     */
    public boolean getCrowned(){
        return crowned;
    }

    /**
     * \brief M??thode qui change l'??tat de la couronne pour le joueur
     */
    public void deCrown(){crowned = false;}
    public void crown(){crowned = true;}

    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings){
        return this.bot.chooseBuildings(buildings);
    }

    /**
     * @author FRANCIS Anas
     * @author DOMINGUEZ Lucas
     * @author Timoth??e Poulain
     * \brief on verifie si le joueur peut construir un b??timent ou pas (parmis la liste des b??timents de sa main!).
     * @return boolean
     * created 27/10/2019
     * Timoth??e Poulain : d??placement dans Player.java
     */
    public boolean canBuildABuilding (){ // dit s'il PEUT constuire un batiment
        if (buildings.size() == 0) return false; //regarde si la main est vide
        for (BaseBuildings building : buildings) {
            if (building.getPrice() <= this.getGold() && !(city.isBuild(building))) {// on v??rifie s'il a assez d'argent pour construir le b??timents
                return (true);
            }
        }
        return false;
    }
    public boolean canBuildABuilding(ArrayList<BaseBuildings> hand){
        if (hand.size() == 0) return false;
        for (BaseBuildings building : hand) {
            if (building.getPrice() <= this.getGold() && !(city.isBuild(building))) {
                return (true);
            }
        }
        return false;
    }


    public void kill (){
        killed = true;
    }
    public void dekill(){killed = false;}
    public boolean getKilled(){
        return killed;
    }




    public ArrayList<BaseBuildings> getBuildingsBuilt(){
        return city.getBuildings();
    }


    void finishCitadel (boolean isFirstToFinish){
        if(isFirstToFinish){finishFirst = true;}
        haveCitadel = true;
    }

    public BaseBuildings laboratoryDecision (){
        return this.bot.laboratoryDecision();
    }


    /**
     * @author FRANCIS Anas
     * @return le joueur qui a plus des batimenst (dans sa cit?? ou dans sa main d??pend du boolean qu'on passe en param??tre)
     * @param builtOrInHand boolean pour v??rifier quel max des b??timents on cherche (max des b??timents qui sont dans sa main ou bien dans sa cit???)
     *                      si c'est le max des b??timents dans la main builtOrInHand = false, si c'est le max des b??timets dans la cit?? builtOrInHand = true
     */
    public Player getPlayerMaxBuildings(ArrayList<Player> gamePlayers,boolean builtOrInHand){//builtOrInHand true -> renvoi joueur qui a construit le plus et sinon celui qui a le plus de carte en main
        if(gamePlayers.size()==1)return gamePlayers.get(0);
        Player maxPlayer;
        if(gamePlayers.size()>1){
            if(gamePlayers.get(0).getCharacter().getPriority() != this.character.getPriority()){// faut v??rifier que le joueur qu'on va retourner n'est pas le joueur lui-m??me
                maxPlayer = gamePlayers.get(0);
            }
            else maxPlayer = gamePlayers.get(1);
            for(Player player : gamePlayers){
                if (builtOrInHand && player.getCity().getNbrBuildings()>maxPlayer.getCity().getNbrBuildings() && player.getCharacter().getPriority() != this.character.getPriority()){
                    maxPlayer = player;
                }
                else if(!builtOrInHand && player.getNbOfbuildingsInHisHand()>maxPlayer.getNbOfbuildingsInHisHand() && player.getCharacter().getPriority() != this.character.getPriority()){
                    maxPlayer = player;
                }
            }
            if(maxPlayer.getCharacter().getPriority() != this.getCharacter().getPriority())return maxPlayer;
        }
        return null;
    }
    /**
     * @author DOMINGUEZ Lucas
     * @return true si tous les building qui sont dans sa main sont d??j?? dans sa cit?? false sinon
     */
    public boolean imposableBuildings(){
        boolean res =true;
        for(BaseBuildings building : getBuildingsOfThisPlayer()){
            if (!getCity().isBuild(building)) res = false;
        }
        return res;
    }
    public int getNbOfbuildingsInHisHand(){return this.buildings.size();}

    /**
     * @author FRANCIS Anas
     * @return tous les joueurs sauf le joueur de priority pass?? en parametre
     * @param priority un entier d??finissant la priorit?? du personnage(voleur, Roi, Magicien, etc.)
     */
    public ArrayList<Player> playersButWithout(int priority) {
        ArrayList<Player> playersToReturn = new ArrayList<>();
        for (Player player: gamePlayers) {
            if(player.getCharacter().getPriority() != priority)playersToReturn.add(player);
        }
        return playersToReturn;
    }

    public int getNbOfbuildingsInCity() {
        return this.getCity().getNbrBuildings();
    }
}

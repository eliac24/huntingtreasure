/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private String treasure;
    private boolean toughTown;
    private boolean alreadyDug;
    private boolean searched;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";
        treasure = getNewTreasure();
        alreadyDug = false;

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        searched = false;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    public String getTreasure(){  return treasure; }

    public boolean getSearched() { return searched; }

    public void setSearched() { searched = true;}

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown(boolean easy) {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (!easy){
                if (checkItemBreak()) {
                    hunter.removeItemFromKit(item);
                    printMessage += "\nUnfortunately, you lost your " + item  ;
                }
            }
            return true;
        }
        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble(boolean easy) {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else if (easy) {
            noTroubleChance = 0.1;
        }else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        }
        else {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance ) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW+goldDiff +Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            }
            else if (hunter.hasItemInKit("sword")){
                printMessage += "the brawler, seeing your sword, realizes they picked a losing fight and gives you their gold";

            }else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(-goldDiff);
            }
        }
    }


    public void digGold(){
       double chance = Math.random() ;

       if(alreadyDug){
           printMessage = "\nYou already dug in this town";
       }
       else if(chance > .49 && hunter.hasItemInKit("shovel")){
           int goldReward = (int) (Math.random() * 20) + 1;
           hunter.changeGold(goldReward);
          printMessage = "\nYou found " + goldReward + " gold";
          alreadyDug = true;
       }
       else if(chance < .49 && hunter.hasItemInKit("shovel")){
          printMessage ="\nYou dug but only found dirt";
          alreadyDug = true;
       }
       else {
           printMessage = "\nYou cannot dig without a shovel";
       }
    }



    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random() * 6;
        if (rnd < 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 5){
            return new Terrain("Jungle", "Machete");
        }else{
            return new Terrain("Marsh", "Boots");
        }
    }

    private String getNewTreasure(){
        double rnd = Math.random() * 4;
        if (rnd < 1){
            return "crown";
        }else if (rnd < 2){
            return "trophy";
        }else if (rnd < 3){
            return "gem";
        }else{
            return "dust";
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }


}
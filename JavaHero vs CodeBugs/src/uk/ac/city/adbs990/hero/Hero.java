package uk.ac.city.adbs990.hero;

import city.cs.engine.*;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.tools.DelayedDestroy;
import uk.ac.city.adbs990.tools.DynamicImages;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Hero extends Walker {
    private int maxHealth = 10;
    private int currentHealth = maxHealth;
    private int strength = 1;
    private int coins;                      //number of coins collected at current level
    private int bombNumber = 0;             //number of bombs hero can plant
    private String direction;
    private BodyImage image;
    private boolean lookingLeft = false;     //initially looks right
    private boolean useHand = false;      //can shoot Hand, initially false
    private static int totalCoins;          //number of coins collected during whole game
                                            //(updated after completing the level)
    private static SoundClip heroHurt;

    private World world;

    /**
     * Return world the Hero is in
     * @return world the Hero is in
     */
    public World getWorld() {
        return world;
    }

/* shapes */
    //looks left
    private final Shape heroLeftShape = new PolygonShape(
            0.68f, 1.54f,
            0.96f, 1.01f,
            1.45f, -1.7f,
            -0.39f, -1.76f,
            -0.67f, -1.64f,
            -0.95f, 0.75f,
            -0.63f, 1.21f,
            0.09f, 1.68f);

    //looks right
    private final Shape heroRightShape = new PolygonShape(
            -0.68f, 1.54f,
            -0.96f, 1.01f,
            -1.45f, -1.7f,
            0.39f, -1.76f,
            0.67f, -1.64f,
            0.95f, 0.75f,
            0.63f, 1.21f,
            -0.09f, 1.68f);

    /**
     * Creates a dynamic body - Hero
     *
     * @param world World the body is created in
     */
    public Hero(World world) {
        super(world);
        this.world = world;

        setBodyImage("super-hero-right");
        SolidFixture heroRight = new SolidFixture(this, heroRightShape);
        addImage(image);
    }

/* shape and image utilities */
    private Shape getHeroLeftShape() {
        return heroLeftShape;
    }
    private Shape getHeroRightShape() {
        return heroRightShape;
    }

    /**
     * Return Hero's direction
     * @return Hero's direction
     */
    public String getDirection() {return direction;}

    /**
     * Set Hero's BodyImage
     * @param imagePath Hero's parameters (eg. Hero-left-harm)
     */
    public void setBodyImage(String imagePath){
        image = new BodyImage("data/images/HeroImages/" + imagePath + ".png", 4f);
        this.removeAllImages();
        addImage(image);
    }

    /**
     * Checks which way the Hero is looking
     * @return true - left, false - right
     */
    public boolean isLookingLeft() {
        return lookingLeft;
    }
    /**
     * Updates which way the Hero is looking
     * @param isLookingLeft true - left, false - right
     */
    public void updateIsLookingLeft(boolean isLookingLeft) {
        this.lookingLeft = isLookingLeft;

        //destroy Hero's previous shape
        this.getFixtureList().get(0).destroy();

        if (isLookingLeft) {
            //add new Fixture (Shape) for the Hero
            SolidFixture heroLeft = new SolidFixture(this, this.getHeroLeftShape());
            //set hero direction (String)
            direction = "left";

        } else {
            //add new Fixture (Shape) for the Hero
            SolidFixture heroRight = new SolidFixture(this, this.getHeroRightShape());
            //set hero direction (String)
            direction = "right";
        }

        this.setBodyImage("super-hero-" + direction);
    }

/* health utilities */
    //max health
    /**
     * Get Hero's maximum health (full health)
     * @return Hero's maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    /**
     * Sets Hero's maximum health (full health)
     * @param maxHealth  Hero's maximum health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    //current health
    /**
     * Get Hero's current health
     * @return Hero's current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Set Hero's current health
     * @param currentHealth New current health
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Respond to Hero getting damaged
     * @param damage Number of health Hero loses
     */
    public void loseHealth(int damage){
        //play the sound of taking damage
        heroHurt.play();
        
        if (currentHealth - damage > 0) {
            //update health
            currentHealth = currentHealth - damage;
        } else {
            currentHealth = 0;

            //rotate the hero to lie on the back
            if (isLookingLeft()) {
                this.rotateDegrees(-90);
            } else {
                this.rotateDegrees(90);
            }
            
            ((GameLevel)world).getGame().switchPanels(2);
        }

    //determine which way the hero is facing to apply hurting image
        if (isLookingLeft()) {
            this.direction = "left";
        } else {
            this.direction = "right";
        }

    // Hero changes body image from not-harm to harm few times
        DynamicImages heroHarm = new DynamicImages(this);
        //start changing hero's image
        Game.getLevel().addStepListener(heroHarm);
        //stop changing hero's image after 2 seconds
        Game.getLevel().addStepListener(new DelayedDestroy(heroHarm, 1));
    }

    /**
     * Increases Hero's health by 1 point
     */
    public void gainHealth(){
        if (currentHealth < maxHealth) {
            currentHealth++;
        }
    }

/* strength utilities */

    /**
     * Get Hero's strength
     * @return Hero's strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Set Hero's strength
     * @param strength New Hero's strength
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

/* coin utilities */
    // coins

    /**
     * Get number of coins Hero has collected in current level
     * @return Coin number
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Set number of coins Hero has collected in current level
     * @param coins Coin number
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Increment coin number by 1
     */
    public void addCoin(){
        coins++;
    }

    //total coins

    /**
     * Get number of coins Hero has collected in previous levels
     * @return Coin number
     */
    public static int getTotalCoins() {
        return totalCoins;
    }

    /**
     * Set number of coins Hero has collected in previous levels
     * @param totalCoins Coin number
     */
    public static void setTotalCoins(int totalCoins) {
        Hero.totalCoins = totalCoins;
    }

    /**
     * Add coins from current level to total number of coins
     *
     * <p>
     *     This method runs when Hero finishes a level
     * </p>
     * @param collectedCoins
     */
    public static void updateTotalCoins(int collectedCoins) {
        totalCoins += collectedCoins;
    }

/* shooting Hand utilities */

    /**
     * Determine whether Hero can shoot Hand
     */
    public boolean canUseHand() {
        return useHand;
    }

    /**
     * Determine whether Hero can shoot Hand
     */
    public void setUseHand(boolean useHand) {
        this.useHand = useHand;
    }

/* planting bomb utilities */

    /**
     * Return number of bombs Hero can create
     * @return Bomb number
     */
    public int getBombNumber() {
        return bombNumber;
    }

    /**
     * Sets number of bombs Hero has
     * @param bombNumber New bomb number
     */
    public void setBombNumber(int bombNumber) {
        this.bombNumber = bombNumber;
    }

    /**
     * Increases/decreases the bomb number Hero has
     * @param change add/remove number of bombs
     */
    public void changeBombNumber(int change) {
        bombNumber += change;
    }

/* sound */
    static {
        try {
            heroHurt = new SoundClip("data/sounds/heroHurt.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add heroHurt to global sound list
        Game.getSoundList().add(heroHurt);
    }
}

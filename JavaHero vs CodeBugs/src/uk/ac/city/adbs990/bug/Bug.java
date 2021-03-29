package uk.ac.city.adbs990.bug;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Bug extends Walker {
    private static ArrayList<Bug> bugList = new ArrayList<Bug>();   //array list containing all bugs
    private String direction;
    private int strength;
    private Vec2 startPosition;
    private static SoundClip bugDestroy;

    /**
     * Returns list of all Bugs
     */
    public static ArrayList<Bug> getBugList() {
        return bugList;
    }

    /**
     * Add bug to a bug list
     *
     * @param bug Bug that is added to the list
     */
    public static void addToBugList(Bug bug){
        bugList.add(bug);
    }

    /**
     * Creates a dynamic body - Bug
     *
     * @param world World the body is created in
     * @param strength Bug's strength (it also determines its health and BodyImage)
     * @param side Side the Bug is looking when created (0 - left, 1 - right)
     * @param bugLeftShape Shape of the Bug looking left
     * @param bugRightShape Shape of the Bug looking right
     */
    public Bug(World world, int strength, int side,
               Shape bugLeftShape, Shape bugRightShape) {
        super(world);
        addToBugList(this);
        this.strength = strength;

        //if side is not 1 or 0, generate a random direction (0, 1)
        if (side != 0 && side != 1){
            side = new Random().nextInt(2);
        }

        //set bug's looking direction and add its Shape
        if (side == 0) {
            direction = "left";
            SolidFixture bugLeft = new SolidFixture(this, bugLeftShape);
        } else  {
            direction = "right";
            SolidFixture bugRight = new SolidFixture(this, bugRightShape);
        }

        //add collision listener
        BugCollisions bugCollisions = new BugCollisions(this);
        this.addCollisionListener(bugCollisions);

        //add destruction listener
        BugDestruction bugDestruction = new BugDestruction();
        this.addDestructionListener(bugDestruction);
    }

/* Image */
    //Bug's image is based on its strength and direction

    /**
     * Sets Bug's looking direction
     * @param direction Bug's direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Returns Bug's looking direction
     * @return Bug's looking direction
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Sets Bug's body image
     * @param imagePath Bug's type (eg. bug3-left)
     */
    public void setBodyImage(String imagePath) {
        BodyImage image = new BodyImage("data/images/BugsImages/" + imagePath + ".png", 4f);
        setBodyImage(image);
    }

    /**
     * Sets Bug's body image
     * @param image
     */
    public void setBodyImage(BodyImage image){
        this.removeAllImages();
        this.addImage(image);
    }

/* strength / health */
    //bugs strength determines the amount of
    //hurt bug does to the Hero when they collide

    //it also influences the BugMinion's appearance
    //additionally it represents its health
    //when strength = 0, bug gets destroyed

    /**
     * @return strength of the Bug
     */
    public int getStrength() {
        return strength;
    }
    /**
     * Sets Bug's strength
     *
     * @param strength New strength of the Bug
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Abstract method determining actions of Bug losing health <br>
     * Health also determines Bug's strength
     *
     * @param lostHealth Health lost
     */
    public abstract void loseHealth(int lostHealth);

/* walking */
    /**
     * Returns the midpoint of Bug's movement
     * <p>
     *     Bug can move X horizontal distance from this point,
     *     after that it changes direction
     * </p>
     * @return midpoint of Bug's movement
     */
    public Vec2 getStartPosition() {
        return startPosition;
    }
    /**
     * Sets the midpoint of Bug's movement
     * <p>
     *     Bug can move X horizontal distance from this point,
     *     after that it changes direction
     * </p>
     * @param startPosition New midpoint of Bug's movement
     */
    public void setStartPosition(Vec2 startPosition) {
        this.startPosition = startPosition;
    }

/* sound */
    static {
        try {
            bugDestroy = new SoundClip("data/sounds/bugDestroy.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add bugDestroy to global sound list
        Game.getSoundList().add(bugDestroy);
    }

    /**
     * Play a sound before the Bug is destroyed
     */
    @Override
    public void destroy() {
        //play sound when bug is destroyed
        bugDestroy.play();
        //continue destruction process
        super.destroy();
    }
}

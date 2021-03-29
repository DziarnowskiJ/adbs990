package uk.ac.city.adbs990.bug;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.tools.DelayedDestroy;
import uk.ac.city.adbs990.tools.DynamicImages;

public class BugBoss extends Bug {
    private int health;
    private GameLevel level;

/* shapes */
    //looking right
    private static final Shape bugRightShape = new PolygonShape(
        3.52f,-1.6f,
        0.83f,-2.78f,
        -1.42f,-2.78f,
        -2.82f,-2.02f,
        -3.48f,-0.36f,
        -2.54f,2.5f,
        -0.72f,2.74f,
        2.01f,2.48f);

    //looking left
    private static final Shape bugLeftShape = new PolygonShape(
            -3.52f,-1.6f,
            -0.83f,-2.78f,
            1.42f,-2.78f,
            2.82f,-2.02f,
            3.48f,-0.36f,
            2.54f,2.5f,
            0.72f,2.74f,
            -2.01f,2.48f);

    /**
     * Creates a dynamic body - BugBoss
     *
     * @param world World the body is created in
     * @param strength Bug's strength
     * @param side Side the Bug is looking when created (0 - left, 1 - right)
     */
    public BugBoss(World world, int strength, int side) {
        super(world, strength, side,
                bugLeftShape, bugRightShape);
        level = (GameLevel)world;
        health = 20;

        //make bug fly
        setGravityScale(0);

        //set bug's image
        setBodyImage("bugBoss-" + getDirection());

        //start bug movement
        setLinearVelocity(new Vec2(2, 3));
    }

/* health */
    /**
     * Decreases BugBoss's health
     * <p>
     * If Health drops down to 0, Bug is destroyed
     * </p>
     *
     * @param lostHealth Health lost
     */
    public void loseHealth(int lostHealth) {
        health = health - lostHealth;

        // Bug changes body image from not-harm to harm few times
        DynamicImages bugHarm = new DynamicImages(this);
        //start changing bug's image
        Game.getLevel().addStepListener(bugHarm);
        //stop changing hero's image after 0.4 seconds
        Game.getLevel().addStepListener(new DelayedDestroy(bugHarm, 0.6f));

        if (health <= 0 ) {
            destroy();
        }
    }

/* health */

    /**
     * Returns BugBoss's health
     * @return BugBoss's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets BugBoss's health
     * @param health New health level
     */
    public void setHealth(int health) {
        this.health = health;
    }

/* image */

    /**
     * Set's BugBoss's BodyImage
     *
     * @param imagePath Bug's type (eg. bugBoss-left)
     */
    @Override
    public void setBodyImage(String imagePath) {
        BodyImage image = new BodyImage("data/images/BugsImages/" + imagePath + ".png", 6f);
        setBodyImage(image);
    }

    /**
     * Update BodyImage
     *
     * <p>
     *  Checks BugBoss's moving direction and adjusts its BodyImage
     * </p>
     */
    public void updateBodyImage() {
        if (getLinearVelocity().x > 0) {        //bug moves to the right
            setDirection("right");
        } else {                                //bug moves to the left
            setDirection("left");
        }
        setBodyImage("bugBoss-" + getDirection());
    }

/* movement */

    /**
     * Makes BugBoss move in the direction of Hero
     *
     * <p>
     * Calculates vector between BugBoss and Hero and sets it as BugBoss's linear movement
     * </p>
     */
    public void followHero() {
        //get current hero
        Hero hero = Game.getLevel().getHero();

        //get BugBoss position
        Vec2 bugVector = this.getPosition();
        //get Hero position
        Vec2 heroVector = hero.getPosition();
        //get direction between BugBoss and Hero
        Vec2 directionVector = heroVector.sub(bugVector);
        //change it to unit vector
        directionVector.normalize();

        //set BugBoss velocity to go in Hero's direction
        //increase speed to 3
        this.setLinearVelocity(directionVector.mul(3));
        updateBodyImage();
    }

}

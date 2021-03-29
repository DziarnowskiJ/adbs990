package uk.ac.city.adbs990.bug;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;

import java.util.Random;

public class BugMinion extends Bug {
    private float halfWalkingDistance;

/* shapes */
    //looking right
    private static final Shape bugRightShape = new PolygonShape(
            0.45f,1.42f,
            1.5f,-0.22f,
            0.87f,-1.93f,
            -1.16f,-1.93f,
            -1.56f,-0.5f,
            -0.85f,0.97f,
            -0.3f,1.42f,
            0.02f,1.54f);

    //looking left
    private static final Shape bugLeftShape = new PolygonShape(
            -0.45f,1.42f,
            -1.5f,-0.22f,
            -0.87f,-1.93f,
            1.16f,-1.93f,
            1.56f,-0.5f,
            0.85f,0.97f,
            0.3f,1.42f,
            -0.02f,1.54f);

    /**
     * Creates a dynamic body - BugMinion
     *
     * @param world World the body is created in
     * @param strength Bug's strength (int value between 1-7)
     * @param side Side the Bug is looking when created (0 - left, 1 - right)
     * @param halfWalkingDistance Distance Bug can go from its midpoint movement position
     */
    public BugMinion(World world, int strength, int side, float halfWalkingDistance) {
        //constructor
        super(world, checkStrength(strength), side,
                bugLeftShape, bugRightShape);
        //set body image
        super.setBodyImage("bug" + getStrength() + "-" + getDirection());

        //set half walking distance
        setHalfWalkingDistance(halfWalkingDistance);

        //make bug walk in proper direction
        if (side == 0) {
            startWalking(-3);
        } else {
            startWalking(3);
        }
    }

    /**
     * Decreases BugMinion's health
     * <p>
     * If Health drops down to 0, Bug is destroyed
     * Health also determines Bug's strength and BodyImage
     * </p>
     *
     * @param lostHealth Health lost
     */
    public void loseHealth(int lostHealth) {
        if (getStrength() - lostHealth > 0) {
            setStrength(getStrength() - lostHealth);
            this.setBodyImage("bug" + getStrength() + "-" + getDirection());
        } else {
            this.destroy();
        }
    }

/* walking */

    /**
     * Get distance Bug can go from its midpoint movement position
     * @return distance Bug can go from its midpoint movement position
     */
    public float getHalfWalkingDistance(){
        return halfWalkingDistance;
    }
    /**
     * Set distance Bug can go from its midpoint movement position
     * @return New distance
     */
    public void setHalfWalkingDistance(float halfWalkingDistance) {
        this.halfWalkingDistance = halfWalkingDistance;
    }

    /**
     * Set walking speed of the BugMinion and adjust its BodyImage
     * <p>
     * If speed is negative Bug moves to the left and BodyImage is set to 'look' left <br>
     * If speed is positive Bug moves to the right and BodyImage is set to 'look' right
     * </p>
     * @param walkingSpeed
     */
    public void changeWalking(int walkingSpeed) {
        this.startWalking(walkingSpeed);

        if (walkingSpeed > 0) {
            //update looking side
            this.setDirection("right");
        } else if (walkingSpeed < 0) {
            //update looking side
            this.setDirection("left");
        }
        this.setBodyImage("bug" + getStrength() + "-" + getDirection());
    }

    //prevent BugMinion from having strength over 7 or below 1
    //by setting strength to random
    private static int checkStrength(int strength) {
        if (strength > 7 || strength < 1) {
            strength = new Random().nextInt(7) + 1;
        }
        return strength;
    }
}

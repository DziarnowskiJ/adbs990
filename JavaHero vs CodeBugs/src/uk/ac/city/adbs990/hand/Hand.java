package uk.ac.city.adbs990.hand;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Hand extends DynamicBody {
    private static Shape handShape;
    private static BodyImage image;
    private static Vec2 linearVelocity;

    private static SoundClip handShoot;

/* shapes */
    private static final Shape handLeftShape = new PolygonShape(
            -0.29f,0.3f,
            -0.58f,0.21f,
            -0.61f,0.0f,
            -0.45f,-0.16f,
            0.51f,0.03f);

    private static final Shape handRightShape = new PolygonShape(
            -0.29f,0.3f,
            -0.58f,0.21f,
            -0.61f,0.0f,
            -0.45f,-0.16f,
            0.51f,0.03f);

/* methods to set hand's image and velocity */

    /**
     * Sets Hand's parameters for hand moving left
     */
    public static void useHandLeftShape(){
        handShape = handLeftShape;
        image = new BodyImage("data/images/HeroImages/hand-left.png", 4);
        linearVelocity = new Vec2(-30, 0);
    }

    /**
     * Sets Hand's parameters for hand moving right
     */
    public static void useHandRightShape(){
        handShape = handRightShape;
        image = new BodyImage("data/images/HeroImages/hand-right.png", 4);
        linearVelocity = new Vec2(30, 0);
    }

/* constructor */

    /**
     * Creates dynamic body - Hand
     * @param world World the hand is created in
     */
    public Hand(World world) {
        super(world, handShape);
        addImage(image);
        setLinearVelocity(linearVelocity);
        setGravityScale(0);
        setBullet(true);

        playHandShoot();
    }

/* sound */
    private static void playHandShoot() {
        handShoot.play();
    }

    static {
        try {
            handShoot = new SoundClip("data/sounds/handShoot.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add handShoot to global sound list
        Game.getSoundList().add(handShoot);
    }
}

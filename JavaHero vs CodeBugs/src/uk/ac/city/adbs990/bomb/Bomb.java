package uk.ac.city.adbs990.bomb;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.tools.DelayedDestroy;
import uk.ac.city.adbs990.tools.DynamicImages;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Bomb extends Walker {
    private static SoundClip bombExplosion;

/* shape */
    private static final Shape bombShape = new PolygonShape(
            -0.233f,0.479f,
            -0.703f,0.304f,
            -0.718f,-0.148f,
            -0.553f,-0.39f,
            0.371f,-0.384f,
            0.611f,-0.181f,
            0.614f,0.267f,
            0.144f,0.472f);

/* images */
    //default image
    private static final BodyImage imageBomb =
            new BodyImage("data/images/landmine.png", 1.0f);

    //explosion image
    private static final BodyImage imageExplosion =
            new BodyImage("data/images/explosion.png", 1.0f);

    /**
     * Returns the BodyImage of the exploding Bomb
     *
     * @return image of explosion
     */
    public static BodyImage getImageExplosion() {
        return imageExplosion;
    }

    /**
     * <p>
     * Creates a dynamic body in a form of a bomb
     * that explodes when collides with a Bug
     * </p>
     *
     * @param  world World the body is created in
     * @param position Position of the newly created body
     */
    public Bomb(World world, Vec2 position) {
        super(world, bombShape);
        addImage(imageBomb);
        setPosition(position);

        //add collision listener
        BombCollisions bombCollisions = new BombCollisions(this);
        this.addCollisionListener(bombCollisions);
    }

    /**
     * Instructions of the explosion
     * <p>
     * When Bomb collides with a body of instance Bug it 'explodes':
     * The BodyImage is set to explosion image that gradually increases size
     *
     */
    public void explode() {
        //play explosion sound
        bombExplosion.play();

        //remove body from Bomb
        //so other bodies no longer can collide with it
        getFixtureList().get(0).destroy();

        //set velocity to 0
        //explosion will not move after collision
        setLinearVelocity(new Vec2(0, 0));

        //make body not fall down
        setGravityScale(0);

        //make the explosion gradually increase
        this.getWorld().addStepListener(new DynamicImages(this));

        //remove bomb after 0.5 seconds
        this.getWorld().addStepListener(
                new DelayedDestroy(this, 0.5f));
    }

/* sound */
    static {
        try {
            bombExplosion = new SoundClip("data/sounds/bombExplosion.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add bombExplosion to global sound list
        Game.getSoundList().add(bombExplosion);
    }
}

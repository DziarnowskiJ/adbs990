package uk.ac.city.adbs990.worldElements.collectables;

import city.cs.engine.*;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.tools.DynamicImages;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public abstract class Collectables extends StaticBody {
    private static final Shape bodyShape = new CircleShape(0.75f);
    private static SoundClip objectCollection;

    private World world;

    /**
     * Returns the Hero from current level
     * @return Hero
     */
    public Hero getHero() {
        return ((GameLevel)world).getHero();
    }

    private static BodyImage image;

    /**
     * Get body's BodyImage
     * @return BodyImage
     */
    public static BodyImage getImage() {
        return image;
    }

    /**
     * Create a Collectable object
     * @param world World the object is created in
     * @param image Image of the object
     */
    public Collectables(World world, BodyImage image) {
        super(world, bodyShape);
        this.image = image;
        this.world = world;
        addImage(image);

        //make image rotate
        world.addStepListener(new DynamicImages(this));
    }

    /**
     * Play a collect sound
     */
    public void collisionInstructions(){
        //play sound when object is collected
        objectCollection.play();
    }

/* sound */
    static {
        try {
            objectCollection = new SoundClip("data/sounds/objectCollection.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add objectCollection to global sound list
        Game.getSoundList().add(objectCollection);
    }
}
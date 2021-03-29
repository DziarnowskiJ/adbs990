package uk.ac.city.adbs990.worldElements;

import city.cs.engine.*;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.tools.DynamicImages;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Portal extends StaticBody {
    private static SoundClip portalNextLevel;

    private static final Shape holeShape = new PolygonShape(
            -1.66f,2.29f,
            -2.45f,0.13f,
            -1.54f,-1.98f,
            -0.03f,-2.34f,
            1.81f,-1.96f,
            2.53f,0.08f,
            1.71f,2.44f,
            0.29f,2.63f);

/* image */
    private static final BodyImage image = new BodyImage("data/images/hole.png", 6);

    /**
     * Get Portal's BodyImage
     * @return Portal's BodyImage
     */
    public static BodyImage getImage() {
        return image;
    }

    /**
     * Creates a Potal that allows Hero to go to next level
     * @param world World the Portal is created in
     */
    public Portal(World world) {
        super(world, holeShape);
        this.setPosition(((GameLevel)world).getPortalPosition());
        addImage(image);

        //make image rotate
        world.addStepListener(new DynamicImages(this));
    }

/* sound */

    /**
     * Plays sound
     */
    public static void playPortalNextLevel() {
        portalNextLevel.play();
    }

    static {
        try {
            portalNextLevel = new SoundClip("data/sounds/portalNextLevel.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        //add portalNextLevel to global sound list
        Game.getSoundList().add(portalNextLevel);
    }
}

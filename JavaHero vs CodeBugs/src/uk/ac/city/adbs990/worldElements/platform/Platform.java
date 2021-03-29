package uk.ac.city.adbs990.worldElements.platform;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.*;

public class Platform extends StaticBody {
    private Shape platformShape;

    //image of platform of width = 1 meter
    BodyImage image = new BodyImage("data/images/platformOptimised.png");

/* shape utilities */
    private void setPlatformShape(float width) {
        this.platformShape = new BoxShape(width, 0.5f);
    }
    private Shape getPlatformShape() {
        return platformShape;
    }

/* constructors */
    //constructor used in GameWorld to create multiple platforms with loop

    /**
     * Creates a platform with image as background
     * <p>
     * Platform's background is created of set of images looped to appear
     * one after another. As each image has the size of 1 unit, for best
     * visual effect set platform's width to be integer
     * </p>
     * @param world World the platform is created in
     * @param position Position of the platform
     * @param halfWidth Half off the width of the platform
     */
    public Platform(World world, Vec2 position, float halfWidth) {
        super(world);

        setPlatformShape(halfWidth);
        SolidFixture platformFixture = new SolidFixture(this, getPlatformShape());

        setPosition(position);
        //for the platform to be completely covered with image
        //Body image must be attached multiple times next to one another
        for (int i = (int) -halfWidth + 1; i < halfWidth; i += 2) {
            AttachedImage partPlatformImage = new AttachedImage(this, image, 1, 0, new Vec2(i, 0));
        }
        setAlwaysOutline(true);
        setLineColor(Color.BLACK);
    }

    //fast constructor
    /**
     * Fast constructor
     * <p>
     * Constructor requiring only world and position <br>
     * Used to create Special Platform
     * </p>
     * @param world World the platform is created in
     * @param position Position of the platform
     */
    public Platform(World world, Vec2 position) {
        super(world);
        setPosition(position);
    }
}

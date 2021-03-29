package uk.ac.city.adbs990.worldElements.platform;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.*;

public class SpecialPlatform extends Platform {
    private static final Shape specialPlatformShape = new BoxShape(3, 0.5f);

    /**
     * Creates Special Platform that disappears in few seconds after collision with Hero
     * @param world World the platform is created in
     * @param position Position of the platfrom
     */
    public SpecialPlatform(World world, Vec2 position) {
        super(world, position);
        SolidFixture specialPlatform = new SolidFixture(this, specialPlatformShape);
        setFillColor(Color.GREEN);
    }
}

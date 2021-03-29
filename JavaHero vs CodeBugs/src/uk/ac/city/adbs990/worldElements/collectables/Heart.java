package uk.ac.city.adbs990.worldElements.collectables;

import city.cs.engine.*;

public class Heart extends Collectables {

    private static final BodyImage image =
            new BodyImage("data/images/heart.png", 1.5f);

    /**
     * Creates Heart that increments Hero's health
     * @param world World the Heart is created in
     */
    public Heart(World world) {
        super(world, image);
    }

    @Override
    public void collisionInstructions() {
        getHero().gainHealth();
        super.collisionInstructions();
    }
}

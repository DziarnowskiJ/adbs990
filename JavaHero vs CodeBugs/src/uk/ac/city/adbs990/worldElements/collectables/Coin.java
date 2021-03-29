package uk.ac.city.adbs990.worldElements.collectables;

import city.cs.engine.*;

public class Coin extends Collectables {

    private static final BodyImage image =
            new BodyImage("data/images/coin.png", 1.5f);

    /**
     * Creates a coin that increments Hero's wealth by 1 when collected
     * @param world World the Coin is created in
     */
    public Coin(World world) {
        super(world, image);
    }

    @Override
    public void collisionInstructions() {
        getHero().addCoin();
        super.collisionInstructions();
    }
}
package uk.ac.city.adbs990.worldElements.collectables;

import city.cs.engine.BodyImage;
import city.cs.engine.World;

public class BombToken extends Collectables {

    private int bombNumber;

    /**
     * Get number of bombs Hero will gain after collecting this token
     * @return Gained bomb number
     */
    public int getBombNumber() {
        return bombNumber;
    }

    private static final BodyImage image =
            new BodyImage("data/images/bomb.png", 1.5f);

    /**
     * Creates a Bomb Token that increases number of Bombs Hero can plant
     * @param world World the BombToken is created in
     * @param bombNumber Number of bombs the token will give to Hero
     */
    public BombToken(World world, int bombNumber) {
        super(world, image);
        this.bombNumber = bombNumber;
    }

    @Override
    public void collisionInstructions(){
        getHero().changeBombNumber(bombNumber);
        super.collisionInstructions();
    }
}

package uk.ac.city.adbs990.bomb;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;

public class BombCollisions implements CollisionListener {
    private Bomb bomb;

    /**
     * Creates a collision listener for a body Bomb
     * @param bomb Listening body (Bomb)
     */
    public BombCollisions(Bomb bomb) {
        this.bomb = bomb;
    }

    /**
     * Determines the collision effect
     * <p>
     *     Depending on the body the bomb collides with there are few effects: <br>
     *     - BugMinion collision -> Bomb 'explodes' and both bodies are destroyed <br>
     *     - BugBoss collision -> Bomb 'explodes', Bug loses 5 points of health <br>
     *     - Hero -> Hero 'picks up' a bomb, bomb is destroyed
     * </p>
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody() instanceof BugMinion) {               //BugMinion encounter
            collisionEvent.getOtherBody().destroy();                            //destroy the Bug
            //explode - sets the image of the bomb to explosion
            //and continuously increases it
            bomb.explode();
            //remove the bomb from the world
            ((GameLevel) bomb.getWorld()).setBomb(null);
        } else if (collisionEvent.getOtherBody() instanceof BugBoss) {          //BugBoss encounter
            //make BugBoss lose 5 hearts                                        //BugBoss loses 5 points of health
            ((BugBoss) collisionEvent.getOtherBody()).loseHealth(5);
            //explode - sets the image of the bomb to explosion
            //and continuously increases it
            bomb.explode();
            //remove the bomb from the world
            ((GameLevel) bomb.getWorld()).setBomb(null);
        } else if (collisionEvent.getOtherBody() instanceof Hero) {             //Hero encounter
            //pickup the bomb - increase the bomb number by 1
            Game.getLevel().getHero().changeBombNumber(1);
            ((GameLevel)bomb.getWorld()).getBomb().destroy();                   //destroys the bomb
            //remove the bomb from the world
            ((GameLevel)bomb.getWorld()).setBomb(null);
        }
    }
}

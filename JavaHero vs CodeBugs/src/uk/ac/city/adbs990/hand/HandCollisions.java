package uk.ac.city.adbs990.hand;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bug.Bug;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;

public class HandCollisions implements CollisionListener {
    private Hand hand;

    /**
     * Creates Collision Listener for Hand
     * @param hand Listening body (Hand)
     */
    public HandCollisions(Hand hand) {
        this.hand = hand;
    }

    /**
     * Determines the collision effect
     * <p>
     *     Depending on the body the Hand collides with there are few effects: <br>
     *     - Bug collision -> Hand is destroyed, Bug loses health equal to Hero's strength <br>
     *     - Any other collision -> Hand is destroyed
     * </p>
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody() instanceof Bug) {                     //Bug encounter
            ((Bug) collisionEvent.getOtherBody()).loseHealth(Game.getLevel().getHero().getStrength());
            hand.destroy();
        } else if ( ! (collisionEvent.getOtherBody() instanceof Hero)) {        //NOT Hero encounter
            //hand is created at hero's position
            //so at the moment of its creation it naturally
            //collides with hero, this if-statement prevents
            //the hand from being destroyed at that moment

            //in case of encounter of anything BUT hero - destroy hand
            hand.destroy();
        }
    }
}

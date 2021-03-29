package uk.ac.city.adbs990.bug;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import uk.ac.city.adbs990.hand.Hand;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.worldElements.platform.SpecialPlatform;

public class BugCollisions implements CollisionListener {
    private Bug bug;

    /**
     * Creates a collision listener for a body Bug
     * @param bug Listening body (Bug)
     */
    public BugCollisions(Bug bug) {
        this.bug = bug;
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        /* collision with Hand is implemented in HandCollisions
         *  collisions with Bomb is implemented in BombCollisions
         */

        /**
         * Determines the collision effect
         * <p>
         *     Depending on the body the Bug collides with there are few effects: <br>
         *     - SpecialPlatform -> platform is destroyed <br>
         *     - Anything BUT Bomb, Hero or Hand -> BugMinion changes direction,
         *     BugBoss starts moving in Hero's direction <br>
         *     <br>
         *     Collisions with Bomb, Hand and Hero
         *     are implemented and described in their classes
         * </p>
         */
        if (collisionEvent.getOtherBody() instanceof SpecialPlatform) {         //encounter with special platform
            collisionEvent.getOtherBody().destroy();                            //destroys the platform
            if (bug instanceof BugBoss) {                //BugBoss instruction
                ((BugBoss)bug).followHero();             //sends BugBoss in Hero's direction
            }
        } else if (!(collisionEvent.getOtherBody() instanceof Hand)) {          //encounter with anything BUT hand
            if (bug instanceof BugMinion) {                             //BugMinion instruction
                                                                        //makes Bug change direction
                if (bug.getDirection().equals("right")) {       //bug was walking right
                    //make bug go left
                    ((BugMinion) bug).changeWalking(-3);
                } else {                                        //bug was walking left
                    //make bug go right
                    ((BugMinion) bug).changeWalking(3);
                }
            } else if (bug instanceof BugBoss) {                        //BugBoss instruction
                ((BugBoss)bug).followHero();                            //sends BugBoss in Hero's direction
            }
        }
    }
}
package uk.ac.city.adbs990.hero;

import city.cs.engine.Body;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.worldElements.Portal;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.worldElements.collectables.Collectables;
import uk.ac.city.adbs990.tools.DelayedDestroy;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.worldElements.platform.SpecialPlatform;


public class HeroCollisions implements CollisionListener {
    private Hero hero;
    private GameLevel level;
    private Game game;

    /**
     * Creates collision listener for Hero
     * @param hero Collision listener body (Hero)
     * @param level Level Hero is currently on
     */
    public HeroCollisions(Hero hero, GameLevel level, Game game) {
        this.hero = hero;
        this.level = level;
        this.game = game;
    }

    /**
     * Determines the collision effect
     * <p>
     *     Depending on the body the Hero collides with there are few effects: <br>
     *     - BugMinion collision -> Bug is destroyed, Hero loses health equal to Bug's strength <br>
     *     - BugBoss collision -> Hero loses health equal to Bug's strength and so does the Bug <br>
     *     - Collectables collision -> Specific instruction described in each Collectable subclass is invoked,
     *          Colleacteble body is destroyed <br>
     *     - Special Platform -> Starts a timer that destroys platform after few seconds <br>
     *     - Portal collision -> Moves Hero to another level
     * </p>
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        Body otherBody = collisionEvent.getOtherBody();

        if (otherBody instanceof BugMinion) {                       //BugMinion encounter
            //hero lose health base on Bug's strength
            hero.loseHealth(((BugMinion) otherBody).getStrength());
            otherBody.destroy();

        } else if (otherBody instanceof BugBoss) {                  //BugBoss encounter
            hero.loseHealth(((BugBoss) otherBody).getStrength());
            ((BugBoss) otherBody).loseHealth(hero.getStrength());

        } else if (otherBody instanceof Collectables) {             //Collectables encounter (Heart, Coin, BombToken)
            //each collectable has specific instruction
            // for collision implemented in their class
            ((Collectables) otherBody).collisionInstructions();
            otherBody.destroy();

        } else if (otherBody instanceof SpecialPlatform) {          //SpecialPlatform encounter
            //remove platform after 3 seconds
            Game.getLevel().addStepListener(
                    new DelayedDestroy(otherBody, 1.5f));

        } else if (otherBody instanceof Portal) {                   //Portal encounter

            //play portalNextLevel sound
            Portal.playPortalNextLevel();

            //go to next level
            game.goToNextLevel();

            //destroy hero
//            collisionEvent.getReportingBody().destroy();
        }
    }
}

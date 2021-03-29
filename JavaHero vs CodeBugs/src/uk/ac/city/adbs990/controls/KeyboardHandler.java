package uk.ac.city.adbs990.controls;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.hand.Hand;
import uk.ac.city.adbs990.hand.HandCollisions;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyboardHandler implements KeyListener {

    private static final float walkingSpeed = 5;
    private Hero hero;
    Game game;

    /**
     * Creates key listener
     *
     * @param game Current game
     * @param hero Hero that is controlled
     */
    public KeyboardHandler(Game game, Hero hero) {
        this.game = game;
        this.hero = hero;
    }

    /**
     * Sets new Hero that will be controlled by the Key Listener
     *
     * @param hero New Hero to control
     */
    public void updateHero(Hero hero) {
        this.hero = hero;
    }

    /**
     * Determines the action depending on the key pressed
     * <p>
     *     Depending on the key pressed there are few effects: <br>
     *     - ESCAPE -> Opens game menu <br>
     *     - A -> Hero moves left <br>
     *     - D -> Hero moves right <br>
     *     - W -> Hero jumps <br>
     *     - SPACE -> Hero shoots Hand <br>
     * </p>
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {                  //open menu (click ESC)
            Game game = ((GameLevel)hero.getWorld()).getGame();
            game.switchPanels(2);

        } else if (code == KeyEvent.VK_A) {                //walk to the left
            hero.updateIsLookingLeft(true);
            hero.startWalking(-walkingSpeed);

        } else if (code == KeyEvent.VK_D) {                 //walk to the right
            hero.updateIsLookingLeft(false);
            hero.startWalking(walkingSpeed);

        } else if (code == KeyEvent.VK_W) {                 //jump
            hero.jump(10f);

        } else if ((code == KeyEvent.VK_SPACE)              //shoot hand
            && (hero.canUseHand())) {
            //check what direction is hero facing
            if (hero.isLookingLeft()) {
                Hand.useHandLeftShape();
            } else {
                Hand.useHandRightShape();
            }
            //create hand and add it to the world
            Hand hand = new Hand(hero.getWorld());

            //add Hand collision listener
            HandCollisions handCollisions = new HandCollisions(hand);
            hand.addCollisionListener(handCollisions);

            //set hand's position on proper side of the hero
            if (hero.isLookingLeft()) {
                hand.setPosition(new Vec2(
                        hero.getPosition().x - 0.5f,   //x-value
                        hero.getPosition().y));           //y-value
            } else {
                hand.setPosition(new Vec2(
                        hero.getPosition().x + 0.5f,   //x-value
                        hero.getPosition().y));           //y-value
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A) {                        //stop walking left
            hero.stopWalking();
        } else if (code == KeyEvent.VK_D) {                 //stop walking right
            hero.stopWalking();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

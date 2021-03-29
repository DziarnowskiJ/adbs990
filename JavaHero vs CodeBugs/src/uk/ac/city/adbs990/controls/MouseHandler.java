package uk.ac.city.adbs990.controls;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.GameView;
import uk.ac.city.adbs990.bomb.Bomb;
import uk.ac.city.adbs990.bomb.BombCollisions;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.worldElements.platform.SpecialPlatform;
import uk.ac.city.adbs990.tools.DelayedDestroy;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {

    private GameLevel world;
    private GameView view;
    private Hero hero;

    /**
     * Creates Mouse Listener
     *
     * @param world World mouse operates
     * @param view  View of the worlds
     */
    public MouseHandler(GameLevel world, GameView view){
        this.world = world;
        this.view = view;
        hero = world.getHero();
    }

    /**
     * Sets the  world mouse operates in
     *
     * @param world New world the mouse operates in
     */
    public void updateMouseHandler(GameLevel world) {
        this.world = world;
        hero = world.getHero();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Determines the action depending on the mouse key pressed
     * <p>
     *     Depending on the key pressed there are two effects: <br>
     *     - LEFT -> Creates a SpecialPlatform at the cursor position <br>
     *     - RIGHT -> Creates a Bomb at the mouse position <br>
     * </p>
     * @param e MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int code = e.getButton();

        //get the mouse coordinates
        Point mousePoint = e.getPoint();
        //transform it to world coordinates
        Vec2 worldPoint = view.viewToWorld(mousePoint);

        if (code == MouseEvent.BUTTON1) {                   //left mouse button
            /* every LEFT mouse press creates
             * the special platform at the mouse position
             * it disappears after some time or faster after collision with hero.
             *
             * Creating new platform destroys the previous one
             */

            //remove the previous platform if exists
            if (world.getSpecialPlatform() != null) {
                world.getSpecialPlatform().destroy();
            }
            //create new special platform
            SpecialPlatform specialPlatform =
                    new SpecialPlatform(world, worldPoint);
            //add it to the world
            world.setSpecialPlatform(specialPlatform);
            //remove platform after 5 seconds
            world.addStepListener(
                    new DelayedDestroy(specialPlatform, 5));

        } else if (code == MouseEvent.BUTTON3) {            //right mouse button
            /** every RIGHT mouse press creates
             * the bomb at the mouse position
             * It destroys the Bugs and itself if they collide with it
             * When Hero steps on it bomb is destroyed
             *
             * Creating a new bomb removes the previous one
             */
            Vec2 heroVector = hero.getPosition();
            Vec2 distanceVector = heroVector.sub(worldPoint);

            if (hero.getBombNumber() > 0) {
                if (distanceVector.length() <= 5) {
                    //decrement number of bombs hero has
                    hero.changeBombNumber(-1);

                    //remove the previous bomb if exists
                    if (world.getBomb() != null) {
                        world.getBomb().destroy();
                        world.setBomb(null);

                        hero.changeBombNumber(1);
                    }
                    //create new bomb
                    Bomb bomb = new Bomb(world, worldPoint);
                    //add it to the world
                    world.setBomb(bomb);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

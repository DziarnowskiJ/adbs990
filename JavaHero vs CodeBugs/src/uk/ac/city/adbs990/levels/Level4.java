package uk.ac.city.adbs990.levels;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bug.Bug;
import uk.ac.city.adbs990.bug.BugBoss;
import uk.ac.city.adbs990.bug.BugCollisions;
import uk.ac.city.adbs990.bug.BugDestruction;

import javax.swing.*;
import java.awt.*;

public class Level4 extends GameLevel {

    private Image background = new ImageIcon("data/images/Backgrounds/Game/background4.jpg").getImage();

    private static Vec2 dimensions = new Vec2(30, 15);
    private final Vec2 portalPosition = new Vec2(0 , 0);

    /**
     * Create level with platforms
     * @param game Game
     */
    public Level4(Game game) {
    /* create level template */
        super(dimensions, game);
    }

    @Override
    public void populate() {
        super.populate();

        /* hero */
        //set Hero's position
        getHero().setPosition(new Vec2(0, -5));
        //let Hero shoot hand
        getHero().setUseHand(true);

        /* BugBoss */
        BugBoss bugBoss = new BugBoss(this,  5, 1);

        //set bug's start position
        bugBoss.setStartPosition(new Vec2(0, 0));
        bugBoss.setPosition(new Vec2(0, 0));
    }

    @Override
    public String getLevelName() {
        return "Level4";
    }

    @Override
    public Image getBackground() {
        return background;
    }

    @Override
    public Vec2 getPortalPosition() {
        return portalPosition;
    }

    @Override
    public boolean isComplete() {
        if (Bug.getBugList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}

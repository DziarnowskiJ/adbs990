package uk.ac.city.adbs990.levels;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.tools.DynamicImages;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bug.Bug;
import uk.ac.city.adbs990.bug.BugCollisions;
import uk.ac.city.adbs990.bug.BugDestruction;
import uk.ac.city.adbs990.worldElements.collectables.Coin;
import uk.ac.city.adbs990.worldElements.collectables.Heart;
import uk.ac.city.adbs990.worldElements.platform.Platform;

import javax.swing.*;
import java.awt.*;

public class Level3 extends GameLevel {

    private Image background = new ImageIcon("data/images/Backgrounds/Game/background3.jpg").getImage();

    private static Vec2 dimensions = new Vec2(60, 15);
    private final Vec2 portalPosition = new Vec2(0, 0);

    /**
     * Create level with platforms
     * @param game Game
     */
    public Level3(Game game) {
    /* create level template */
        super(dimensions, game);

    /* platforms */
        //array with platforms' information
        float[][] platformLocations = {
                {6, -2, 4},                 //{x-value, y-value, platform half width}
                {15, 8, 5},
                {-13, 5, 3},
                {20, -5, 4},
                {-6, 0, 3},
                {-19, -5, 8},
                {-5, -10, 3}
        };

        //loop to create platform
        for (float[] platformLocation : platformLocations) {
            Platform platform = new Platform(this,
                    new Vec2(platformLocation[0],        //x-position
                    platformLocation[1]),                //y-position
                    platformLocation[2]);                //half width
        }
    }

    @Override
    public void populate() {
        super.populate();

        /* hero */
        //set hero's position
        getHero().setPosition(new Vec2(0, -12));
        //let hero shoot hand
        getHero().setUseHand(true);

        /* bugs */
        //clear BugList from previous level
        Bug.getBugList().clear();

        //array with bugs' information
        int[][] bugsData = {
                {-12, 8, 6, 1, 1},      //{x-value, y-value, strength(1-7), side(0/1), halfWalkingDistance}
                {-19, -2, 7, 1, 6},     //SIDE: 0-left, 1-right
                {-6, 2, 2, 0, 2},
                {25, -12, 4, 1, 2},
                {15, 10, 2, 0, 0},
                {6, 1, 3, 0, 3},
                {-9, -3, 1, 1, 6},
                {15, -10, 10, 2, 7},      //<-- for test as strength > 7 and side != 0 or 1
                {12, 10, 5, 1, 0}};       // it will set random strength and side (check Bug class)

        //loop to create bugs
        for (int[] bugsDatum : bugsData) {
            //create a new bug and add it to the world
            BugMinion bug = new BugMinion(this, bugsDatum[2], bugsDatum[3], bugsDatum[4]);

            //set bug's start position
            bug.setStartPosition(new Vec2(bugsDatum[0], bugsDatum[1]));
            bug.setPosition(new Vec2(bugsDatum[0], bugsDatum[1]));
        }

        /* coins */
        //array with coins' information
        float[][] coinLocations = {
                {1, 8},                 //{x-value, y-value}
                {5, -6},
                {-8, 7},
                {-10, -12},
                {-13, -2}};

        //loop to create coins
        for (float[] coinLocation : coinLocations) {
            //create new coin and add it to the world
            Coin coin = new Coin(this);
            //set coin's position
            coin.setPosition(new Vec2(coinLocation[0], coinLocation[1]));
            //make it rotate
            this.addStepListener(new DynamicImages(coin));
        }

        /* hearts */
        Heart heart1 = new Heart(this);
        heart1.setPosition(new Vec2(3, -6));
        //make the image rotate
        this.addStepListener(new DynamicImages(heart1));

        Heart heart2 = new Heart(this);
        heart2.setPosition(new Vec2(-9, 10));
        //make the image rotate
        this.addStepListener(new DynamicImages(heart2));
    }

    @Override
    public String getLevelName() {
        return "Level3";
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

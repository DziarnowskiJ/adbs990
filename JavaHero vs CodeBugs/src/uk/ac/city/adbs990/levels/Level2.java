package uk.ac.city.adbs990.levels;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.worldElements.collectables.BombToken;
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

public class Level2 extends GameLevel {

    private Image background = new ImageIcon("data/images/Backgrounds/Game/background2.jpg").getImage();

    private static Vec2 dimensions = new Vec2(60, 15);
    private Vec2 portalPosition = new Vec2(0 ,-5);

   /**
    * Create level with platforms
    * @param game Game
    */
    public Level2(Game game) {
    /* create level template */
        super(dimensions, game);

    /* platforms */
        //array with platforms' information
        float[][] platformLocations = {
                {-20, -7, 10},                 //{x-value, y-value, platform half width}
                {0, -10, 5},
                {-10, -1, 5},
                {-25, 4, 5},
                {1, 2, 1},
                {7, 5, 2},
                {15, 0, 5},
                {25, 8, 5},
                {27, -7, 3}
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
        getHero().setPosition(new Vec2(-27, -12));

        /* bugs */
        //clear BugList from previous level
        Bug.getBugList().clear();

        //array with bugs' information
        int[][] bugsData = {
                {-20, -4, 7, 1, 10},      //{x-value, y-value, strength(1-7), side(0/1), halfWalkingDistance}
                {-25, 7, 5, 1, 5},     //SIDE: 0-left, 1-right
                {-15, -13, 2, 1, 9},
                {0, -8, 3, 0, 3},
                {10, -10, 4, 1, 12},
                {25, -12, 5, 0, 15},
                {-10, 2, 3, 1, 3},
                {7, 7, 6, 0, 0},
                {16, 5, 6, 1, 4},
                {28, -5, 2, 0, 3},
                {26, 11, 7, 1, 4},
        };

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
                {0, 8},                 //{x-value, y-value}
                {-27, 0},
                {5, -6},
                {-10, -12},
                {-13, -4},
                {15, -5},
                {-25, 10},
                {15, 10},
        };

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
        heart1.setPosition(new Vec2(0, 10));
        //make the image rotate
        this.addStepListener(new DynamicImages(heart1));

        /* bomb tokens */
        BombToken bombToken1 = new BombToken(this, 7);
        bombToken1.setPosition(new Vec2(-25, -12));

        //make the image rotate
        this.addStepListener(new DynamicImages(bombToken1));

        BombToken bombToken2 = new BombToken(this, 4);
        bombToken2.setPosition(new Vec2(-25, 0));

        //make the image rotate
        this.addStepListener(new DynamicImages(bombToken2));
    }

    @Override
    public String getLevelName() {
        return "Level2";
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

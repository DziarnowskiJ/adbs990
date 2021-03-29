package uk.ac.city.adbs990.levels;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.worldElements.Portal;
import uk.ac.city.adbs990.bug.BugMinion;
import uk.ac.city.adbs990.tools.DynamicImages;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bug.BugCollisions;
import uk.ac.city.adbs990.bug.BugDestruction;
import uk.ac.city.adbs990.worldElements.collectables.Coin;
import uk.ac.city.adbs990.worldElements.collectables.Heart;
import uk.ac.city.adbs990.worldElements.platform.Platform;

public class Level1 extends GameLevel {

    private static Vec2 dimensions = new Vec2(80, 15);
    private final Vec2 portalPosition = new Vec2(35, 10);

    /**
     * Create level with platforms
     * @param game Game
     */
    public Level1(Game game) {
    /* create level template */
        super(dimensions, game);

    /* platforms */
        //array with platforms' information
        float[][] platformLocations = {
                {-20, -5, 20},          //{x-value, y-value, platform half width}
                {-5, -10, 5},
                {5, -5, 10},
                {15, -10, 10},
                {33, -5, 7},
                {20, 5, 20},
                {-35, 5, 5},
                {12.5f, 0, 3f},
                {2.5f, 0, 3f},
                {-15, 10, 5}
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

        /** hero */
        //set hero's position
        getHero().setPosition(new Vec2(-37, -10));

        /** bugs */
        //array with bugs' information
        int[][] bugsData = {
                {-20, -12, 2, 1, 13},       //{x-value, y-value, strength(1-7), side(0/1), halfWalkingDistance}
                {-5, -12, 6, 0, 0},         //SIDE: 0-left, 1-right
                {10, -7, 3, 1, 3},
                {33, -3, 7, 0, 6},
                {7, -2, 5, 0, 0},
                {-10, -2, 4, 1, 8},
                {-20, -2, 1, 0, 12},
                {-15, 13, 4, 0, 3},
                {38, -12, 7, 1, 0},
                {15, 8, 9, 2, 12}           //<-- for test as strength > 7 and side != 0 or 1
        };                                  // it will set random strength and side (check Bug class)

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
                {-9, -12},          //{x-value, y-value}
                {7, 8},
                {37, -7},
                {33, 3},
                {-35, 3},
                {-35, 10},
                {-10, 12},
                {-15, -3},
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

        /** hearts */
        Heart heart1 = new Heart(this);
        heart1.setPosition(new Vec2(-18, 12));
        //make the image rotate
        this.addStepListener(new DynamicImages(heart1));

        Heart heart2 = new Heart(this);
        heart2.setPosition(new Vec2(-9, 10));
        //make the image rotate
        this.addStepListener(new DynamicImages(heart2));

        /** portal */
        Portal portal = new Portal(this);

        //make the image rotate
        this.addStepListener(new DynamicImages(portal));

    }

    @Override
    public String getLevelName() {
        return "Level1";
    }

    @Override
    public Vec2 getPortalPosition() {
        return portalPosition;
    }

    @Override
    public boolean isComplete() {
        //this level doesn't require any additional actions
        //except getting to the Portal
        return true;
    }
}
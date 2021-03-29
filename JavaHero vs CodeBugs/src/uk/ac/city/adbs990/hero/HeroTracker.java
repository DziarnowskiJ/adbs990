package uk.ac.city.adbs990.hero;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.GameView;
import uk.ac.city.adbs990.levels.GameLevel;

public class HeroTracker implements StepListener {
    private GameView view;
    private Hero hero;
    private Vec2 dimensions;

    /**
     * Creates step listener allowing to center the view at Hero's position
     * @param view Current game view
     * @param level Current game level
     */
    public HeroTracker(GameView view, GameLevel level) {
        this.view = view;
        hero = level.getHero();
        this.dimensions = level.getDimensions();
    }

    public void preStep(StepEvent e) {

    }

    /**
     * Tracker changes the view to always see a hero
     *
     * <p>
     * Based on the location appropriately sets the center of the view <br>
     * so that the view never leaves the GameLevel border <br>
     *
     * ----------------------------------------------------------------------- <br><br>
     *
     * For BIG LEVEL: <br>
     * GameLevel is divided into 3 vertical sections
     * (left, right and middle) <br>
     * Each section is divided into another 3 smaller parts
     * (top, bottom and middle) <br><br>
     *
     * This creates 3x3 grid: <br>
     *
     * left-top,        middle=top,         right-top       <br>
     * left-middle,     middle-middle,      right-middle    <br>
     * left-bottom,     middle-bottom,      right-bottom    <br><br>
     *
     * Following if statement first determine in which section
     * the hero currently is (left, right or middle) <br>
     * And later in nested if determines the part
     * (top, bottom, middle) <br><br>
     *
     * ------------------------------------------------------------------------- <br><br>
     *
     * For VERTICAL and HORIZONTAL LEVELS: <br>
     * tracker works analogically to the BIG LEVEL
     * but divides view for only 3 parts: <br>
     *      vertical - top, middle, bottom <br>
     *      horizontal - left, middle, right <br><br>
     *
     * ------------------------------------------------------------------------- <br><br>
     *
     * For SMALL LEVEL: <br>
     * tracker sets center of the level
     * to the points 0, 0.
     *
     * </p>
     */
    public void postStep(StepEvent e) {
        /* big level (width > 15 & height > 15) */
        if (dimensions.x / 2 > 15 && dimensions.y > 15) {
            if (hero.getPosition().x < -dimensions.x / 2 + 15) {          //left
                if (hero.getPosition().y < -dimensions.y / 2 + 5) {             //top
                    view.setCentre(new Vec2(-dimensions.x / 2 + 15, dimensions.y / 2 - 15));
                } else if (hero.getPosition().y > dimensions.y / 2 - 5) {       //bottom
                    view.setCentre(new Vec2(-dimensions.x / 2 + 15, -dimensions.y / 2 + 15));
                } else {                                                        //middle
                    view.setCentre(new Vec2(-dimensions.x / 2 + 15, hero.getPosition().y));
                }

            } else if (hero.getPosition().x > dimensions.x / 2 - 15) {    //right
                if (hero.getPosition().y < -dimensions.y / 2 + 5) {             //top
                    view.setCentre(new Vec2(dimensions.x / 2 - 15, dimensions.y / 2 - 15));
                } else if (hero.getPosition().y > dimensions.y / 2 - 5) {       //bottom
                    view.setCentre(new Vec2(dimensions.x / 2 - 15, -dimensions.y / 2 + 15));
                } else {                                                        //middle
                    view.setCentre(new Vec2(dimensions.x / 2 - 15, hero.getPosition().y));
                }

            } else {                                                     //middle
                if (hero.getPosition().y < -dimensions.y / 2 + 5) {             //top
                    view.setCentre(new Vec2(hero.getPosition().x, dimensions.y / 2 - 15));
                } else if (hero.getPosition().y > dimensions.y / 2 - 5) {       //bottom
                    view.setCentre(new Vec2(hero.getPosition().x, -dimensions.y / 2 + 15));
                } else {                                                        //middle
                    view.setCentre(new Vec2(hero.getPosition().x, hero.getPosition().y));
                }
            }
        /* 'vertical level'  (width/2 <= 15 & height > 15) */
        } else if ((dimensions.x/2 <= 15) && !(dimensions.y <= 15)) {
            if (hero.getPosition().y > dimensions.y / 2 - 5) {              //top
                view.setCentre(new Vec2(0, -dimensions.y / 2 + 15));
            } else if (hero.getPosition().y < -dimensions.y / 2 + 5) {      //bottom
                view.setCentre(new Vec2(0, dimensions.y / 2 - 15));
            } else {                                                        //middle
                view.setCentre(new Vec2(0, hero.getPosition().y));
            }
        /* 'horizontal level'  (height <= 15 & width > 15) */
        } else if ((dimensions.y <= 15) && !(dimensions.x/2 <= 15)) {
            if (hero.getPosition().x < -dimensions.x / 2 + 15) {            //left
                view.setCentre(new Vec2(-dimensions.x / 2 + 15, 0));
            } else if (hero.getPosition().x > dimensions.x / 2 - 15) {      //right
                view.setCentre(new Vec2(dimensions.x / 2 - 15, 0));
            } else {                                                        //middle
                view.setCentre(new Vec2(hero.getPosition().x, 0));
            }
        /* any other case (small levels (width < 15 & height < 15)) */
        } else {
            view.setCentre(new Vec2(0, 0));
        }
    }
}

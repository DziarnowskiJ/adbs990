package uk.ac.city.adbs990.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.bomb.Bomb;
import uk.ac.city.adbs990.bug.Bug;
import uk.ac.city.adbs990.hand.Hand;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.hero.HeroCollisions;
import uk.ac.city.adbs990.worldElements.platform.SpecialPlatform;

import javax.swing.*;
import java.awt.*;

public abstract class GameLevel extends World {

    private Image background;

    /**
     * Set default background image
     * <p>
     * to change background image overwrite this method in LevelX class
     * </p>
     * @return Default background image
     */
    public Image getBackground() {
        return new ImageIcon("data/images/Backgrounds/Game/background1.jpg").getImage();
    };

    private Hero hero;
    public Hero getHero(){
        return hero;
    }
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    private SpecialPlatform specialPlatform;
    public SpecialPlatform getSpecialPlatform() {
        return specialPlatform;
    }
    public void setSpecialPlatform(SpecialPlatform specialPlatform) {
        this.specialPlatform = specialPlatform;
    }

    private Bomb bomb;
    public Bomb getBomb() {
        return bomb;
    }
    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    private Vec2 dimensions;
    public Vec2 getDimensions(){
        return dimensions;
    }

    private Game game;
    public Game getGame() {
        return game;
    }

    /**
     * Create game level template
     * @param dimensions Dimensions of the level
     * @param game Game
     */
    public GameLevel(Vec2 dimensions, Game game) {
        this.dimensions = dimensions;
        this.game = game;

        //make sure the list of bugs is empty
        //required for proper level transition with menu
        Bug.getBugList().clear();

    /* background */
        background = getBackground();

    /* ground and ceiling */
        Shape groundShape = new BoxShape(dimensions.x/2, 0.5f);
        //ground
        StaticBody ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0, -(dimensions.y - 0.5f)));
        //ceiling
        StaticBody ceiling = new StaticBody(this, groundShape);
        ceiling.setPosition(new Vec2(0, dimensions.y));

    /* walls */
        Shape wallShape = new BoxShape(0.5f, dimensions.y);
        //right wall
        StaticBody rightWall = new StaticBody(this, wallShape);
        rightWall.setPosition(new Vec2(dimensions.x/2,0f));
        //left wall
        StaticBody leftWall = new StaticBody(this, wallShape);
        leftWall.setPosition(new Vec2(-dimensions.x/2,0f));

    }

    /**
     * Add bodies to the level
     */
    public void populate() {
        /** hero */
        //create hero
        hero = new Hero(this);

        //collisions
        HeroCollisions heroCollisions = new HeroCollisions(hero, this, game);
        hero.addCollisionListener(heroCollisions);

        //initially hero is looking right
        hero.updateIsLookingLeft(false);

        //initially use right hand to shoot
        Hand.useHandRightShape();
    }

    /**
     * Get level name
     * @return Level Name
     */
    public abstract String getLevelName();

    /**
     * Get Portal position
     * @return Portal position
     */
    public abstract Vec2 getPortalPosition();

    /**
     * Check whether level is complete
     */
    public abstract boolean isComplete();

}

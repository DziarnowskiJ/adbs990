package uk.ac.city.adbs990;

import city.cs.engine.UserView;
import city.cs.engine.World;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;

import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {

    private final Image handImage = new ImageIcon("data/images/HeroImages/hand-up.png").getImage();
    private final Image bombImage = new ImageIcon("data/images/bomb.png").getImage();
    private final Image coinImage = new ImageIcon("data/images/coinEmpty.png").getImage();

    private GameLevel gameLevel;
    private Hero hero;

    /**
     * Creates View that displays the game
     * @param world World the view shows
     * @param width Width of the view
     * @param height Height of the view
     */
    public GameView(World world, int width, int height) {
        super(world, width, height);
        gameLevel = (GameLevel)world;
        hero = gameLevel.getHero();
    }

    /**
     * Sets world the view displays, additionally updates a Hero and GameLevel
     * @param world
     */
    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        this.gameLevel = (GameLevel)world;
        this.hero = gameLevel.getHero();
    }

    /**
     * Paint background
     */
    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(gameLevel.getBackground(), 0, 0, this);
    }

    /**
     * Paint foreground
     * <p>
     * Shows Hero's statistics like health, number of coins collected,
     * number of bombs Gero can plant and whether Hero can shoot hand
     * </p>
     * @param g
     */
    @Override
    protected void paintForeground(Graphics2D g) {
    /* draw health bar */
        //max health bar
        g.setColor(Color.WHITE);
        g.drawRect(380, 20, hero.getMaxHealth()*20, 20);

        //current health bar
        for (int i = 0; i < hero.getCurrentHealth(); i++) {
            //border
            g.setColor(Color.WHITE);
            g.drawRect(380 + i*20, 20, 20, 20);
            //fill
            g.setColor(Color.RED);
            g.fillRect(380 + i*20, 20, 20, 20);
        }

    /* show hand */
        //show hand only if hero can shoot
        if (hero.canUseHand()) {
            if (hero.getBombNumber() >= 1) {
                //if hero has bombs draw, hand next to the bomb
                g.drawImage(handImage, 465, 50, 40, 40, this);
            } else {
                //if hero doesn't have bombs, draw hand next to the coin
                g.drawImage(handImage, 510, 50, 40, 40, this);
            }
        }

    /* show number of bombs */
        //show this only if hero has at least 1 bomb
        if (hero.getBombNumber() > 0) {
            //draw bomb image
            g.drawImage(bombImage, 510, 50, 40, 40, this);
            //set font color and size
            g.setColor(Color.WHITE);
            g.setFont(new Font("Stencil", Font.BOLD, 20));
            //determine whether number of bombs has one digit or two
            //so the number is centralised
            if (hero.getBombNumber() < 10) {
                g.drawString("" + hero.getBombNumber(), 523, 82);
            } else {
                g.drawString("" + hero.getBombNumber(), 516, 82);
            }
        }

    /* show number of coins */
        //during each level hero collects coins that are added to coins field
        //when hero enters portal at the end of the level those coins are "approved"
        //and added to totalCoins field
        //at the beginning of each level coins field is erased
        int heroCoins = hero.getCoins() + Hero.getTotalCoins();
        //show this only if hero has at least 1 coin
        if (heroCoins > 0) {
            //draw coin image
            g.drawImage(coinImage, 550, 55, 40, 40, this);
            //set font color and size
            g.setColor(Color.BLACK);
            g.setFont(new Font("Stencil", Font.BOLD, 20));
            //determine whether number of coins has one digit or two
            //so the number is centralised
            if (heroCoins < 10) {
                g.drawString("" + (hero.getCoins() + Hero.getTotalCoins()), 565, 82);
            } else {
                g.drawString("" + (hero.getCoins() + Hero.getTotalCoins()), 560, 82);
            }
        }
    }
}

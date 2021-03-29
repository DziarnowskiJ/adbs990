package uk.ac.city.adbs990;

import city.cs.engine.SoundClip;

import uk.ac.city.adbs990.bug.BugTracker;
import uk.ac.city.adbs990.controls.KeyboardHandler;
import uk.ac.city.adbs990.controls.MouseHandler;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.hero.HeroTracker;
import uk.ac.city.adbs990.levels.*;
import uk.ac.city.adbs990.menu.*;
import uk.ac.city.adbs990.tools.GameTimer;
import uk.ac.city.adbs990.tools.GiveFocus;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

/* The World in which the bodies move and interact. */
    private static GameLevel level;

    /**
     * Get current game level
     *
     * @return Returns current Game Level
     */
    public static GameLevel getLevel() {
        return level;
    }

/* A graphical display of the world (a specialised JPanel). */
    private static GameView view;

    /**
     * Get current game view
     * @return Returns current game view
     */
    public static GameView getGameView(){
        return view;
    }

/* Debug view of the world */
    private static JFrame debugView;
    private final JFrame frame;
    private GameMenu mainMenu;
    private SaveMenu saveMenu;
    private AdvancedMenu advancedMenu;
    private InstructionMenu instructionMenu;
    private ScoreMenu scoreMenu;
    private FinalMenu finalMenu;

/* Controllers, handlers and trackers */
    private KeyboardHandler heroController;
    private MouseHandler mouseHandler;
    private HeroTracker heroTracker;

/* Sound */
    private static ArrayList<SoundClip> soundList = new ArrayList<SoundClip>();

    /**
     * Get global sound list
     *
     * @return Returns global sound list
     */
    public static ArrayList<SoundClip> getSoundList(){
        return soundList;
    }

    private static SoundClip gameMusic;

/* Game */
    /**
    *  Initialise a new Game
    */
    public Game() {

    /* add music */
        try {
            // Open an audio input stream
            gameMusic = new SoundClip("data/sounds/gameMusic.wav");
            // Set it to continuous playback (looping)
//            gameMusic.loop();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
        soundList.add(gameMusic);


    /* create the world */
        level = new Level1(this);
        level.populate();

    /* create a view */
        view = new GameView(level, 600, 600);

    /* controllers and listeners */
        //mouse
        mouseHandler = new MouseHandler(level, view);
        view.addMouseListener(mouseHandler);
        view.addMouseListener(new GiveFocus(view));

        //hero
        heroController = new KeyboardHandler(this, level.getHero());
        view.addKeyListener(heroController);

        //HeroTracker
        heroTracker = new HeroTracker(view, level);
        level.addStepListener(heroTracker);

        //BugTracker
        level.addStepListener(new BugTracker());

        //Game Timer
        level.addStepListener(new GameTimer());


    /* frame */
        // add the view to a frame (Java top level window)
        frame = new JFrame("Java Hero vs. Code Bugs");
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setMaximumSize(new Dimension(600, 600));

        //create all menu panels
        mainMenu = new GameMenu(this);
        scoreMenu = new ScoreMenu(this);
        saveMenu = new SaveMenu(this);
        advancedMenu = new AdvancedMenu(this);
        instructionMenu = new InstructionMenu(this);
        finalMenu = new FinalMenu(this);

        //start frame with main menu opened
        frame.add(mainMenu);

        // enable the frame to quit the application
        // when the x button is pressed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(false);
        // don't let the frame be resized
        frame.setResizable(false);
        // size the frame to fit the world view
        frame.pack();
        // make the frame visible
        frame.setVisible(true);

    /* VISUAL UTILITIES */
        // draw a 1-metre grid over the view
//         view.setGridResolution(1);
    }

    /**
     * Switches between game and menu panels
     * <p>
     * Depending on the input switches to a different panel: <br>
     *     - 0 -> Game view <br>
     *     - 1 -> Main menu <br>
     *     - 2 -> Score menu <br>
     *     - 3 -> Save menu <br>
     *     - 4 -> Advanced menu <br>
     *     - 5 -> Instruction menu
     *     - 6 -> Final menu
     * </p>
     * @param state Number of view to be opened
     */
    public void switchPanels(int state) {
        //stop the game
        getLevel().stop();

        //remove possible panels
        frame.remove(view);
        frame.remove(mainMenu);
        frame.remove(scoreMenu);
        frame.remove(saveMenu);
        frame.remove(advancedMenu);
        frame.remove(instructionMenu);
        frame.remove(finalMenu);

        GameTimer.stopTimer();

        /*
        Some of the menus have to be constructed
        each time they are opened, to
        properly update them and their components
         */

        switch (state) {
            //GAME
            case 1:
                frame.add(view);
                view.requestFocusInWindow();
                getLevel().start();
                GameTimer.startTimer();
                break;
            //MAIN MENU
            case 2:
                frame.add(mainMenu);
                break;
            //SAVE MENU
            case 3:
                saveMenu = new SaveMenu(this);
                frame.add(saveMenu);
                break;
            //SCORE MENU
            case 4:
                scoreMenu = new ScoreMenu(this);
                frame.add(scoreMenu);
                break;
            //ADVANCED MENU
            case 5:
                frame.add(advancedMenu);
                break;
            //INSTRUCTION MENU
            case 6:
                frame.add(instructionMenu);
                break;
            //FINAL MENU
            case 7:
                finalMenu = new FinalMenu(this);
                frame.add(finalMenu);
                break;
        }

        //make sure the frame was updated
        //and shows proper content
        frame.repaint();
        frame.revalidate();
        frame.pack();
    }

/* Changing level */

    /**
     * Natural game progression
     * <p>
     *  Method checks whether the level is comlete and if it is,
     *  Hero progresses to the next level
     * </p>
     */
    public void goToNextLevel(){
        if (level.isComplete()) {
            //add coins collected in this level to total coins
            Hero.updateTotalCoins(level.getHero().getCoins());
            if (level instanceof Level1) {
                goToLevel("Level2");
            } else if (level instanceof Level2) {
                goToLevel("Level3");
            } else if (level instanceof Level3) {
                goToLevel("Level4");
            } else if (level instanceof Level4) {
                switchPanels(7);
            }
        }
    }

    /**
     * Jump to level
     * <p>
     * Depending on the input string, go to specified level
     * </p>
     * @param levelName New Level
     */
    public void goToLevel(String levelName) {
        GameLevel level = null;
        switch(levelName) {
            case "Level1": level = new Level1(this);
                break;
            case "Level2": level = new Level2(this);
                break;
            case "Level3": level = new Level3(this);
                break;
            case "Level4": level = new Level4(this);
                break;
            default:
        }

        level.populate();
        setLevel(level);
    }

    /**
     * Update level's information
     * and move to selected level
     * @param newLevel New level
     */
    public void setLevel(GameLevel newLevel) {
        /* end previous level and overwrite it */
        //stop the current level
        level.stop();
        //create the new (appropriate) level
        //level now refers to new level
        level = newLevel;
        //change the view to look into new level
        view.setWorld(level);

        /* controllers and trackers */
        //change the controller to control the
        //hero in the new world
        heroController.updateHero(level.getHero());

        //change mouse handler to function in
        //the new world
        mouseHandler.updateMouseHandler(level);

        //change hero tracker to function in
        //the new world
        heroTracker = new HeroTracker(view, level);
        level.addStepListener(heroTracker);

        //add bug tracker (bugs will walk only as far as instructed)
        level.addStepListener(new BugTracker());

        //add Game Timer
        level.addStepListener(new GameTimer());

        /* utilities */
        //update debug view (used in Game constructor, section VISUAL UTILITIES)
        AdvancedMenu.updateDebugView(level);

        /* start */
        level.start();
    }


/** Run a new game */
    public static void main(String[] args) {

        new Game();

    }
}

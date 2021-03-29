package uk.ac.city.adbs990.menu;

import city.cs.engine.SoundClip;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.tools.GameTimer;
import uk.ac.city.adbs990.hero.Hero;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel {
    private Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground.jpg").getImage();

    private final JLabel title;
    private final JButton resumeButton;

    private final JButton startANewGameButton;
    private final JButton restartLevelButton;
    private final JButton scoreMenuButton;
    private final JButton saveMenuButton;
    private final JButton advancedMenuButton;
    private final JButton instructionMenuButton;
    private final JSlider volumeSlider;
    private final JLabel volumeLabel;
    private final JButton quitButton;

    private final JPanel volumePanel;


    /**
     * Creates menu with basic options and buttons to open other menus
     */
    public GameMenu(Game game) {
        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create components
        title = new JLabel();
        resumeButton = new JButton();
        startANewGameButton = new JButton();
        restartLevelButton = new JButton();
        scoreMenuButton = new JButton();
        saveMenuButton = new JButton();
        advancedMenuButton = new JButton();
        instructionMenuButton = new JButton();
        volumePanel = new JPanel();
        volumeLabel = new JLabel();
        volumeSlider = new JSlider();
        quitButton = new JButton();

        //add components to the menu
        add(Box.createVerticalGlue());
        this.add(title);
        add(Box.createVerticalGlue());
        this.add(resumeButton);
        add(Box.createVerticalGlue());

        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(startANewGameButton);
        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(restartLevelButton);
        add(Box.createVerticalGlue());
        this.add(scoreMenuButton);
        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(saveMenuButton);
        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(advancedMenuButton);
        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(instructionMenuButton);
        add(Box.createVerticalGlue());
        this.add(volumePanel);
        volumePanel.add(volumeLabel);
        this.add(volumeSlider);
        add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(quitButton);
        add(Box.createVerticalGlue());

    /* TITLE */
        /* title label */
        title.setText("JavaHero vs CodeBugs");
        title.setFont(new Font("Stencil", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

    /* RESUME GAME */
        /* resume game button */
        resumeButton.setText("Start game");

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //after click change text to "Resume level"
                //instead of "Start game"
                resumeButton.setText("Resume game");

                //reset level if hero died
                if (game.getLevel().getHero().getCurrentHealth() <= 0) {
                    //go to level 1
                    game.goToLevel("Level1");
                }
                //exit menu
                game.switchPanels(1);
            }
        });

    /* START NEW GAME / RESTART LEVEL */
        /* start new game button - go to level 1 */
        startANewGameButton.setText("Start a new game");

        GameTimer.resetTimer();

        startANewGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //reset coin number
                Hero.setTotalCoins(0);
                //go to level 1
                game.goToLevel("Level1");
                //exit menu
                game.switchPanels(1);
            }
        });

        /** restart level button */
        restartLevelButton.setText("Restart level");

        restartLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //set current level as new level
                String newLevel = Game.getLevel().getLevelName();
                game.goToLevel(newLevel);

                //exit menu
                game.switchPanels(1);
            }
        });

    /* MENU buttons */
        /* score menu button */
        scoreMenuButton.setText("Scores");

        scoreMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.switchPanels(4);
            }
        });

        /* save menu button */
        saveMenuButton.setText("Save menu");

        saveMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.switchPanels(3);
            }
        });

        /* advanced menu button */
        advancedMenuButton.setText("Advanced menu");

        advancedMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.switchPanels(5);
            }
        });

        /* instructions menu button */
        instructionMenuButton.setText("Instructions");

        instructionMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.switchPanels(6);
            }
        });

    /* VOLUME */

        /* volume panel */
        volumePanel.setMaximumSize(new Dimension(200, 20));

        /* volume label */
        volumeLabel.setText("Volume:");

        /* volume slider */
        volumeSlider.setMaximumSize(new Dimension(200, 20));

        //set minimum and maximum values of the slider
        volumeSlider.setMinimum(1);
        volumeSlider.setMaximum(200);

        //set ticks showing values of the slider
        volumeSlider.setMinorTickSpacing(10);
        volumeSlider.setMajorTickSpacing(50);
        //draw ticks
        volumeSlider.setPaintTicks(true);
        //change color of ticks to black
        volumeSlider.setForeground(Color.BLACK);

        //put slider's arrow in the middle
        volumeSlider.setValue(101);

        //make arrow stop only on ticks
        volumeSlider.setSnapToTicks(true);
        //paint slider's track
        volumeSlider.setPaintTrack(true);

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                /* slider is created in such way that
                 * it takes values between 1 and 200
                 * as setVolume() can be 0 < x <= 2
                 * value from slider is divided by 100
                 */
                double gameMusicVolume = source.getValue() / 100f;
                if (!source.getValueIsAdjusting()) {
                    //change volume of all sounds in the game (located on soundList)
                    for (SoundClip soundClip : Game.getSoundList()) {
                        soundClip.setVolume(gameMusicVolume);
                    }
                }
            }
        });

    /* QUIT */
        /* quit game button */
        quitButton.setText("Quit game");

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //exit game
                System.exit(0);
            }
        });

    /* for all components */
        for (Component component : this.getComponents()) {
            //align all components to the center
            ((JComponent)component).setAlignmentX(Component.CENTER_ALIGNMENT);
            //change background color for all components
            component.setBackground(new Color(255, 222, 222));
        }

    }

    /**
     * Paint background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // paint the background image
        g.drawImage(background, 0, 0, this);
    }
}

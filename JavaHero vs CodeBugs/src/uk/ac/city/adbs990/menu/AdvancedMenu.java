package uk.ac.city.adbs990.menu;

import city.cs.engine.DebugViewer;
import city.cs.engine.SoundClip;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.levels.GameLevel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvancedMenu extends JPanel {
    private Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground4.jpg").getImage();

    private static JFrame debugView;

    private final JLabel title;
    private final JButton openDebugViewButton;
    private final JComboBox<String> choseLevelDropdown;
    private final JButton backToMainMenuButton;

    /**
     * Creates menu with advanced options
     */
    public AdvancedMenu(Game game) {
        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create components
        title = new JLabel();
        openDebugViewButton = new JButton();
        choseLevelDropdown = new JComboBox<String>();
        backToMainMenuButton = new JButton();

        //add components to the menu
        add(Box.createVerticalGlue());
        this.add(title);

        add(Box.createVerticalGlue());
        this.add(openDebugViewButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(choseLevelDropdown);

        add(Box.createVerticalGlue());
        this.add(backToMainMenuButton);
        add(Box.createVerticalGlue());


        /* title label */
        title.setText("Advanced menu");
        title.setFont(new Font("Stencil", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        /* DEBUG VIEW */
        openDebugViewButton.setText("Open Debug View");

        openDebugViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (debugView == null) {
                    debugView = new DebugViewer(Game.getLevel(), 600, 600);
                    debugView.setLocation(650, 10);
                }
                //start game
                game.switchPanels(1);
            }
        });

        /* chose level box */
        choseLevelDropdown.addItem("Go to level:");
        choseLevelDropdown.addItem("Level 1");
        choseLevelDropdown.addItem("Level 2");
        choseLevelDropdown.addItem("Level 3");
        choseLevelDropdown.addItem("Level 4");

        choseLevelDropdown.setMaximumSize(new Dimension(200, 20));

        choseLevelDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();

                if (source.getSelectedIndex() != 0) {               //level has been chosen
                    //go to chosen level
                    game.goToLevel("Level" + source.getSelectedIndex());
                    //exit menu
                    game.switchPanels(1);
                }

                //make dropdown menu show 1st element on the list - "Go to level:"
                //instead of chosen level
                choseLevelDropdown.setSelectedIndex(0);
            }
        });

        /* BACK TO MENU */
        backToMainMenuButton.setText("Back to main menu");

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to main menu
                game.switchPanels(2);
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
     * Update Debug View when there is a transition to another level
     * @param level New level
     */
    public static void updateDebugView(GameLevel level) {
        if (debugView != null) {
            ((DebugViewer) debugView).setWorld(level);
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

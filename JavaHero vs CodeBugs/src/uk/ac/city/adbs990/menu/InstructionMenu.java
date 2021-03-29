package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructionMenu extends JPanel {
    private Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground5.jpg").getImage();

    private int infoNo;

    private final JLabel title;
    private final JTabbedPane instructionTabs;
    private final JPanel instructionContentPanel1;
    private final JPanel instructionContentPanel2;
    private final JPanel instructionContentPanel3;
    private final JPanel instructionContentPanel4;
    private final JPanel switchButtonsPanel;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JButton backToMainMenuButton;

    /**
     * Creates menu with game instructions
     */
    public InstructionMenu(Game game) {
        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create components
        title = new JLabel();
        instructionTabs = new JTabbedPane();
        switchButtonsPanel = new JPanel();
        previousButton = new JButton();
        nextButton = new JButton();
        backToMainMenuButton = new JButton();

        //instruction panels
        instructionContentPanel1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // paint the background image
                Image background = new ImageIcon(
                        "data/images/Backgrounds/Instruction/instruction1.png").getImage();
                g.drawImage(background, 0, 0, this);
            }
        };
        instructionContentPanel2 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // paint the background image
                Image background = new ImageIcon(
                        "data/images/Backgrounds/Instruction/instruction2.png").getImage();
                g.drawImage(background, 0, 0, this);
            }
        };
        instructionContentPanel3 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // paint the background image
                Image background = new ImageIcon(
                        "data/images/Backgrounds/Instruction/instruction3.png").getImage();
                g.drawImage(background, 0, 0, this);
            }
        };
        instructionContentPanel4 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // paint the background image
                Image background = new ImageIcon(
                        "data/images/Backgrounds/Instruction/instruction4.png").getImage();
                g.drawImage(background, 0, 0, this);
            }
        };

        //add components to the panel
        add(Box.createVerticalGlue());
        this.add(title);

        add(Box.createVerticalGlue());
        this.add(instructionTabs);
        instructionTabs.addTab("Objective", instructionContentPanel1);
        instructionTabs.addTab("Keyboard", instructionContentPanel2);
        instructionTabs.addTab("Mouse", instructionContentPanel3);
        instructionTabs.addTab("Points", instructionContentPanel4);

        add(Box.createVerticalGlue());
        this.add(switchButtonsPanel);
        switchButtonsPanel.add(previousButton);
        switchButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        switchButtonsPanel.add(nextButton);

        add(Box.createVerticalGlue());
        this.add(backToMainMenuButton);
        add(Box.createVerticalGlue());


    /* TITLE */
        /* title */
        title.setText("Game instructions");
        title.setFont(new Font("Stencil", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

    /* INSTRUCTION TAB */
        /* tabs */
        instructionTabs.setMaximumSize(new Dimension(500, 380));

        instructionTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane source = (JTabbedPane)e.getSource();

                infoNo = source.getSelectedIndex();
            }
        });

        /* instructions */
        instructionContentPanel1.add(Box.createRigidArea(new Dimension(500, 380)));
        instructionContentPanel2.add(Box.createRigidArea(new Dimension(500, 380)));
        instructionContentPanel3.add(Box.createRigidArea(new Dimension(500, 380)));
        instructionContentPanel4.add(Box.createRigidArea(new Dimension(500, 380)));

    /* SWITCH BUTTONS */
        /* previous button */
        previousButton.setText("Previous slide");
        previousButton.setBackground(new Color(255, 222, 222));

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (infoNo > 0) {
                    infoNo--;
                } else {
                    infoNo = instructionTabs.getTabCount()-1;
                }
                instructionTabs.setSelectedIndex(infoNo);
            }
        });

        /* next button */
        nextButton.setText("Next slide");
        nextButton.setBackground(new Color(255, 222, 222));

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (infoNo < instructionTabs.getTabCount()-1) {
                    infoNo++;
                } else {
                    infoNo = 0;
                }
                instructionTabs.setSelectedIndex(infoNo);
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

            if (component instanceof JPanel) {
                ((JComponent)component).setAlignmentX(component.CENTER_ALIGNMENT);
                component.setMaximumSize(new Dimension(240, 100));
                ((JPanel)component).setBorder(null);
                ((JPanel)component).setOpaque(false);
            } else {
                component.setBackground(new Color(255, 222, 222));
            }
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

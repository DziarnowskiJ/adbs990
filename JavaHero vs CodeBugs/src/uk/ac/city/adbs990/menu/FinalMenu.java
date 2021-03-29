package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.hero.Hero;
import uk.ac.city.adbs990.tools.GameSaveLoader;
import uk.ac.city.adbs990.levels.GameLevel;
import uk.ac.city.adbs990.tools.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FinalMenu extends JPanel {
    private Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground6.jpg").getImage();

    private final JLabel title;
    private final JPanel scorePanel;
    private final JLabel overallScore;
    private final JLabel coinScore;
    private final JLabel healthScore;
    private final JLabel timeScore;
    private final JPanel savePanel;
    private final JLabel saveLabel;
    private final JTextField saveAsTextField;
    private final JLabel saveOverwriteLabel;
    private final JComboBox<String> saveOverwriteBox;
    private final JLabel savedLabel1;
    private final JLabel savedLabel2;
    private final JButton backToMainMenuButton;

    private final String saveFolderPath = "data/saves";
    private final File saveFolder = new File("data/saves");

    /**
     * Creates menu Hero's statistics and option to save the score
     */
    public FinalMenu(Game game) {
        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create components
        title = new JLabel();

        scorePanel = new JPanel();
        overallScore = new JLabel();
        coinScore = new JLabel();
        healthScore = new JLabel();
        timeScore = new JLabel();

        savePanel = new JPanel();
        saveLabel = new JLabel();
        saveAsTextField = new JTextField();
        saveOverwriteLabel = new JLabel();
        saveOverwriteBox = new JComboBox<String>();
        savedLabel1 = new JLabel();
        savedLabel2 = new JLabel();

        backToMainMenuButton = new JButton();

        //add components to the menu
        add(Box.createVerticalGlue());
        this.add(title);

        add(Box.createVerticalGlue());
        this.add(scorePanel);
        scorePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        scorePanel.add(overallScore);
        scorePanel.add(coinScore);
        scorePanel.add(healthScore);
        scorePanel.add(timeScore);
        scorePanel.add(Box.createRigidArea(new Dimension(0, 5)));

        add(Box.createVerticalGlue());
        this.add(savePanel);
        savePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savePanel.add(saveOverwriteLabel);
        savePanel.add(saveOverwriteBox);
        savePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savePanel.add(saveLabel);
        savePanel.add(saveAsTextField);
        savePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savePanel.add(savedLabel1);
        savePanel.add(savedLabel2);
        savePanel.add(Box.createRigidArea(new Dimension(0, 5)));


        add(Box.createVerticalGlue());
        this.add(backToMainMenuButton);
        add(Box.createVerticalGlue());

    /* TITLE */
        /* title */
        title.setText("Congratulations! You won!");
        title.setFont(new Font("Stencil", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

    /* SCORE */
        Hero hero = Game.getLevel().getHero();

        overallScore.setText("You managed to score " +
                (Hero.getTotalCoins()*100 -
                  GameTimer.getTime()/20 -
                  (hero.getMaxHealth()-hero.getCurrentHealth())*30)
                + " points");
        coinScore.setText("You collected " + Hero.getTotalCoins() + " coins");
        healthScore.setText("You lost " + (hero.getMaxHealth()-hero.getCurrentHealth()) + " health");
        timeScore.setText("And you did it in " + GameTimer.getTime()/60 + " seconds");

        /* save overwrite label */
        saveOverwriteLabel.setText("Overwrite your save from the list:");

        /* save overwrite box */
        saveOverwriteBox.addItem("Overwrite save:");

        saveOverwriteBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();

                if (source.getSelectedIndex() != 0) {               //level has been chosen

                    String save = (String)saveOverwriteBox.getSelectedItem();
                    //remove selected file
                    removeFile(save);

                    saveFile(save);

                    //set text under text area
                    savedLabel1.setText("Your progress has been saved as");
                    savedLabel2.setText(save);

                }

                //make dropdown menu show 1st element on the list - "Overwrite save:"
                //instead of chosen level
                saveOverwriteBox.setSelectedIndex(0);
            }
        });

        /* save label */
        saveLabel.setText("Or save your progress as a new file:");

        /* save text field */
        saveAsTextField.setText("Save as...");

        saveAsTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //get unique name for the file
                //in case of duplicates file will be saved with a number at the end
                //in case of empty field, number will become a file name
                String saveName = getUniqueName(saveAsTextField.getText());

                //create new save
                saveFile(saveName);

                //change focus to backToMainMenu button
                backToMainMenuButton.requestFocusInWindow();
            }
        });

        saveAsTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                //Erase text when field gains focus
                saveAsTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                //Set text in a field to "Save as:"
                //when field loses focus
                saveAsTextField.setText("Save as...");
            }
        });

        /* after save label */
        savedLabel1.setText("");
        savedLabel2.setText("");

        for (String saveFile : saveFolder.list()) {
            String[] fileNameTokens = saveFile.split(".txt");

            //add saves in a folder as options in save overwrite, load and remove boxes
            saveOverwriteBox.addItem(fileNameTokens[0]);
        }

    /* BACK TO MENU */
        backToMainMenuButton.setText("Back to main menu");

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //remove text from saved label
                savedLabel1.setText("");
                savedLabel2.setText("");
                //go to main menu
                game.switchPanels(2);
            }
        });


        /* for all components */
        for (Component component : this.getComponents()) {
            //align all components to the center
            ((JComponent)component).setAlignmentX(Component.CENTER_ALIGNMENT);
            //change background color for all components
            component.setBackground(new Color(128, 191, 255));

            if (component instanceof JPanel) {
                ((JPanel) component).setLayout(new BoxLayout((JPanel)component, BoxLayout.Y_AXIS));
                component.setMaximumSize(new Dimension(250, 100));

                for (Component innerComponent : ((JPanel) component).getComponents()) {
                    //change background color for all components
                    innerComponent.setBackground(new Color(128, 191, 255));
                    innerComponent.setMaximumSize(new Dimension(240, 100));
                    ((JComponent)innerComponent).setAlignmentX(innerComponent.CENTER_ALIGNMENT);

                    if (innerComponent instanceof JLabel) {
                        ((JLabel)innerComponent).setHorizontalTextPosition(SwingConstants.LEFT);
                    }
                }
            }
        }
    }

    /**
     * Checks whether name is unique (there is no other file with that name) <br>
     * If there isn't returns that name, otherwise adds a counter to the name
     * @param name Name to be used if is unique
     * @return Unique name
     */
    private String getUniqueName(String name) {
        ArrayList<String> fileNames = new ArrayList<>();
        int i;

        //gets names of the save files without '.txt'
        for (String saveFile : saveFolder.list()) {
            String[] fileNameTokens = saveFile.split(".txt");
            fileNames.add(fileNameTokens[0]);
        }

        //checks if name is already used or is empty
        //if yes, than adds a counter to the name
        if (fileNames.contains(name) || name.equals("")) {
            i = 1;
            while (fileNames.contains(name + i)) {
                i++;
            }
            return name + i;
        } else {
            return name;
        }
    }

    /**
     * Save level state
     * @param file Save name
     */
    private void saveFile(String file) {
        try {
            GameSaveLoader.save(Game.getLevel(), saveFolderPath + "/" + file + ".txt");

            //add option to chose the file from save overwrite, load and remove box
            saveOverwriteBox.addItem(file);

            //set text under text area
            savedLabel1.setText("Your progress has been saved as ");
            savedLabel2.setText(file);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Remove file containing a saved level state
     * @param file File name
     */
    private void removeFile(String file) {
        File myObj = new File(saveFolderPath + "/" + file + ".txt");
        if (myObj.delete()) {
            System.out.println("File removed " + myObj);

            //remove option to chose the file from save overwrite, load and remove box
            saveOverwriteBox.removeItem(file);
        } else {
            System.out.println("File not removed");
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

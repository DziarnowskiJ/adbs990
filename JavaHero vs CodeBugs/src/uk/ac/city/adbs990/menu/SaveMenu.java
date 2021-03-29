package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.tools.GameSaveLoader;
import uk.ac.city.adbs990.levels.GameLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SaveMenu extends JPanel {
    private Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground3.jpg").getImage();

    private JLabel title;
    private JPanel savePanel;
    private JLabel saveLabel;
    private JTextField saveAsTextField;
    private JLabel saveOverwriteLabel;
    private JComboBox<String> saveOverwriteBox;
    private JLabel savedLabel1;
    private JLabel savedLabel2;
    private JPanel loadPanel;
    private JLabel loadLabel;
    private JComboBox<String> loadBox;
    private JPanel removePanel;
    private JLabel removeLabel;
    private JComboBox<String> removeBox;
    private JButton backToMainMenuButton;

    private final String saveFolderPath = "data/saves";
    private final File saveFolder = new File("data/saves");

    /**
     * Creates menu with save and load level options
     */
    public SaveMenu(Game game) {

        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create components
        title = new JLabel();

        savePanel = new JPanel();
        saveLabel = new JLabel();
        saveAsTextField = new JTextField();
        saveOverwriteLabel = new JLabel();
        saveOverwriteBox = new JComboBox<String>();
        savedLabel1 = new JLabel();
        savedLabel2 = new JLabel();

        loadPanel = new JPanel();
        loadLabel = new JLabel();
        loadBox = new JComboBox<String>();

        removePanel = new JPanel();
        removeLabel = new JLabel();
        removeBox = new JComboBox<String>();

        backToMainMenuButton = new JButton();

        //add components to the menu
        add(Box.createVerticalGlue());
        this.add(title);

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
        this.add(loadPanel);
        loadPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loadPanel.add(loadLabel);
        loadPanel.add(loadBox);
        loadPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        add(Box.createVerticalGlue());
        this.add(removePanel);
        removePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        removePanel.add(removeLabel);
        removePanel.add(removeBox);
        removePanel.add(Box.createRigidArea(new Dimension(0, 5)));

        add(Box.createVerticalGlue());
        this.add(backToMainMenuButton);
        add(Box.createVerticalGlue());

    /* TITLE */
        /* title */
        title.setText("Save menu");
        title.setFont(new Font("Stencil", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

    /* SAVE */
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

    /* LOAD */
        /* load label */
        loadLabel.setText("Load your save");

        /* load box */
        loadBox.addItem("Load:");

        loadBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();

                if (source.getSelectedIndex() != 0) {               //level has been chosen
                    //load level
                    try {
                        GameLevel newLevel = GameSaveLoader.load(Game.getLevel().getGame(),
                                saveFolderPath + "/" + loadBox.getSelectedItem() + ".txt");
                        game.setLevel(newLevel);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                //make dropdown menu show 1st element on the list - "Load:"
                //instead of chosen level
                loadBox.setSelectedIndex(0);

                //go to game
                game.switchPanels(1);
            }
        });

    /* REMOVE */
        /* remove label */
        removeLabel.setText("Remove unwanted save");
        /* remove box */
        removeBox.addItem("Remove:");

        removeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();

                if (source.getSelectedIndex() != 0) {               //level has been chosen
                    //remove selected file
                    removeFile((String)removeBox.getSelectedItem());

                }

                //make dropdown menu show 1st element on the list - "Remove:"
                //instead of chosen level
                removeBox.setSelectedIndex(0);
            }
        });

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
            component.setBackground(new Color(255, 222, 222));

            if (component instanceof JPanel) {
                ((JPanel) component).setLayout(new BoxLayout((JPanel)component, BoxLayout.Y_AXIS));
                ((JComponent)component).setAlignmentX(component.CENTER_ALIGNMENT);
                component.setMaximumSize(new Dimension(250, 100));

                for (Component innerComponent : ((JPanel) component).getComponents()) {
                    //change background color for all components
                    innerComponent.setBackground(new Color(255, 222, 222));
                    innerComponent.setMaximumSize(new Dimension(240, 100));
                    ((JComponent)innerComponent).setAlignmentX(innerComponent.CENTER_ALIGNMENT);

                    if (innerComponent instanceof JLabel) {
                        ((JLabel)innerComponent).setHorizontalTextPosition(SwingConstants.LEFT);
                    }
                }
            }
        }

    /* for all boxes (save, load and remove) */
        for (String saveFile : saveFolder.list()) {
            String[] fileNameTokens = saveFile.split(".txt");

            //add saves in a folder as options in save overwrite, load and remove boxes
            saveOverwriteBox.addItem(fileNameTokens[0]);
            loadBox.addItem(fileNameTokens[0]);
            removeBox.addItem(fileNameTokens[0]);
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
            loadBox.addItem(file);
            removeBox.addItem(file);

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
            loadBox.removeItem(file);
            removeBox.removeItem(file);
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

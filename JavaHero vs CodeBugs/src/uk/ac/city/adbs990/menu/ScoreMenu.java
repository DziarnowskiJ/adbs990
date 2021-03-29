package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.table.*;


public class ScoreMenu extends JPanel {
    private final Image background = new ImageIcon("data/images/Backgrounds/Menu/menuBackground2.jpg").getImage();
    private static final File saveFolder = new File("data/saves");
    private static final String saveFolderPath = "data/saves";

    private int scoreTabNo;

    private final JLabel title;
    private final JLabel subtitleLabel;

    private final JTabbedPane scoreTabs;

    private JScrollPane table1 = null;
    private JScrollPane table2 = null;
    private JScrollPane table3 = null;
    private JScrollPane table4 = null;

    private final JPanel switchButtonsPanel;
    private final JButton previousButton;
    private final JButton nextButton;

    private final JLabel noDataLabel;

    private final JButton backToMainMenuButton;

    /**
     * Creates menu with tables containing scores loaded from save files
     */
    public ScoreMenu(Game game) {
        setSize(600, 600);
        setBackground(new Color(161, 170, 181));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[][] data = null;
        try {
            data = getScoreData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create components
        title = new JLabel();

        subtitleLabel = new JLabel();
        scoreTabs = new JTabbedPane();

        switchButtonsPanel = new JPanel();
        previousButton = new JButton();
        nextButton = new JButton();

        noDataLabel = new JLabel();

        backToMainMenuButton = new JButton();

        //add components to the menu
        add(Box.createVerticalGlue());
        this.add(title);

        add(Box.createVerticalGlue());

        if (data != null) {
            this.add(subtitleLabel);
            this.add(scoreTabs);

            add(Box.createVerticalGlue());
            this.add(switchButtonsPanel);
            switchButtonsPanel.add(previousButton);
            switchButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            switchButtonsPanel.add(nextButton);
        } else {
            this.add(noDataLabel);
        }

        add(Box.createVerticalGlue());
        this.add(backToMainMenuButton);
        add(Box.createVerticalGlue());

    /* TITLE */
        /* title */
        title.setText("High scores");
        title.setFont(new Font("Stencil", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        /* subtitle */
        subtitleLabel.setText("Scores ordered by:");
        subtitleLabel.setFont(new Font("Stencil", Font.BOLD, 20));
        subtitleLabel.setForeground(Color.WHITE);

    /* SCORE TAB */
        scoreTabs.setMaximumSize(new Dimension(550, 100));

        scoreTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane source = (JTabbedPane) e.getSource();

                scoreTabNo = source.getSelectedIndex();
            }
        });

    /* TABLES */
        if (data != null) {
            /* create tables */
            table1 = scoreTable(data, new int[]{0, 1, 2, 3, 4, 5}, false);
            table2 = scoreTable(data, new int[]{0, 1, 3, 2, 4, 5}, false);
            table3 = scoreTable(data, new int[]{0, 1, 4, 2, 3, 5}, true);
            table4 = scoreTable(data, new int[]{0, 1, 5, 2, 3, 4}, true);

            /* add tables to the tabs */
            scoreTabs.addTab("Overall scores", table1);
            scoreTabs.addTab("Most coins collected", table2);
            scoreTabs.addTab("Shortest time", table3);
            scoreTabs.addTab("Least health lost", table4);
        }

    /* SWITCH BUTTONS */
        /* button panel */
        switchButtonsPanel.setAlignmentX(switchButtonsPanel.CENTER_ALIGNMENT);
        switchButtonsPanel.setMaximumSize(new Dimension(240, 100));
        switchButtonsPanel.setBorder(null);
        switchButtonsPanel.setOpaque(false);

        /* previous button */
        previousButton.setText("Previous table");
        previousButton.setBackground(new Color(255, 222, 222));

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scoreTabNo > 0) {
                    scoreTabNo--;
                } else {
                    scoreTabNo = scoreTabs.getTabCount() - 1;
                }
                scoreTabs.setSelectedIndex(scoreTabNo);
            }
        });

        /* next button */
        nextButton.setText("Next table");
        nextButton.setBackground(new Color(255, 222, 222));

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scoreTabNo < scoreTabs.getTabCount() - 1) {
                    scoreTabNo++;
                } else {
                    scoreTabNo = 0;
                }
                scoreTabs.setSelectedIndex(scoreTabNo);
            }
        });

    /* NO DATA */
        noDataLabel.setText("Unfortunately, there are no scores to display");
        noDataLabel.setFont(new Font("Stencil", Font.BOLD, 24));
        noDataLabel.setForeground(Color.WHITE);

    /* BACK TO MENU */
        backToMainMenuButton.setText("Back to main menu");

        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //go to main menu
                game.switchPanels(2);
            }
        });

        /* align all components */
        for (Component component : this.getComponents()) {
            //align all components to the center
            ((JComponent) component).setAlignmentX(Component.CENTER_ALIGNMENT);
            //change background color for all components

                component.setBackground(new Color(255, 222, 222));

            }
        }

    /**
     * Creates table with score data
     * <p>
     * Creates table with data sorted in either ascending or descending direction based on the 3 column <br>
     * First 3 columns are red, rest is yellow <br>
     * Initial order is [Place, Save name, Overall score, Collected coins, Time taken, Lost health]
     * </p>
     * @param data Data array
     * @param order Order of columns
     * @param ascending Order of rows
     * @return Returns ordered ScrollPane with table containing score data
     */
    public JScrollPane scoreTable(String[][] data, int[] order, boolean ascending) {
        //create String array with names of columns
        //in a specific order
        String[] columnNames = new String[order.length];
        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 0 -> columnNames[i] = "Place";
                case 1 -> columnNames[i] = "Save name";
                case 2 -> columnNames[i] = "Overall score";
                case 3 -> columnNames[i] = "Collected coins";
                case 4 -> columnNames[i] = "Time taken";
                case 5 -> columnNames[i] = "Lost health";
            }
        }

        //create array with 1 column more than data array
        String[][] newData = new String[data.length][data[0].length+1];

        //transfer data from data array to newData array
        //in order the newData is sorted
        for (int j = 0; j < order.length; j++) {
            for (int i = 0; i < data.length; i++) {
                newData[i][j] = data[i][order[j]];
            }
        }

        //sort array by 2nd column
        sortArray(newData, 2, ascending);

        //set first entry of each row as its row number
        //starting at 1
        for (int i = 0; i < newData.length; i++) {
            newData[i][0] = "" + (i + 1);
        }

        //create table with data
        JTable table = new JTable(newData, columnNames) {
            //disable editing cells
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //change column appearance
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                                                               Object value,
                                                               boolean isSelected,
                                                               boolean hasFocus,
                                                               int row,
                                                               int column) {
                    setText(value.toString());
                    setHorizontalAlignment(RIGHT);

                    if (column < 3) {
                        //first 3 columns

                        //set font to BOLD
                        setFont(new Font("Stencil", Font.BOLD, 12));
                        //set background color to red
                        setBackground(isSelected ? new Color(247, 158, 158) : new Color(247, 158, 158));
                    } else {
                        //rest of the table

                        //set font to PLAIN
                        setFont(new Font("Stencil", Font.PLAIN, 12));
                        //set background color to yellow
                        setBackground(isSelected ? new Color(245, 239, 159) : new Color(245, 239, 159));
                    }
                    return this;
                }
            });
        }

        //disable dragging columns
        table.getTableHeader().setReorderingAllowed(false);

        //change background color of header to blue
        table.getTableHeader().setBackground(new Color(174, 210, 214));

        //put table into scrollPane
        JScrollPane scrollTable = new JScrollPane(table);

        if (table.getRowCount() <= 24) {
            scrollTable.setPreferredSize(new Dimension(table.getWidth(), ((table.getRowCount()) * table.getRowHeight() - 33)));
        } else {
            scrollTable.setPreferredSize(new Dimension(table.getWidth(), 300));
        }

        return scrollTable;
    }

    /**
     * Sort array of data
     * @param data  Data to sort
     * @param colNo Column number the array is sorted based on
     * @param ascending Ascending/descending
     */
    private void sortArray(String[][] data, int colNo, boolean ascending) {
        //sort new array
        Arrays.sort(data, new Comparator<>() {
            @Override
            public int compare(String[] array1, String[] array2) {
                // get the second element of each array, and transform it into a Float
                float f1 = Float.parseFloat(array1[colNo]);
                float f2 = Float.parseFloat(array2[colNo]);

                if (ascending) {
                    if (f1 > f2) return 1;
                    else return -1;
                } else {
                    if (f1 < f2) return 1;
                    else return -1;
                }
            }
        });
    }

    /**
     * Get array of all scores
     * @return Data array
     * @throws IOException
     */
    private static String[][] getScoreData() throws IOException {
        String[][] data = null;

        if (saveFolder.list().length > 0) {
            data = new String[saveFolder.list().length][6];
        }

        for (int i = 0; i < saveFolder.list().length; i++) {
            FileReader fr = null;
            BufferedReader reader = null;
            try {
                fr = new FileReader(saveFolderPath + "/" + saveFolder.list()[i]);
                reader = new BufferedReader(fr);
                String line = reader.readLine();

                while (line != null) {
                    // file is assumed to contain one name, score pair per line
                    String[] tokens = line.split(",");
                    String name = tokens[0];

                    if (name.equals("Hero")) {
                        int coinsCollected = (Integer.parseInt(tokens[6]) + Integer.parseInt(tokens[10]));
                        int lostHealth = (Integer.parseInt(tokens[4]) - Integer.parseInt(tokens[3]));
                        float timeTaken = Float.parseFloat(tokens[13])/60;

                        //placeholder
                        data[i][0] = "POSITION" + 1;
                        //save name
                        data[i][1] = saveFolder.list()[i].split(".txt")[0];
                        //overall score
                        data[i][2] = "" + (coinsCollected*100 - timeTaken*3 - lostHealth*30);
                        //coins collected
                        data[i][3] = "" + coinsCollected;
                        //time taken
                        data[i][4] = "" + timeTaken;
                        //lost health
                        data[i][5] = "" + lostHealth;
                    }

                    line = reader.readLine();
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (fr != null) {
                    fr.close();
                }
            }
        }
        return data;
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

package uk.ac.city.adbs990;

import uk.ac.city.adbs990.controls.KeyboardHandler;
import uk.ac.city.adbs990.controls.MouseHandler;
import uk.ac.city.adbs990.menu.EndMenu;
import uk.ac.city.adbs990.menu.MainMenu;
import uk.ac.city.adbs990.table.FreeCellTable;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.KlondikeTable;
import uk.ac.city.adbs990.tools.GiveFocus;

import javax.swing.*;

public class Game {

    //The World in which the bodies move and interact.
    private GameTable table;
    public GameTable getGameTable() {
        return table;
    }
    public void setGameTable(GameTable newTable) {
        table = newTable;
    }

    //A graphical display of the world (a specialised JPanel).
    private GameView view;
    private MainMenu mainMenu;
    private EndMenu endMenu;
    private int panelNumber;
    
/* Controllers, handlers and trackers */
    private final MouseHandler mouseHandler;
    private final KeyboardHandler keyboardHandler;
    
    /**
     * Get current game view
     * @return Returns current game view
     */
    public GameView getGameView(){
        return view;
    }
    
    
/* Frame containing the GameView */
    private final JFrame frame;

/* Game */
    public Game() {
    /* create the table */
        table = new KlondikeTable(this);
        table.populate();
//        table.fakePopulate();
        
    /* create a view */
        view = new GameView(table, 1100, 600);
    /* create main menu*/
        mainMenu = new MainMenu(this);
    /* create end game menu */
        endMenu = new EndMenu(this);
    
    /* controllers and listeners */
        // mouse
        mouseHandler = new MouseHandler(table, view);
        view.addMouseListener(mouseHandler);
        view.addMouseListener(new GiveFocus(view));
        // keyboard
        keyboardHandler = new KeyboardHandler(this);
        view.addKeyListener(keyboardHandler);
    
    /* frame */
        // add the view to a frame (Java top level window)
        frame = new JFrame("My solitaire");

//        frame.add(view);
        frame.add(mainMenu);

        // enable the frame to quit the application
        // when the x button is pressed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // locate the window in the top-left corner
        frame.setLocationByPlatform(false);
        frame.setLocation(0, 0);
        // don't let the frame be resized
        frame.setResizable(false);
        // size the frame to fit the world view
        frame.pack();
        // make the frame visible
        frame.setVisible(true);
        

    /* VISUAL UTILITIES */
        // draw a 1-metre grid over the view
//        view.setGridResolution(1);
    
        // create a debug view
//        DebugViewer debugViewer = new DebugViewer(table, 600, 600);
//        debugViewer.setLocation(700, 0);
        
        table.start();
    }
    
    public void newGame(int x) {
        table.stop();
        switch (x) {
            case 0: table = new KlondikeTable(this); break;
            case 1: table = new FreeCellTable(this); break;
        }
        table.populate();
//        table.fakePopulate();
        view.setGameTable(table);
        mouseHandler.updateMouseHandler(table);
        table.start();
    }
    
    /**
     * Switches between game and menu panels
     * <p>
     * Depending on the input switches to a different panel: <br>
     *     - 0 -> Game view <br>
     *     - 1 -> Main menu <br>
     *     - 2 -> End menu <br>
     * </p>
     * @param x Number of panel which will be opened
     */
    public void switchPanels(int x) {
        //stop the game
        table.stop();
    
        //remove possible panels
        frame.remove(view);
        frame.remove(mainMenu);
        frame.remove(endMenu);

        panelNumber = x;
        
        // depending on the 'x' switch to proper panel
        switch (x) {
            //GAME
            case 0:
                frame.add(view);
                view.requestFocusInWindow();
                table.start();
                break;
            //MAIN MENU
            case 1:
                frame.add(mainMenu);
                break;
            //SAVE MENU
            case 2:
                frame.add(endMenu);
                break;
        }
        
        //make sure the frame was updated
        //and shows proper content
        frame.repaint();
        frame.revalidate();
        frame.pack();
    }
    
    public int getPanelNumber() {
        return panelNumber;
    }

/** Run a new game */
    public static void main(String[] args) {
        
        // TODO: beautify game menu
        // TODO: add new game button on table x2
        
        // possible TODO: restart game (with deck.getUsedCards())
        
        // future TODO: add spider game mode
        
        // advanced TODO: change way of selecting a card - instead of iteration do it on collision!
        
        
        new Game();

    }
}

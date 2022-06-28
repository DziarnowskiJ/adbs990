package uk.ac.city.adbs990.table;

import city.cs.engine.World;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.cards.Deck;
import uk.ac.city.adbs990.controls.CompleteBtn;
import uk.ac.city.adbs990.table.tableParts.*;
import uk.ac.city.adbs990.table.tableParts.freeCellParts.FreeColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.KlondikeCardColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.CoveredColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.ShownColumn;
import uk.ac.city.adbs990.tools.EndAnimation;
import uk.ac.city.adbs990.tools.SequentialComplete;

public abstract class GameTable extends World {

/* Non-static fields */
    // Common columns
    protected FinalColumn[] finalColumns;
    protected CardColumn[] cardColumns;
    // Klondike columns
    protected ShownColumn shownColumn;
    protected CoveredColumn coveredColumn;
    // Free-cell columns
    protected FreeColumn[] freeColumns;
    // Other fields
    protected Game game;
    protected CompleteBtn completeBtn;
    protected Deck deck;
    protected String tableState;
    
    private EndAnimation endAnimation;
    
/* Non-static getters */
    // Common columns
    public FinalColumn[] getFinalColumns() {
    return finalColumns;
}
    public CardColumn[] getCardColumns() {
        return cardColumns;
    }
    // Klondike columns
    public ShownColumn getShownColumn() {
        return shownColumn;
    }
    public CoveredColumn getCoveredColumn() {
        return coveredColumn;
    }
    // FreeCell columns
    public FreeColumn[] getFreeColumns() {
        return freeColumns;
    }
    // Other getters
    public Game getGame() {
        return game;
    }
    public CompleteBtn getCompleteBtn() {
        return completeBtn;
    }
    
    
    /* Constructor */
    public GameTable(Game game) {
        this.game = game;
        
        finalColumns = new FinalColumn[4];
        tableState = "unfinished";
    }
    
/* abstract methods */
    /**
     * Creates a deck, shuffles it and calls populate(deck) function
     */
    public void populate() {
        /* Create a deck and shuffle it */
        deck = new Deck(this);
        deck.shuffle();
        populate(deck);
    }
    
    /**
     * Populate the table - depending on the table type
     * creates a number of ColumnSpaces of a proper type
     * @param deck Deck of cards that will be on the table
     */
    public abstract void populate(Deck deck);
    
    /**
     * Special populate() methods
     * GameTable will be almost completely solved
     */
    public abstract void fakePopulate();
    
    /**
     * Returns the state of the table
     * @return {"unfinished", "solved", "completed"}
     */
    public abstract String getTableState();
    
/* Other methods */
    /**
     * Creates "CompleteButton" allowing for calling completeTable() method
     */
    public void addCompleteBtn() {
        if (completeBtn == null)
            completeBtn = new CompleteBtn(this);
    }
    
    /**
     * Automatically completes a table
     * Requiers: All cards are in the CardColumn and are in order
     */
    public void completeTable() {
        SequentialComplete seqCompl = new SequentialComplete(this);
        this.addStepListener(seqCompl);
    }
    
    /**
     * Creates end-game animation:
     * All cards are moved to the center of the screen and move in random directions with random velocity
     */
    public void explodeCards() {
        if (endAnimation == null) {
            endAnimation = new EndAnimation(this, deck);
            this.addStepListener(endAnimation);
        }
    }
}

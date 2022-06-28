package uk.ac.city.adbs990.table;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.cards.Deck;
import uk.ac.city.adbs990.table.tableParts.*;
import uk.ac.city.adbs990.table.tableParts.freeCellParts.FreeCellCardColumn;
import uk.ac.city.adbs990.table.tableParts.freeCellParts.FreeColumn;

public class FreeCellTable extends GameTable {

/* Constructor */
	public FreeCellTable(Game game) {
		super(game);
		
		freeColumns = new FreeColumn[4];
		cardColumns = new FreeCellCardColumn[8];
	}
	
/* Other methods */
	/**
	 * Populate the table:
	 * - 8 columns with cards
	 * - 4 slots for final cards
	 * - 4 slots with free space for cards
	 * @param paramDeck Deck of cards that will be on the table
	 */
	@Override
	public void populate(Deck paramDeck) {
		this.deck = paramDeck;
		
	/* create 8 columns with cards */
		for (int i = 0; i < 8; i++)
			cardColumns[i] = new FreeCellCardColumn(this, new Vec2((i - 3.5f) * 5 , 6));
		
	/* create 4 final columns */
		for (int i = 0; i < 4; i++)
			finalColumns[i] = new FinalColumn(this, new Vec2((i+1)  * 5, 13), Deck.getTypes()[i]);
		
	/* create 4 free-space columns */
		for (int i = 0; i < 4; i++)
			freeColumns[i] = new FreeColumn(this, new Vec2(-(i+1) * 5, 13));
			
	/* put all cards in the CardColumns */
		int remainingDeckSize = deck.getCards().size();
		for (int i = 0; i < remainingDeckSize; i++) {
			Card card = deck.pickCard();
			card.forceDrop(cardColumns[i % 8]);
			card.swapSide();
		}
	}
	
	/**
	 * Special populate() methods
	 * GameTable will be almost completely solved
	 */
	@Override
	public void fakePopulate() {
	
	/* Create a deck */
		deck = new Deck(this);
		
	/* create 8 columns with cards */
		for (int i = 0; i < 8; i++)
			cardColumns[i] = new FreeCellCardColumn(this, new Vec2((i - 3.5f) * 5 , 6));
		
	/* create 4 final columns */
		for (int i = 0; i < 4; i++)
			finalColumns[i] = new FinalColumn(this, new Vec2((i+1)  * 5, 13), Deck.getTypes()[i]);
		
	/* create 4 free-space columns */
		for (int i = 0; i < 4; i++)
			freeColumns[i] = new FreeColumn(this, new Vec2(-(i+1) * 5, 13));
		
	/* put 48 cards in finalColumns */
		for (int i = 0; i < 48; i++) {
			Card card = deck.pickCard();
			card.forceDrop(cardColumns[1]);
			for (FinalColumn colFin : finalColumns)
				card.drop(colFin);
		}
		
	/* take care of the rest of the cards */
		int remainingDeckSize = deck.getCards().size();
		for (int i = 0; i < remainingDeckSize; i++) {
			Card card = deck.pickCard();
			card.swapSide();
			card.forceDrop(cardColumns[3]);
		}
	}
	
	/**
	 * Returns the state of the table
	 * @return {"unfinished", "completed"}
	 */
	@Override
	public String getTableState() {
		
		// check whether all final columns are full
		boolean finalCompleted = true;
		for (FinalColumn fc : finalColumns) {
			if (fc.getSpaceContent().size() < 13)
				finalCompleted = false;
		}
		
		if (finalCompleted)
			return "completed";
		else
			return "unfinished";
	}
}

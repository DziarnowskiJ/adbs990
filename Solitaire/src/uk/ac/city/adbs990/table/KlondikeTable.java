package uk.ac.city.adbs990.table;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.cards.Deck;
import uk.ac.city.adbs990.table.tableParts.CardColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.KlondikeCardColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.CoveredColumn;
import uk.ac.city.adbs990.table.tableParts.FinalColumn;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.ShownColumn;

public class KlondikeTable extends GameTable {
	
	public KlondikeTable(Game game) {
		super(game);
		
		cardColumns = new KlondikeCardColumn[7];
	}
	
	
/* Other methods */
	/**
	 * Populate the table:
	 * - 7 columns with cards
	 * - 4 slots for final cards
	 * - slot for remaining cards (with those cards)
	 * - slot for uncovered cards
	 * @param paramDeck Deck of cards that will be on the table
	 */
	@Override
	public void populate(Deck paramDeck) {
		this.deck = paramDeck;
		
	/* create 7 columns with cards */
		for (int i = 0; i < 7; i++) {
			
			cardColumns[i] = new KlondikeCardColumn(this, new Vec2((i - 3) * 5 , 6));
			
			// add cards (all backside)
			for (int j = 0; j < i + 1; j++) {
				Card card = deck.pickCard();
				card.forceDrop(cardColumns[i]);
				if (j == i)
					card.swapSide();
			}
		}
		
	/* create 4 final columns */
		for (int i = 0; i < 4; i++) {
			finalColumns[i] = new FinalColumn(this, new Vec2(i*5, 13), Deck.getTypes()[i]);
		}
		
	/* create column with shown cards */
		shownColumn = new ShownColumn(this);
		
	/* create column with covered cards */
		coveredColumn = new CoveredColumn(this);
		
	/* take care of the rest of the cards */
		int remainingDeckSize = deck.getCards().size();
		for (int i = 0; i < remainingDeckSize; i++)
			deck.pickCard().forceDrop(coveredColumn);
		
		// make sure all cards in CoveredColumn are covered
		for (Card card : coveredColumn.getSpaceContent())
			if (card.isFrontSide())
				card.swapSide();
	}
	
	/**
	 * Special populate() methods
	 * GameTable will be almost completely solved
	 */
	@Override
	public void fakePopulate() {
	
	/* Create a deck */
		deck = new Deck(this);
		
	/* create 4 final columns */
		for (int i = 0; i < 4; i++)
			finalColumns[i] = new FinalColumn(this, new Vec2(i*5, 13), Deck.getTypes()[i]);
		
	/* create column with shown cards */
		shownColumn = new ShownColumn(this);
		
	/* create column with covered cards */
		coveredColumn = new CoveredColumn(this);
		
	/* create 7 columns with cards */
		for (int i = 0; i < 7; i++)
			cardColumns[i] = new KlondikeCardColumn(this, new Vec2((i - 3) * 5 , 6));
		
	/* put 48 cards in finalColumns */
		for (int i = 0; i < 48; i++) {
			Card card = deck.pickCard();
			card.forceDrop(shownColumn);
			for (FinalColumn colFin : finalColumns)
				card.drop(colFin);
		}
		
	/* put 2 cards in cardColumn */
		deck.pickCard().forceDrop(cardColumns[3]);
		{
			Card card = deck.pickCard();
			card.forceDrop(cardColumns[3]);
			card.swapSide();
		}
		
	/* take care of the rest of the cards */
		int remainingDeckSize = deck.getCards().size();
		for (int i = 0; i < remainingDeckSize; i++)
			deck.pickCard().forceDrop(coveredColumn);
		
		// make sure all cards in CoveredColumn are covered
		for (Card card : coveredColumn.getSpaceContent())
			if (card.isFrontSide())
				card.swapSide();
	}
	
	/**
	 * Returns the state of the table
	 * @return {"unfinished", "solved", "completed"}
	 */
	@Override
	public String getTableState() {
		
		// check whether all final columns are full
		boolean finalCompleted = true;
		for (FinalColumn fc : finalColumns) {
			if (fc.getSpaceContent().size() < 13)
				finalCompleted = false;
		}
		
		// check whether all cards are uncovered
		boolean allCardsUncovered = true;
		for (CardColumn cc : cardColumns) {
			for (Card card : cc.getSpaceContent())
				if (!card.isFrontSide())
					allCardsUncovered = false;
		}
		
		// check whether coveredColumn and shownColumn are empty
		boolean columnsEmpty = (coveredColumn.getSpaceContent().isEmpty() &&
				shownColumn.getSpaceContent().isEmpty());
		
		
		if (finalCompleted)
			tableState = "completed";
		else if (allCardsUncovered && columnsEmpty)
			tableState = "solved";
		else
			tableState = "unfinished";
		
		return tableState;
	}

}


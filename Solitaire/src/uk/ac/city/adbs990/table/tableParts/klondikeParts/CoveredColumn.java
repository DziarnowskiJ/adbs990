package uk.ac.city.adbs990.table.tableParts.klondikeParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;

public class CoveredColumn extends ColumnSpace {

/* Static fields */
	protected static final Vec2 spacePos = new Vec2(-15, 13);
	
/* Constructor */
	public CoveredColumn(GameTable table) {
		super(table, "CoveredColumn", spacePos);
	}
	
/* Overrides of superclass' abstract methods */
	/**
	 * Cards can never be added by a player to this column
	 * @param card Card to be added
	 * @return Always false
	 */
	@Override
	public boolean canBeAdded(Card card) {
		return false;
	}
	
/* Overrides of superclass' other methods */
	/**
	 * Adds a card to the column and sets it on a proper position.
	 * @param card Card to be added.
	 * Although cards cannot be added to this column by a player
	 * they can be forceDrop()'ed here.
	 * Additionally, sets a card to be facing back side
	 */
	@Override
	public void addCard(Card card) {
		
		// set back side to be visible
		if (card.isFrontSide())
			card.swapSide();
		
		super.addCard(card);
	}
}

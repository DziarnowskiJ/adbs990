package uk.ac.city.adbs990.table.tableParts.klondikeParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;

public class ShownColumn extends ColumnSpace {

/* Static fields */
	protected static final Vec2 spacePos = new Vec2(-10, 13);

/* Constructor */
	public ShownColumn(GameTable table) {
		super(table, "ShownColumn", spacePos);
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
	 *
	 * Although cards cannot be added to this column by a player
	 * they can be forceDrop()'ed here.
	 * Additionally, sets a card to be facing font side
	 * @param card Card to be added.
	 */
	@Override
	public void addCard(Card card) {
		
		// set front side to be visible
		if (!card.isFrontSide())
			card.swapSide();
		
		// move last card out of the view
		if (!spaceContent.isEmpty())
			spaceContent.get(spaceContent.size() - 1).setPosition(new Vec2(-100, 100));
		
		super.addCard(card);
	}
}

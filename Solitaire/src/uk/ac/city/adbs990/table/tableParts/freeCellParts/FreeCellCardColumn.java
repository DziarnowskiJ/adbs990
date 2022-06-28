package uk.ac.city.adbs990.table.tableParts.freeCellParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.CardColumn;

public class FreeCellCardColumn extends CardColumn{

/* Constructor */
	public FreeCellCardColumn(GameTable table, Vec2 spacePos) {
		super(table, "CardColumn", spacePos);
	}
	
/* Overrides of superclass' abstract methods */
	/**
	 * Checks whether card can be added to the column according to the FreeCell rules
	 * @param card Card to be added
	 * @return true if card can be added, otherwise false
	 */
	@Override
	public boolean canBeAdded(Card card) {
		if (card.hasLowerCard())
			return false;
		if (spaceContent.isEmpty()) {
			return true;
		} else {
			Card lastCard = spaceContent.get(spaceContent.size() - 1);
			
			// test if cards have different color && last card is higher than new card by 1
			return ((lastCard.isRed() && !card.isRed()) || (!lastCard.isRed() && card.isRed())) &&
					numbers.indexOf(lastCard.getNumber()) - 1 == numbers.indexOf(card.getNumber());
		}
	}
	
/* Overrides of superclass' other methods */
	/**
	 * Removes a card from the column.
	 * Additionally, updates last remaining card to have 'lowerCard' set tu null
	 * @param card Card to be removed
	 */
	@Override
	public void removeCard(Card card) {
		if (!card.hasLowerCard()) {
			spaceContent.remove(card);
			if (!spaceContent.isEmpty()) {
				spaceContent.get(spaceContent.size() - 1).setLowerCard(null);
			}
		}
	}
}


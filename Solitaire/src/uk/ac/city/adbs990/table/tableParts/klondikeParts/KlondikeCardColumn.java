package uk.ac.city.adbs990.table.tableParts.klondikeParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.CardColumn;

public class KlondikeCardColumn extends CardColumn {

/* Constructor */
	public KlondikeCardColumn(GameTable table, Vec2 columnPos) {
		super(table, "CardColumn", columnPos);
	}
	
/* Overrides of superclass' abstract methods */
	/**
	 * Checks whether card can be added to the column according to the Klondike (Standard solitaire) rules
	 * @param card Card to be added
	 * @return true if card can be added, otherwise false
	 */
	@Override
	public boolean canBeAdded(Card card) {
		// if there are no cards only K can be added
		if (spaceContent.isEmpty()) {
			return card.getNumber().equals("K");
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
	 * Additionally, removes all cards that are lower than the chosen card
	 * and updates last remaining card to have 'lowerCard' set tu null
	 * @param card Card to be removed
	 */
	@Override
	public void removeCard(Card card) {
		spaceContent.remove(card);
		if (card.hasLowerCard())
			removeCard(card.getLowerCard());
		if (!spaceContent.isEmpty()) {
			spaceContent.get(spaceContent.size() - 1).setLowerCard(null);
		}
	}
}

package uk.ac.city.adbs990.table.tableParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;

public abstract class CardColumn extends ColumnSpace{

/* Constructor */
	public CardColumn(GameTable table, String type, Vec2 spacePos) {
		super(table, type, spacePos);
	}
	
/* Overrides of superclass' abstract methods */
	/**
	 * Adds a card to the column and sets it on a proper position.
	 * Additionally, adds all cards that are recursively set as this card's 'lowerCard'
	 * @param card Card to be added
	 */
	@Override
	public void addCard(Card card) {
		if (!spaceContent.isEmpty())
			spaceContent.get(spaceContent.size()-1).setLowerCard(card);
		
		super.addCard(card);
	}

/* Overrides of superclass' other methods */
	/**
	 * Checks whether point is inside column's boundaries
	 * @param vec Position of the point to be checked
	 * @return true if point is inside the column's boundaries
	 */
	public boolean isInside(Vec2 vec) {
		return (getPosition().x - width/2 <= vec.x && vec.x <= getPosition().x + width/2
				&& spacePos.y - height * 4 <= vec.y && vec.y <= spacePos.y);
	}
	
	/**
	 * Returns the position of the next card to be added
	 * @return Vec2 - position of the next card in the column
	 */
	@Override
	public Vec2 getEndPosition() {
		return new Vec2(spacePos.x, spacePos.y - (spaceContent.size()+3) * Card.getCardDimensions().y / 6);
	}
}


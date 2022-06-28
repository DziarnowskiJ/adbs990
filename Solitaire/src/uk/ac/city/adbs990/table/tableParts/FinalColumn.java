package uk.ac.city.adbs990.table.tableParts;

import city.cs.engine.BodyImage;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.FreeCellTable;
import uk.ac.city.adbs990.table.GameTable;

public class FinalColumn extends ColumnSpace {

/* Non-static fields */
	private final String type;

/* Constructor */
	public FinalColumn(GameTable table, Vec2 spacePos, String type) {
		super(table, "FinalColumn" + type.toUpperCase(), spacePos);
		this.type = type;
		
		addImage(new BodyImage("data/graphics/columns/" + type + ".png", 8));
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
		if (spaceContent.isEmpty())
			return (card.getNumber().equals("A") &&
					type.equals(card.getType()));
		else {
			Card lastCard = spaceContent.get(spaceContent.size() - 1);
			
			// test if card have same type && last card is lower than new card by 1
			return (type.equals(card.getType())) &&
					numbers.indexOf(lastCard.getNumber()) + 1 == numbers.indexOf(card.getNumber());
		}
	}
	
/* Overrides of superclass' other methods */
	/**
	 * Adds a card to the column and sets it on a proper position.
	 * @param card Card to be added
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
		
		// check the state of the table -> if completed make end-game animation
		if (table.getTableState().equals("completed"))
			table.explodeCards();
	}
}

package uk.ac.city.adbs990.table.tableParts;

import city.cs.engine.BoxShape;
import city.cs.engine.Sensor;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.cards.Deck;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public abstract class ColumnSpace extends StaticBody {

/* Static fields */
	protected static final List<String> numbers = Arrays.asList(Deck.getNumbers());
	protected final float width = Card.getCardDimensions().x;
	protected final float height = Card.getCardDimensions().y;;

/* Non-static fields */
	protected ArrayList<Card> spaceContent;
	protected final Vec2 spacePos;
	protected final GameTable table;
	protected final String type;

/* Non-static getters */
	public ArrayList<Card> getSpaceContent() {
		return spaceContent;
	}
	public String getType() {
		return type;
	}
	public Card getLastCard() {
		if (spaceContent.isEmpty())
			return null;
		else
			return spaceContent.get(spaceContent.size() - 1);
	}

/* Constructor */
	public ColumnSpace(GameTable table, String type, Vec2 spacePos) {
		super(table, new BoxShape(Card.getCardDimensions().x/2, Card.getCardDimensions().y/2));
		this.type = type;
		this.spacePos = spacePos;
		spaceContent = new ArrayList<Card>();
		this.table = table;
		
		setPosition(new Vec2(spacePos.x, spacePos.y - height/2));
		
		new Sensor(this,
				new BoxShape(width/2,height/2));
		
		setAlwaysOutline(true);
		setFillColor(Color.lightGray);
	}

/* Abstract methods */
	/**
	 * Checks whether card can be added to the space according to the Klondike (Standard solitaire) rules
	 * @param card Card to be added
	 * @return true if card can be added, otherwise false
	 */
	abstract public boolean canBeAdded(Card card);
	
/* Other methods */
	/**
	 * Adds a card to the column
	 * @param card Card to be added
	 */
	public void addCard(Card card) {
		// move card to the correct position in the column
		card.setPosition(getEndPosition());
		
		// add card to the column's content
		spaceContent.add(card);
		card.setCurrentSpace(this);
	};
	
	/**
	 * Removes a card from the column
	 * @param card Card to be removed
	 */
	public void removeCard(Card card) {
		spaceContent.remove(card);
		
		// move last card to be visible
		if (!spaceContent.isEmpty())
			spaceContent.get(spaceContent.size() - 1).setPosition(getEndPosition());
	};
	
	/**
	 * Checks whether point is inside column's boundaries
	 * @param vec Position of the point to be checked
	 * @return true if point is inside the column's boundaries
	 */
	public boolean isInside(Vec2 vec) {
		return (getPosition().x - width/2 <= vec.x && vec.x <= getPosition().x + width/2
				&& spacePos.y - height <= vec.y && vec.y <= spacePos.y);
	}
	
	/**
	 * Returns the position of the next card to be added
	 * @return Vec2 - position of the next card in the column
	 */
	public Vec2 getEndPosition() {
		return new Vec2(spacePos.x, spacePos.y - height/2);
	};
	
/* Overrides */
	/**
	 * Overrides toString() function
	 * @return "Space{content[], position}"
	 */
	@Override
	public String toString() {
		String info = getClass() + "{" +
				"content= [";
		
		for (Card c : spaceContent)
			info = info + '\n' + "\t\t\t\t" +  c;
		
		info = info + "]," + '\n' + "\t\t\t\t" + "spacePos=" + spacePos +
				'}';
		
		return info;
	}
}


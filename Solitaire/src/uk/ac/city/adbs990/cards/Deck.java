package uk.ac.city.adbs990.cards;

import uk.ac.city.adbs990.table.GameTable;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

/* Static fields */
	// possible Numbers of Card
	private static final String[] numbers = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	// possible Types of Card
	private static final String[] types = {"spades", "hearts", "clubs", "diamonds", };
	
/* Static getters */
	public static String[] getNumbers() {
		return numbers;
	}
	public static String[] getTypes() {
		return types;
	}
	
/* Non-static fields */
	// list of all cards in a deck
	private ArrayList<Card> cards = new ArrayList<>();
	private ArrayList<Card> usedCards = new ArrayList<>();
	
/* Non-static getters */
	public ArrayList<Card> getCards() {
		return cards;
	}
	public ArrayList<Card> getUsedCards() {
		return usedCards;
	}

/* Constructor */
	/**
	 * Constructor, adds created cards to the cards ArrayList
	 * @param table GameTable to which the Cards from this deck will be added
	 */
	public Deck(GameTable table) {
		for (String type : types)
			for (String number : numbers)
				cards.add(new Card(table, number, type, false));
	}
	
/* Other methods */
	/**
	 * Shuffle the cards in the deck
	 * (Sets random order in ArrayList<Card>)
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * Removes first card from the deck and returns it
	 * @return First Card in the deck
	 */
	public Card pickCard() {
		Card card = cards.get(0);
		cards.remove(card);
		usedCards.add(card);
		return card;
	}
}

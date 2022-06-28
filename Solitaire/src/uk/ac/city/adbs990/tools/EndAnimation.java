package uk.ac.city.adbs990.tools;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.cards.Deck;

public class EndAnimation implements StepListener {
	private final GameTable table;
	private final Deck deck;
	private int counter;
	
	public EndAnimation(GameTable table, Deck deck) {
		this.table = table;
		this.deck = deck;
		counter = 0;
		
		for (Card card : deck.getUsedCards()) {
			card.setPosition(new Vec2(0, 0));
			card.setLinearVelocity(new Vec2((int) (Math.random() * 41 - 20), (int) (Math.random() * 41 - 20)));
		}
	}
	
	@Override
	public void preStep(StepEvent stepEvent) {
		for (Card card : deck.getUsedCards()) {
			// x-axis (-20, 20)
			if (card.getPosition().x > 20 || card.getPosition().x < -20)
				card.setLinearVelocity(new Vec2(-card.getLinearVelocity().x, card.getLinearVelocity().y));
			
			// y-axis (-15, 15)
			if (card.getPosition().y > 15 || card.getPosition().y < -15)
				card.setLinearVelocity(new Vec2(card.getLinearVelocity().x, -card.getLinearVelocity().y));
		}
	}
	
	@Override
	public void postStep(StepEvent stepEvent) {
		counter++;
		
		if (counter == 250) {
			table.getGame().switchPanels(1);
		}
	}
}
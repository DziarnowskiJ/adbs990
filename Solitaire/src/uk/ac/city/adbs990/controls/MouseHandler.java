package uk.ac.city.adbs990.controls;

import city.cs.engine.DynamicBody;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.FreeCellTable;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.GameView;
import uk.ac.city.adbs990.table.KlondikeTable;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;
import uk.ac.city.adbs990.table.tableParts.FinalColumn;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MouseHandler implements MouseListener {

/* Non-static fields */
	GameTable table;
	GameView view;
	MouseMotionHandler currentMML;
	Card currentCard;
	ColumnSpace currentSpace;
	
/* Constructor */
	public MouseHandler(GameTable table, GameView view) {
		this.table = table;
		this.view = view;
	}
	
/* Other methods */
	public void updateMouseHandler(GameTable table) {
		this.table = table;
	}
	
/* Overrides */
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int code = e.getButton();
		
		currentCard = null;
		currentSpace = null;
		
		if (code == MouseEvent.BUTTON1) { // LEFT MOUSE CLICK
			// check whether CoveredColumn has been clicked (Only in Klondike Mode)
			if (table instanceof KlondikeTable) {
				if (table.getCoveredColumn().isInside(view.viewToWorld(e.getPoint()))) {
					currentSpace = table.getCoveredColumn();
					ArrayList<Card> ccContent = currentSpace.getSpaceContent();
					
					if (!ccContent.isEmpty()) {
						Card card = ccContent.get(ccContent.size() - 1);
						currentSpace.removeCard(card);
						card.forceDrop(table.getShownColumn());
					} else {
						ArrayList<Card> scContent = table.getShownColumn().getSpaceContent();
						while (!scContent.isEmpty())
							scContent.get(scContent.size() - 1).forceDrop(currentSpace);
					}
					
					return;
				}
				else if (table.getCompleteBtn() != null) {
					if (table.getCompleteBtn().isInside(view.viewToWorld(e.getPoint())))
						table.completeTable();
				}
			}
			else if (table instanceof FreeCellTable) {
				table.completeTable();
			}
			
			// find a selected card (if any)
			for (DynamicBody body : table.getDynamicBodies()) {
				if (body instanceof Card)
					if (((Card) body).isInside(view.viewToWorld(e.getPoint())))
						currentCard = (Card) body;
			}
			
			// if card was selected it is possible to drag it
			if (currentCard != null) {
				ArrayList<Card> oldColumnContent = currentCard.getCurrentSpace().getSpaceContent();
				int cardIndex = oldColumnContent.indexOf(currentCard);
				
				if (table instanceof FreeCellTable) {
					if (currentCard.getCurrentSpace() instanceof FinalColumn ||
							cardIndex + 1 != oldColumnContent.size()) {
						currentCard = null;
						return;
					}
				}
				
				// Move card
				if (currentCard.isFrontSide()) {
					
					// update the last remaining card in the column to "forget" about it's lower card (this card)
					if (cardIndex - 1 >= 0)
						oldColumnContent.get(cardIndex - 1).setLowerCard(null);
					
					// create handler responsible for card movement
					currentMML = new MouseMotionHandler(view, currentCard);
					view.addMouseMotionListener(currentMML);
				}
			}
			
		
		} else { //RIGHT MOUSE CLICK
			if (code == MouseEvent.BUTTON3) {
			
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		ColumnSpace newSpace = null;
		
		// find a selected column
		for (StaticBody body : table.getStaticBodies())
			if (body instanceof ColumnSpace)
				if (((ColumnSpace) body).isInside(view.viewToWorld(e.getPoint())))
					newSpace = (ColumnSpace) body;
				
		if (currentCard != null)
			if (newSpace == null) {
				currentCard.drop(currentCard.getCurrentSpace());
			}
			else {
				currentCard.drop(newSpace);
			}
		
		view.removeMouseMotionListener(currentMML);
		
		if (table.getTableState().equals("solved")) {
			table.addCompleteBtn();
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
/* MouseMotionHandler */
	private class MouseMotionHandler implements MouseMotionListener {
		GameView view;
		Card card;
		
		public MouseMotionHandler(GameView view, Card card){
			this.view = view;
			this.card = card;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (card != null) {
				if (card.hasLowerCard())
					card.setPosition(new Vec2(view.viewToWorld(e.getPoint()).x,
							view.viewToWorld(e.getPoint()).y - Card.getCardDimensions().y/3));
				else
					card.setPosition(view.viewToWorld(e.getPoint()));
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
		
		}
	}
}

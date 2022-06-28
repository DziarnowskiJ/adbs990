package uk.ac.city.adbs990.cards;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.KlondikeCardColumn;

public class Card extends Walker {
/* Static fields */
    private static final float width = 85/20;
    private static final float height = 125/20;
    private static final Shape cardShape = new BoxShape(width/2, height/2);
    private static final Shape cardTop = new BoxShape(width/2, height/10, new Vec2(0, 4f*height/10));

/* Static getters */
    public static Vec2 getCardDimensions() {
        return new Vec2(width, height);
    }
    
/* Non-static fields */
    private final String number;
    private final String type;
    private final GameTable table;
    private boolean frontSide;
    private ColumnSpace currentSpace;
    private Card lowerCard;

/* Non-static getters */
    public String getNumber() {
        if (this != null)
            return number;
        else
            return null;
    }
    public String getType() {
        return type;
    }
    public ColumnSpace getCurrentSpace() {
        return currentSpace;
    }
    public Card getLowerCard() {
        return lowerCard;
    }
    
/* Non-static booleans */
    public boolean isFrontSide() {
        return frontSide;
    }
    public boolean hasLowerCard() {
        return lowerCard != null;
    }
    
/* Non-static setters */
    public void setCurrentSpace(ColumnSpace space) {
        this.currentSpace = space;
    }
    public void setLowerCard(Card lowerCard) {
        
        // remove all fixtures from the body
        for (Fixture fix : getFixtureList())
            if (fix instanceof GhostlyFixture)
                fix.destroy();
    
        this.lowerCard = lowerCard;
        
        // assign proper ghostly fixture
        if (lowerCard != null)
            new GhostlyFixture(this, cardTop);
        else
            new GhostlyFixture(this, cardShape);
    }
    
/* Constructor */
    /**
     * Constructor
     * @param table GameTable to which the card will be added
     * @param number Number of card in String form (A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K)
     * @param type Type of the card (diamonds, hearts, spades, clubs)
     * @param frontSide boolean value, determines showing back or front of the card
     */
    public Card(GameTable table, String number, String type, boolean frontSide) {
        super(table);
        this.number = number;
        this.type = type;
        this.frontSide = frontSide;
        this.table = table;
        lowerCard = null;
        setClipped(true);
        
        new GhostlyFixture(this, cardShape);
        
        updateImage();
        
        setGravityScale(0);
        setLinearVelocity(new Vec2(0, 0));
    }
    
/* Boolean methods */
    /**
     * Determines whether point is inside the Card body
     * @param vec - point to be checked
     * @return true if point is inside Card's bounds
     */
    public boolean isInside(Vec2 vec) {
        if (this.hasLowerCard()) {
            return (getPosition().x - width / 2 <= vec.x && vec.x <= getPosition().x + width / 2
                    && getPosition().y + height / 3 <= vec.y && vec.y <= getPosition().y + height / 2);
        } else {
            return (getPosition().x - width / 2 <= vec.x && vec.x <= getPosition().x + width / 2
                    && getPosition().y - height / 2 <= vec.y && vec.y <= getPosition().y + height / 2);
        }
    }
    
    /**
     * Tests whether color of the card is red or black
     * @return true if card.type = "diamonds" || "hearts" else return false
     */
    public boolean isRed() {
        return type.equals("diamonds") || type.equals("hearts");
    }
    
/* Other methods */
    /**
     * Changes the image of the card between frontSide and backSide
     */
    public void swapSide() {
        frontSide = !frontSide;
        updateImage();
    }
    
    /**
     * Updates the image of the card accordingly to its:
     * - type
     * - number
     * - side shown (back / front)
     */
    public void updateImage() {
        //remove all previous images
        removeAllImages();
        
        // set proper image according to cards type, number and side shown
        if (frontSide)
            addImage(new BodyImage(
                    "data/graphics/cards/" + type + "/" + number + ".png",
                    6));
        else
            addImage(new BodyImage(
                    "data/graphics/cards/backside.png",
                    6));
    }
    
    /**
     * Drops a card in a specified column
     * @param space Column the card will be placed in
     */
    public void forceDrop(ColumnSpace space) {
        if (currentSpace != null) {
            currentSpace.removeCard(this);
            
            if (currentSpace.getSpaceContent().size() > 0 && currentSpace instanceof KlondikeCardColumn) {
                Card upperCard = currentSpace.getSpaceContent().get(currentSpace.getSpaceContent().size() - 1);
                if (!upperCard.isFrontSide() && currentSpace != space)
                    upperCard.swapSide();
            }
        }
        
        currentSpace = space;
        space.addCard(this);
    }
    
    /**
     * Checks whether card can be added to the column:
     * - can be added -> uses forceDrop() method and adds a card to this column
     * - cannot be added -> puts the card in the column it already was
     * @param columnSpace Column the card will be placed in (if possible)
     */
    public void drop(ColumnSpace columnSpace) {
        if (columnSpace.canBeAdded(this) || columnSpace == currentSpace) {
            forceDrop(columnSpace);
    
            if (hasLowerCard())
                lowerCard.drop(columnSpace);
        }
        else
            drop(currentSpace);
    }
   
/* Overrides */
    /**
     * Override return type of getWorld()
     * Instead of World return GameTable
     * @return GameTable
     */
    @Override
    public GameTable getWorld() {
        return ((GameTable)super.getWorld());
    }
    
    /**
     * Sets position of the card.
     * If card has a lower card it recursively sets its position as well.
     * @param position New position of the card
     */
    @Override
    public void setPosition(Vec2 position) {
        if (hasLowerCard()) {
            lowerCard.setPosition(new Vec2(position.x, position.y - height / 6));
        }
        super.setPosition(position);
    }
    
    /**
     * Overrides standard toString() method, prints Card's details
     * @return "Card{number, type, isFrontSide, hasLowerCard, columnSpaceType}"
     */
    @Override
    public String toString() {
        return "Card{" +
                "number='" + number +
                "', type='" + type +
                "', isFrontSide='" + isFrontSide() +
                "', hasLowerCard='" + hasLowerCard() +
                "', columnSpace='" + currentSpace.getType() +
                "'}";
    }
}

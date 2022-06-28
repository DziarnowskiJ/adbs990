package uk.ac.city.adbs990.controls;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.table.GameTable;

public class CompleteBtn extends StaticBody {

/* Static fields */
	private static final Vec2 btnPos = new Vec2(-5, 10);
	private static final float width = 5;
	private static final float height = 2;
	
/* Constructor */
	public CompleteBtn(GameTable table) {
		super(table, new BoxShape(width/2, height/2));
		setPosition(btnPos);
		addImage(new BodyImage(
				"data/graphics/others/completeBtn.png", 2));
	}
	
/* Other methods */
	/**
	 * Determines whether point is inside the button's body
	 * @param vec - point to be checked
	 * @return true if point is inside button's bounds
	 */
	public boolean isInside(Vec2 vec) {
		return (getPosition().x - width/2 <= vec.x && vec.x <= getPosition().x + width/2
				&& getPosition().y - height/2 <= vec.y && vec.y <= getPosition().y + height/2);
	}
}

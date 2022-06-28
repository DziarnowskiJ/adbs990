package uk.ac.city.adbs990.table.tableParts.freeCellParts;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;

public class FreeColumn extends ColumnSpace {
	public FreeColumn(GameTable table, Vec2 spacePos) {
		super(table, "FreeColumn", spacePos);
	}
	
	@Override
	public boolean canBeAdded(Card card) {
		return spaceContent.isEmpty() && !card.hasLowerCard();
	}
}

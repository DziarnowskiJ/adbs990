package uk.ac.city.adbs990.tools;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;

import uk.ac.city.adbs990.cards.Deck;
import uk.ac.city.adbs990.table.FreeCellTable;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.cards.Card;
import uk.ac.city.adbs990.table.KlondikeTable;
import uk.ac.city.adbs990.table.tableParts.CardColumn;
import uk.ac.city.adbs990.table.tableParts.ColumnSpace;
import uk.ac.city.adbs990.table.tableParts.klondikeParts.KlondikeCardColumn;
import uk.ac.city.adbs990.table.tableParts.FinalColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

public class SequentialComplete implements StepListener {
	
	private final List<String> numbers = Arrays.asList(Deck.getNumbers());
	private int counter;
	private final GameTable table;
	
	public SequentialComplete(GameTable table) {
		this.table = table;
		counter = 0;
	}
	
	@Override
	public void preStep(StepEvent stepEvent) {
		counter++;
		if (table instanceof KlondikeTable) {
			if (table.getTableState().equals("solved")) {
				if (counter % 20 == 0)
					for (CardColumn col : table.getCardColumns())
						if (!col.getSpaceContent().isEmpty()) {
							Card card = col.getSpaceContent().get(col.getSpaceContent().size() - 1);
							for (FinalColumn colFin : table.getFinalColumns())
								card.drop(colFin);
							
						}
			}
		}
		else if (table instanceof FreeCellTable) {
			ArrayList<Card> spaceContent;
			int diamondsIndex = -1;
			int heartsIndex = -1;
			int spadesIndex = -1;
			int clubsIndex = -1;
			//determine index of each final column
			for (FinalColumn finCol : table.getFinalColumns()) {
				spaceContent = finCol.getSpaceContent();
				if (!spaceContent.isEmpty()) {
					switch (finCol.getType()) {
						case "FinalColumnDIAMONDS" -> diamondsIndex = numbers.indexOf(finCol.getLastCard().getNumber());
						case "FinalColumnHEARTS" -> heartsIndex = numbers.indexOf(finCol.getLastCard().getNumber());
						case "FinalColumnSPADES" -> spadesIndex = numbers.indexOf(finCol.getLastCard().getNumber());
						case "FinalColumnCLUBS" -> clubsIndex = numbers.indexOf(finCol.getLastCard().getNumber());
					}
				}
			}
			
			int redIndex = min(diamondsIndex, heartsIndex);
			int blackIndex = min(clubsIndex, spadesIndex);
			
			for (CardColumn col : table.getCardColumns()) {
				if (counter % 30 == 0) {
					if (!col.getSpaceContent().isEmpty()) {
						if (counter % 60 == 0) {
							Card lastCard = col.getLastCard();
							int lastCardIndex = numbers.indexOf(lastCard.getNumber());
							for (FinalColumn finCol : table.getFinalColumns()) {
								switch (lastCard.getType()) {
									case "diamonds":
										if (diamondsIndex + 1 == lastCardIndex && blackIndex + 1 >= lastCardIndex)
											lastCard.drop(finCol);
										break;
									case "hearts":
										if (heartsIndex + 1 == lastCardIndex && blackIndex + 1 >= lastCardIndex)
											lastCard.drop(finCol);
										break;
									case "spades":
										if (spadesIndex + 1 == lastCardIndex && redIndex + 1 >= lastCardIndex)
											lastCard.drop(finCol);
										break;
									case "clubs":
										if (clubsIndex + 1 == lastCardIndex && redIndex + 1 >= lastCardIndex)
											lastCard.drop(finCol);
										break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void postStep(StepEvent stepEvent) {
	
	}
}

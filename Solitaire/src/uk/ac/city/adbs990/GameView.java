package uk.ac.city.adbs990;

import city.cs.engine.UserView;
import city.cs.engine.World;
import uk.ac.city.adbs990.table.GameTable;
import uk.ac.city.adbs990.table.KlondikeTable;

import java.awt.*;

public class GameView extends UserView {
    
    private GameTable table;
    
    public GameTable getGameTable() {
        return table;
    }
    public void setGameTable(GameTable table) {
        setWorld((World)table);
        this.table = table;
    }
    
    public GameView(GameTable table, int width, int height) {
        super(table, width, height);
        this.table = table;
    }
    
    @Override
    protected void paintForeground(Graphics2D g) {
        if (table.getTableState().equals("completed")) {
            g.setFont(new Font("Stencil", Font.BOLD, 48));
            g.drawString("CONGRATULATIONS!",280 , 300);
        }
    }
}

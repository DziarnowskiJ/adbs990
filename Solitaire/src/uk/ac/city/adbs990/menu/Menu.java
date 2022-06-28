package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;

import javax.swing.*;
import java.awt.*;

public abstract class Menu extends JPanel {
	// Non-static fields
	 private final Game game;
	 
	 public Menu(Game game) {
		 this.game = game;
		
		 setPreferredSize(new Dimension(1100, 600));
		 setBackground(new Color(161, 170, 181));
		 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	 }
}

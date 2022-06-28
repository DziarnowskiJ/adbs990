package uk.ac.city.adbs990.controls;

import org.jbox2d.common.Vec2;
import uk.ac.city.adbs990.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {

/* Non-static fields */
	Game game;
	
/* Constructor */
	public KeyboardHandler(Game game) {
		this.game = game;
	}
	
	/**
	 * Determines the action depending on the key pressed
	 * <p>
	 *     Depending on the key pressed there are few effects: <br>
	 *     - ESCAPE -> Opens game menu <br>
	 *     - SPACE -> If possible - auto complete <br>
	 * </p>
	 * @param e KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_ESCAPE) {                  // open menu (click ESC)
			if (game.getPanelNumber() == 0) {
				game.switchPanels(1);
			}
		} else if (code == KeyEvent.VK_SPACE) {            // auto-complete table (if CompleteBtn exists)
			if (game.getGameTable().getCompleteBtn() != null)
				game.getGameTable().completeTable();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
}

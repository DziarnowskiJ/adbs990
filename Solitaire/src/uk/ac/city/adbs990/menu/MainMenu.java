package uk.ac.city.adbs990.menu;

import uk.ac.city.adbs990.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends Menu {
	
	// Components
	private JLabel title;
	
	private JPanel btnPanel;
	
	private JLabel btnText;
	private JButton klondikeButton;
	private JButton continueButton;
//	private JButton spiderButton;
	private JButton freeCellButton;
	
	public MainMenu(Game game) {
		super(game);
		
		//create components
		title = new JLabel();
		btnPanel = new JPanel();
		
		btnText = new JLabel();
		continueButton = new JButton();
		klondikeButton = new JButton();
//		spiderButton = new JButton();
		freeCellButton = new JButton();
		
		//add components to the menu
		add(Box.createVerticalGlue());
		this.add(title);
		add(Box.createVerticalGlue());
		this.add(btnPanel);
		btnPanel.add(continueButton);
		btnPanel.add(Box.createVerticalGlue());
		btnPanel.add(btnText);
		btnPanel.add(Box.createVerticalGlue());
		btnPanel.add(klondikeButton);
		btnPanel.add(Box.createVerticalGlue());
//		btnPanel.add(spiderButton);
//		btnPanel.add(Box.createVerticalGlue());
		btnPanel.add(freeCellButton);
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		
		// TITLE
		title.setText("My Solitaire");
		title.setFont(new Font("Stencil", Font.BOLD, 48));
		title.setForeground(Color.WHITE);
		
		// PANEL WITH BUTTONS
		btnPanel.setBackground(new Color(161, 170, 181));
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
		
		btnText.setText("Chose game type:");
		btnText.setFont(new Font("Stencil", Font.BOLD, 24));
		btnText.setForeground(Color.WHITE);
		
		continueButton.setText("Continue Game");
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchPanels(0);
			}
		});
		
		klondikeButton.setText("Klondike");
		klondikeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.newGame(0);
				game.switchPanels(0);
			}
		});

		freeCellButton.setText("Free Cell");
		freeCellButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.newGame(1);
				game.switchPanels(0);
			}
		});
		
		// FOR ALL COMPONENTS
		for (Component component : this.getComponents()) {
			//align all components to the center
			((JComponent)component).setAlignmentX(Component.CENTER_ALIGNMENT);
			if (component instanceof JPanel)
				for (Component component2 : ((JPanel) component).getComponents())
					((JComponent)component2).setAlignmentX(Component.CENTER_ALIGNMENT);
			//change background color for all components
//			component.setBackground(new Color(255, 222, 222));
		}
	}
}

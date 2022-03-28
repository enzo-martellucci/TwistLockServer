package com.twistlock.view;

import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;

public class PanelDocker extends JPanel
{
	// Constants
	public static final char ODD  = 'O';
	public static final char EVEN = 'E';


	// Attributes
	private Game game;
	private char parity;


	// Constructor
	public PanelDocker(Game game, char parity)
	{
		this.game   = game;
		this.parity = parity;
	}


	// Methods
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(Color.BLUE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}

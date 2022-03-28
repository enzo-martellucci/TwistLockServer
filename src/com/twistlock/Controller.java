package com.twistlock;

import com.twistlock.model.Game;
import com.twistlock.view.ViewCUI;

public class Controller
{
	// Attributes
	private Game    game;
	private ViewCUI viewCUI;


	// Constructor
	public Controller(String[] lstName, int nbLine, int nbCol)
	{
		// Initialisation
		this.game    = new Game(lstName, nbLine, nbCol);
		this.viewCUI = new ViewCUI(this.game);

		// Playing
		this.viewCUI.showGame();
		while (!this.game.isGameOver())
		{
			String[] position = this.viewCUI.play();

			int line   = Integer.parseInt(position[0]) - 1;
			int col    = position[1].charAt(0) - 'A';
			int corner = Integer.parseInt(position[2]);

			this.game.play(line, col, corner);
			this.viewCUI.showGame();
		}
	}


	// Main
	public static void main(String[] args)
	{
		new Controller(new String[]{ "J1", "J2" }, 10, 7);
	}
}

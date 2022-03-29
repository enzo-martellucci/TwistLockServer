package com.twistlock;

import com.twistlock.model.Game;
import com.twistlock.view.ViewGUI;

public class Controller
{
	// Attributes
	private Game    game;
	private ViewGUI viewGUI;


	// Constructor
	public Controller(String[] lstName, int nbLine, int nbCol)
	{
		// Initialisation
		this.game    = new Game(lstName, nbLine, nbCol);
		this.viewGUI = new ViewGUI(this, this.game);
	}


	// Methods
	public void play(int l, int c)
	{
		this.game.play(l, c);
		this.viewGUI.maj();

		if (this.game.isGameOver())
			this.viewGUI.end(this.game.getWinner());
	}


	// Main
	public static void main(String[] args)
	{
		args = new String[]{ "Antoine", "Enzo" };

		if (args.length < 2 || args.length > 4)
		{
			System.out.println("Number of dockers should be between 2 and 4.");
			return;
		}

		int line = 4 + (int) (Math.random() * 6);
		int col  = 4 + (int) (Math.random() * 6);

		new Controller(args, line, col);
	}
}

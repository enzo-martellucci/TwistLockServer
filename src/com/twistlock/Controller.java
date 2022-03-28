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
		char action = ' ';
		while (action != 'Q')
		{
			action = this.viewCUI.enterAction();
			switch (action)
			{
				case 'S' -> this.viewCUI.showGame();
				case 'P' -> this.viewCUI.notImplemented();
			}
		}

		this.viewCUI.quit();
	}


	// Methods


	// Main
	public static void main(String[] args)
	{
		new Controller(new String[]{ "J1", "J2" }, 10, 7);
	}
}

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
	}


	// Main
	public static void main(String[] args)
	{
		new Controller(new String[]{ "J1", "J2", "J3", "J4" }, 10, 7);
	}
}

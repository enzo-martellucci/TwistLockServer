package com.twistlock;

import com.twistlock.model.Game;
import com.twistlock.net.Network;
import com.twistlock.view.ViewGUI;

public class Controller
{
	// Attributes
	private Game    game;
	private ViewGUI viewGUI;
	private Network network;


	// Constructor
	public Controller(int port, int nbDocker)
	{
		// Initialisation
		this.game    = new Game(nbDocker);
		this.viewGUI = new ViewGUI(this, this.game);
		this.network = new Network(this, this.game, port);
	}


	// Methods
	public void addDocker(String name)
	{
		this.game.addDocker(name);
		this.viewGUI.maj();
	}

	public int play(int l, int c, int corner)
	{
		int validity = this.game.play(l, c, corner);
		this.viewGUI.maj();
		return validity;
	}

	public void end()
	{
		this.viewGUI.end(this.game.getWinner());
	}


	// Main
	public static void main(String[] args)
	{
		args = new String[]{ "8000", "2" };
		if (args.length != 2)
		{
			System.out.println("Usage : Controller port nbDocker");
			return;
		}

		new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}

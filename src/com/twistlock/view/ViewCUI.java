package com.twistlock.view;

import com.twistlock.model.Docker;
import com.twistlock.model.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ViewCUI
{
	// Constants
	public static final  String                 RESET  = "\u001B[0m";
	private static final Map<Character, String> MAP_FG = new HashMap<>();
	private static final Map<Character, String> MAP_BG = new HashMap<>();

	static
	{
		MAP_FG.put('.', RESET);
		MAP_FG.put('R', "\u001B[31m");
		MAP_FG.put('G', "\u001B[32m");
		MAP_FG.put('B', "\u001B[34m");
		MAP_FG.put('Y', "\u001B[33m");

		MAP_BG.put('.', RESET);
		MAP_BG.put('R', "\u001B[41m");
		MAP_BG.put('G', "\u001B[42m");
		MAP_BG.put('B', "\u001B[44m");
		MAP_BG.put('Y', "\u001B[43m");
	}

	// Attributes
	private Game    game;
	private Scanner input;


	// Constructor
	public ViewCUI(Game game)
	{
		this.game  = game;
		this.input = new Scanner(System.in);
	}


	// Methods
	public char enterAction()
	{
		System.out.println("What to do :");
		System.out.println("[S] Show the game");
		System.out.println("[P] Play");
		System.out.println("[Q] Quit");

		System.out.print("Your choice : ");
		return Character.toUpperCase(this.input.next().charAt(0));
	}

	public void showGame()
	{
		StringBuilder sbGame = new StringBuilder();


		// Adding the grid (corner and value)
		int[][]  gridValue  = this.game.getGridValue();
		char[][] gridColor  = this.game.getGridColor();
		char[][] gridCorner = this.game.getGridCorner();

		// Construct corner and value string, and adding them
		StringBuilder sbCorner, sbValue;
		for (int l = 0, c; l < gridValue.length; l++)
		{
			sbCorner = new StringBuilder();
			sbValue  = new StringBuilder();
			for (c = 0; c < gridValue[l].length; c++)
			{
				sbCorner.append(String.format("%s%c%s--", MAP_FG.get(gridCorner[l][c]), gridCorner[l][c], RESET));
				sbValue.append(String.format("|%s%2d%s", MAP_BG.get(gridColor[l][c]), gridValue[l][c], RESET));
			}
			sbCorner.append(String.format("%s%c%s", MAP_FG.get(gridCorner[l][c]), gridCorner[l][c], RESET));
			sbValue.append('|');

			sbGame.append(sbCorner);
			sbGame.append('\n');
			sbGame.append(sbValue);
			sbGame.append('\n');
		}

		sbCorner = new StringBuilder();
		sbCorner.append(gridCorner[gridCorner.length - 1][0]);

		for (int l = gridCorner.length - 1, c = 1; c < gridCorner[l].length; c++)
		     sbCorner.append(String.format("--%s%c%s", MAP_FG.get(gridCorner[l][c]), gridCorner[l][c], RESET));

		sbGame.append(sbCorner);
		sbGame.append('\n');

		// Adding the docker
		Docker[] lstDocker = this.game.getLstDocker();
		int      docker    = this.game.getDocker();

		String color, name;
		int    nbLock, pts;
		for (int i = 0; i < lstDocker.length; i++)
		{
			color  = MAP_FG.get(lstDocker[i].getColor());
			name   = lstDocker[i].getName();
			nbLock = lstDocker[i].getNbLock();
			pts    = lstDocker[i].getScore();
			sbGame.append(String.format("%s%-8s (%2d twist lock) : %3d pts%s\n", color, name, nbLock, pts, RESET));
		}

		System.out.println(sbGame.toString());
	}

	public void notImplemented()
	{
		System.out.println("Not implemented yet!");
	}

	public void quit()
	{
		System.out.println("Good bye!");
	}
}

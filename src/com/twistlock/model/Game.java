package com.twistlock.model;

import java.util.Arrays;

public class Game
{
	// Constants
	public static final char   NEUTRAL   = '.';
	public static final char[] LST_COLOR = new char[]{ 'R', 'G', 'B', 'Y' };


	// Attributes
	private Docker[] lstDocker;
	private int      docker;

	private int[][]  gridValue;
	private char[][] gridColor;
	private char[][] gridCorner;

	private int line;
	private int col;
	private int corner;


	// Constructor
	public Game(String[] lstName, int nbLine, int nbCol)
	{
		// Dockers initialisation
		this.lstDocker = new Docker[lstName.length];
		for (int i = 0; i < this.lstDocker.length; i++)
		     this.lstDocker[i] = new Docker(LST_COLOR[i], lstName[i]);

		// Grids initialisation
		this.gridValue  = new int[nbLine][nbCol];
		this.gridColor  = new char[nbLine][nbCol];
		this.gridCorner = new char[nbLine + 1][nbCol + 1];

		for (int l = 0; l < this.gridColor.length; l++)
		     Arrays.fill(this.gridColor[l], NEUTRAL);

		for (int l = 0; l < this.gridCorner.length; l++)
		     Arrays.fill(this.gridCorner[l], NEUTRAL);
	}


	// Getters
	public Docker[] getLstDocker() { return this.lstDocker; }
	public int getDocker()          { return this.docker; }

	public int[][] getGridValue()   { return this.gridValue; }
	public char[][] getGridColor()  { return this.gridColor; }
	public char[][] getGridCorner() { return this.gridCorner; }


	// Methods
	public void chooseLine(String lineS)
	{
	}
	public void chooseCol(String col)
	{
	}
	public void chooseCorner(String corn)
	{
	}
}

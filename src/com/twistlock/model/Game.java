package com.twistlock.model;

import java.util.Arrays;
import java.util.Random;

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

	private boolean gameOver;


	// Constructor
	public Game(String[] lstName, int nbLine, int nbCol)
	{
		// Dockers initialisation
		this.lstDocker = new Docker[lstName.length];
		for (int i = 0; i < this.lstDocker.length; i++)
		     this.lstDocker[i] = new Docker(LST_COLOR[i], lstName[i]);

		// Grids initialisation
		Random random = new Random();
		this.gridValue  = new int[nbLine][nbCol];
		this.gridColor  = new char[nbLine][nbCol];
		this.gridCorner = new char[nbLine + 1][nbCol + 1];

		for (int l = 0; l < this.gridValue.length; l++)
			for (int c = 0; c < this.gridValue[0].length; c++)
			     this.gridValue[l][c] = 5 + random.nextInt(49);

		for (int l = 0; l < this.gridColor.length; l++)
		     Arrays.fill(this.gridColor[l], NEUTRAL);

		for (int l = 0; l < this.gridCorner.length; l++)
		     Arrays.fill(this.gridCorner[l], NEUTRAL);

		this.gameOver = true;
	}


	// Getters
	public Docker[] getLstDocker() { return this.lstDocker; }
	public int getDocker()          { return this.docker; }

	public int[][] getGridValue()   { return this.gridValue; }
	public char[][] getGridColor()  { return this.gridColor; }
	public char[][] getGridCorner() { return this.gridCorner; }


	// Methods
	public void play(int l, int c, int corner)
	{
		int lCorn = l;
		int cCorn = c;

		if (corner == 2)
			cCorn++;
		else if (corner == 4)
			lCorn++;
		else if (corner == 3)
		{
			lCorn++;
			cCorn++;
		}

		if (l < 0 || l > this.gridValue.length - 1 || c < 0 || c > this.gridValue[0].length - 1 || corner < 1 || corner > 4 || this.gridCorner[lCorn][cCorn] != NEUTRAL)
		{
			this.lstDocker[this.docker].removeLock(2);
			this.nextDocker();
			return;
		}

		this.lstDocker[this.docker].removeLock(1);
		this.placeCorner(lCorn, cCorn);
		this.nextDocker();
	}

	private void placeCorner(int lCorn, int cCorn)
	{
		int  cpt;
		char color    = ' ';
		int  maxValue = 0;

		this.gridCorner[lCorn][cCorn] = this.lstDocker[this.docker].getColor();

		int lMin = Math.max(0, lCorn - 1);
		int lMax = Math.min(this.gridColor.length, lCorn + 1);
		int cMin = Math.max(0, cCorn - 1);
		int cMax = Math.min(this.gridColor[0].length, cCorn + 1);
		for (int l = lMin; l < lMax; l++)
		{
			for (int c = cMin; c < cMax; c++)
			{
				maxValue = 0;
				for (int d = 0; d < this.lstDocker.length; d++)
				{
					cpt = 0;
					if (this.gridCorner[l][c] == this.lstDocker[d].getColor()) cpt++;
					if (this.gridCorner[l][c + 1] == this.lstDocker[d].getColor()) cpt++;
					if (this.gridCorner[l + 1][c] == this.lstDocker[d].getColor()) cpt++;
					if (this.gridCorner[l + 1][c + 1] == this.lstDocker[d].getColor()) cpt++;

					if (cpt == maxValue)
						color = NEUTRAL;
					else if (cpt > maxValue)
					{
						maxValue = cpt;
						color    = this.lstDocker[d].getColor();
					}
				}
				this.gridColor[l][c] = color;
			}
		}
	}

	public void nextDocker()
	{
		this.majGameOver();
		if (this.gameOver)
			return;

		do
		{
			this.docker = (docker + 1) % this.lstDocker.length;
		}
		while (this.lstDocker[this.docker].getNbLock() == 0);
	}

	private void majGameOver()
	{
		boolean padlockLeft = false;
		boolean cornerFree  = false;

		for (int i = 0; i < this.lstDocker.length; i++)
			if (this.lstDocker[i].getNbLock() > 0)
			{
				padlockLeft = true;
				break;
			}


		exit:
		for (int l = 0; l < this.gridCorner.length; l++)
			for (int c = 0; c < this.gridCorner.length; c++)
				if (this.gridCorner[l][c] == NEUTRAL)
				{
					cornerFree = true;
					break exit;
				}

		this.gameOver = !cornerFree || !padlockLeft;
	}
}

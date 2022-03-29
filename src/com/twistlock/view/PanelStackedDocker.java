package com.twistlock.view;

import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;

public class PanelStackedDocker extends JPanel
{
	private static int           num = 0;
	private        PanelDocker[] gridPanels;

	public PanelStackedDocker(Game game, int nbDocker)
	{
		this.gridPanels = new PanelDocker[nbDocker];
		this.setLayout(new GridLayout(nbDocker, 1));
		for (int i = 0; i < nbDocker; i++)
		{
			this.gridPanels[i] = new PanelDocker(game, num++);
			this.add(this.gridPanels[i]);
		}
	}

	public void maj()
	{
		for (PanelDocker pp : this.gridPanels)
			pp.maj();
	}
}

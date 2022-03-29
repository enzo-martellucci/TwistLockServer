package com.twistlock.view;

import com.twistlock.Controller;
import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ViewGUI extends JFrame
{
	// Attributes
	private PanelStackedDocker panelDockerL;
	private PanelStackedDocker panelDockerR;
	private PanelGrid          panelGrid;


	// Constructor
	public ViewGUI(Controller ctrl, Game game)
	{
		// Create and add Panel
		int nbDocker = game.getLstDocker().length;
		this.panelDockerL = new PanelStackedDocker(game, nbDocker / 2);
		nbDocker -= nbDocker / 2;
		this.panelDockerR = new PanelStackedDocker(game, nbDocker);
		this.panelGrid    = new PanelGrid(ctrl, game);

		this.add(this.panelDockerL, BorderLayout.WEST);
		this.add(this.panelDockerR, BorderLayout.EAST);
		this.add(this.panelGrid, BorderLayout.CENTER);


		// Parameters
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				ViewGUI.this.panelDockerL.setPreferredSize(new Dimension((int) (0.15 * ViewGUI.this.getWidth()), ViewGUI.this.getHeight()));
				ViewGUI.this.panelDockerR.setPreferredSize(new Dimension((int) (0.15 * ViewGUI.this.getWidth()), ViewGUI.this.getHeight()));
				ViewGUI.this.panelDockerL.repaint();
				ViewGUI.this.panelDockerR.repaint();
			}
		});

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (0.8 * screen.width), (int) (0.8 * screen.height));
		this.setLocation((int) (0.1 * screen.width), (int) (0.1 * screen.height));

		this.setTitle("Twist Lock");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}


	// Methods
	public void maj()
	{
		this.panelDockerL.maj();
		this.panelDockerR.maj();
		this.panelGrid.repaint();
	}
}

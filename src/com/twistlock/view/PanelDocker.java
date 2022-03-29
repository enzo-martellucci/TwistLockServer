package com.twistlock.view;

import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;

public class PanelDocker extends JPanel
{
	private int    color;
	private JLabel lblScore;
	private Game   game;

	public PanelDocker(Game game, int color)
	{
		this.color = color;
		this.game  = game;

		this.setLayout(new BorderLayout());

		this.lblScore = new JLabel("Score : 0");

		this.add(new JLabel(this.game.getLstDocker()[this.color].getName()), BorderLayout.NORTH);
		this.add(this.lblScore, BorderLayout.SOUTH);
		this.add(new JLabel(new ImageIcon(new ImageIcon("./pics/shape_" + this.color + ".gif").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT))));
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
	}

	public void maj()
	{
		System.out.println(color);
		this.lblScore.setText("Score : " + this.game.getLstDocker()[this.color].getScore());
	}
}

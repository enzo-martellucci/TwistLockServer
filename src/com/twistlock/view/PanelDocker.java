package com.twistlock.view;

import com.twistlock.model.Docker;
import com.twistlock.model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;

import static com.twistlock.Parameter.*;

public class PanelDocker extends JPanel
{
	// Attributes
	private Game   game;
	private Docker docker;

	private Image picture;

	public PanelDocker(Game game, Docker docker)
	{
		this.game   = game;
		this.docker = docker;

		try
		{
			this.picture = ImageIO.read(this.getClass().getClassLoader().getResource("images/shape" + docker.getColor() + ".gif"));
		}
		catch (IOException e) { e.printStackTrace(); }
		this.maj();
	}

	public void paintComponent(Graphics gSrc)
	{
		super.paintComponent(gSrc);
		Graphics2D g = (Graphics2D) gSrc;

		FontMetrics metrics = g.getFontMetrics();
		int         x       = 0;
		int         y       = 0;

		/* Background */
		g.setFont(FONT);
		g.setStroke(STROKE);
		g.setColor(DOCKER_BG);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		/* Name */
		x += PADDING_DOCKER;
		y += PADDING_DOCKER + metrics.getHeight();
		g.setColor(Color.BLACK);
		g.drawString(this.docker.getName(), x, y);

		/* Picture */
		Image img = this.picture.getScaledInstance(-1, this.getHeight() - 6 * PADDING_DOCKER - 2 * metrics.getHeight(), Image.SCALE_SMOOTH);
		y += 2 * PADDING_DOCKER;
		g.drawImage(img, x, y, null);

		/* Twist Lock */
		g.setColor(OWNER.get(this.docker.getColor()));

		int oX   = x + img.getWidth(null) + PADDING_DOCKER + DOCKER_LOCK_D / 2;
		int maxX = this.getWidth() - PADDING_DOCKER - DOCKER_LOCK_D / 2;

		int xLock = oX;
		int yLock = y + DOCKER_LOCK_D / 2;
		for (int i = this.docker.getNbLock(); i > 0; i--)
		{
			if (xLock > maxX)
			{
				xLock = oX;
				yLock += PADDING_DOCKER + DOCKER_LOCK_D / 2;
			}
			g.fillOval(xLock, yLock, DOCKER_LOCK_D, DOCKER_LOCK_D);
			xLock += PADDING_DOCKER + DOCKER_LOCK_D / 2;
		}

		/* Score */
		y += img.getHeight(null) + 2 * PADDING_DOCKER + metrics.getHeight();
		g.setColor(Color.BLACK);
		g.drawString("Score : " + this.docker.getScore(), x, y);
	}

	public void maj()
	{
		if (this.game.getLstDocker()[this.game.getDocker()] == this.docker)
			this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		else
			this.setBorder(null);

		this.repaint();
	}
}

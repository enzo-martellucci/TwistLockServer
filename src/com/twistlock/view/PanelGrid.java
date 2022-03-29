package com.twistlock.view;

import com.twistlock.Controller;
import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class PanelGrid extends JPanel
{
	// Constants
	private static final Font FONT = new Font("Arial", Font.BOLD, 20);

	private static final int PADDING  = 20;
	private static final int SPACING  = 6;
	private static final int RADIUS   = 10;
	private static final int DIAMETER = RADIUS * 2;

	private static final Stroke                STROKE     = new BasicStroke(2);
	private static final Color                 BACKGROUND = new Color(36, 21, 113);
	private static final Color                 BORDER     = Color.BLACK;
	private static final Map<Character, Color> OWNER      = new HashMap<>();

	static
	{
		OWNER.put('.', Color.WHITE);
		OWNER.put('R', new Color(168, 22, 32));
		OWNER.put('G', new Color(87, 157, 28));
		OWNER.put('B', new Color(114, 159, 207));
		OWNER.put('Y', new Color(236, 240, 0));
	}


	// Attributes
	private Controller ctrl;
	private Game       game;

	private int oX;
	private int oY;
	private int maxX;
	private int maxY;

	private int containerW;
	private int containerH;


	// Constructor
	public PanelGrid(Controller ctrl, Game game)
	{
		this.ctrl = ctrl;
		this.game = game;

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();

				if (x < PanelGrid.this.oX || x > PanelGrid.this.maxX || y < PanelGrid.this.oY || y > PanelGrid.this.maxY)
					return;

				int closestX = x - (x - PanelGrid.this.oX) % (containerW + SPACING);
				int closestY = y - (y - PanelGrid.this.oY) % (containerH + SPACING);
				x -= RADIUS;
				y -= RADIUS;

				double distance = Math.sqrt(Math.pow(x - closestX, 2) + Math.pow(y - closestY, 2));
				if (distance <= RADIUS)
				{
					int l = (closestY - PanelGrid.this.oY) / containerH;
					int c = (closestX - PanelGrid.this.oX) / containerW;
					PanelGrid.this.ctrl.play(l, c);
				}
			}
		});
	}


	// Methods
	public void paintComponent(Graphics gSrc)
	{
		super.paintComponent(gSrc);
		Graphics2D g = (Graphics2D) gSrc;

		/* Calculus */
		char[][] gridColor = this.game.getGridColor();
		int[][]  gridValue = this.game.getGridValue();
		char[][] gridLock  = this.game.getGridCorner();

		// Width for containers only
		int width  = this.getWidth() - (2 * PADDING + (gridColor[0].length - 1) * SPACING);
		int height = this.getHeight() - (2 * PADDING + (gridColor.length - 1) * SPACING);

		// Height and width of containers
		this.containerH = (int) Math.min((double) height / gridColor.length, width / (gridColor[0].length * 1.5));
		this.containerW = (int) (1.5 * containerH);

		// Origin and max to center the whole container & lock group
		this.oX   = PADDING + (width - gridColor[0].length * containerW) / 2;
		this.oY   = PADDING + (height - gridColor.length * containerH) / 2;
		this.maxX = this.oX + gridColor[0].length * (containerW + SPACING);
		this.maxY = this.oY + gridColor.length * (containerH + SPACING);

		// Shift for the value in the container
		int shiftValX = containerW / 2;
		int shiftValY = containerH / 2;

		/* Background */
		g.setStroke(STROKE);
		g.setFont(FONT);
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		/* Container */
		FontMetrics metrics = g.getFontMetrics();
		String      value;
		for (int l = 0, prevY = oY; l < gridColor.length; l++, prevY += containerH + SPACING)
			for (int c = 0, prevX = oX; c < gridColor[l].length; c++, prevX += containerW + SPACING)
			{
				g.setColor(OWNER.get(gridColor[l][c]));
				g.fillRect(prevX, prevY, containerW, containerH);
				g.setColor(BORDER);
				g.drawRect(prevX, prevY, containerW, containerH);

				value = "" + gridValue[l][c];
				g.drawString(value, prevX + shiftValX - metrics.stringWidth(value) / 2, prevY + shiftValY + metrics.getAscent() / 2);
			}

		/* Lock */
		oX = oX - DIAMETER / 2 - SPACING / 2;
		oY = oY - DIAMETER / 2 - SPACING / 2;
		for (int l = 0, prevY = oY; l < gridLock.length; l++, prevY += containerH + SPACING)
			for (int c = 0, prevX = oX; c < gridLock[l].length; c++, prevX += containerW + SPACING)
			{
				g.setColor(OWNER.get(gridLock[l][c]));
				g.fillOval(prevX, prevY, DIAMETER, DIAMETER);
				g.setColor(BORDER);
				g.drawOval(prevX, prevY, DIAMETER, DIAMETER);
			}
	}
}

package com.twistlock.view;

import com.twistlock.Controller;
import com.twistlock.model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelGrid extends JPanel
{
	// Constants
	private static final Font FONT = new Font("Arial", Font.BOLD, 20);

	private static final int PADDING  = 20;
	private static final int SPACING  = 6;
	private static final int DIAMETER = 20;

	private static final Stroke                STROKE     = new BasicStroke(2);
	private static final Color                 BACKGROUND = new Color(36, 21, 113);
	private static final Color                 BORDER     = Color.BLACK;
	private static final Map<Character, Color> OWNER      = new HashMap<>();

	static
	{
		OWNER.put('.', Color.WHITE);
		OWNER.put('R', new Color(169, 27, 13));
		OWNER.put('G', new Color(1, 50, 32));
		OWNER.put('B', new Color(19, 56, 90));
		OWNER.put('Y', new Color(255, 244, 79));
	}


	// Attributes
	private Controller ctrl;
	private Game       game;


	// Constructor
	public PanelGrid(Controller ctrl, Game game)
	{
		this.ctrl = ctrl;
		this.game = game;

		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
		int containerH = (int) Math.min((double) height / gridColor.length, width / (gridColor[0].length * 1.5));
		int containerW = (int) (1.5 * containerH);

		// Origin to center the whole container & lock group
		int originX = PADDING + (width - gridColor[0].length * containerW) / 2;
		int originY = PADDING + (height - gridColor.length * containerH) / 2;

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
		for (int l = 0, prevY = originY; l < gridColor.length; l++, prevY += containerH + SPACING)
			for (int c = 0, prevX = originX; c < gridColor[l].length; c++, prevX += containerW + SPACING)
			{
				g.setColor(OWNER.get(gridColor[l][c]));
				g.fillRect(prevX, prevY, containerW, containerH);
				g.setColor(BORDER);
				g.drawRect(prevX, prevY, containerW, containerH);

				value = "" + gridValue[l][c];
				g.drawString(value, prevX + shiftValX - metrics.stringWidth(value) / 2, prevY + shiftValY + metrics.getAscent() / 2);
			}

		/* Lock */
		originX = originX - DIAMETER / 2 - SPACING / 2;
		originY = originY - DIAMETER / 2 - SPACING / 2;
		for (int l = 0, prevY = originY; l < gridLock.length; l++, prevY += containerH + SPACING)
			for (int c = 0, prevX = originX; c < gridLock[l].length; c++, prevX += containerW + SPACING)
			{
				g.setColor(OWNER.get(gridLock[l][c]));
				g.fillOval(prevX, prevY, DIAMETER, DIAMETER);
				g.setColor(BORDER);
				g.drawOval(prevX, prevY, DIAMETER, DIAMETER);
			}
	}
}

package com.twistlock.net;

import com.twistlock.Controller;
import com.twistlock.model.Docker;
import com.twistlock.model.Game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.twistlock.Parameter.LST_FULL_COLOR;
import static com.twistlock.net.Message.*;

public class Network
{
	// Attributes
	private Controller ctrl;
	private Game       game;

	private DatagramSocket socket;

	private int           connected;
	private InetAddress[] lstIp;
	private int[]         lstPort;


	// Constructor
	public Network(Controller ctrl, Game game, int port)
	{
		this.ctrl = ctrl;
		this.game = game;

		try
		{
			this.socket = new DatagramSocket(port);
		}
		catch (SocketException e) { e.printStackTrace(); }

		this.connected   = 0;
		this.lstIp   = new InetAddress[this.game.getLstDocker().length];
		this.lstPort = new int[this.lstIp.length];

		System.out.println("Numéro du port : " + port);

		this.waitDocker();
		this.startGame();
		this.play();
	}


	// Methods
	public void waitDocker()
	{
		DatagramPacket packet;
		String         name;
		String         welcome;

		// Waiting players name and send welcome message
		for (int i = 0; i < this.lstIp.length; i++)
		{
			try
			{
				// Receive name and adding docker to the game
				packet = new DatagramPacket(new byte[512], 512);
				this.socket.receive(packet);
				name = new String(packet.getData());
				name = name.substring(0, name.indexOf(0));
				this.ctrl.addDocker(name);
				this.connected++;

				// Store Ip and Port
				this.lstIp[i]   = packet.getAddress();
				this.lstPort[i] = packet.getPort();

				// Send welcome message
				welcome = String.format(WELCOME, i, name, i, LST_FULL_COLOR[i]);
				this.send(welcome, i);

				// Print docker name
				System.out.println(name);
			}
			catch (Exception e)
			{
				this.lstIp[i] = null;
				this.connected--;
			}
		}
	}

	public void startGame()
	{
		String map           = this.generateMap();
		String lstDockerName = this.generateLstDockerName();

		String start;
		String id;

		// Send nbDocker, map, and id
		for (int i = 0; i < this.lstIp.length; i++)
		{
			if (this.lstIp[i] == null)
				continue;

			start = String.format(START, lstDockerName, map);
			this.send(start, i);
			id = String.format(ID, (i + 1));
			this.send(id, i);
		}

		// Print map and players
		System.out.println("MAP=" + map);
		Docker[] lstDocker = this.game.getLstDocker();
		for (int i = 0; i < lstDocker.length; i++)
		     System.out.printf("Joueur %d %-5s : %s\n", i + 1, LST_FULL_COLOR[i], lstDocker[i].getName());
	}

	public void play()
	{
		String request;
		int    validity;

		String position;
		int    line;
		int    col;
		int    corner;

		// Play will the game is not over and there is at least one docker connected
		int i = this.game.getDocker();
		while (!this.game.isGameOver() && this.connected != 0)
		{
			position = "0A0";

			// Send playing request
			if (this.lstIp[i] != null)
			{
				request = String.format(YOU_PLAY, LST_FULL_COLOR[i]);
				this.send(request, i);
			}

			// Receive played position
			if (this.lstIp[i] != null)
				position = this.receive(i, 128);

			// Parse the position and play it
			try
			{
				line   = Integer.parseInt("" + position.charAt(0)) - 1;
				col    = position.charAt(1) - 'A';
				corner = Integer.parseInt("" + position.charAt(2));
			}
			catch (Exception e) { line = col = corner = -1; }
			validity = this.ctrl.play(line, col, corner);

			switch (validity)
			{
				case 0:
					this.sendToAll(String.format(OPP_PLAY, position), i);
					break;
				case 1:
					this.send(YOU_ILLEGAL, i);
					this.sendToAll(OPP_ILLEGAL, i);
					break;
				case 2:
					this.send(CANT_PLAY, i);
					break;
			}

			// Print docker position
			System.out.printf("mess recu de %d : %s\n", i, position);
			i = this.game.getDocker();
		}

		String score = this.generateScore();
		for (int j = 0; j < this.lstIp.length; j++)
			if (this.lstIp[j] != null)
				this.send(score, j);
		this.ctrl.end();
	}

	private void sendToAll(String msg, int exclude)
	{
		for (int i = 0; i < this.lstIp.length; i++)
			if (i != exclude)
				this.send(msg, i);
	}

	private void send(String msg, int i)
	{
		try
		{
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), this.lstIp[i], this.lstPort[i]);
			this.socket.send(packet);
		}
		catch (Exception e)
		{
			this.lstIp[i] = null;
			this.connected--;
		}
	}

	private String receive(int i, int buffer)
	{
		String msg = null;
		try
		{
			DatagramPacket packet;

			boolean wrongPlayer;
			do
			{
				packet = new DatagramPacket(new byte[buffer], buffer);
				this.socket.receive(packet);
				msg = new String(packet.getData());
				msg = msg.substring(0, msg.indexOf(0));

				wrongPlayer = !packet.getAddress().equals(this.lstIp[i]);
				if (wrongPlayer)
					this.socket.send(new DatagramPacket(INVALID.getBytes(), INVALID.length(), packet.getAddress(), packet.getPort()));
			}
			while (wrongPlayer);

			this.lstPort[i] = packet.getPort();
		}
		catch (IOException e)
		{
			this.lstIp[i] = null;
			this.connected--;
		}

		return msg;
	}

	private String generateMap()
	{
		StringBuilder map       = new StringBuilder();
		int[][]       gridValue = this.game.getGridValue();

		for (int l = 0; l < gridValue.length; l++)
		{
			for (int c = 0; c < gridValue[l].length; c++)
			{
				map.append(gridValue[l][c]);
				map.append(':');
			}
			map.deleteCharAt(map.length() - 1);
			map.append('|');
		}

		return map.toString();
	}

	private String generateLstDockerName()
	{
		StringBuilder lstDockerName = new StringBuilder();
		Docker[]      lstDocker     = this.game.getLstDocker();

		for (int i = 0; i < lstDocker.length; i++)
		{
			lstDockerName.append(lstDocker[i].getName());
			lstDockerName.append(',');
		}
		lstDockerName.deleteCharAt(lstDockerName.length() - 1);

		return lstDockerName.toString();
	}

	private String generateScore()
	{
		StringBuilder lstDockerScore = new StringBuilder();
		Docker[]      lstDocker      = this.game.getLstDocker();

		for (int i = 0; i < lstDocker.length; i++)
		{
			lstDockerScore.append(lstDocker[i].getScore());
			lstDockerScore.append("-");
		}
		lstDockerScore.deleteCharAt(lstDockerScore.length() - 1);

		return String.format(END, lstDockerScore.toString());
	}
}

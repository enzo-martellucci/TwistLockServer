package com.twistlock.net;

public final class Message
{
	public static final String WELCOME = "%d-Bonjour %s\n" + "Vous etes le Joueur %d (%s), attente suite...";
	public static final String START   = "01-la partie va commencer (%s)\nMAP=%s";
	public static final String ID      = "id =%d";

	public static final String YOU_PLAY = "10-A vous de jouer (%s) :";
	public static final String OPP_PLAY = "20-coup adversaire:%s";

	public static final String YOU_ILLEGAL = "21-coup joué illegal";
	public static final String OPP_ILLEGAL = "22-coup adversaire illegal";
	public static final String CANT_PLAY   = "50-Vous ne pouvez plus jouer";
	public static final String INVALID     = "91-demande non valide";

	public static final String END = "88-Partie Terminee, %s";

	private Message() { }
}

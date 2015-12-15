package fr.univpau.sma.projet.agent;

import jade.core.Agent;
import examples.protocols.BrokerAgent;

public class MarketAgent extends BrokerAgent {
	public static final int registerEvent = 0;
	public static final int takerSubscribed = 1;
	public static final int toAnnounce = 2;
	public static final int toBid = 3;
	public static final int toAttribute = 4;
	public static final int toGive = 5;
	public static final int toPay = 6;
}

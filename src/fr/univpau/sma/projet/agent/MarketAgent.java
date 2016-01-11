package fr.univpau.sma.projet.agent;

import java.util.HashMap;
import java.util.List;

import fr.univpau.sma.projet.objects.Auction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

public class MarketAgent extends Agent {

	public static final int registerEvent = 0;
	public static final int takerSubscribed = 1;
	public static final int toAnnounce = 2;
	public static final int toBid = 3;
	public static final int toAttribute = 4;
	public static final int toGive = 5;
	public static final int toPay = 6;
	public static final int auctionSpotted = 7;
	
	public static final String marketType = "black-market";
	
	private String _AgentName = null;
	private AID _AgentAID = null;
	private List<DealerAgent> _Dealers;
	private List<TakerAgent> _Takers;
	private HashMap<DealerAgent, Auction> _ProposedAuctions;
	private HashMap<TakerAgent, Auction> _ParticipatingTakers;
	
	@Override
	public void setup() {
		super.setup();
		_AgentAID = getAID();
		Object[] args = getArguments();
		if(args != null && args.length >0)
		{
			_AgentName = (String) args[0];
		}
		
		// Enregistrement du market dans les pages jaunes.
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(_AgentAID);
		ServiceDescription sd = new ServiceDescription();
		sd.setType(marketType);
		sd.setName(_AgentName);
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
		try
		{
			DFService.deregister(this);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
		super.takeDown();
	}
}

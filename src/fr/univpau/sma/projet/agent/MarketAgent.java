package fr.univpau.sma.projet.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.univpau.sma.projet.agent.behaviour.market.MarketCyclicBehaviour;
import fr.univpau.sma.projet.objects.Auction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.*;

@SuppressWarnings("serial")
public class MarketAgent extends Agent {
	
	public static final String marketType = "black-market";
	
	private String _AgentName = "";
	private AID _AgentAID = null;
	private List<AID> _Dealers;
	private List<AID> _Takers;
	private HashMap<AID, Auction> _ProposedAuctions;
	private HashMap<AID, Auction> _ParticipatingTakers;
	
	@Override
	public void setup() {
		super.setup();
		this._Dealers = new ArrayList<AID>();
		this._Takers = new ArrayList<AID>();
		this._ProposedAuctions = new HashMap<AID, Auction>();
		this._ParticipatingTakers = new HashMap<AID, Auction>();
		_AgentAID = getAID();
		Object[] args = getArguments();
		if(args != null && args.length >0)
		{
			_AgentName = (String) args[0];
		}
		
		// Registering MarketAgent into yellow pages
		
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
		
		addBehaviour(new MarketCyclicBehaviour(this));
		
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
	
	public String get_AgentName() {
		return _AgentName;
	}

	public void set_AgentName(String _AgentName) {
		this._AgentName = _AgentName;
	}

	public AID get_AgentAID() {
		return _AgentAID;
	}

	public void set_AgentAID(AID _AgentAID) {
		this._AgentAID = _AgentAID;
	}

	public List<AID> get_Dealers() {
		return _Dealers;
	}

	public void set_Dealers(List<AID> _Dealers) {
		this._Dealers = _Dealers;
	}

	public List<AID> get_Takers() {
		return _Takers;
	}

	public void set_Takers(List<AID> _Takers) {
		this._Takers = _Takers;
	}

	public HashMap<AID, Auction> get_ProposedAuctions() {
		return _ProposedAuctions;
	}

	public void set_ProposedAuctions(HashMap<AID, Auction> _ProposedAuctions) {
		this._ProposedAuctions = _ProposedAuctions;
	}

	public HashMap<AID, Auction> get_ParticipatingTakers() {
		return _ParticipatingTakers;
	}

	public void set_ParticipatingTakers(HashMap<AID, Auction> _ParticipatingTakers) {
		this._ParticipatingTakers = _ParticipatingTakers;
	}
	
}

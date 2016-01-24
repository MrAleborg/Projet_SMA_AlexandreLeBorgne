package fr.univpau.sma.projet.agent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import fr.univpau.sma.projet.agent.behaviour.dealer.fsm.DealerFSMBehaviour;
import fr.univpau.sma.projet.gui.dealer.DealerGUI;
import fr.univpau.sma.projet.objects.Auction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class DealerAgent extends Agent {
	
	private DealerFSMBehaviour agentD_behaviour;
	private ThreadedBehaviourFactory tbf;
	private AID _market;
	private Auction _ProposedAuction = null;
	private boolean _FirstAnnounce = true;
	private List<AID> _RegisteredTakers = null;
	private long _BidTimer = 5000;
	private List<AID> _Bidders = null;
	private DealerGUI frame;
	
	
	
	public void setup(){
		System.out.println("Agent Dealer prépare sa dope");
		
		
		set_RegisteredTakers(new ArrayList<AID>());
		if(this.getArguments().length > 0)
		{
			_ProposedAuction = (Auction) this.getArguments()[0];
			_ProposedAuction.set_dealerName(this.getLocalName());
			this._BidTimer=_ProposedAuction.getTimer();
		}
		
		setFrame(new DealerGUI(_ProposedAuction, this));
		getFrame().setVisible(true);
		getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(MarketAgent.marketType);
		template.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(this, template);
			set_market(result[0].getName());
		}
		catch (FIPAException e){
			e.printStackTrace();
		}
		
		System.out.println("Agent Dealer va commencer à initialiser la state machine");
		
		agentD_behaviour = new DealerFSMBehaviour(this);
		tbf = new ThreadedBehaviourFactory();
		addBehaviour(tbf.wrap(agentD_behaviour));
		
		
	}

	@Override
	protected void takeDown() {
		tbf.interrupt();
		super.takeDown();
	}
	
	
	
	public AID get_market() {
		return _market;
	}

	public void set_market(AID _market) {
		this._market = _market;
	}

	public Auction get_PorposedAuction() {
		return _ProposedAuction;
	}

	public void set_PorposedAuction(Auction _PorposedAuction) {
		this._ProposedAuction = _PorposedAuction;
	}

	public boolean is_FirstAnnounce() {
		return _FirstAnnounce;
	}

	public void set_FirstAnnounce(boolean _FirstAnnounce) {
		this._FirstAnnounce = _FirstAnnounce;
	}

	public long get_BidTimer() {
		return _BidTimer;
	}

	public void set_BidTimer(long _BidTimer) {
		this._BidTimer = _BidTimer;
	}

	public DealerGUI getFrame() {
		return frame;
	}

	public void setFrame(DealerGUI frame) {
		this.frame = frame;
	}

	public List<AID> get_RegisteredTakers() {
		return _RegisteredTakers;
	}

	public void set_RegisteredTakers(List<AID> _RegisteredTakers) {
		this._RegisteredTakers = _RegisteredTakers;
	}

	public List<AID> get_Bidders() {
		return _Bidders;
	}

	public void set_Bidders(List<AID> _Bidders) {
		this._Bidders = _Bidders;
	}

	
	
}

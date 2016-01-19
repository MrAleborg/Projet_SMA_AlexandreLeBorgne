package fr.univpau.sma.projet.agent;

import java.util.List;

import fr.univpau.sma.projet.agent.behaviour.taker.RegisterAtMarket;
import fr.univpau.sma.projet.objects.Auction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class TakerAgent extends Agent {
	
	private FSMBehaviour agentT_behaviour;
	private ParallelBehaviour _ParallelAuctions = null;
	List<Auction> _Auctions = null;
	List<Auction> _ChosenAuctions = null;
	private ThreadedBehaviourFactory tbf = null;
	AID _market;
	private int lower = 1;
	private int _Wallet;
	
	
	private static final int MAXMONEY = 1000;
	private static final int MINMONEY = 500;
	
	public void setup() {
		System.out.println("Agent " + getLocalName() + " est chaud pour pécho des trucs");
		System.out.println("Agent " + getLocalName() + " met ses bottes");
		
		this.set_Wallet((int) (Math.random() * (MAXMONEY - MINMONEY)) + MINMONEY);
		
		System.out.println("Agent " + getLocalName() + " embarque " + this.get_Wallet() + "€ avec lui pour faire ses courses");
		
		this._ParallelAuctions = new ParallelBehaviour();
				
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

		setTbf(new ThreadedBehaviourFactory());
		
		addBehaviour(getTbf().wrap(new RegisterAtMarket(this)));
		
	}
	
	public AID get_market() {
		return _market;
	}

	public void set_market(AID _market) {
		this._market = _market;
	}

	public List<Auction> get_ChosenAuctions() {
		return _ChosenAuctions;
	}

	public void set_ChosenAuctions(List<Auction> _ChosenAuctions) {
		this._ChosenAuctions = _ChosenAuctions;
	}

	public List<Auction> get_Auctions() {
		return _Auctions;
	}

	public void set_Auctions(List<Auction> _Auctions) {
		this._Auctions = _Auctions;
	}

	public ThreadedBehaviourFactory getTbf() {
		return tbf;
	}

	public void setTbf(ThreadedBehaviourFactory tbf) {
		this.tbf = tbf;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public FSMBehaviour getAgentT_behaviour() {
		return agentT_behaviour;
	}

	public void setAgentT_behaviour(FSMBehaviour agentT_behaviour) {
		this.agentT_behaviour = agentT_behaviour;
	}

	public ParallelBehaviour get_ParallelAuctions() {
		return _ParallelAuctions;
	}

	public void set_ParallelAuctions(ParallelBehaviour _ParallelAuctions) {
		this._ParallelAuctions = _ParallelAuctions;
	}

	public int get_Wallet() {
		return _Wallet;
	}

	public void set_Wallet(int _Wallet) {
		this._Wallet = _Wallet;
	}

	
	
}

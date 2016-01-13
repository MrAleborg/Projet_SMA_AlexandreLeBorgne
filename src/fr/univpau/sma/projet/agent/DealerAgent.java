package fr.univpau.sma.projet.agent;

import fr.univpau.sma.projet.agent.behaviour.dealer.RegisterAtMarket;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class DealerAgent extends Agent {
	
	private FSMBehaviour agentD_behaviour;
	private ThreadedBehaviourFactory tbf;
	private AID _market;
	private Auction _PorposedAuction = null;
	
	private static final String register = "registerAtMarket";
	private static final String waitfortakers = "waitForTaker";
	private static final String announce = "announce";
	private static final String waitforbids = "waitForBids";
	private static final String attribute = "attribute";
	private static final String give = "give";
	private static final String end = "end";
	
	
	public void setup(){
		System.out.println("Agent Dealer prépare sa dope");
		System.out.println("Agent Dealer va commencer à initialiser la state machine");
		
		agentD_behaviour = new FSMBehaviour();
		
		agentD_behaviour.registerFirstState(new RegisterAtMarket(this), register);
		agentD_behaviour.registerState(new WaitForTakers(), waitfortakers);
		agentD_behaviour.registerState(new Announce(), announce);
		agentD_behaviour.registerState(new WaitForBids(), waitforbids);
		agentD_behaviour.registerState(new Attribute(), attribute);
		agentD_behaviour.registerState(new Give(), give);
		agentD_behaviour.registerLastState(new End(), end);
		
		agentD_behaviour.registerTransition(register, waitfortakers, ProtocolMessage.registerEvent);
		agentD_behaviour.registerTransition(waitfortakers, announce, ProtocolMessage.takerSubscribed);
		agentD_behaviour.registerTransition(announce, waitforbids, ProtocolMessage.toAnnounce);
		agentD_behaviour.registerTransition(waitforbids, waitforbids, ProtocolMessage.toAnnounce);
		agentD_behaviour.registerTransition(waitforbids, waitforbids, ProtocolMessage.toBid);
		agentD_behaviour.registerTransition(waitforbids, attribute, ProtocolMessage.toAttribute);
		agentD_behaviour.registerTransition(attribute, give, ProtocolMessage.toGive);
		agentD_behaviour.registerTransition(give, end, ProtocolMessage.toPay);
		
		tbf = new ThreadedBehaviourFactory();
		
		addBehaviour(tbf.wrap(agentD_behaviour));
		
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
		
	}
	
	public AID get_market() {
		return _market;
	}

	public void set_market(AID _market) {
		this._market = _market;
	}

	public Auction get_PorposedAuction() {
		return _PorposedAuction;
	}

	public void set_PorposedAuction(Auction _PorposedAuction) {
		this._PorposedAuction = _PorposedAuction;
	}

	private class WaitForTakers extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Le dealer attend ses premiers clients...");
			
		}
		
	}
	
	private class Announce extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class WaitForBids extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Attribute extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Give extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class End extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}

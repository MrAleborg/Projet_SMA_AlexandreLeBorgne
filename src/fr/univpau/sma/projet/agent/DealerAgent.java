package fr.univpau.sma.projet.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.sma.projet.agent.behaviour.dealer.RegisterAtMarket;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
	private boolean _FirstAnnounce = true;
	private List<AID> _RegisteredTakers = null;
	
	private static final String register = "registerAtMarket";
	private static final String waitfortakers = "waitForTaker";
	private static final String announce = "announce";
	private static final String waitforbids = "waitForBids";
	private static final String attribute = "attribute";
	private static final String give = "give";
	private static final String end = "end";
	
	
	public void setup(){
		System.out.println("Agent Dealer prépare sa dope");
		
		_RegisteredTakers = new ArrayList<AID>();
		
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
		return _PorposedAuction;
	}

	public void set_PorposedAuction(Auction _PorposedAuction) {
		this._PorposedAuction = _PorposedAuction;
	}

	public boolean is_FirstAnnounce() {
		return _FirstAnnounce;
	}

	public void set_FirstAnnounce(boolean _FirstAnnounce) {
		this._FirstAnnounce = _FirstAnnounce;
	}

	private class WaitForTakers extends OneShotBehaviour {


		ProtocolMessage start = null;
		ThreadedBehaviourFactory thbf = null;
		
		@Override
		public void action() {
			System.out.println("Le dealer attend ses premiers clients avant de commencer l'enchère...");
			do
			{
				start = (ProtocolMessage) blockingReceive();
			}while(start == null || start.getPerformative() != ProtocolMessage.takerSubscribed);
			_RegisteredTakers.add(start.get_Source());
			System.out.println("Le dealer fait connaissance avec ses premiers clients, il va commencer l'enchère");
			thbf = new ThreadedBehaviourFactory();
			addBehaviour(thbf.wrap(new WaitForNewTaker()));
		}
		
		@Override
		public int onEnd() {
			return start.getPerformative();
		}
		
		@Override
		protected void finalize() throws Throwable {
			thbf.interrupt();
			super.finalize();
		}
		
	}
	
	private class WaitForNewTaker extends CyclicBehaviour {
		
		ProtocolMessage start = null;
		
		@Override
		public void action() {
			if(agentD_behaviour.getExecutionState() == attribute || agentD_behaviour.getExecutionState() == give || agentD_behaviour.getExecutionState() == end)
				try {
					System.out.println("Le dealer fait maintenant la sourde oreille aux nouveaux arrivants qui ralent d'être arrivés en retard...");
					this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			System.out.println("Le dealer reste à l'écoute des nouveaux arrivants");
			do
			{
				start = (ProtocolMessage) blockingReceive();
			}while(start == null || start.getPerformative() != ProtocolMessage.takerSubscribed);
			_RegisteredTakers.add(start.get_Source());
			ProtocolMessage firstAnnounce = new ProtocolMessage();
			firstAnnounce.setPerformative(ProtocolMessage.toAnnounce);
			firstAnnounce.get_Takers().add(start.get_Source());
			try {
				firstAnnounce.setContentObject(_PorposedAuction);
			} catch (IOException e) {
				e.printStackTrace();
			}
			send(firstAnnounce);
		}
		
	}
	
	private class Announce extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("C'est parti pour une vente de folie!!! le dealer fait sa première annonce!!!");
			ProtocolMessage messageAnnounce = new ProtocolMessage();
			messageAnnounce.setPerformative(ProtocolMessage.toAnnounce);
			messageAnnounce.get_Takers().addAll(_RegisteredTakers);
			try {
				messageAnnounce.setContentObject(get_PorposedAuction());
			} catch (IOException e) {
				e.printStackTrace();
			}
			messageAnnounce.addReceiver(get_market());
			send(messageAnnounce);
			System.out.println("Dealer a annoncé");
			while(true){}
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

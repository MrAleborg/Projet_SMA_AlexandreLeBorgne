package fr.univpau.sma.projet.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class DealerAgent extends Agent {
	
	private FSMBehaviour agentD_behaviour;
	
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
		
		agentD_behaviour.registerFirstState(new RegisterAtMarket(), register);
		agentD_behaviour.registerState(new WaitForTakers(), waitfortakers);
		agentD_behaviour.registerState(new Announce(), announce);
		agentD_behaviour.registerState(new WaitForBids(), waitforbids);
		agentD_behaviour.registerState(new Attribute(), attribute);
		agentD_behaviour.registerState(new Give(), give);
		agentD_behaviour.registerLastState(new End(), end);
		
		agentD_behaviour.registerTransition(register, waitfortakers, MarketAgent.registerEvent);
		agentD_behaviour.registerTransition(waitfortakers, announce, MarketAgent.takerSubscribed);
		agentD_behaviour.registerTransition(announce, waitforbids, MarketAgent.toAnnounce);
		agentD_behaviour.registerTransition(waitforbids, waitforbids, MarketAgent.toAnnounce);
		agentD_behaviour.registerTransition(waitforbids, waitforbids, MarketAgent.toBid);
		agentD_behaviour.registerTransition(waitforbids, attribute, MarketAgent.toAttribute);
		agentD_behaviour.registerTransition(attribute, give, MarketAgent.toGive);
		agentD_behaviour.registerTransition(give, end, MarketAgent.toPay);
		
		addBehaviour(agentD_behaviour);
		
	}
	
	private class RegisterAtMarket extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class WaitForTakers extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
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

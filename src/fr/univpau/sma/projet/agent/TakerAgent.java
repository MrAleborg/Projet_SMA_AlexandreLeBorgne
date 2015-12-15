package fr.univpau.sma.projet.agent;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class TakerAgent extends Agent {
	
	private FSMBehaviour agentT_behaviour;
	
	private static final String register = "registerAtMarket";
	private static final String waitforauction = "waitForAuction";
	private static final String waitforannounce = "waitForAnnounce";
	private static final String bid = "bid";
	private static final String waitforattribute = "waitForAttribute";
	private static final String waitforgive = "waitForGive";
	private static final String pay = "pay";
	private static final String end = "end";
	
	public void setup() {
		System.out.println("Agent Taker est chaud pour pécho des trucs");
		System.out.println("Agent Taker met ses bottes");
		
		agentT_behaviour = new FSMBehaviour();
		
		agentT_behaviour.registerFirstState(new RegisterAtMarket(), register);
		agentT_behaviour.registerState(new WaitForAuction(), waitforauction);
		agentT_behaviour.registerState(new waitForAnnounce(), waitforannounce);
		agentT_behaviour.registerState(new Bid(), bid);
		agentT_behaviour.registerState(new WaitForAttribute(), waitforattribute);
		agentT_behaviour.registerState(new WaitForGive(), waitforgive);
		agentT_behaviour.registerState(new Pay(), pay);
		agentT_behaviour.registerLastState(new End(), end);
		
		agentT_behaviour.registerTransition(register, waitforauction, MarketAgent.registerEvent);
		agentT_behaviour.registerTransition(waitforauction, waitforannounce, MarketAgent.auctionSpotted);
		agentT_behaviour.registerTransition(waitforannounce, bid, MarketAgent.toAnnounce);
		agentT_behaviour.registerTransition(bid, bid, MarketAgent.toAnnounce);
		agentT_behaviour.registerTransition(bid, waitforattribute, MarketAgent.toBid);
		agentT_behaviour.registerTransition(waitforattribute, waitforgive, MarketAgent.toAttribute);
		agentT_behaviour.registerTransition(waitforgive, pay, MarketAgent.toGive);
		agentT_behaviour.registerTransition(pay, end, MarketAgent.toPay);
		
		addBehaviour(agentT_behaviour);
		
	}
	
	private class RegisterAtMarket extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class WaitForAuction extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class waitForAnnounce extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Bid extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class WaitForAttribute extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class WaitForGive extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Pay extends OneShotBehaviour {

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

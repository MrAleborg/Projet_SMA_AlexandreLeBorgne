package fr.univpau.sma.projet.agent.behaviour.taker.fsm;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

@SuppressWarnings("serial")
public class TakerFSMBehaviour extends FSMBehaviour {
	
	private Auction _Auction;
	private TakerAgent takerAgent;
	
	private static final String register = "registerAtMarket";
	private static final String waitforauction = "waitForAuction";
	private static final String waitforannounce = "waitForAnnounce";
	private static final String bid = "bid";
	private static final String waitforattribute = "waitForAttribute";
	private static final String waitforgive = "waitForGive";
	private static final String pay = "pay";
	private static final String end = "end";

	public TakerFSMBehaviour(TakerAgent a, Auction _Auction) {
		super(a);
		this.takerAgent = a;
		this.set_Auction(_Auction);
		
		registerFirstState(new waitForAnnounce(), waitforannounce);
		registerState(new Bid(), bid);
		registerState(new WaitForAttribute(), waitforattribute);
		registerState(new WaitForGive(), waitforgive);
		registerState(new Pay(), pay);
		registerLastState(new End(), end);
		

		registerTransition(waitforannounce, bid, ProtocolMessage.toAnnounce);
		registerTransition(bid, bid, ProtocolMessage.toAnnounce);
		registerTransition(bid, waitforattribute, ProtocolMessage.toBid);
		registerTransition(waitforattribute, waitforgive, ProtocolMessage.toAttribute);
		registerTransition(waitforgive, pay, ProtocolMessage.toGive);
		registerTransition(pay, end, ProtocolMessage.toPay);
		
	}

	public Auction get_Auction() {
		return _Auction;
	}

	public void set_Auction(Auction _Auction) {
		this._Auction = _Auction;
	}
	
	public class waitForAnnounce extends OneShotBehaviour {
		
		ProtocolMessage announce = null;
		
		@Override
		public void action() {
			System.out.println("Le Taker attend une annonce...");
			while(announce == null || announce.getPerformative() == ProtocolMessage.toAnnounce)
				announce = (ProtocolMessage) takerAgent.receive();
			
			if(announce.getPerformative() == ProtocolMessage.toAnnounce)
			{
				System.out.println("Taker reçoit l'annonce");
			}
			
		}
		
		@Override
		public int onEnd() {
			return announce.getPerformative();
		}
		
	}
	
	private class Bid extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Taker réfléchit pour savoir si il va lever la main");
			
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

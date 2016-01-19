package fr.univpau.sma.projet.agent.behaviour.taker.fsm;

import java.io.IOException;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

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
	private static final String lose = "lose";
	private static final int YOULOSE = 0;

	public TakerFSMBehaviour(TakerAgent a, Auction _Auction) {
		super(a);
		this.takerAgent = a;
		this.set_Auction(_Auction);
		
		registerFirstState(new waitForAnnounce(), waitforannounce);
		registerState(new Bid(), bid);
//		registerState(new WaitForAttribute(), waitforattribute);
		registerState(new WaitForGive(), waitforgive);
		registerState(new Pay(), pay);
		registerLastState(new End(), end);
		registerLastState(new Lose(), lose);
		

		registerTransition(waitforannounce, bid, ProtocolMessage.toAnnounce);
		registerDefaultTransition(bid, waitforannounce);
//		registerTransition(bid, waitforannounce, ProtocolMessage.toAnnounce);
//		registerTransition(waitforannounce, waitforattribute, ProtocolMessage.toBid);
//		registerTransition(waitforattribute, waitforgive, ProtocolMessage.toAttribute);
		registerTransition(waitforannounce, waitforgive, ProtocolMessage.toAttribute);
		registerTransition(waitforgive, pay, ProtocolMessage.toGive);
		registerTransition(pay, end, ProtocolMessage.toPay);
		registerTransition(waitforannounce, lose, YOULOSE);
		
	}

	public Auction get_Auction() {
		return _Auction;
	}

	public void set_Auction(Auction _Auction) {
		this._Auction = _Auction;
	}
	
	private class waitForAnnounce extends OneShotBehaviour {
		
		ProtocolMessage announce;
		boolean winAuction = false;
		
		@Override
		public void action() {
			announce = null;
			System.out.println("Le Taker " + takerAgent.getLocalName() + " attend une annonce...");
			
			ProtocolMessage p1 = new ProtocolMessage();
			ProtocolMessage p2 = new ProtocolMessage();
			try {
				p1.setContentObject(_Auction);
				p1.setPerformative(ProtocolMessage.toAnnounce);
				p2.setContentObject(_Auction);
				p2.setPerformative(ProtocolMessage.toAttribute);
				MessageTemplate t1 = MessageTemplate.MatchPerformative(ProtocolMessage.toAnnounce);
				MessageTemplate t2 = MessageTemplate.MatchPerformative(ProtocolMessage.toAttribute);
				t1.match(p1);
				t2.match(p2);
				
				announce = (ProtocolMessage) takerAgent.blockingReceive(MessageTemplate.or(t1, t2));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(announce.getPerformative() == ProtocolMessage.toAnnounce)
			{
				try {
					_Auction = (Auction) announce.getContentObject();
					System.out.println(takerAgent.getLocalName() + " reçoit l'annonce : " + _Auction.get_dealerName() + " --> " + _Auction.get_name() + " : " + _Auction.get_price());
				} catch (UnreadableException e) {
					System.out.println(takerAgent.getLocalName() + " n'a pas pu récupérer l'annonce");
					e.printStackTrace();
				}
			}
			else if(announce.getPerformative() == ProtocolMessage.toAttribute)
			{
				winAuction=true;
			}
			
		}
		
		@Override
		public int onEnd() {
			if(announce.getPerformative()==ProtocolMessage.toAttribute && !winAuction)
				return YOULOSE;
			return announce.getPerformative();
		}
		
	}
	
	private class Bid extends OneShotBehaviour {
		
		ProtocolMessage takerBids = null;

		@Override
		public void action() {
			System.out.println("Taker réfléchit pour savoir si il va lever la main");
			if(_Auction.get_price()<=takerAgent.get_Wallet())
			{
				System.out.println(takerAgent.getLocalName() + " se décide à lever le bras");
				takerBids = new ProtocolMessage();
				takerBids.setPerformative(ProtocolMessage.toBid);
				takerBids.addReceiver(takerAgent.get_market());
				try {
					takerBids.setContentObject(_Auction);
					takerAgent.send(takerBids);
				} catch (IOException e) {
					System.out.println(takerAgent.getLocalName() + " n'a pas pu bider");
					e.printStackTrace();
				}
			}
			else System.out.println(takerAgent.getLocalName() + " ne veut pas bider, l'enchère est trop élevée, il va attendre que ça redescende.");
		}
		
	}
	
//	private class WaitForAttribute extends OneShotBehaviour {
//
//		@Override
//		public void action() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	
	private class WaitForGive extends OneShotBehaviour {

		private ProtocolMessage giveMessage = null;
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int onEnd() {
		// TODO Auto-generated method stub
		return super.onEnd();
		}
		
	}
	
	private class Pay extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			return super.onEnd();
		}
		
	}
	
	private class End extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			return super.onEnd();
		}
		
	}
	
	private class Lose extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
 	
}

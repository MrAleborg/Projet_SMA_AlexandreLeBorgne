package fr.univpau.sma.projet.agent.behaviour.taker.fsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.NotFoundException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

@SuppressWarnings("serial")
public class TakerFSMBehaviour extends FSMBehaviour {

	private Auction _Auction;
	private TakerAgent takerAgent;
	private waitForUserBid _wfub = null;

	//	private static final String register = "registerAtMarket";
	//	private static final String waitforauction = "waitForAuction";
	private static final String waitforannounce = "waitForAnnounce";
	private static final String bid = "bid";
	//	private static final String waitforattribute = "waitForAttribute";
	private static final String waitforgive = "waitForGive";
	private static final String pay = "pay";
	//	private static final String end = "end";
	private static final String lose = "lose";
	private static final int YOULOSE = 111;

	public TakerFSMBehaviour(TakerAgent a, Auction _Auction) {
		super(a);
		this.takerAgent = a;
		this.set_Auction(_Auction);

		registerFirstState(new waitForAnnounce(), waitforannounce);
		registerState(new Bid(), bid);
		//		registerState(new WaitForAttribute(), waitforattribute);
		registerState(new WaitForGive(), waitforgive);
		registerLastState(new Pay(), pay);
		//		registerLastState(new End(), end);
		registerLastState(new Lose(), lose);


		registerTransition(waitforannounce, bid, ProtocolMessage.toAnnounce);
		registerDefaultTransition(bid, waitforannounce);
		//		registerTransition(bid, waitforannounce, ProtocolMessage.toAnnounce);
		//		registerTransition(waitforannounce, waitforattribute, ProtocolMessage.toBid);
		//		registerTransition(waitforattribute, waitforgive, ProtocolMessage.toAttribute);
		registerTransition(waitforannounce, waitforgive, ProtocolMessage.toAttribute);
		registerTransition(waitforgive, pay, ProtocolMessage.toGive);
		//		registerTransition(pay, end, ProtocolMessage.toPay);
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
			ProtocolMessage p3 = new ProtocolMessage();
			try {
				p1.setContentObject(_Auction);
				p1.setPerformative(ProtocolMessage.toAnnounce);
				p2.setContentObject(_Auction);
				p2.setPerformative(ProtocolMessage.toAttribute);
				p3.setContentObject(_Auction);
				p3.setPerformative(ProtocolMessage.toWithdraw);
				MessageTemplate t1 = MessageTemplate.MatchPerformative(ProtocolMessage.toAnnounce);
				MessageTemplate t2 = MessageTemplate.MatchPerformative(ProtocolMessage.toAttribute);
				MessageTemplate t3 = MessageTemplate.MatchPerformative(ProtocolMessage.toWithdraw);
				t1.match(p1);
				t2.match(p2);
				t3.match(p3);

				announce = (ProtocolMessage) takerAgent.blockingReceive(MessageTemplate.or(t1, MessageTemplate.or(t2, t3)));
				if(_wfub != null)
					_wfub=null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if(announce.getPerformative() == ProtocolMessage.toAnnounce)
			{
				try {
					_Auction = (Auction) announce.getContentObject();
					System.out.println(takerAgent.getLocalName() + " reçoit l'annonce : " + _Auction.get_dealerName() + " --> " + _Auction.get_name() + " : " + _Auction.get_price());
					takerAgent.getFrame().updateAuction(_Auction);
				} catch (UnreadableException e) {
					System.out.println(takerAgent.getLocalName() + " n'a pas pu récupérer l'annonce");
					e.printStackTrace();
				}
			}
			else if(announce.getPerformative() == ProtocolMessage.toAttribute)
			{
				if(takerAgent.getAID().equals(announce.get_Source()))
				{
					winAuction=true;
				}
			}

		}

		@Override
		public int onEnd() {
			if((announce.getPerformative()==ProtocolMessage.toAttribute || announce.getPerformative()==ProtocolMessage.toWithdraw) && !winAuction)
			{
				return YOULOSE;
			}

			return announce.getPerformative();
		}

	}

	private class Bid extends OneShotBehaviour {

		ProtocolMessage takerBids = null;

		@Override
		public void action() {
			System.out.println("Taker réfléchit pour savoir si il va lever la main");
			if(_Auction.get_price()<=takerAgent.get_Wallet() && takerAgent.is_autoMode())
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
			else if(_Auction.get_price()<=takerAgent.get_Wallet() && !takerAgent.is_autoMode())
			{
				System.out.println("avant WFUB");
				_wfub = new waitForUserBid();
				takerAgent.addBehaviour(takerAgent.getTbf().wrap(_wfub));
			}
			else System.out.println(takerAgent.getLocalName() + " ne veut pas bider, l'enchère est trop élevée, il va attendre que ça redescende.");
		}

	}
	
	private class waitForUserBid extends Behaviour {

		ProtocolMessage takerBids = null;
		boolean done = false;
		
		@Override
		public void action() {
			Set<Auction> la = takerAgent.getFrame().getModele().get_mappingAuctionBid().keySet();
			for(Auction a : la)
				if(_Auction.compareTo(a)==0)
					if(takerAgent.getFrame().getModele().get_mappingAuctionBid().get(a))
					{
						System.out.println(takerAgent.getLocalName() + " se décide à lever le bras");
						takerBids = new ProtocolMessage();
						takerBids.setPerformative(ProtocolMessage.toBid);
						takerBids.addReceiver(takerAgent.get_market());
						try {
							takerBids.setContentObject(_Auction);
							takerAgent.send(takerBids);
							done=true;
						} catch (IOException e) {
							System.out.println(takerAgent.getLocalName() + " n'a pas pu bider");
							e.printStackTrace();
						}
					}
		}

		@Override
		public boolean done() {
			return done;
		}
		
		
	}



	private class WaitForGive extends OneShotBehaviour {

		private ProtocolMessage giveMessage = null;

		@Override
		public void action() {
			System.out.println(takerAgent.getLocalName() + " a gagné l'enchère de " + _Auction.get_dealerName() + ". Il est vraiment très content!");

			ProtocolMessage p = new ProtocolMessage();
			MessageTemplate t = null;
			try {
				p.setContentObject(_Auction);
				p.setPerformative(ProtocolMessage.toGive);
				t = MessageTemplate.MatchPerformative(ProtocolMessage.toGive);
				t.match(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(takerAgent.getLocalName() + " attend sa livraison");
			giveMessage = (ProtocolMessage) takerAgent.blockingReceive(t);
			System.out.println(takerAgent.getLocalName() + " a reçu les trucs qu'il vient d'acheter");
		}

		@Override
		public int onEnd() {

			return giveMessage.getPerformative();
		}

	}

	private class Pay extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println(takerAgent.getLocalName() + " doit maintenant passer à la caisse... Hé ouais!");
			ProtocolMessage payMessage = new ProtocolMessage();
			payMessage.addReceiver(takerAgent.get_market());
			payMessage.setPerformative(ProtocolMessage.toPay);
			try {
				payMessage.setContentObject(_Auction);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			takerAgent.send(payMessage);
			if(takerAgent.get_WonAuctions() == null)
				takerAgent.set_WonAuctions(new ArrayList<Auction>());
			takerAgent.get_WonAuctions().add(_Auction);
			takerAgent.set_Wallet(takerAgent.get_Wallet()-_Auction.get_price());
			takerAgent.getFrame().addPastAuction(_Auction, true);
			takerAgent.getFrame().updateMoneyLeft();
		}

		//		@Override
		//		public int onEnd() {
		//			return ProtocolMessage.toPay;
		//		}

	}


	private class Lose extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println(takerAgent.getLocalName() + " a perdu l'enchère de " + _Auction.get_dealerName() + ". Autant dire qu'il est sacrément déçu... :/");
			if(takerAgent.get_LostAuctions() == null)
				takerAgent.set_LostAuctions(new ArrayList<Auction>());
			takerAgent.get_LostAuctions().add(_Auction);
			takerAgent.getFrame().addPastAuction(_Auction, false);
		}

	}

}

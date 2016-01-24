package fr.univpau.sma.projet.agent.behaviour.dealer.fsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.sma.projet.agent.DealerAgent;
import fr.univpau.sma.projet.agent.behaviour.dealer.RegisterAtMarket;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class DealerFSMBehaviour extends FSMBehaviour {

	private AID _market;
	private Auction _ProposedAuction = null;
	private List<AID> _RegisteredTakers = null;
	private List<AID> _Bidders = null;
	private DealerAgent _agent;
	
	private static final String register = "registerAtMarket";
	private static final String waitfortakers = "waitForTaker";
	private static final String announce = "announce";
	private static final String waitforbids = "waitForBids";
	private static final String attribute = "attribute";
	private static final String give = "give";
	private static final String end = "end";
	private static final String withdraw = "withdraw";
	
	public DealerFSMBehaviour(DealerAgent agent){

		this._agent = agent;
		this._ProposedAuction = this._agent.get_PorposedAuction();
		this._market = this._agent.get_market();
		this._RegisteredTakers=this._agent.get_RegisteredTakers();
		this._Bidders = this._agent.get_Bidders();
		
		registerFirstState(new RegisterAtMarket(_agent), register);
		registerState(new WaitForTakers(), waitfortakers);
		registerState(new Announce(), announce);
		registerState(new WaitForBids(_agent.get_BidTimer()), waitforbids);
		registerState(new Attribute(), attribute);
		registerState(new Give(), give);
		registerLastState(new End(), end);
		registerLastState(new WithDraw(), withdraw);
		
		registerTransition(register, waitfortakers, ProtocolMessage.registerEvent);
		registerTransition(waitfortakers, announce, ProtocolMessage.takerSubscribed);
		registerTransition(announce, waitforbids, ProtocolMessage.toAnnounce);
		registerTransition(waitforbids, announce, ProtocolMessage.toAnnounce);
		registerTransition(waitforbids, waitforbids, ProtocolMessage.toBid);
		registerTransition(waitforbids, attribute, ProtocolMessage.toAttribute);
		registerTransition(waitforbids, withdraw, ProtocolMessage.toWithdraw);
		registerTransition(attribute, give, ProtocolMessage.toGive);
		registerTransition(give, end, ProtocolMessage.toPay);
	}
	
	private class WaitForTakers extends OneShotBehaviour {


		ProtocolMessage start = null;
		ThreadedBehaviourFactory thbf = null;
		
		@Override
		public void action() {
			System.out.println("Le dealer attend ses premiers clients avant de commencer l'enchère...");
			do
			{
				start = (ProtocolMessage) _agent.blockingReceive();
			}while(start == null || start.getPerformative() != ProtocolMessage.takerSubscribed);
			_RegisteredTakers.add(start.get_Source());
			System.out.println("Le dealer fait connaissance avec ses premiers clients, il va commencer l'enchère");
			thbf = new ThreadedBehaviourFactory();
			_agent.addBehaviour(thbf.wrap(new WaitForNewTaker()));
		}
		
		@Override
		public int onEnd() {
			System.out.println("C'est parti pour une vente de folie!!! le dealer va faire sa première annonce!!!");
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
			if(getExecutionState() == attribute || getExecutionState() == give || getExecutionState() == end)
				try {
					System.out.println("Le dealer fait maintenant la sourde oreille aux nouveaux arrivants qui ralent d'être arrivés en retard...");
					this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			System.out.println("Le dealer reste à l'écoute des nouveaux arrivants");
			
			start = (ProtocolMessage) _agent.blockingReceive(MessageTemplate.MatchPerformative(ProtocolMessage.takerSubscribed));
			_RegisteredTakers.add(start.get_Source());
			ProtocolMessage firstAnnounce = new ProtocolMessage();
			firstAnnounce.setPerformative(ProtocolMessage.toAnnounce);
			firstAnnounce.get_Takers().add(start.get_Source());
			firstAnnounce.addReceiver(_market);
			try {
				firstAnnounce.setContentObject(_ProposedAuction);
			} catch (IOException e) {
				e.printStackTrace();
			}
			_agent.send(firstAnnounce);
		}
		
	}
	
	private class Announce extends OneShotBehaviour {

		@Override
		public void action() {
			this.reset();
			_agent.getFrame().initBidders();
			_ProposedAuction.initBids();
			_agent.getFrame().updatePrice(_ProposedAuction.get_price());
			_agent.getFrame().updateBids(_ProposedAuction.get_bids());
			_Bidders = null;
			ProtocolMessage messageAnnounce = new ProtocolMessage();
			messageAnnounce.setPerformative(ProtocolMessage.toAnnounce);
			messageAnnounce.get_Takers().addAll(_RegisteredTakers);
			try {
				messageAnnounce.setContentObject(_agent.get_PorposedAuction());
			} catch (IOException e) {
				messageAnnounce.addReceiver(_agent.get_market());
				_agent.send(messageAnnounce);
				System.out.println("Dealer a annoncé");
				e.printStackTrace();
			}
			messageAnnounce.addReceiver(_agent.get_market());
			_agent.send(messageAnnounce);
			System.out.println("Dealer a annoncé");
		}
		
		@Override
		public int onEnd() {
			return ProtocolMessage.toAnnounce;
		}
		
	}
	
	private class WaitForBids extends OneShotBehaviour {
		
		ProtocolMessage bidMessage;
		long timer;
		boolean bidReceived;
		boolean timerStarted = false;

		public WaitForBids(long timer) {
			this.timer = timer;
			
		}		
		
		@Override
		public void action() {
			if(!timerStarted)
			{
				_agent.getFrame().startTimer();
				timerStarted = true;
			}
			bidMessage = null;
			bidReceived = false;
			System.out.println(_agent.getLocalName() + " attend des bids");
			
			bidMessage = (ProtocolMessage) _agent.blockingReceive(MessageTemplate.MatchPerformative(ProtocolMessage.toBid), timer);
			
			if(bidMessage != null)
			{
				bidReceived = true;
				_ProposedAuction.bid();
				if(_Bidders == null)
					_Bidders = new ArrayList<AID>();
				_Bidders.add(bidMessage.get_Source());
				_agent.getFrame().addBidder(bidMessage.get_Source().getLocalName());
				System.out.println(_agent.getLocalName() + " a reçu un bid de " + bidMessage.get_Source().getLocalName());
				System.out.println("NB BIDS = " + _ProposedAuction.get_bids());
				bidMessage = null;
			}
		}
		
		@Override
		public int onEnd() {
			if(bidReceived)
			{	
				_agent.getFrame().updateBids(_ProposedAuction.get_bids());
				timerStarted = false;
				_agent.getFrame().stopTimer();
				return ProtocolMessage.toBid;
			}
			
			if(_ProposedAuction.get_bids()>=2)
			{
				timerStarted = false;
				_agent.getFrame().stopTimer();
				_ProposedAuction.increasePrice();
				return ProtocolMessage.toAnnounce;
			}
			else if(_ProposedAuction.get_bids() == 1 && _Bidders.size()==1)
			{
				return ProtocolMessage.toAttribute;
			}
			timerStarted = false;
			_agent.getFrame().stopTimer();
			_ProposedAuction.decreasePrice();
			if(_ProposedAuction.get_price()<_ProposedAuction.get_minPrice())
				return ProtocolMessage.toWithdraw;
			return ProtocolMessage.toAnnounce;
		}

		
	}
	
	private class WithDraw extends OneShotBehaviour {

		@Override
		public void action() {
			ProtocolMessage withdrawMessage = new ProtocolMessage();
			withdrawMessage.set_Message("Annulé");
			withdrawMessage.set_Takers(_RegisteredTakers);
			withdrawMessage.setPerformative(ProtocolMessage.toWithdraw);
			withdrawMessage.addReceiver(_market);
			try {
				withdrawMessage.setContentObject(_ProposedAuction);
				_agent.send(withdrawMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	private class Attribute extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println(_agent.getLocalName() + " a trouvé un acheteur!!!");
			if(_Bidders.size() == 1)
			{
				System.out.println("Il vient de pigeonner " + _Bidders.get(0).getLocalName());
				ProtocolMessage attributionMessage = new ProtocolMessage();
				attributionMessage.set_Source(_Bidders.get(0));
				attributionMessage.set_Takers(_RegisteredTakers);
				attributionMessage.setPerformative(ProtocolMessage.toAttribute);
				attributionMessage.addReceiver(_market);
				try {
					attributionMessage.setContentObject(_ProposedAuction);
					_agent.send(attributionMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public int onEnd() {
			return ProtocolMessage.toGive;
		}
		
	}
	
	private class Give extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println(_agent.getLocalName() + " fait péter la came pour " + _Bidders.get(0).getLocalName());
			ProtocolMessage giveMessage = new ProtocolMessage();
			giveMessage.setPerformative(ProtocolMessage.toGive);
			giveMessage.addReceiver(_market);
			giveMessage.set_Source(_Bidders.get(0));
			try {
				giveMessage.setContentObject(_ProposedAuction);
				_agent.send(giveMessage);
			} catch (IOException e) {
				System.out.println(_agent.getLocalName() + " n'a pas pu livrer la marchandise");
				e.printStackTrace();
			}
		}
		
		@Override
		public int onEnd() {
			return ProtocolMessage.toPay;
		}
		
	}
	
	private class End extends OneShotBehaviour {

		ProtocolMessage payment = null;
		
		@Override
		public void action() {
			
			System.out.println(_agent.getLocalName() + " attend d'être payé");
			
			payment = (ProtocolMessage) _agent.blockingReceive(MessageTemplate.MatchPerformative(ProtocolMessage.toPay));
			
			if(payment != null)
			{
				ProtocolMessage endOfDeal = new ProtocolMessage();
				endOfDeal.setPerformative(ProtocolMessage.auctionFinished);
				endOfDeal.addReceiver(_market);
				try {
					endOfDeal.setContentObject(_ProposedAuction);
				} catch (IOException e) {
					System.out.println(_agent.getLocalName() + " ne s'est pas terminé correctement.");
					e.printStackTrace();
				}
			}
			else System.out.println(_agent.getLocalName() + " ne se termine pas bien...");
		}
		
		@Override
		public int onEnd() {
			_agent.doDelete();
			return super.onEnd();
		}
		
	}

	
}

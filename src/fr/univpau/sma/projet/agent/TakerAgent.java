package fr.univpau.sma.projet.agent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.UnreadableException;

@SuppressWarnings("serial")
public class TakerAgent extends Agent {
	
	private FSMBehaviour agentT_behaviour;
	private ParallelBehaviour _ParallelAuctions = null;
	private List<Auction> _Auctions = null;
	private List<Auction> _ChosenAuctions = null;
	private ThreadedBehaviourFactory tbf = null;
	private AID _market;
	private int lower = 1;
	
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
		
//		agentT_behaviour.registerFirstState(new RegisterAtMarket(), register);
//		agentT_behaviour.registerState(new WaitForAuction(), waitforauction);
//		agentT_behaviour.registerState(new waitForAnnounce(), waitforannounce);
		agentT_behaviour.registerFirstState(new waitForAnnounce(), waitforannounce);
		agentT_behaviour.registerState(new Bid(), bid);
		agentT_behaviour.registerState(new WaitForAttribute(), waitforattribute);
		agentT_behaviour.registerState(new WaitForGive(), waitforgive);
		agentT_behaviour.registerState(new Pay(), pay);
		agentT_behaviour.registerLastState(new End(), end);
		
//		agentT_behaviour.registerTransition(register, waitforauction, ProtocolMessage.registerEvent);
//		agentT_behaviour.registerTransition(waitforauction, waitforannounce, ProtocolMessage.auctionSpotted);
		agentT_behaviour.registerTransition(waitforannounce, bid, ProtocolMessage.toAnnounce);
		agentT_behaviour.registerTransition(bid, bid, ProtocolMessage.toAnnounce);
		agentT_behaviour.registerTransition(bid, waitforattribute, ProtocolMessage.toBid);
		agentT_behaviour.registerTransition(waitforattribute, waitforgive, ProtocolMessage.toAttribute);
		agentT_behaviour.registerTransition(waitforgive, pay, ProtocolMessage.toGive);
		agentT_behaviour.registerTransition(pay, end, ProtocolMessage.toPay);
		
		//tbf = new ThreadedBehaviourFactory();
		
		//addBehaviour(tbf.wrap(agentT_behaviour));
		
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
		
		addBehaviour(new RegisterAtMarket());
		
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

	private class RegisterAtMarket extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Le taker n'est pas stressé, il arrive au marché les mains dans les poches...");
			ProtocolMessage message = new ProtocolMessage(ProtocolMessage.taker);
			message.addReceiver(_market);
			message.setPerformative(ProtocolMessage.registerEvent);
			send(message);
			
		}
		
		@Override
		public int onEnd() {
			ProtocolMessage message = null;
			while(message == null){
				message = (ProtocolMessage) receive();
			}
			addBehaviour(new WaitForAuction());
			return message.getPerformative();
		}
		
	}
	
	private class WaitForAuction extends CyclicBehaviour {

		@Override
		public void action() {
			System.out.println("le taker attend de trouver de bonnes enchères");
			ProtocolMessage m = null;
			
			// Reception de la liste des enchères
			m = (ProtocolMessage) blockingReceive();
			if(m!=null && m.getPerformative() == ProtocolMessage.auctionSpotted)
			{
				System.out.println("Taker récupère les enchères");
				try {
					_Auctions = (List<Auction>) m.getContentObject();
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				System.out.println("ENCHERES : " + _Auctions.size());
				
				// Souscription aux enchères
				if(_Auctions != null && !_Auctions.isEmpty())
				{
					System.out.println("Taker veut souscrire à une ou plusieurs enchères");
					if(!(_ChosenAuctions==null))
					{
						for (Auction auction : _ChosenAuctions) {
							_Auctions.remove(auction);
						}
						lower = 0;
					}
					else
					{
						_ChosenAuctions = new ArrayList<Auction>();
					}
					int nbAuctionToSubscribe = (int) (Math.random() * (_Auctions.size()-lower)) + lower;
					List<Auction> tempList = new ArrayList<Auction>();
					int i=0;
					while(i<nbAuctionToSubscribe)
					{
						int auctionToSubscribe = (int) (Math.random() * (_Auctions.size()-1) +1);
						if(!tempList.contains(_Auctions.get(auctionToSubscribe-1)))
						{
							tempList.add(_Auctions.get(auctionToSubscribe-1));
						}
						i++;
					}
					_ChosenAuctions.addAll(tempList);
					
					// Envoi du message de souscription aux enchères choisies
					ProtocolMessage subscription = new ProtocolMessage();
					subscription.setPerformative(ProtocolMessage.takerSubscribed);
					try {
						subscription.setContentObject((Serializable) tempList);
						subscription.addReceiver(_market);
						send(subscription);
					} catch (IOException e) {
						System.out.println("Le message de souscription n'a pas pu être envoyé");
						e.printStackTrace();
					}
					
					System.out.println("Taker a choisi les enchères auxquelles il veut participer, il en a choisi : " + tempList.size());
				}
				else System.out.println("_Auctions is EMPTY!!!");
				
			}
			else block();
			
		}
		
	}
	
	private class waitForAnnounce extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("Le Taker attend une annonce...");
			
		}
		
		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			return super.onEnd();
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

package fr.univpau.sma.projet.agent.behaviour.market;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import fr.univpau.sma.projet.agent.MarketAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.UnreadableException;

@SuppressWarnings("serial")
public class MarketCyclicBehaviour extends CyclicBehaviour {
	
	MarketAgent _marketAgent = null;

	public MarketCyclicBehaviour() {
	}

	public MarketCyclicBehaviour(MarketAgent a) {
		super(a);
		this._marketAgent = a;
	}

	@Override
	public void action() {

		ProtocolMessage message;
		Auction auction = null;
		String msgStr = "";
		message = (ProtocolMessage) this._marketAgent.receive();
		AID sender = null;
		System.out.println("marketBehaviour");
		if(message != null)
		{
			System.out.println("Agent Market a reçu un message");
		try {
			sender = message.getSender();
			auction = new Auction();
			msgStr = message.get_Message();
			auction = (Auction) message.getContentObject();

		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		
		switch(message.getPerformative())
		{
			case ProtocolMessage.registerEvent:
				System.out.println("msgStr : " + msgStr);
				if(!msgStr.equals(""))
				{
					 ProtocolMessage subscription = new ProtocolMessage();
					 subscription.setPerformative(ProtocolMessage.registerEvent);
					 if(msgStr.equals(ProtocolMessage.taker))
					 {
						 List<AID> _Takers = _marketAgent.get_Takers();
						 if(!_Takers.contains(sender))
						 {
							 _Takers.add(sender);
							 _marketAgent.set_Takers(_Takers);
							System.out.println("Taker enregistré dans le market");
							 subscription.addReceiver(sender);
							 this._marketAgent.send(subscription);
							 if(!this._marketAgent.get_Auctions().isEmpty())
							 {
								 System.out.println("Envoi des enchères");
								 ProtocolMessage auctionMessage = new ProtocolMessage();
								 auctionMessage.addReceiver(sender);
								 try {
									auctionMessage.setContentObject((Serializable) this._marketAgent.get_Auctions());
									auctionMessage.setPerformative(ProtocolMessage.auctionSpotted);
									this._marketAgent.send(auctionMessage);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 }
						 }
					 }
					 else if(msgStr.equals(ProtocolMessage.dealer))
					 {
						 try
						 {
							 List<AID> _Dealers = this._marketAgent.get_Dealers();
							 List<Auction> _Auctions = this._marketAgent.get_Auctions();
							 HashMap<AID, Auction> _ProposedAuctions = this._marketAgent.get_ProposedAuctions();
							 System.out.println("Dealer a envoyé sa demande d'adhésion au market");
							 if(!_Dealers.contains(sender) && auction != null)
							 {
								_Dealers.add(sender);
								_Auctions.add(auction);
								_ProposedAuctions.put(sender, auction);
								this._marketAgent.set_Dealers(_Dealers);
								this._marketAgent.set_ProposedAuctions(_ProposedAuctions);
								this._marketAgent.set_Auctions(_Auctions);
								subscription.addReceiver(sender);
								subscription.setPerformative(ProtocolMessage.registerEvent);
								this._marketAgent.send(subscription);
								ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
								System.out.println("Dealer enregistré dans le market");
								this._marketAgent.addBehaviour(tbf.wrap(new SpreadAuctionsBehaviour(this._marketAgent)));
							 }
						 }
						 catch(Exception e)
						 {
							 e.printStackTrace();
						 }
					 }
				}
				break;
			case ProtocolMessage.takerSubscribed:
				break;
			case ProtocolMessage.auctionSpotted:
				break;
			case ProtocolMessage.toAnnounce:
				break;
			case ProtocolMessage.toBid:
				break;
			case ProtocolMessage.toAttribute:
				break;
			case ProtocolMessage.toGive:
				break;
			case ProtocolMessage.toPay:
				break;
			default:
				System.out.println("Le Market n'a pas su traiter le message");
				break;
		}
		}
		else block();

		System.out.println("marketBehaviour END");
	}

}

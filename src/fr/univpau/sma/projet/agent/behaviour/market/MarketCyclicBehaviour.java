package fr.univpau.sma.projet.agent.behaviour.market;

import java.util.HashMap;
import java.util.List;

import fr.univpau.sma.projet.agent.MarketAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
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
					 if(msgStr.equals(ProtocolMessage.taker))
					 {
						 List<AID> _Takers = _marketAgent.get_Takers();
						 if(!_Takers.contains(sender))
						 {
							 _Takers.add(sender);
							 _marketAgent.set_Takers(_Takers);
						 }
						 subscription.addReceiver(sender);
						 this._marketAgent.send(subscription);
						 
					 }
					 else if(msgStr.equals(ProtocolMessage.dealer))
					 {
						 try
						 {
							 List<AID> _Dealers = this._marketAgent.get_Dealers();
							 HashMap<AID, Auction> _ProposedAuctions = this._marketAgent.get_ProposedAuctions();
							 if(!_Dealers.contains(sender) && auction != null)
							 {
							 	_Dealers.add(sender);
							 	_ProposedAuctions.put(sender, auction);
							 	this._marketAgent.set_Dealers(_Dealers);
							 	this._marketAgent.set_ProposedAuctions(_ProposedAuctions);
							 }
							 subscription.addReceiver(sender);
							 subscription.setPerformative(ProtocolMessage.registerEvent);
							 this._marketAgent.send(subscription);
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

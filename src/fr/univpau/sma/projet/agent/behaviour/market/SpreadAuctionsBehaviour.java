package fr.univpau.sma.projet.agent.behaviour.market;

import java.io.IOException;
import java.io.Serializable;

import fr.univpau.sma.projet.agent.MarketAgent;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;

@SuppressWarnings("serial")
public class SpreadAuctionsBehaviour extends Behaviour {
	
	private boolean _terminate = false;

	public SpreadAuctionsBehaviour(MarketAgent a) {
		super(a);
	}

	@Override
	public void action() {
		System.out.println("Le market doit avertir tout le monde de la nouvelle enchère");
		if(!((MarketAgent) myAgent).get_Auctions().isEmpty() || !((MarketAgent) myAgent).get_Takers().isEmpty()){
			ProtocolMessage spreadMessage = new ProtocolMessage();
			spreadMessage.setPerformative(ProtocolMessage.auctionSpotted);
			try {
				spreadMessage.setContentObject((Serializable) ((MarketAgent) myAgent).get_Auctions());
				
				for (AID r : ((MarketAgent) myAgent).get_Takers()) {
					spreadMessage.addReceiver(r);
				}
				
				myAgent.send(spreadMessage);
				_terminate = true;
			} catch (IOException e) {
				_terminate = false;
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean done() {
		return _terminate;
	}

}

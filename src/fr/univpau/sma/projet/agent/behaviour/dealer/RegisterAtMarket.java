package fr.univpau.sma.projet.agent.behaviour.dealer;

import java.io.IOException;

import fr.univpau.sma.projet.agent.DealerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

@SuppressWarnings("serial")
public class RegisterAtMarket extends OneShotBehaviour {
	
	private DealerAgent _DealerAgent = null;
	private AID _market = null;
	private Auction _ProposedAuction = null;

//	public RegisterAtMarket() {
//		super();
//	}

	public RegisterAtMarket(DealerAgent a) {
		super(a);
		this._DealerAgent = a;
		this._market = this._DealerAgent.get_market();
		this._ProposedAuction = this._DealerAgent.get_PorposedAuction();
	}
	
	@Override
	public void action() {
		System.out.println("Le dealer paye sa licence pour vendre ses trucs");
		// A enlever avec l'interface utilisateur
		this._ProposedAuction = new Auction(10, 10, 1, 2, 1, "truc", this._DealerAgent.getLocalName(), 0);
		ProtocolMessage message = new ProtocolMessage(ProtocolMessage.dealer);
		message.addReceiver(_market);
		message.setPerformative(ProtocolMessage.registerEvent);
		System.out.println("market : " + _market.getName());
		try {
			message.setContentObject(this._ProposedAuction);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._DealerAgent.send(message);
	}
	
	@Override
	public int onEnd() {
		ProtocolMessage message = null;
		while(message == null){
			message = (ProtocolMessage) this._DealerAgent.receive();
		}
		return message.getPerformative();
	}

}
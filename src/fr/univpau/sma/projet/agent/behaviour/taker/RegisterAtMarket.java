package fr.univpau.sma.projet.agent.behaviour.taker;

import jade.core.behaviours.OneShotBehaviour;
import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.objects.ProtocolMessage;
import fr.univpau.sma.projet.agent.behaviour.taker.WaitForAuction;;

@SuppressWarnings("serial")
public class RegisterAtMarket extends OneShotBehaviour {

	/**
	 * 
	 */
	private final TakerAgent takerAgent;

	/**
	 * @param takerAgent
	 */
	public RegisterAtMarket(TakerAgent takerAgent) {
		this.takerAgent = takerAgent;
	}

	@Override
	public void action() {
		System.out.println("Le taker n'est pas stressé, il arrive au marché les mains dans les poches...");
		ProtocolMessage message = new ProtocolMessage(ProtocolMessage.taker);
		message.addReceiver(this.takerAgent.get_market());
		message.setPerformative(ProtocolMessage.registerEvent);
		this.takerAgent.send(message);
		
	}
	
	@Override
	public int onEnd() {
		ProtocolMessage message = null;
		while(message == null){
			message = (ProtocolMessage) this.takerAgent.receive();
		}
		
		this.takerAgent.addBehaviour(this.takerAgent.getTbf().wrap(new WaitForAuction(takerAgent)));
		return message.getPerformative();
	}
	
}
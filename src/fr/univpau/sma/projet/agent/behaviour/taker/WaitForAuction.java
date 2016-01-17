package fr.univpau.sma.projet.agent.behaviour.taker;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.agent.behaviour.taker.fsm.TakerFSMBehaviour;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;

@SuppressWarnings("serial")
public class WaitForAuction extends CyclicBehaviour {

	/**
	 * 
	 */
	private TakerAgent takerAgent;

	/**
	 * @param takerAgent
	 */
	WaitForAuction(TakerAgent takerAgent) {
		this.takerAgent = takerAgent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		System.out.println("le taker attend de trouver de bonnes enchères");
		ProtocolMessage m = null;
		
		// Reception de la liste des enchères
		m = (ProtocolMessage) this.takerAgent.receive();
		if(m!=null && m.getPerformative() == ProtocolMessage.auctionSpotted)
		{
			System.out.println("Taker récupère les enchères");
			try {
				this.takerAgent.set_Auctions((List<Auction>) m.getContentObject());
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			System.out.println("ENCHERES : " + this.takerAgent.get_Auctions().size());
			
			// Souscription aux enchères
			if(this.takerAgent.get_Auctions() != null && !this.takerAgent.get_Auctions().isEmpty())
			{
				System.out.println("Taker veut souscrire à une ou plusieurs enchères");
				if(!(this.takerAgent.get_ChosenAuctions()==null))
				{
					for (Auction auction : this.takerAgent.get_ChosenAuctions()) {
						this.takerAgent.get_Auctions().remove(auction);
					}
					this.takerAgent.setLower(0);
				}
				else
				{
					this.takerAgent.set_ChosenAuctions(new ArrayList<Auction>());
				}
				int nbAuctionToSubscribe = (int) (Math.random() * (this.takerAgent.get_Auctions().size()-this.takerAgent.getLower())) + this.takerAgent.getLower();
				List<Auction> tempList = new ArrayList<Auction>();
				int i=0;
				while(i<nbAuctionToSubscribe)
				{
					int auctionToSubscribe = (int) (Math.random() * (this.takerAgent.get_Auctions().size()-1) +1);
					if(!tempList.contains(this.takerAgent.get_Auctions().get(auctionToSubscribe-1)))
					{
						tempList.add(this.takerAgent.get_Auctions().get(auctionToSubscribe-1));
					}
					i++;
				}
				this.takerAgent.get_ChosenAuctions().addAll(tempList);
				
				// Envoi du message de souscription aux enchères choisies
				ProtocolMessage subscription = new ProtocolMessage();
				subscription.setPerformative(ProtocolMessage.takerSubscribed);
				try {
					subscription.setContentObject((Serializable) tempList);
					subscription.addReceiver(this.takerAgent.get_market());
					this.takerAgent.send(subscription);
				} catch (IOException e) {
					System.out.println("Le message de souscription n'a pas pu être envoyé");
					e.printStackTrace();
				}
				
				System.out.println("Taker a choisi les enchères auxquelles il veut participer, il en a choisi : " + tempList.size());
				
				for(Auction auction : this.takerAgent.get_ChosenAuctions())
				{
					System.out.println("création des fsm du client");
					this.takerAgent.addBehaviour(this.takerAgent.getTbf().wrap(new TakerFSMBehaviour(takerAgent, auction)));
//					this.takerAgent.get_ParallelAuctions().addSubBehaviour(new TakerFSMBehaviour(takerAgent, auction));
				}
			}
			else System.out.println("_Auctions is EMPTY!!!");
			
		}
		else block();
		
	}
	
}
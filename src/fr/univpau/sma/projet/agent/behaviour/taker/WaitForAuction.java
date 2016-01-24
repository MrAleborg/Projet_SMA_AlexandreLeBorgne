package fr.univpau.sma.projet.agent.behaviour.taker;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.BehaviourLoadingVocabulary;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.agent.behaviour.taker.fsm.TakerFSMBehaviour;
import fr.univpau.sma.projet.gui.taker.TakerGUI;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.ProtocolMessage;

@SuppressWarnings("serial")
public class WaitForAuction extends CyclicBehaviour {

	/**
	 * 
	 */
	private TakerAgent takerAgent;
	private TakerGUI takerGUI;
	private WaitForUserSelection wfus = null;
	/**
	 * @param takerAgent
	 */
	WaitForAuction(TakerAgent takerAgent, TakerGUI takerGUI) {
		this.takerAgent = takerAgent;
		this.takerGUI = takerGUI;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		System.out.println("le taker attend de trouver de bonnes enchères");
		ProtocolMessage m = null;
		
		// Reception de la liste des enchères
		m = (ProtocolMessage) this.takerAgent.blockingReceive(MessageTemplate.MatchPerformative(ProtocolMessage.auctionSpotted));
		if(m!=null)
		{
			
			System.out.println("Taker récupère les enchères");
			List<Auction> listOfAuctions = new ArrayList<Auction>();
			try {
//				this.takerAgent.set_Auctions((List<Auction>) m.getContentObject());
				listOfAuctions.addAll((List<Auction>) m.getContentObject());
				
				// Souscription aux enchères
				if(!listOfAuctions.isEmpty())
				{
					List<Auction> chosenAuctions = this.takerAgent.get_ChosenAuctions();
					System.out.println("Taker veut souscrire à une ou plusieurs enchères");
					if(!(chosenAuctions==null))
					{
						for (Auction auction : chosenAuctions) {
							for(int i = 0 ; i<listOfAuctions.size(); i++)
							{
								if(listOfAuctions.get(i).compareTo(auction) == 0)
								{
									listOfAuctions.remove(i);
								}								
							}
						}
//						this.takerAgent.set_Auctions(listOfAuctions);
//						this.takerAgent.setLower(0);
					}
					else
					{
						this.takerAgent.set_ChosenAuctions(new ArrayList<Auction>());
						chosenAuctions = new ArrayList<Auction>();
					}

//					System.out.println("AUCTIONS 1 --> " + listOfAuctions.size());
//					int nbAuctionToSubscribe = (int) (Math.random() * (listOfAuctions.size()-this.takerAgent.getLower())) + this.takerAgent.getLower();
					List<Auction> tempList = listOfAuctions;
					
					for(Auction a : tempList)
						if(!takerAgent.is_autoMode())
							this.takerAgent.getFrame().addAuctionToSubscribe(a);
					

					this.takerAgent.get_ChosenAuctions().addAll(tempList);
					
									
					if(!tempList.isEmpty() && this.takerAgent.is_autoMode())
					{
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

						System.out.println("NB D'ENCHERES STOCKEES : " + this.takerAgent.get_ChosenAuctions().size());

						for(Auction auction : tempList)
						{
							this.takerGUI.addAuction(auction);
							System.out.println("création des fsm du client : " + auction.get_dealerName());
							this.takerAgent.addBehaviour(this.takerAgent.getTbf().wrap(new TakerFSMBehaviour(takerAgent, auction)));
						}
					}
					else if(!tempList.isEmpty() && !this.takerAgent.is_autoMode())
					{
						System.out.println("Selection manuelle");
						this.takerAgent.set_Auctions(tempList);
						for(Auction a : takerGUI.getModele2().get_mappingAuctionChosen().keySet())
							for(Auction auction : tempList)
							{
								if(a.compareTo(auction)==0)
									this.takerAgent.addBehaviour(this.takerAgent.getTbf().wrap(new WaitForUserSelection(auction, a)));
							}
					}
				}
				else System.out.println("_Auctions is EMPTY!!!");

				
			} catch (UnreadableException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
						
		}
		
	}
	
	private class WaitForUserSelection extends Behaviour {

		boolean done = false;
		private Auction _auction;
		private Auction _a;
		
		public WaitForUserSelection(Auction auction, Auction a) {
			_auction = auction;
			_a = a;
		}
		
		@Override
		public void action() {
			if(takerGUI.getModele2().get_mappingAuctionChosen().get(_a))
			{
				// Envoi du message de souscription aux enchères choisies
				ProtocolMessage subscription = new ProtocolMessage();
				subscription.setPerformative(ProtocolMessage.takerSubscribed);
				try {
					List<Auction> templist = new ArrayList<Auction>();
					templist.add(_auction);
					subscription.setContentObject((Serializable) templist);
					subscription.addReceiver(takerAgent.get_market());
					takerAgent.send(subscription);
				} catch (IOException e) {
					System.out.println("Le message de souscription n'a pas pu être envoyé");
					e.printStackTrace();
				}

				takerGUI.addAuction(_a);
//				takerAgent.get_ChosenAuctions().add(_auction);
				System.out.println("création des fsm du client : " + _auction.get_dealerName());
				takerAgent.addBehaviour(takerAgent.getTbf().wrap(new TakerFSMBehaviour(takerAgent, _auction)));
				done = true;
			}
		}

		@Override
		public boolean done() {
			return done;
		}
		
	}
	
}
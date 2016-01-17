package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

@SuppressWarnings({ "deprecation", "serial" })
public class ProtocolMessage extends ACLMessage {

	public static final int registerEvent = ACLMessage.SUBSCRIBE;
	public static final int getAuctions = 100;
	public static final int takerSubscribed = 101;
	public static final int auctionSpotted = 102;
	public static final int toAnnounce = ACLMessage.CFP;
	public static final int toBid = ACLMessage.PROPOSE;
	public static final int toAttribute = ACLMessage.ACCEPT_PROPOSAL;
	public static final int toGive = ACLMessage.AGREE;
	public static final int toPay = ACLMessage.CONFIRM;

	public static final String taker = "taker";
	public static final String dealer = "dealer";
	
	private String _Message = null;
	private AID _Source = null;
	private List<AID> _Takers = new ArrayList<AID>();
	
	public ProtocolMessage() {
	}

	public ProtocolMessage(String _Message) {
		this._Message = _Message;
	}

	public String get_Message() {
		return _Message;
	}

	public void set_Message(String _Message) {
		this._Message = _Message;
	}

	public AID get_Source() {
		return _Source;
	}

	public void set_Source(AID _Source) {
		this._Source = _Source;
	}

	public List<AID> get_Takers() {
		return _Takers;
	}

	public void set_Takers(List<AID> _Takers) {
		this._Takers = _Takers;
	}
	
}

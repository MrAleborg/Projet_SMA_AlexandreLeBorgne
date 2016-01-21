package fr.univpau.sma.projet.objects;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MarketCurrentAuctionsTable extends AbstractTableModel {
	
	List<Auction> _Auctions = new ArrayList<Auction>();
	
	HashMap<AID, List<Auction>> _mappingAuctions = new HashMap<AID, List<Auction>>();

	public MarketCurrentAuctionsTable(List<Auction> _Auctions,
			HashMap<AID, List<Auction>> _mappingAuctions) {
		super();
		this._Auctions = _Auctions;
//		Auction a = new Auction(50, 10, 5, 50, 10, "TEST", "MRTRUC", 0);
//		this._Auctions.add(a);
		this._mappingAuctions = _mappingAuctions;
	}

	private final String[] entetes = {"Vendeur", "Produit", "Prix", "Prenneurs potentiels"};
	
	@Override
	public int getRowCount() {
		return _Auctions.size();
	}

	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		    case 0:
		        return _Auctions.get(rowIndex).get_dealerName();
		    case 1:
		        return _Auctions.get(rowIndex).get_name();
		    case 2:
		        return _Auctions.get(rowIndex).get_price();
		    case 3:
		    	List<String> takers = new ArrayList<String>();
		    	Set<AID> aidTakers = _mappingAuctions.keySet();
	    		for(AID aid : aidTakers)
	    		{
	    			List<Auction> temp = _mappingAuctions.get(aid);
	    			for(Auction a : temp)
		    			if(a.compareTo(_Auctions.get(rowIndex))==0)
		    			{
		    				takers.add(aid.getLocalName());
		    			}
	    		}
		    	if(!takers.isEmpty())
			    	System.out.println("TAKER : " + takers.get(0));
		    	return takers;
		    default:
		        return null;
		}
	}
	
	public void addAuction(Auction a) {
		
//        _Auctions.add(a);
 
        fireTableRowsInserted(_Auctions.size() -1, _Auctions.size() -1);
    }
 
    public void removeAuction(int rowIndex) {
        _Auctions.remove(rowIndex);

    	fireTableDataChanged();
    	
    }
    
    public void addParticipating(AID tak, Auction auc)
    {
    	if(_mappingAuctions.containsKey(tak))
    		_mappingAuctions.get(tak).add(auc);
    	else
		{
    		List<Auction> l = new ArrayList<Auction>();
    		l.add(auc);
    		_mappingAuctions.put(tak, l);
		}
    	fireTableDataChanged();
    }

    public void removeParticipating(AID tak)
    {
//    	if(_mappingAuctions.containsKey(tak))
//    	{
//    		_mappingAuctions.remove(tak);
//    	}
    	fireTableDataChanged();
    }
    
    public void updateAuction()
    {
    	fireTableDataChanged();
    }

}

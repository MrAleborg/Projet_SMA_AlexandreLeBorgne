package fr.univpau.sma.projet.gui.market;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import fr.univpau.sma.projet.objects.Auction;

@SuppressWarnings("serial")
public class MarketPastAuctionsTable extends AbstractTableModel {

	private final String[] entetes = {"Vendeur", "Produit", "Prix", "Vainqueur"};
	private List<Auction> _Auctions = new ArrayList<Auction>();
	private HashMap<Auction, String> _mappingAutions = new HashMap<Auction, String>();
	
	
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
		    	return _mappingAutions.get(_Auctions.get(rowIndex));
		    default:
		        return null;
		}
	}
	
	public void addAuction(Auction a, String s) {
        _Auctions.add(a);
        _mappingAutions.put(a, s);
 
        fireTableRowsInserted(_Auctions.size() -1, _Auctions.size() -1);
    }

}

package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TakerPastAuctionsTable extends AbstractTableModel {

	private final String[] entetes = {"Produit", "Prix", "Remporté"};
	private List<Auction> _Auctions = new ArrayList<Auction>();
	private HashMap<Auction, Boolean> _mappingAutions = new HashMap<Auction, Boolean>();
	
	
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
		        return _Auctions.get(rowIndex).get_name();
		    case 1:
		        return _Auctions.get(rowIndex).get_price();
		    case 2:
		    	return _mappingAutions.get(_Auctions.get(rowIndex))?"OUI":"NON";
		    default:
		        return null;
		}
	}
	
	public void addAuction(Auction a, Boolean g) {
        _Auctions.add(a);
        _mappingAutions.put(a, g);
 
        fireTableRowsInserted(_Auctions.size() -1, _Auctions.size() -1);
    }

}

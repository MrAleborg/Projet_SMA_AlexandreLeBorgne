package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TakerCurrentAuctionsTable extends AbstractTableModel {

	String[] entetes = {"Enchère", "prix"};
	List<Auction> _Auctions = new ArrayList<Auction>();
	HashMap<Auction, Boolean> _mappingAuctionBid = new HashMap<Auction, Boolean>();
	
	public TakerCurrentAuctionsTable(List<Auction> _Auctions) {
		super();
		this._Auctions = _Auctions;
	}

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
		    default:
		        return null;
		}
	}
	
	public void addAuction(Auction a) {
		
//      _Auctions.add(a);

      fireTableRowsInserted(_Auctions.size() -1, _Auctions.size() -1);
  }

  public void removeAuction(int rowIndex) {
      _Auctions.remove(rowIndex);

  	fireTableDataChanged();
  	
  }
  
  public void updateAuction()
  {
  	fireTableDataChanged();
  }


}

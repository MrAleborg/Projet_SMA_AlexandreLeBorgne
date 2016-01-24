package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TakerAuctionSelectionTable extends AbstractTableModel {

	String[] entetes = {"Enchère", "prix"};
	List<Auction> _Auctions = new ArrayList<Auction>();
	private HashMap<Auction, Boolean> _mappingAuctionChosen = new HashMap<Auction, Boolean>();

	public TakerAuctionSelectionTable() {
		super();
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
	public Object getValueAt(final int rowIndex, int columnIndex) {
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
		System.out.println("ADD AUCTION");
		_Auctions.add(a);
//		for(int i=0 ; i<_Auctions.size(); i++)
//			if(a.compareTo(_Auctions.get(i))==0)
//				this._mappingAuctionChosen.put(_Auctions.get(i), false);
		this._mappingAuctionChosen.put(a, false);
		fireTableDataChanged();
	}

	public void removeAuction(int rowIndex) {
//		for(Auction a : _mappingAuctionChosen.keySet())
//			if(_Auctions.get(rowIndex).compareTo(a)==0)
//				this._mappingAuctionChosen.remove(a);
		_Auctions.remove(rowIndex);

		fireTableDataChanged();

	}

	public void updateAuction()
	{
		fireTableDataChanged();
	}

	public HashMap<Auction, Boolean> get_mappingAuctionChosen() {
		return _mappingAuctionChosen;
	}

	public void set_mappingAuctionChosen(HashMap<Auction, Boolean> _mappingAuctionChosen) {
		this._mappingAuctionChosen = _mappingAuctionChosen;
	} 

	public void ChoseAuctionAt(int i)
	{
//		for(Auction a : _mappingAuctionChosen.keySet())
//			if(_Auctions.get(i).compareTo(a)==0)
//			{
				this._mappingAuctionChosen.replace(_Auctions.get(i), true);
				removeAuction(i);
//			}
//		fireTableDataChanged();
	}
	
}

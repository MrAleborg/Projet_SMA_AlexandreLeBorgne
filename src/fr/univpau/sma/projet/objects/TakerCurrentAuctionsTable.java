package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TakerCurrentAuctionsTable extends AbstractTableModel {

	String[] entetes = {"Enchère", "prix"};
	boolean auto;
	List<Auction> _Auctions = new ArrayList<Auction>();
	private HashMap<Auction, Boolean> _mappingAuctionBid = new HashMap<Auction, Boolean>();

	public TakerCurrentAuctionsTable(List<Auction> _Auctions, boolean auto) {
		super();
		this._Auctions = _Auctions;
		this.auto = auto;
		if(!auto)
		{
			String[] e = {"Enchère", "Prix", "Bid"};
			entetes = e;
		}
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
		case 2:
//			try{
//				return _mappingAuctionBid.get(_Auctions.get(rowIndex))?"OUI":"NON";
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//				return null;
//			}
			for(Auction a : _mappingAuctionBid.keySet())
				if(a.compareTo(_Auctions.get(rowIndex)) == 0)
					return _mappingAuctionBid.get(a)?"OUI":"NON";
				return "null";
		default:
			return null;
		}
	}

	public void addAuction(Auction a) {

		_Auctions.add(a);
		this._mappingAuctionBid.put(a, false);
		fireTableDataChanged();
	}

	public void removeAuction(int rowIndex) {
		Auction a = _Auctions.get(rowIndex);
		this._mappingAuctionBid.remove(a);
		_Auctions.remove(rowIndex);

		fireTableDataChanged();

	}

	public void updateAuction(Auction auct)
	{
//		for(Auction a : _mappingAuctionBid.keySet())
//			for(Auction auction : _Auctions)
//				if(auction.compareTo(a)==0)
		_mappingAuctionBid.replace(auct, false);
		fireTableDataChanged();
	}

	public HashMap<Auction, Boolean> get_mappingAuctionBid() {
		return _mappingAuctionBid;
	}

	public void set_mappingAuctionBid(HashMap<Auction, Boolean> _mappingAuctionBid) {
		this._mappingAuctionBid = _mappingAuctionBid;
	} 

	public void bidAuctionAt(int i)
	{
		if(_Auctions.get(i).get_price()>=_Auctions.get(i).get_minPrice())
			for(Auction a : _mappingAuctionBid.keySet())
				if(_Auctions.get(i).compareTo(a)==0)
					this._mappingAuctionBid.replace(a, true);
		fireTableDataChanged();
	}
	
}

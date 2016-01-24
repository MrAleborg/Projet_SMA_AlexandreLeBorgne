package fr.univpau.sma.projet.objects;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DealerTakerTable extends AbstractTableModel
{
	
	String[] entetes = {"Bidders"};
	List<String> _Bidders = new ArrayList<String>();

	@Override
	public int getRowCount() {
		return _Bidders.size();
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
			return _Bidders.get(rowIndex);
		default:
			return null;
		}
	}

	public void addBidder(String s) {

		_Bidders.add(s);
		fireTableDataChanged();
	}

	public void removeBidder(int rowIndex) {
		_Bidders.remove(rowIndex);

		fireTableDataChanged();

	}
	
	public void initBidders()
	{
		_Bidders = new ArrayList<String>();
		fireTableDataChanged();
	}

}

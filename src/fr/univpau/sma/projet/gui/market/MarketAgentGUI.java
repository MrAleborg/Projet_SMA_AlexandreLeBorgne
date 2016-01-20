package fr.univpau.sma.projet.gui.market;

import jade.core.AID;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.ScrollPaneAdjustable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JLabel;

import fr.univpau.sma.projet.objects.Auction;

public class MarketAgentGUI {

	private JFrame frame;
	private JTable currentAuctionsTable;
	private JTable pastAuctionsTable;
	

	List<Auction> _Auctions = new ArrayList<Auction>();
	
	HashMap<AID, List<Auction>> _mappingAuctions = new HashMap<AID, List<Auction>>();

	public MarketAgentGUI(List<Auction> _Auctions,
			HashMap<AID, List<Auction>> _mappingAuctions) {
		initialize();
		this._Auctions = _Auctions;
		this._mappingAuctions = _mappingAuctions;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarketAgentGUI window = new MarketAgentGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MarketAgentGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 538);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1,2));
				
		JPanel p = new JPanel();
		panel.add(p, BorderLayout.NORTH);
		
		
		JLabel currentAuctions = new JLabel("Enchères en cours");
		p.add(currentAuctions);		
		
		MarketCurrentAuctionsTable modele = new MarketCurrentAuctionsTable(_Auctions, _mappingAuctions);
		
		currentAuctionsTable = new JTable(modele);
		JScrollPane scrollPane = new JScrollPane(currentAuctionsTable);
		scrollPane.setSize(500, 250);
		p.add(scrollPane, BorderLayout.AFTER_LAST_LINE);
		

		JPanel p1 = new JPanel();
		panel.add(p1, BorderLayout.CENTER);
		
		JLabel pastAuctions = new JLabel("Enchères terminées");
		p1.add(pastAuctions, BorderLayout.NORTH);	
		
		MarketPastAuctionsTable modele1 = new MarketPastAuctionsTable();
		
		pastAuctionsTable = new JTable(modele1);
		JScrollPane scrollPane2 = new JScrollPane(pastAuctionsTable);
		p1.add(scrollPane2, BorderLayout.CENTER);
		
		
	}

}

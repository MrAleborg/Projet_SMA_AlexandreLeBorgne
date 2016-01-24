package fr.univpau.sma.projet.gui.taker;

import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import fr.univpau.sma.projet.agent.TakerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.TakerAuctionSelectionTable;
import fr.univpau.sma.projet.objects.TakerCurrentAuctionsTable;
import fr.univpau.sma.projet.objects.TakerPastAuctionsTable;

@SuppressWarnings("serial")
public class TakerGUI extends JFrame {

//	private JPanel contentPane;
	
	List<Auction> _Auctions = new ArrayList<Auction>();
	List<Auction> _PastAuctions = new ArrayList<Auction>();
	private TakerCurrentAuctionsTable modele;
	TakerPastAuctionsTable modele1;
	private TakerAuctionSelectionTable modele2;   

	boolean _autoMode = true;
	TakerAgent _agent;
	
    
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JTabbedPane jTabbedPane2;
	private javax.swing.JTable jTable1;
	private javax.swing.JTable jTable2;
	private javax.swing.JTable jTable3;             
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
	
	public TakerGUI(TakerAgent _agent, boolean _autoMode)
			throws HeadlessException {
		this._agent = _agent;
		this._autoMode=_autoMode;
        initComponents();
	}
	
	
    private void initComponents() {
    	
    	String s = "Taker Agent -- " + _agent.getLocalName();
    	if(this._autoMode)
    		s += " -- AUTOMATIQUE";
    	else s += " -- MANUEL";
    		
    	
    	this.setTitle(s);
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		setModele(new TakerCurrentAuctionsTable(_Auctions, _autoMode));
		jTable1.setModel(getModele());
		jTable1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable t = (JTable) e.getSource();
				Point p = e.getPoint();
				int row = t.rowAtPoint(p);
				System.out.println("Click!!");
				if(e.getClickCount()>=2)
				{
					System.out.println("Double Click!! " + row);
					modele.bidAuctionAt(row);
				}
			}
		});
		
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane2.addTab("Enchères en cours", jScrollPane1);

		modele1 = new TakerPastAuctionsTable();
		jTable2.setModel(modele1);
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane2.addTab("Enchères terminées", jScrollPane2);
        
        if(!this._autoMode)
        {
        	setModele2(new TakerAuctionSelectionTable());
        	jTable3.setModel(getModele2());
        	jTable3.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mousePressed(MouseEvent e) {
    				JTable t = (JTable) e.getSource();
    				Point p = e.getPoint();
    				int row = t.rowAtPoint(p);
    				System.out.println("Click!!");
    				if(e.getClickCount()>=2)
    				{
    					System.out.println("Double Click!! " + row);
    					modele2.ChoseAuctionAt(row);
    				}
    			}
    		});
        	jScrollPane3.setViewportView(jTable3);
        	jTabbedPane2.addTab("Incriptions", jScrollPane3);
        
        }
        
        jLabel1.setText("Argent restant");

        jLabel2.setText(""+this._agent.get_Wallet());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(42, 42, 42)
                            .addComponent(jLabel2)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addContainerGap())
        );
        
        pack();
    }
	
    public void addAuction(Auction auction)
    {
//    	this._Auctions.add(auction);
    	this.getModele().addAuction(auction);
    }
    
    public void removeAuction(Auction auction)
    {
    	int i = -1;
    	for(Auction a : _Auctions)
    		if(a.compareTo(auction)==0)
    			i = _Auctions.indexOf(a);
    	if(i>-1)
    		this.getModele().removeAuction(i);
    }
    
    public void addPastAuction(Auction a, Boolean g)
    {
    	removeAuction(a);
    	
    	modele1.addAuction(a, g);
    	
    }
    
    public void updateAuction(Auction a)
    {
    	for(Auction auct : _Auctions)
    	{
    		if(auct.compareTo(a)==0)
    		{
    			_Auctions.set(_Auctions.indexOf(auct), a);
    		}
    	}

    	getModele().updateAuction(a);
    }


	public TakerCurrentAuctionsTable getModele() {
		return modele;
	}


	public void setModele(TakerCurrentAuctionsTable modele) {
		this.modele = modele;
	}


	public TakerAuctionSelectionTable getModele2() {
		return modele2;
	}


	public void setModele2(TakerAuctionSelectionTable modele2) {
		this.modele2 = modele2;
	}
	
	public void addAuctionToSubscribe(Auction a)
	{
		this.modele2.addAuction(a);
	}
	
	public void updateMoneyLeft()
	{
        jLabel2.setText(""+this._agent.get_Wallet());
	}
	

}

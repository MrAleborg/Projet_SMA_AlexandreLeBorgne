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
import fr.univpau.sma.projet.objects.TakerCurrentAuctionsTable;
import fr.univpau.sma.projet.objects.TakerPastAuctionsTable;

@SuppressWarnings("serial")
public class TakerGUI extends JFrame {

//	private JPanel contentPane;
	
	List<Auction> _Auctions = new ArrayList<Auction>();
	List<Auction> _PastAuctions = new ArrayList<Auction>();
	private TakerCurrentAuctionsTable modele;
	TakerPastAuctionsTable modele1;

	boolean _autoMode = true;
	TakerAgent _agent;
	
    
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane jTabbedPane2;
	private javax.swing.JTable jTable1;
	private javax.swing.JTable jTable2;
	
	public TakerGUI(TakerAgent _agent, boolean _autoMode)
			throws HeadlessException {
		this._agent = _agent;
		this._autoMode=_autoMode;
        initComponents();
	}
	
	
    private void initComponents() {
    	
//    	this.setSize(600, 300);
    	
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        pack();
    }
	
    public void addAuction(Auction auction)
    {
    	this._Auctions.add(auction);
    	this.getModele().addAuction(auction);
    }
    
    public void removeAuction(Auction auction)
    {
    	int i = -1;
    	for(Auction a : _Auctions)
    		if(a.compareTo(auction)==0)
    			i = _Auctions.indexOf(a);
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
    	getModele().updateAuction();
    }


	public TakerCurrentAuctionsTable getModele() {
		return modele;
	}


	public void setModele(TakerCurrentAuctionsTable modele) {
		this.modele = modele;
	}
	
	

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TakerGUI frame = new TakerGUI();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

//	/**
//	 * Create the frame.
//	 */
//	public TakerGUI() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
//	}
	
//	public TakerGUI()
//	{
//        initComponents();
//	}

}

package fr.univpau.sma.projet.gui.dealer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import fr.univpau.sma.projet.agent.DealerAgent;
import fr.univpau.sma.projet.objects.Auction;
import fr.univpau.sma.projet.objects.DealerTakerTable;

@SuppressWarnings("serial")
public class DealerGUI extends JFrame {

	private Auction proposedAuction;
    DealerTakerTable modele = new DealerTakerTable();
	
	DealerAgent _agent;
	
	   /**
  * Creates new form DealerAuctionCreation
	 * @param _agent 
  */
 public DealerGUI(Auction auction, DealerAgent a){
	 proposedAuction = auction;
	 _agent = a;
     initComponents();
 }

 /**
  * This method is called from within the constructor to initialize the form.
  * WARNING: Do NOT modify this code. The content of this method is always
  * regenerated by the Form Editor.
  */
 // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
 private void initComponents() {
	 
	 this.setTitle("Vendeur -- " + _agent.getLocalName());

     jLabel1 = new javax.swing.JLabel();
     jLabel2 = new javax.swing.JLabel();
     jLabel3 = new javax.swing.JLabel();
     jLabel4 = new javax.swing.JLabel();
     jLabel5 = new javax.swing.JLabel();
     jLabel6 = new javax.swing.JLabel();
     jLabel7 = new javax.swing.JLabel();
     jScrollPane1 = new javax.swing.JScrollPane();
     jTable1 = new javax.swing.JTable();
     jLabel8 = new javax.swing.JLabel();
     jLabel9 = new javax.swing.JLabel();
     jLabel10 = new javax.swing.JLabel();
     jLabel11 = new javax.swing.JLabel();
     jLabel12 = new javax.swing.JLabel();
     jLabel13 = new javax.swing.JLabel();
     jLabel14 = new javax.swing.JLabel();
     jLabel15 = new javax.swing.JLabel();
     jLabel16 = new javax.swing.JLabel();

     setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

     jLabel1.setText("Produit");

     jLabel2.setText("Prix Minimum");

     jLabel3.setText("Prix Courant");

     jLabel4.setText("Prix Initial");

     jLabel5.setText("Pas d'incrémentation");

     jLabel6.setText("Pas de décrémentation");

     jLabel7.setText("Bids");

     
     jTable1.setModel(modele);
     
     jScrollPane1.setViewportView(jTable1);

     jLabel8.setText(proposedAuction.get_name());

     jLabel9.setText(""+proposedAuction.get_minPrice());

     jLabel10.setText(""+proposedAuction.get_price());

     jLabel11.setText(""+proposedAuction.get_initPrice());

     jLabel12.setText(""+proposedAuction.get_increaseStep());

     jLabel13.setText(""+proposedAuction.get_decreaseStep());

     jLabel14.setText(""+proposedAuction.get_bids());

     jLabel15.setText("Temps Restant");

     jLabel16.setText("" + proposedAuction.getTimer()/1000L);

     javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
             .addContainerGap()
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                 .addComponent(jLabel1)
                 .addComponent(jLabel2)
                 .addComponent(jLabel3)
                 .addComponent(jLabel4)
                 .addComponent(jLabel5)
                 .addComponent(jLabel6)
                 .addComponent(jLabel7)
                 .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
             .addGap(60, 60, 60)
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                 .addComponent(jLabel16)
                 .addComponent(jLabel14)
                 .addComponent(jLabel13)
                 .addComponent(jLabel12)
                 .addComponent(jLabel11)
                 .addComponent(jLabel10)
                 .addComponent(jLabel9)
                 .addComponent(jLabel8))
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
             .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addContainerGap())
     );
     layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
             .addContainerGap()
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                 .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                 .addGroup(layout.createSequentialGroup()
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel1)
                         .addComponent(jLabel8))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel2)
                         .addComponent(jLabel9))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel3)
                         .addComponent(jLabel10))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel4)
                         .addComponent(jLabel11))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel5)
                         .addComponent(jLabel12))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel6)
                         .addComponent(jLabel13))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel7)
                         .addComponent(jLabel14))
                     .addGap(18, 18, 18)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                         .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                         .addComponent(jLabel16))))
             .addContainerGap(13, Short.MAX_VALUE))
     );
     

     pack();
 }// </editor-fold>                        

 Timer timer;

 class TimerListener implements ActionListener{
     int elapsedSeconds = (int) (proposedAuction.getTimer()/1000L);

 	@Override
     public void actionPerformed(ActionEvent evt){
         elapsedSeconds--;
         jLabel16.setText(""+elapsedSeconds);
         if(elapsedSeconds <= 0){
             timer.stop();
         }
     }

 }
 
 public void startTimer()
 {
	 initTimer();
	 timer.start();
 }
 
 public void stopTimer()
 {
	 timer.stop();
 }
 
 public void initTimer()
 {
     timer  = new Timer(1000, (ActionListener) new TimerListener());
 }
 
 public void updateBids(int i)
 {
	 jLabel14.setText(""+i);
 }
 
 public void updatePrice(int i)
 {
	 jLabel10.setText(""+i);
 }
 
	public void addBidder(String s) {

		modele.addBidder(s);

	}

	public void removeBidder(int rowIndex) {
		
		modele.removeBidder(rowIndex);

	}
	
	public void initBidders()
	{
		modele.initBidders();
	}

 // Variables declaration - do not modify                     
 private javax.swing.JLabel jLabel1;
 private javax.swing.JLabel jLabel10;
 private javax.swing.JLabel jLabel11;
 private javax.swing.JLabel jLabel12;
 private javax.swing.JLabel jLabel13;
 private javax.swing.JLabel jLabel14;
 private javax.swing.JLabel jLabel15;
 private javax.swing.JLabel jLabel16;
 private javax.swing.JLabel jLabel2;
 private javax.swing.JLabel jLabel3;
 private javax.swing.JLabel jLabel4;
 private javax.swing.JLabel jLabel5;
 private javax.swing.JLabel jLabel6;
 private javax.swing.JLabel jLabel7;
 private javax.swing.JLabel jLabel8;
 private javax.swing.JLabel jLabel9;
 private javax.swing.JScrollPane jScrollPane1;
 private javax.swing.JTable jTable1;
 // End of variables declaration   

}

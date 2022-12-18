
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;


public class ProducerConsumer
{
	// main method
	public static void main(String [] args)
	{
		ProducerConsumerGUI pcg = new ProducerConsumerGUI();
        }

   
	// custom thread subclass for producer
	private static	class Importing  extends Thread {
		 private List<Integer> inventory;
		static int inventorySize; //maximum number of products which inventory can hold at a time.
		  static int productionSize; //Total no of items to be produced by each producer
		 int TruckNo;
		 public Importing(List<Integer> inventory, int TruckNo) {
		     this.inventory = inventory;
		     this.TruckNo = TruckNo;
		 }

		 @Override
		 public void run() {
		     for (int i = 1; i <= productionSize; i++) { //produce products.
		         try {
		             Import(i);

		         } catch (InterruptedException e) {  e.printStackTrace(); }
		     }
		     
		}
		 private void Import(int i) throws InterruptedException {
			  
			    synchronized (inventory) {            

			       //if sharedQuey is full wait until consumer consumes.
			       while (inventory.size() == inventorySize) {
			             System.out.println(Thread.currentThread().getName()+"|| Factory's inventory is full, Factory is waiting for "
			                    + "ExportingTrucks  to export products, inventory size= "+inventorySize);
			             inventory.wait();
			         }

			       //Bcz each producer must produce unique product
			             //Ex= producer0 will produce 1-5  and producer1 will produce 6-10 in random order
			       int producedItem = (productionSize*TruckNo)+ i;  
			       
			       System.out.println(Thread.currentThread().getName() +" import Product num : " + producedItem);
			       inventory.add(producedItem);
			         Thread.sleep((long)(Math.random() * 1000));
			         inventory.notify();
			     }
			 }   
			}
   
	// custom thread subclass for consumer
	private static class Exporting extends Thread {
	    private List<Integer> inventory;
	    static int wants = Importing.productionSize;
	    int gets = 0;
	 public Exporting(List<Integer> inventory) {
	     this.inventory = inventory;
	 }
	 
	    @Override
	public void run() {
	    gets = 0;
	    while (gets < wants) {
	        try {
	            Export();
	            gets++;

	        } catch (InterruptedException e) {  e.printStackTrace(); }
	    }
	}

	 private void Export() throws InterruptedException {
	                

	    synchronized (inventory) {
	       //if sharedQuey is empty wait until producer produces.
	       while (inventory.size() == 0) {
	           System.out.println(Thread.currentThread().getName()+"|| Factory's inventory is empty, Factory is waiting for "
	                           + "importing Trucks to import Products, inventory size= 0");  
	           inventory.wait();  
	         }
	       Thread.sleep((long)(Math.random() * 2000));
	         System.out.println(Thread.currentThread().getName()+" Export product num : "+ inventory.remove(0));
	         inventory.notify();
	     }  
	 } 
	}

   
   static class ProducerConsumerGUI extends JFrame 
   implements ActionListener
   {
	   
	   private JFrame errorFrame, actionFrame;
	   private JPanel prepPanel, actionPanel, prepProducersPanel,
	   prepConsumersPanel, prepCenterPanel,
	    prepStartButtonPanel;
	   private JLabel prepProducersLabel, prepConsumersLabel, 
	   prepTitle;
	   private JButton startButton;
	   private JTextField prepProducersField, prepConsumersField;
	  
	   private JScrollPane actionScrollPane;
	   static JTextArea errorText, actionResourceText, actionConsoleText;
	   
	   private Font prepFontSmall = new Font("Arial", Font.PLAIN, 14);
	   private Font actionFont = new Font("Arial", Font.PLAIN, 10);
	   private Font resourceFont = new Font("Impact", Font.PLAIN, 30);
	   private Font prepFontLarge = new Font("Arial", Font.PLAIN, 26);
	   private GridLayout prepCenterGridLayout = new GridLayout(4, 1);
	   private JPanel gridSpace = new JPanel();
	   
	   
	   
	   public ProducerConsumerGUI() 
	   {
		   // initial frame setup
		   super("Producer-Consumer Interface");
		   setSize(600, 550);
		   this.setResizable(true);
		   this.setLayout(new BorderLayout());
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   
		   
		   prepPanel = new JPanel();
		   
	
		   prepProducersLabel = new JLabel("Inventory Size:", SwingConstants.LEFT);
		   prepProducersLabel.setFont(prepFontLarge);
		   prepProducersField = new JTextField(30);
		   prepProducersField.setPreferredSize(new Dimension(100,40));
		   
		   prepProducersPanel = new JPanel(new BorderLayout());
		   prepProducersPanel.add(prepProducersLabel, BorderLayout.CENTER);
		   prepProducersPanel.add(prepProducersField, BorderLayout.EAST);
		   
		   // *consumers field and its label
		   prepConsumersLabel = new JLabel("Production Size:", SwingConstants.LEFT);
		   prepConsumersLabel.setFont(prepFontLarge);
		   prepConsumersField = new JTextField(30);
		   prepConsumersField.setPreferredSize(new Dimension(100,40));
		   
		   prepConsumersPanel = new JPanel(new BorderLayout());
		   prepConsumersPanel.add(prepConsumersLabel, BorderLayout.CENTER);
		   prepConsumersPanel.add(prepConsumersField, BorderLayout.EAST);
		   
		  
		   // *start button
		   startButton = new JButton("Start");
		   startButton.setFont(prepFontLarge);
		   
		   prepStartButtonPanel = new JPanel(new GridLayout(1, 3));
		   
		   prepStartButtonPanel.add(gridSpace);
		   prepStartButtonPanel.add(gridSpace);
		   prepStartButtonPanel.add(startButton, BorderLayout.SOUTH);
		   
		   
		   prepCenterPanel = new JPanel(prepCenterGridLayout);
		   prepCenterPanel.setSize(250, 160);
		   prepCenterGridLayout.setVgap(50);
		   prepCenterPanel.add(prepProducersPanel);
		  
		   prepCenterPanel.add(prepConsumersPanel);
		  
		   
		   prepPanel.add(prepCenterPanel, BorderLayout.CENTER);
		   prepPanel.add(prepStartButtonPanel, BorderLayout.SOUTH);
		   
		   this.add(prepPanel);
		   
		   // implement actionlistener for start button that begins action phase when clicked
		   startButton.addActionListener((event) -> 
		   actionPhaseCheck(prepProducersField.getText(),
				   prepConsumersField.getText()));
		   
		   // set to visible, completing the constructor process
		   this.add(prepPanel);
		   setVisible(true);
	   }

	   public void actionPhaseCheck(String pro ,String con) 
	   {
		   try 
		   {
			 Importing.inventorySize = Integer.parseInt(pro);
			  
			 Importing.productionSize = Integer.parseInt(con);
			  
			   
			   actionPhase( Importing.inventorySize,  Importing.productionSize );
					 
		   }
		   catch(NumberFormatException e) 
		   {
			   // an error results in a catch that creates a new frame describing the error to the user
			   errorFrame = new JFrame("Error Encountered");
			   errorFrame.setLayout(new BorderLayout());
			   errorFrame.setSize(350, 60);
			   errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			   errorText = new JTextArea(1, 1);
			   errorText.setEditable(false);
			   errorText.setFont(prepFontSmall);
			   errorText.setText("Oops! Please enter integers only into the text boxes.");
			   errorFrame.add(errorText, BorderLayout.CENTER);
			   errorFrame.show();
		   }
	   }
	   
	   public void actionPhase(int pro, 
			   int con) 
	   {
		   
		   List<Integer> inventory = new LinkedList<>(); //Creating shared object
		   
		   Importing importingTruck0=new Importing(inventory, 0);
		   Exporting ExportingTruck0=new Exporting(inventory);

		      Thread import0 = new Thread(importingTruck0, "importingTruck Num 0->");
		      Thread export0 = new Thread(ExportingTruck0, "ExportingTruck Num 0->");
		      import0.start();
		      export0.start();
		      
		      
		   Importing importingTruck1=new Importing(inventory, 1);
		   Exporting ExportingTruck1=new Exporting(inventory);

		      Thread import1 = new Thread(importingTruck1, "importingTruck Num 1->");
		      Thread export1 = new Thread(ExportingTruck1, "ExportingTruck Num 1->");
		      import1.start();
		      export1.start();
		   
		   
		   class ConsoleOutputStream extends OutputStream 
		   {
			   private JTextArea textArea;
			   
			   public ConsoleOutputStream(JTextArea textArea) 
			   {
				   this.textArea = textArea;
			   }
			   
			   @Override
			   public void write(int b) throws IOException 
			   {
				   textArea.setText(textArea.getText() + String.valueOf((char)b));
				   textArea.setCaretPosition(textArea.getDocument().getLength());
				   textArea.update(textArea.getGraphics());
			   }
		   }
		   
		   
		   actionFrame = new JFrame("Action Phase");
		   actionFrame.setLayout(new BorderLayout());
		   actionFrame.setSize(420, 310);
		   actionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   
		   actionConsoleText = new JTextArea(35, 15);
		   actionConsoleText.setEditable(false);
		   actionScrollPane = new JScrollPane(actionConsoleText);
		   actionScrollPane.setSize(420, 220);
		   
		   actionResourceText = new JTextArea(1, 1);
		   actionResourceText.setFont(resourceFont);
		   actionResourceText.setEditable(false);
		   actionResourceText.setSize(120, 120);
		   
		   actionPanel = new JPanel(new BorderLayout());
		   actionPanel.add(actionResourceText, BorderLayout.NORTH);
		   actionPanel.add(actionScrollPane, BorderLayout.CENTER);
		   
		   PrintStream ps = new PrintStream(new ConsoleOutputStream(actionConsoleText));
		   System.setOut(ps);
		   System.setErr(ps);
		   
		   actionFrame.add(actionPanel);
		   actionFrame.show();  
	   }

	   @Override
	   public void actionPerformed(ActionEvent arg0) {}
	}
} 

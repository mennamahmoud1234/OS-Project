/*
 * cameron campbell
 * advanced java
 * occc spring 2021
 * concurrency gui (producer-consumer)
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/*
 * modified version of the given Producer-Consumer
 * program that accommodates a GUI for ease of use
 * and user control over producers and consumers
 */
public class ProducerConsumer
{
	// class-relevant variables, fields and objects
	


   
	

   
	// main method
	public static void main(String [] args)
	{
		ProducerConsumerGUI pcg = new ProducerConsumerGUI();
	}

   
	// custom thread subclass for producer
	private static class Producer extends Thread
	{ 
		 private List<Integer> sharedQueue;
		static int maxSize; //maximum number of products which sharedQueue can hold at a time.
		  static int productionSize; //Total no of items to be produced by each producer
		  int producerNo;
		
		public Producer(List<Integer> sharedQueue, int producerNo) {
		     this.sharedQueue = sharedQueue;
		     this.producerNo = producerNo;
		 }
		

		 public void run() {
		     for (int i = 1; i <= productionSize; i++) { //produce products.
		         try {
		        	 synchronized (sharedQueue) {            

		        	       //if sharedQuey is full wait until consumer consumes.
		        	       while (sharedQueue.size() == maxSize) {
		        	             System.out.println(Thread.currentThread().getName()+", Queue is full, producerThread is waiting for "
		        	                    + "consumerThread to consume, sharedQueue's size= "+maxSize);
		        	             sharedQueue.wait();

		        	         }

		        	       //Bcz each producer must produce unique product
		        	             //Ex= producer0 will produce 1-5  and producer1 will produce 6-10 in random order
		        	       int producedItem = (productionSize*producerNo)+ i;  
		        	       
		        	       System.out.println(Thread.currentThread().getName() +" Produced : " + producedItem);
		        	       sharedQueue.add(producedItem);
		        	         Thread.sleep((long)(Math.random() * 1000));
		        	         sharedQueue.notify();
		        	     }
		         } catch (InterruptedException e) {  e.printStackTrace(); }
		     }
		     
		}
			
	}

   
	// custom thread subclass for consumer
	private static class Consumer extends Thread
	{
		  private List<Integer> sharedQueue;
		    static int wants = Producer.productionSize;
		    int gets = 0;
		 public Consumer(List<Integer> sharedQueue) {
		     this.sharedQueue = sharedQueue;
		 }
		 public void run() {
			    gets = 0;
			    while (gets < wants) {
			        try {
			        	synchronized (sharedQueue) {
			        	       //if sharedQuey is empty wait until producer produces.
			        	       while (sharedQueue.size() == 0) {
			        	           System.out.println(Thread.currentThread().getName()+", Queue is empty, consumerThread is waiting for "
			        	                           + "producerThread to produce, sharedQueue's size= 0");  
			        	           sharedQueue.wait();
			        	            
			        	         }

			        	       Thread.sleep((long)(Math.random() * 2000));
			        	         System.out.println(Thread.currentThread().getName()+", CONSUMED : "+ sharedQueue.remove(0));
			        	         sharedQueue.notify();
			        	     }
			            gets++;

			        } catch (InterruptedException e) {  e.printStackTrace(); }
			    }
			}

		
   } 

   
   /*
    * entirety of the GUI class, appended to the Producer-Consumer class.
    * the GUI is divided between two phases, represented by two master frames
    * where the subpanels and controls are housed: the prep phase and action phase.
    * the prep phase is where the user can input the number of producers, consumers,
    * average amount consumed, and average amount produced before entering the action
    * phase, where the user can witness the actions of the producers and consumers
    * through the constantly-updated resource integer and a text pane detailing what
    * the producers and consumers are doing in the console output stream
    */
   static class ProducerConsumerGUI extends JFrame 
   implements ActionListener
   {
	   /*
	    * subclass-relevant objects, fields, and controls. i used a custom grid 
	    * layout for the start button panel so that i could have more 
	    * control over the size, shape, and alignment of the fields on the prep
		* panel. i also chose to create an empty JPanel for the purpose of giving
		* me more control over the exact positioning of controls in my grid layouts;
		* any time i need a negative space, i can just add the gridSpace JPanel to
		* the next grid cell
	    */
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
	   
	   
	   /*
	    * the constructor performs basic frame setup for the prep pane. once
	    * the user clicks the start button, the actionPhase method is called,
	    * setting up the action panel
	    */
	   public ProducerConsumerGUI() 
	   {
		   // initial frame setup
		   super("Producer-Consumer Interface");
		   setSize(250, 240);
		   this.setResizable(false);
		   this.setLayout(new BorderLayout());
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   
		   /*
		    * prep panel setup
		    */
		   // *panel
		   prepPanel = new JPanel(new BorderLayout());
		   
		   // *title
		   prepTitle = new JLabel("Preparation Phase", SwingConstants.CENTER);
		   prepTitle.setFont(prepFontLarge);
		   prepPanel.add(prepTitle, BorderLayout.NORTH);
		   
		   // *producers field and its label
		   prepProducersLabel = new JLabel("maxsize:", SwingConstants.RIGHT);
		   prepProducersLabel.setFont(prepFontSmall);
		   prepProducersField = new JTextField(5);
		   
		   prepProducersPanel = new JPanel(new BorderLayout());
		   prepProducersPanel.add(prepProducersLabel, BorderLayout.CENTER);
		   prepProducersPanel.add(prepProducersField, BorderLayout.EAST);
		   
		   // *consumers field and its label
		   prepConsumersLabel = new JLabel("production size:", SwingConstants.RIGHT);
		   prepConsumersLabel.setFont(prepFontSmall);
		   prepConsumersField = new JTextField(5);
		   
		   prepConsumersPanel = new JPanel(new BorderLayout());
		   prepConsumersPanel.add(prepConsumersLabel, BorderLayout.CENTER);
		   prepConsumersPanel.add(prepConsumersField, BorderLayout.EAST);
		   
		  
		   // *start button
		   startButton = new JButton("Start");
		   startButton.setFont(prepFontSmall);
		   
		   prepStartButtonPanel = new JPanel(new GridLayout(1, 3));
		   
		   prepStartButtonPanel.add(gridSpace);
		   prepStartButtonPanel.add(gridSpace);
		   prepStartButtonPanel.add(startButton, BorderLayout.SOUTH);
		   
		   /*
		    * assemble all controls and panels into the prep panel and
		    * add prep panel to frame
		    */
		   prepCenterPanel = new JPanel(prepCenterGridLayout);
		   prepCenterPanel.setSize(250, 160);
		   prepCenterGridLayout.setVgap(10);
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
	   
	   
	   /*
	    * actionPhaseCheck method handles the inputs for the four text fields
	    * in the prep phase panel and checks if they're valid. if they are, then
	    * they're sent directly to the actionPhase method for processing into the
	    * final portion of the GUI. otherwise, the user is prompted to retry their
	    * inputs until they become valid
	    */
	   public void actionPhaseCheck(String pro ,String con) 
	   {
		   try 
		   {
			  Producer.maxSize = Integer.parseInt(pro);
			  
			  Producer.productionSize = Integer.parseInt(con);
			  
			   
			   actionPhase(Producer.maxSize ,  Producer.productionSize);
					 
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
	   
	   
	   /*
	    * the actionPhase method uses the successfully passed integers from actionPhaseCheck
	    * to setup the static average variables used by the consumer and producer custom threads
	    * ('theBuffer' has become 'resource' and the random resource generated or consumed is now
	    * based off of these average values). the method then initializes the frame for the action
	    * phase, sets up the console's output to the JTextArea in the action panel, runs the frame,
	    * and then finally sets up the SwingWorker for the resource value so that it will constantly
	    * update for the user
	    */
	   public void actionPhase(int pro, 
			   int con) 
	   {
		   // initial setup of producers and consumers from passed integers
		   
		   
		   List<Integer> sharedQueue = new LinkedList<>();

		   Producer producer0=new Producer(sharedQueue, 0);
		   Consumer consumer0=new Consumer(sharedQueue);

		      Thread producerThread0 = new Thread(producer0, "ProducerThread0");
		      Thread consumerThread0 = new Thread(consumer0, "ConsumerThread0");
		      producerThread0.start();
		      consumerThread0.start();
		      
		      
		   Producer producer1=new Producer(sharedQueue, 1);
		   Consumer consumer1=new Consumer(sharedQueue);

		      Thread producerThread1 = new Thread(producer1, "ProducerThread1");
		      Thread consumerThread1 = new Thread(consumer1, "ConsumerThread1");
		      producerThread1.start();
		      consumerThread1.start();
		   
		   /*
		    * to display the console in actionTextPane, a replacement class must be created
		    * for the output stream. this will allow the program to redirect all console 
		    * messages to actionTextPane
		    */
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
		   
		   /*
		    * now that everything has been prepared, the action phase is ready
		    * to begin; the actionFrame and its components are instantiated and
		    * display the current resource value and a log of the console's
		    * output. a custom thread will need to be created in order to 
		    * provide this information to the user, and so the GUI class
		    * will also house an override of SwingWorker
		    */
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
		   
		   /*
		    * finally, a swingworker object is instantiated to update the actionResourceText
		    * with the current resource value now that it has been initialized
		    */
//		   SwingWorker updater = new SwingWorker()
//		   {
//			   @Override
//			   protected Object doInBackground() throws Exception
//			   {
//				   while (true) 
//				   {
//					   actionResourceText.setText("Number of Resources: " + String.valueOf(resource));
//				   }
//			   }
//		   };
//		   updater.execute();
	   }
	   
	   
	   /*
	    * obligatory implemented ActionListener method. i prefer individual
	    * methods called by the parameters of the addActionListener calls
	    * for each control in the constructor, as it's generally cleaner and
	    * more reliable
	    */
	   @Override
	   public void actionPerformed(ActionEvent arg0) {}
	}
} 

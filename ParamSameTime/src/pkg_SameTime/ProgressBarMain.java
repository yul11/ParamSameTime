package pkg_SameTime;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProgressBarMain extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
    private JButton startButton;
    private JButton crashButton;    
    private JTextArea taskOutput;
    private Task task;
    private Ecu mEcu;
    int progress;
    boolean running;
    TWatchAll tw;
    
    
    class Task extends SwingWorker<Void, Void> {   	  	    	
        int progress = 0;
        boolean stop = false;
        Ecu ecu;
        
        Task (Ecu ecu){
        	super();
        	this.ecu = ecu;
        }            
        
        public Void doInBackground() {
            Random random = new Random();
            
            //Initialize progress property.
            setProgress(0);
            while (progress < 100 && !stop) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
                ecu.setProgress(progress);
            }
            return null;
        }


        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
        
        public void stop() {
        	stop = true;
        } 
        
        public boolean isRunning(){
        	if (progress <= 0 || progress >= 100 || stop)
        		running = false;
        	else
        		running = true;
        	return running;
    	}
    }

    
    
    
    
    public ProgressBarMain(Ecu ecu) {

        super(new BorderLayout());       
        mEcu = ecu;        

        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        
        crashButton = new JButton("Crash");
        crashButton.setActionCommand("crash");
        crashButton.addActionListener(this);        
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.GREEN);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(crashButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    
    public void actionPerformed(ActionEvent evt) {
    	
    	if ("start".equals(evt.getActionCommand())) {
            startButton.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Instances of javax.swing.SwingWorker are not reusable, so
            //we create new instances as needed.
    		progressBar.setBackground(Color.GRAY);    		
    		crashButton.setForeground(Color.BLACK);
    		crashButton.setBackground(Color.GRAY);
    		
    		mEcu.setState(2);
    		System.out.println(mEcu.getName() + " setze state auf 2");	
    		mEcu.setProgress(1);
    		
            task = new Task(mEcu);
            task.addPropertyChangeListener(this);
            task.execute();            
            new TWatchOne(mEcu).start();
    	}
    	
    	if ("crash".equals(evt.getActionCommand())) {
    		System.out.println(mEcu.getName() + " crashed!!!");	
    		crashButton.setForeground(Color.YELLOW);
    		crashButton.setBackground(Color.RED);
    		progressBar.setBackground(Color.RED);
    		progressBar.setValue(0); 
    		task.stop(); 
    		
    		mEcu.setState(999);
    		mEcu.setProgress(progress);
    		
    		System.out.println("juo: ProgressBarMain()-> ecu: " + mEcu.getName() + " progress: " + progress + " state: " + mEcu.getState());
    	}    	
    }


    //Invoked when task's progress property changes.
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName() && mEcu.getState()!=999) {
            progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
            mEcu.setState(0);
            System.out.println("juo: ProgressBarMain()-> ecu: " + mEcu.getName() + " progress: " + progress + " state: " + mEcu.getState());
        } 
    }
    
    
    public boolean taskStillRunning() {
    	if (task.isRunning())
    		return true;
    	else
    		return false;
    }


    //Create the GUI and show it. As with all GUI code, this must run on the event-dispatching thread.
    private static ProgressBarMain createAndShowGUI(String headline,Ecu e,int x,int y) {
    	
        //Create and set up the window.
        JFrame frame = new JFrame(headline);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        ProgressBarMain newContentPane = new ProgressBarMain(e);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);                

        //Display the window.
        frame.pack();
        frame.setLocation(x,y);
        frame.setVisible(true);

        return newContentPane;
    }
    
    
    
    public static void main(String[] args) {
    	
    	Map<String, Ecu> mp = new HashMap<String, Ecu>();   
    	TWatchAll ta = new TWatchAll(mp);
    	ta.start();
 
    	
    	
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	            	
            	Map<String, Ecu> mp = new HashMap<String, Ecu>();            	
            	
                Ecu ecu1 = new Ecu();
                ecu1.setName("PTM      ");
                mp.put("PTM      ", ecu1);
                
                Ecu ecu2 = new Ecu();
                ecu2.setName("EDC17    "); 
                mp.put("EDC17    ", ecu2);
 
                Ecu ecu3 = new Ecu();
                ecu3.setName("Intarder3"); 
                mp.put("Intarder3", ecu3); 
                                
                TWatchAll ta = new TWatchAll(mp);                
                                                
                createAndShowGUI("PTM",ecu1,1500,292);
                createAndShowGUI("EDC17",ecu2,1500,500);
                createAndShowGUI("Intarder3",ecu3,1500,708);
                
                ta.init();
                ta.start();
 
            }
        });
    }
}

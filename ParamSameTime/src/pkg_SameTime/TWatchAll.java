package pkg_SameTime;
import java.util.Map;

public class TWatchAll extends Thread{
	
	Map<String, Ecu> m;
	boolean init=false;
	boolean end = false;		//indicates that all ecus-threads ended
	boolean oneTRuns = false;  //indicates that one ecu-thread is still running
	

    
	TWatchAll(Map<String,Ecu> mp){	
		this.m = mp;
		end = false;
	}
	
	public void warteZeit() {
		try {Thread.sleep(Math.round( 2000 * Math.random() ));}
		catch (InterruptedException e) {}
	}
	
	public void init(){
		this.init = true;
		
	}
	
	private void printResult(Map<String,Ecu> m){
		
		System.out.println("juo: TWatchAll()-> Ergebnis: ");	
		System.out.println("**********************************************************************");		
	    for(String key : m.keySet())
	    {	       	       
	       System.out.println("*juo: TWatchAll()-> Key:      " + key);	
	       Ecu e = m.get(key);
	       System.out.println("*juo: TWatchAll()-> name:     " + e.getName());
	       System.out.println("*juo: TWatchAll()-> progress: " + e.getProgress());
	       System.out.println("*juo: TWatchAll()-> state:    " + e.getState() + "\n");	       	
	    }
	    System.out.println("**********************************************************************");
		
	}

	
	
	public void run() {
		warteZeit();		
		
		while (init && !end){
			oneTRuns = false;
		    for(String key : m.keySet())
		    {
		       System.out.println("juo: TWatchAll()-> Key:      " + key);	
		       Ecu e = m.get(key);
		       System.out.println("juo: TWatchAll()-> name:     " + e.getName());
		       System.out.println("juo: TWatchAll()-> progress: " + e.getProgress());
		       System.out.println("juo: TWatchAll()-> state:    " + e.getState());
		      
		       if (e.getProgress()<100  && e.getState()!=999){
		    	  System.out.println("juo: TWatchAll()-> process:  " + e.getName() + " still running..." + "\n");
		    	  oneTRuns = true;
		       }
		       else{
			       System.out.println("juo: TWatchAll()-> process:  " + e.getName() + " ended" + "\n");
		       }		    	  
		    }
		    if (!oneTRuns){
		       printResult(m);	
		       end = true;		    	   		    	
		    }
		    warteZeit();		    
		}			
	}
}




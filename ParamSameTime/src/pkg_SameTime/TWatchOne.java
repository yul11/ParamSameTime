package pkg_SameTime;
public class TWatchOne extends Thread{
	
	private Ecu ecu;
    boolean running;
    
	TWatchOne(Ecu e){		
		this.ecu = e;
	}

	
	public void warteZeit() {
		try {Thread.sleep(Math.round( 1000 * Math.random() ));}
		catch (InterruptedException e) {}
	}
	
	
    public boolean isRunning(){
    	
    	if (ecu.getProgress()<100) {
    		running = true;
    		System.out.println("juo: TWatchOne()-> running = true");    		
    	}
    	else {
    		running = false;
    		System.out.println("juo: TWatchOne()-> running = false");
    	}
    	return running;
	}
    
    
    
    
	public void run() {
		warteZeit();		
		while (ecu.getProgress()<100 && ecu.getState()!=999){			
			System.out.println("juo: TWatchOne()-> ------------- progress of: " + ecu.getName() + " is: " + ecu.getProgress() + " ------------------");
			warteZeit();
		}
		if (ecu.getProgress()>=100)
			System.out.println("juo: TWatchOne()-> " + ecu.getName() + " hit target *** " + ecu.getProgress() + " percent");
		if (ecu.state==999)
			System.out.println("juo: TWatchOne()-> " + ecu.getName() + " did not hit target, error occured *** ");
	}	
}

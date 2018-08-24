package pkg_SameTime;

public class Ecu{
	
	
	String  name;
	int     progress;
	int     state;
	
	public Ecu() {		
		name 	 = "NN";
		progress = 0;
		state    = 1;		
	}
	
	
	public void setName(String n) {
		this.name = n;
	}
	public void setProgress(int p) {
		this.progress = p;
	}
	public void setState(int s) {
		this.state = s;
	}
	
	
	public String getName() {
		return this.name;
	}
	public int getProgress() {
		return this.progress;
	}
	public int getState() {
		return this.state;
	}	
}

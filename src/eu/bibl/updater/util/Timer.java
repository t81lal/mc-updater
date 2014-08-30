package eu.bibl.updater.util;

public class Timer {

	private long time;
	
	public Timer(){
		start();
	}
	
	public void start(){
		time = System.nanoTime();
	}
	
	public long getElapsedTime(){
		return System.nanoTime() - time;
	}
	
	public void reset(){
		time = System.nanoTime();
	}
	
	public long stop(){
		long elapsed = getElapsedTime();
		reset();
		return elapsed;
	}
	
	public long stopMs(){
		return stop() / 1000000;
	}
}
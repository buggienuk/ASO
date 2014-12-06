package Program;
import gui.GUI;

import javax.swing.JFrame;


public class Main extends JFrame {
	World w;
	GUI gui;
	final int SCREEN_HOR = 600;
	final int SCREEN_VER = 600;
	Config c;
	int currentCount;
	boolean step;
	
	
	Main(String[] args)
	{
		currentCount = 0;
				
		// no args, default settings:
		if(args.length == 0)
		{ 
			c = new Config();
		} else {
			// read configuration from file.
			c = new Config(args[0]);		
		}
		
		gui = new GUI(SCREEN_HOR, SCREEN_VER,c);
	
		w = new World(c.clone());
		
		gui.updateGraphics(w);
	}
	
	public void start() throws InterruptedException
	{	int count = 0;
		while(true) // World not full
		{
			step();	
			count += 1;
			System.out.println(count);
			
			
		}
	}
	
	private void step() throws InterruptedException
	{
		update();
		checkPaused();
		if (currentCount == 0){
			initializeWorld();
		}else{
			w.doIteration();
		}
		updateGraphics();
	}
	
	private void initializeWorld()
	{	
		w.generateNew();
	}
	
	private void updateGraphics()
	{
		if(currentCount == c.updateEvery || currentCount > c.updateEvery || step)
		{
			gui.updateGraphics(w);
			currentCount = 0;
			step = false;
		} else { currentCount++; }
	}
	
	private void update() throws InterruptedException
	{
		Thread.sleep(c.sleepTime);
		c = gui.update(w);
		
		// check if we're reset, if yes, create a new world and draw it. 
		if(gui.reset())
		{
			w = new World(c.clone());
			gui.updateGraphics(w);
		}
		if(gui.step())
		{
			step = true;
		}
		
	}
	
	private void checkPaused() throws InterruptedException
	{
		while(w.paused() && !step) { 
			Thread.sleep(c.sleepTime);
			c = gui.update(w);
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		new Main(args).start();
	}
}
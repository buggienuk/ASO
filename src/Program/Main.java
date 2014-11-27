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
	
		w = new World(c);
	}
	
	public void start() throws InterruptedException
	{
		while(true)
		{
			c = gui.update(w);
			while(w.paused()) { 
				Thread.sleep(c.sleepTime);
				c = gui.update(w);
				System.out.println("paused!");
			}
			//w.doIteration();
			w.generateNew();
			
			// update every 50 iterations
			if(currentCount == c.updateEvery || currentCount > c.updateEvery)
			{
				gui.updateGraphics(w);
				currentCount = 0;
			} else { currentCount++; }
			
			// sleep a bit, otherwise the program moves too fast. 
			try {
				Thread.sleep(c.sleepTime);
			} catch (InterruptedException e) {
				// catch don't care, go on.
			}
		}
	}
	
	
	
	public static void main(String[] args) throws InterruptedException
	{
		new Main(args).start();
	}
}

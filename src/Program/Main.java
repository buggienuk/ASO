package Program;
import java.io.FileNotFoundException;

import gui.GUI;

import javax.swing.JFrame;


public class Main extends JFrame {
	World w;
	GUI gui;
	final int SCREEN_HOR = 600;
	final int SCREEN_VER = 600;
	Config c;
	int currentCount;
	int currentIteration;
	final int MAX_ITERATIONS = 2000;
	final int START_STORING_DATA = 1900;
	boolean step;
	Writer writer;
	
	public void start() throws InterruptedException, FileNotFoundException
	{
		while(true) // World not full
		{
			System.out.printf("Iteration %d\n", currentIteration);
			step();	
			currentIteration++;
		}
	}
	
	private void step() throws InterruptedException, FileNotFoundException
	{
		update();
		checkPaused();
		w.iteration();
		updateGraphics();
		if(currentIteration > START_STORING_DATA && currentIteration < MAX_ITERATIONS)
		{
			writer.write_data_to_file(currentIteration, w);
		}
	}
	
	
	 
	
	
	private void update() throws InterruptedException, FileNotFoundException
	{
		Thread.sleep(c.sleepTime);
		c = gui.update(w);
		
		// check if we're reset, if yes, create a new world and draw it. 
		if(gui.reset() || currentIteration > MAX_ITERATIONS)
		{
			writer.write_data_to_file(currentIteration, w);
			writer.close();
			writer = new Writer(c);
			currentIteration = 0;
			w = new World(c.clone());
			gui.updateGraphics(w);
		}
		if(gui.step())
		{
			step = true;
		}
	}
	
	
	Main(String[] args) throws FileNotFoundException
	{
		currentCount = 0;
		currentIteration = 0;
		
		// no args, default settings:
		if(args.length == 0)
		{ 
			c = new Config();
		} else {
			// read configuration from file.
			c = new Config(args[0]);		
		}
		
		writer = new Writer(c);
		
		gui = new GUI(SCREEN_HOR, SCREEN_VER,c);
	
		w = new World(c.clone());
		
		gui.updateGraphics(w);
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
	
	
	
	private void checkPaused() throws InterruptedException
	{
		while(w.paused() && !step) { 
			Thread.sleep(c.sleepTime);
			c = gui.update(w);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException
	{
		new Main(args).start();
	}
}
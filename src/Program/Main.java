package Program;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	File f;
	PrintWriter writer;
	
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
			write_data_to_file();
		}
	}
	
	private String makeFilename()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return "./ASO_results_"+sdf.format(date)+".csv";
	}
	 
	private void createFileAndWriter() throws FileNotFoundException
	{
		f = new File(makeFilename());
		writer = new PrintWriter(f);
		
		writeHeaders();
	}
	
	private void writeHeaders()
	{
		// we will create a csv file, the format will be:
		// current_iteration, total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, number_of_groups, num_agents_in_group_0, ... num_agents_in_group_n, num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false
		String headers = "current_iteration, total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, number_of_groups";
		for(int i = 0; i < c.numGroups; i++)
		{
			headers += ", num_agents_in_group_" + Integer.toString(i);
		}
		headers += ", num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false\n";
		writer.write(headers);
		writer.flush();
	}
	
	private void write_data_to_file()
	{
		/**
		 *  schrijf de data naar een file
		 */

		String data = "";
		data += Integer.toString(currentIteration);
		data += w.generateAgentData();
		data += "\n";
		writer.write(data);
		writer.flush();
	}
	
	private void update() throws InterruptedException, FileNotFoundException
	{
		Thread.sleep(c.sleepTime);
		c = gui.update(w);
		
		// check if we're reset, if yes, create a new world and draw it. 
		if(gui.reset() || currentIteration > MAX_ITERATIONS)
		{
			write_data_to_file();
			writer.close();
			createFileAndWriter();
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
		
		createFileAndWriter();
		
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
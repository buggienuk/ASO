package Program;
import java.io.File;
import java.util.Scanner;


public class Config {
	// class used for storing configuration information, otherwise the main programs will be way too big.
	String configFileLocation;
	
	// settings for the model
	public int numGroups;
	public int percentageFilled;
	public int horNumAgents;

	public int verNumAgents;
	public boolean nurture;
	public boolean aseksual;
	public int basePTR;
	
	// program settings
	public int updateEvery;
	public int sleepTime; // in milliseconds
	
	// use base settings (not recommended)
	Config()
	{
		defaultSettings();
	}
	
	// clone it, this way the world only works on the config file it received when the world was created. 
	public Config clone()
	{
		Config c = new Config();
		c.numGroups = this.numGroups;
		c.percentageFilled = this.percentageFilled;
		c.horNumAgents = this.horNumAgents;
		c.verNumAgents = this.verNumAgents;
		c.nurture = this.nurture;
		c.aseksual = this.aseksual;
		c.basePTR = this.basePTR;
		c.updateEvery = this.updateEvery;
		c.sleepTime = this.sleepTime;
		
		return c;
	}
	
	private void defaultSettings()
	{
		numGroups = 4;
		percentageFilled = 25;
		horNumAgents = 50;
		verNumAgents = 50;
		nurture = false;
		aseksual = false;
		basePTR = 13;
		
		updateEvery = 50;
		sleepTime = 50;
	}
	
	// read from file
	Config(String fileLocation)
	{
		this.configFileLocation = fileLocation;
		readConfigFile();
	}
	
	private void readConfigFile()
	{
		// set default settings in case of when not all needed information is provided in the config.
		defaultSettings();
		try{
			File f = new File(this.configFileLocation);
			Scanner scan = new Scanner(f);
			while(scan.hasNext()){
				readLine(scan.nextLine());
			}
		} catch (Exception e)
		{
			System.out.println("error with config file, using default settings");
			System.out.println(e.getMessage());
			defaultSettings();
		}
	}
	
	private void readLine(String l)
	{
		Scanner line = new Scanner(l);
		line.useDelimiter("\t");
		String name = line.next();
		if(name.equals("numGroups"))
		{
			this.numGroups = line.nextInt();
			return;
		}
		if(name.equals("percentageFilled"))
		{
			this.percentageFilled = line.nextInt();
			return;
		}
		if(name.equals("horizontalSize"))
		{
			this.horNumAgents = line.nextInt();
			return;
		}
		if(name.equals("verticalSize"))
		{
			this.verNumAgents = line.nextInt();
			return;
		}
		if(name.equals("nurture"))
		{
			this.nurture = line.nextBoolean();
			return;
		}
		if(name.equals("aseksual"))
		{
			this.aseksual = line.nextBoolean();
			return;
		}
		if(name.equals("basePTR"))
		{
			this.basePTR = line.nextInt();
			return;
		}
		if(name.equals("updateEvery"))
		{
			this.updateEvery = line.nextInt();
			return;
		}
		if(name.equals("sleepTime"))
		{
			this.sleepTime = line.nextInt();
			return;
		}
	}
	
	
	
	
}

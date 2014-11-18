
public class Config {
	// class used for storing configuration information, otherwise the main programs will be way too big.
	String configFileLocation;
	
	// settings for the model
	int numGroups;
	int percentageFilled;
	int horNumAgents, verNumAgents;
	boolean nurture;
	boolean aseksual;
	int basePTR;
	
	// program settings
	int updateEvery;
	int sleepTime; // in milliseconds
	
	// use base settings (not recommended)
	Config()
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
	
	public void readConfigFile()
	{
		
	}
	
	
	
	
}

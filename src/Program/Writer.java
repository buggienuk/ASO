package Program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Writer {
	PrintWriter writer;
	Config c;
	
	Writer(Config c) throws FileNotFoundException
	{
		this.c = c;
		createFileAndWriter();
	}
	
	private void writeHeaders()
	{
		// we will create a csv file, the format will be:
		// current_iteration, total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, number_of_groups, num_agents_in_group_0, ... num_agents_in_group_n, num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false
		String headers = "current_iteration, total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, total_number_of_actions, number_of_groups";
		for(int i = 0; i < c.numGroups; i++)
		{
			headers += ", num_agents_in_group_" + Integer.toString(i);
		}
		headers += ", num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false\n";
		writer.write(headers);
		writer.flush();
	}
	
	
	private void createFileAndWriter() throws FileNotFoundException
	{
		File f = new File(makeFilename());
		writer = new PrintWriter(f);
		
		writeHeaders();
	}
	
	
	private String makeFilename()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return "./ASO_results_"+sdf.format(date)+".csv";
	}
	
	
	public void write_data_to_file(int currentIteration, World w)
	{
		/**
		 *  schrijf de data naar een file
		 */

		String data = "";
		data += Integer.toString(currentIteration);
		data += generateAgentData(w);
		data += "\n";
		writer.write(data);
		writer.flush();
	}
	
	private String generateAgentData(World w)
	{
		String result = "";
		// total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, number_of_groups, num_agents_in_group_0, ... num_agents_in_group_n, num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false
		ArrayList<Human> humans = w.getAliveHumans();
		result += ", " + Integer.toString(humans.size());
		result += ", " + Integer.toString(w.prev_ethnocentricBehaviour);
		result += ", " + Integer.toString(w.prev_otherBehaviour);
		result += ", " + Integer.toString(w.prev_numberOfDilemmas);
		result += ", " + Integer.toString(c.numGroups);

		int[] numAgents = new int[c.numGroups];
		int ethno = 0, other = 0; 
		for(int i = 0; i < humans.size(); i++)
		{
			numAgents[humans.get(i).group]++;
			ethno = humans.get(i).strategyOwnColor ? ethno + 1: ethno;
			other = humans.get(i).strategyOtherColor ? other + 1: other;
		}
		
		for(int i = 0; i < c.numGroups; i++)
		{
			result += ", " + Integer.toString(numAgents[i]);
		}
		
		// num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false
		result += ", " + Integer.toString(ethno);
		result += ", " + Integer.toString(humans.size() - ethno);
		
		result += ", " + Integer.toString(other);
		result += ", " + Integer.toString(humans.size() - other);
			
		return result;
	}
	
	public void close()
	{
		writer.close();
	}
}

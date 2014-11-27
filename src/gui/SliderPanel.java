package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import Program.Config;

public class SliderPanel extends JPanel{
	SliderSet speed, refresh, percentageAgents, numGroups;
	int SPEED = 0, REFRESH = 1, PERC_AGENTS = 2, NUM_GROUPS = 3;
	int NUM_SLIDERS = 4;
	Config c;
	
	SliderPanel(Config c)
	{
		this.c = c;
		setLayout(new GridLayout(5,1));
		createSliders(c);
	}
	
	public void createSliders(Config c)
	{	  
		percentageAgents = new SliderSet(c.percentageFilled, "# of agents  ");
		percentageAgents.addTo(this);
		
		speed = new SliderSet(c.sleepTime, "Wait time ms ", 1000);
		speed.addTo(this);
		
		refresh = new SliderSet(c.updateEvery, "Update every", 100);
		refresh.addTo(this);
		
		numGroups = new SliderSet(c.numGroups, "# of groups ", 10);
		numGroups.addTo(this);
	}
	
	int[] getValues()
	{
		int[] result = new int[NUM_SLIDERS];
		result[SPEED] = speed.getValue();
		result[REFRESH] = refresh.getValue();
		result[PERC_AGENTS] = percentageAgents.getValue();
		result[NUM_GROUPS] = numGroups.getValue();
		
		return result;
	}
	
	public Config updateConfig(Config c)
	{
		c.updateEvery = refresh.getValue();
		c.sleepTime = speed.getValue();
		c.percentageFilled = percentageAgents.getValue();
		c.numGroups = numGroups.getValue();
		
		return c;
	}
	
	public void update()
	{
		speed.updateSlider();
		refresh.updateSlider();
		percentageAgents.updateSlider();
		numGroups.updateSlider();
	}
	
}

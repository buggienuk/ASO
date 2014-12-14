package gui;

import java.awt.Color;

import Program.Human;

public class ColorGroup {
	Color groupColors[];
	Color empty;
	
	ColorGroup(int size)
	{
		groupColors = new Color[size+1];
		initRectangleColors();
	}
	
	private void initRectangleColors()
	{
		this.groupColors[0] = new Color(100,100,100);
		this.groupColors[1] = new Color(155,0,0);
		this.groupColors[2] = new Color(0,155,0);
		this.groupColors[3] = new Color(0,0,155);
		this.groupColors[4] = new Color(255,255,255);
		
		empty = new Color(255,255,255);
	}
	
	public Color getRect(Human h)
	{
		if(!h.alive) { return groupColors[groupColors.length -1]; }
		return get(h.group);
	}
	
	public Color getCirc(Human h)
	{
		return get(h.strategyOwnColor, h.strategyOtherColor);
	}
	
	public Color get(int num)
	{
		return groupColors[num];
	}
	
	public Color get(boolean own, boolean other)
	{
		// own & other 	= 255,255,0
		// own 			= 0,200,200
		// other 		= 255,0,255
		// none 		= 0,0,0
		
		if(own && other)
		{
			return new Color(255,255,0);
		}
		if(own)
		{
			return new Color(0,200,200);
		}
		if(other)
		{
			return new Color(255,0,255);
		}
		
		return new Color(255,255,255);	
	} 
}

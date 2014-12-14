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
		// own & other 	= 175, 250, 175
		// own 			= 175, 175, 100
		// other 		= 100, 175, 175
		// none 		= 100, 100, 100
		
		int r = 100;
		int g = 100;
		int b = 100;
		if(own)
		{
			r += 75;
			g += 75;
		} else {
			g += 75;
		}
		
		if(other) {
			g += 75;
			b += 75;
		} else {
			b += 75;
		}
		
		return new Color(r,g,b);
	} 
}

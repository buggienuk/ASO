import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;


public class Human {
	Rectangle r;
	Ellipse2D c;
	Color col_r, col_c;
	boolean alive;
	boolean nurture;
	boolean aseksual;
	int group;
	int PTR;	// in integers, e.g. 13% is value 13.
	
	
	Human(Rectangle r, Ellipse2D c, Color col_r, Color col_c, int group, int PTR, boolean nurture, boolean aseksual)
	{
		this.alive = true;
		this.r = r;
		this.c = c;
		this.col_r = col_r;
		this.col_c = col_c;
		this.group = group;
		this.PTR = PTR;
		this.nurture = nurture;
		this.aseksual = aseksual;
	}
	
	Human(Rectangle r, Ellipse2D c, Color col_r)
	{
		this.alive = false;
		this.r = r;
		this.c = c;
		this.col_r = col_r;
	}
	
	
	
	
	
	/**
	 * Below are basic getters, they can be ignored for the most parts when adding to the code.
	 */
	public Rectangle rect()
	{
		return r;
	}
	
	public Color colRect()
	{
		return col_r;
	}
	
	public Ellipse2D circle()
	{
		return c;
	}
	
	public Color colCircle()
	{
		return col_c;
	}
	
	public boolean alive()
	{
		return this.alive;
	}
}
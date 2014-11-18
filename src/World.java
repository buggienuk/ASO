import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.Random;


public class World {
	Human[][] world;
	Color[] groupColors;
	static int SQUARE_SIZE = 10;
	Config c;
	boolean paused;
	
	public void doIteration()
	{
		// do iterations here. 
	}
	
	World(Config c)
	{
		this.c = c; 
		initColors(c.numGroups);
		initWorld(c);
		paused = false;
	}
	
	private void initColors(int numGroups)
	{
		// add 1 for the base color of an empty square.
		groupColors = new Color[numGroups+1];
		
		// TODO: Automatiseer deze shit!
		this.groupColors[0] = new Color(100,100,100);
    	this.groupColors[1] = new Color(255,0,0);
    	this.groupColors[2] = new Color(0,255,0);
    	this.groupColors[3] = new Color(0,0,255);
    	this.groupColors[4] = new Color(255,255,255);
	}
	
	private void initWorld(Config c)
	{
		world = new Human[c.horNumAgents][c.verNumAgents];
		Random ran = new Random();
		
		int num;
		Rectangle r;
		Ellipse2D e;
		for(int i = 0; i < c.horNumAgents; i++)
		{
			for(int j = 0; j < c.verNumAgents; j++)
			{
				num = ran.nextInt(100);
				
				// create a rectangle and set its x and y location, as well as its size.
				r = new Rectangle(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				e = new Ellipse2D.Double(i*SQUARE_SIZE +1, j*SQUARE_SIZE +1, SQUARE_SIZE-2, SQUARE_SIZE-2);
				
				/**
				 *  create an alive human
				 */
				if(num < c.percentageFilled)
				{
					num = ran.nextInt(c.numGroups);
					world[i][j] = new Human(r, e, groupColors[num], groupColors[(num+1) % c.numGroups], num, c.basePTR, c.nurture, c.aseksual);
				} else { // create a dead human
					world[i][j] = new Human(r, e, groupColors[c.numGroups]);
				}
			}
		}
	}
	
	
	public Human get(int i, int j)
	{
		return world[i][j];
	}
	
	public boolean paused()
	{
		return paused;
	}
	
	
	
	/**
	 * Test code, can be removed later on!!
	 */
	public void generateNew()
    {
    	int num; 
    	Random ran = new Random();
        for(int i = 0; i < c.horNumAgents; i++)
    	{
    		for(int j = 0; j < c.verNumAgents; j++)
    		{
    			num = ran.nextInt(4);
        		Rectangle r = new Rectangle(i*10, j*10, 10, 10);
        		Ellipse2D e = new Ellipse2D.Double(i*10+1, j*10+1, 8, 8);
        		
        		if(num == 0)
        		{
        			num = ran.nextInt(4);
	        		world[i][j] = new Human(r, e, groupColors[num], groupColors[(num+1) % c.numGroups], num, 13, false, false);
        		} else {
        			world[i][j] = new Human(r, e, groupColors[c.numGroups]);
        		}
    		}
    	}
    }
}

package Program;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class World {
	Human[][] world;
	Color[] groupColors;
	public Color empty;
	static int SQUARE_SIZE = 10;
	Config c;
	public boolean paused;
	public boolean step;
	static int WEST = 0, NORTH = 1, EAST = 2, SOUTH = 3;
	Random ran;
	int ethnocentricBehaviour;
	int prev_ethnocentricBehaviour;
	int otherBehaviour;
	int prev_otherBehaviour;
	int numberOfDilemmas;
	int prev_numberOfDilemmas;
	
	/**
	 * constructor, experiment starts paused.
	 */
	World(Config c)
	{
		this.c = c; 
		initWorld(c);
		ran = new Random();
		paused = true;
	}
	
	
	/**
	 * The paper model has four phases, namely:
	 * 1. immigration		- new agents randomly enter the world
	 * 2. interaction		- iterate randomly over all agents, performing the prisoners dillema
	 * 3. reproduction		- reproduce
	 * 4. death				- every agent has a 10% chance to die
	 */
	public void iteration()
	{
		immigration();		
		interaction(); 		
		reproduction();		
		death();			
	
		// this isn't defined in the paper, but this way its easy to change the ptr and interaction flags.
		resetHumanInfo();	
	}
	
	private void reproduction()
	{
		if(c.aseksual)
		{
			reproduction_aseksual();	
		} else {
			reproduction_seksual();
		}
	}

	
	private void reproduction_aseksual()
	{
		ArrayList<Human> aliveHumans = getAliveHumans();
		int chance;
		for(int i = 0; i < aliveHumans.size(); i++)
		{
			chance = ran.nextInt(100);
			Human parent = aliveHumans.get(i);
			
			// are we having a baby??
			if(chance > parent.PTR){ continue; }
			
			// try to find an empty spot.
			Human emptySpot = findEmptySpot(parent.x, parent.y);
			if(emptySpot == null) { continue; }
			
			// we've found an empty spot, LEZGO! MAKE THAT BABY. 
			Human child = new Human(parent, emptySpot, c, groupColors);
			
			// check for nurture stuff. 
			if (child.getNurture()){
				Human[] newNeighbours = getNeighbors(emptySpot.x,emptySpot.y);
				child.nurture(newNeighbours, c);
			}
			
			// place him on the empty spot location, and we're done, for a single iteration. 
			world[emptySpot.x][emptySpot.y] = child;
			
			
		} 
	}
	
	private void reproduction_seksual()
	{
		ArrayList<Human> aliveHumans = getAliveHumans();
		int chance;
		for(int i = 0; i < aliveHumans.size(); i++)
		{
			chance = ran.nextInt(100);
			Human parent = aliveHumans.get(i);
			
			// are we having a baby??
			if(chance > parent.PTR){ continue; }
						
			Human[] neighbors = getNeighbors(parent.x,parent.y);
			Human child = null;
			
			int start = ran.nextInt(4);
			for(int k = 0; k < 4; k++)
			{
				if(child == null) { k = 4; continue; }
				// the neighbor on that side is dead.
				if(!neighbors[start].alive) { start = (start+1) % 4; continue; }		

				// same gender?
				if(neighbors[start].gender == parent.gender) { start = (start+1) % 4; continue; }

				// try to find an empty spot.
				Human emptySpot = findEmptySpot(parent.x, parent.y);
				if(emptySpot == null) { continue; }

				// actually breed the baby.
				child = parent.breedSeksual(neighbors[start], emptySpot, c, groupColors);
				
				// place him on the empty spot location, and we're done, for a single iteration. 
				// unless the child is nurtured right?
				if (child.getNurture()){
					Human[] newNeighbours = getNeighbors(emptySpot.x,emptySpot.y);
					child.nurture(newNeighbours, c);
				}
				world[emptySpot.x][emptySpot.y] = child;
				break;
			}
		}
		
	}

	
	private Human findEmptySpot(int x, int y)
	{
		// if nurture, spawn the child on a random position
		if(c.nurture)
		{
			return findRandomEmptySpot();
		} else {
			return findEmptySpotAround(x,y);
		}
		
	}
	
	// find an empty spot around the human (4 squares)
	private Human findEmptySpotAround(int x, int y)
	{
		return findEmpty_4(x,y);
		//return findEmpty_9(x,y);
	}
	
	private Human findEmpty_4(int x, int y)
	{
		Human[] spaces = getNeighbors(x,y);
		int start = ran.nextInt(spaces.length);
		
		for(int i = 0; i < spaces.length; i++)
		{
			if(spaces[start].alive) { start = (start+1)%spaces.length; continue; }
			
			return spaces[start];
		}
		
		return null;
	}
	
	private Human findEmpty_9(int x, int y)
	{
		int start_x = ran.nextInt(3) -1;
		int start_y = ran.nextInt(3) -1;
		int pos_x;
		int pos_y;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				pos_x = (start_x + i) < 2 ? (x + start_x + i) : x-1;
				pos_y = (start_y + j) < 2 ? (y + start_y + j) : y-1;
				if(pos_x < 0) { pos_x = c.horNumAgents-1; }
				else if(pos_x == c.horNumAgents){ pos_x = 0; }
				
				if(pos_y < 0) { pos_y = c.verNumAgents-1; }
				else if(pos_y == c.verNumAgents){ pos_y = 0; }
				
				if(!world[pos_x][pos_y].alive())
				{
					world[pos_x][pos_y].x = pos_x;
					world[pos_x][pos_y].y = pos_y;
					return world[pos_x][pos_y];
				}
			}
		}
		return null;
	}
	
	private void interaction()
	{
		Human[] neighbors;
		for(int i = 0; i < c.horNumAgents; i++)
		{
			for(int j = 0; j < c.verNumAgents; j++)
			{
				neighbors = getNeighbors(i,j);
				for(int k = 0; k < 4; k++)
				{
					// the neighbor on that side is dead.
					if(!neighbors[k].alive) { continue; }

					// this neighbor has already helped us, so no need to help him
					if(neighbors[k].helped[(k+2) % 4]) { continue; }

					// actually perform the dilemma
					performDilemma(world[i][j], neighbors[k]);
				}
			}
		}
	}
	
	private void performDilemma(Human h1, Human h2)
	{
		boolean h1_to_h2_action; // true = cooperate, false = defect
		boolean h2_to_h1_action; // true = cooperate, false = defect
		numberOfDilemmas+=2;
		
		// are they the same group?
		boolean sameGroup = h1.group == h2.group;
		
		// do we help our own and is the other one of us? yes, help him
		if(h1.strategyOwnColor && sameGroup)
		{
			h1_to_h2_action = true;
			ethnocentricBehaviour++;
		// else, are we a cheater and help other groups? yes, help him 
		} else if(h1.strategyOtherColor && !sameGroup)
		{
			otherBehaviour++;
			h1_to_h2_action = true;
		// none of the above cases hold, we will not help him.
		} else {
			h1_to_h2_action = false;
		}
		
		// same, other way around.
		if(h2.strategyOwnColor && sameGroup)
		{
			h2_to_h1_action = true; 
			ethnocentricBehaviour++;
		} else if(h2.strategyOtherColor && !sameGroup)
		{
			otherBehaviour++;
			h2_to_h1_action = true;
		} else {
			h2_to_h1_action = false;
		}
		
		
		// now update the PTR states:
		
		// h1 helps the other, subtract 1% from h1 and add 3% to the h2
		if(h1_to_h2_action) 
		{
			h1.PTR--;
			h2.PTR += 3;
		}
		// other way around
		if(h2_to_h1_action)
		{
			h2.PTR--;
			h1.PTR += 3;
		}
		
		// and we're done here!
	}


	// creates a random human and adds him to the world
	private void immigration()
	{
		// todo, randomize whether an agent immigrates??

		int i, j;
		int num = ran.nextInt(c.numGroups);

		do {
			i = ran.nextInt(c.horNumAgents);
			j = ran.nextInt(c.verNumAgents);
		} while ( world[i][j].alive );

		Rectangle r = new Rectangle(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		Ellipse2D e = new Ellipse2D.Double(i*SQUARE_SIZE +1, j*SQUARE_SIZE +1, SQUARE_SIZE-2, SQUARE_SIZE-2);
		boolean gender = ran.nextBoolean();
		
		
		world[i][j] = new Human(r, e, num, c.basePTR, c.nurture, gender);
	}

	private Human[] getNeighbors(int x, int y)
	{
		// we go in a clock-wise direction in the array, e.g. left, up, right, down neighbors.
		// we use wrap-around borders.
		Human[] result = new Human[4];
		result[EAST] = getNeighbor(x-1,y);
		result[NORTH] = getNeighbor(x,y-1);
		result[SOUTH] = getNeighbor(x+1,y);
		result[WEST] = getNeighbor(x,y+1);

		return result;
	}

	/**
	 * wrap around the borders and return the agent on that field. 
	 */
	private Human getNeighbor(int x, int y)
	{
		if( x > c.horNumAgents -1)
		{
			x = 0;
		}
		if(x < 0) 
		{
			x = c.horNumAgents -1;
		}
		if( y > c.verNumAgents -1)
		{
			y = 0;
		}
		if(y < 0) 
		{
			y = c.verNumAgents -1;
		}

		world[x][y].x = x;
		world[x][y].y = y;
		return world[x][y];
	}

	private void initWorld(Config c)
	{
		numberOfDilemmas = 0;
		ethnocentricBehaviour = 0;
		otherBehaviour = 0;
		
		world = new Human[c.horNumAgents][c.verNumAgents];

		generateNew();
	}


	/**
	 *  zoek een random plek voor het kind (nurture!!)
	 */
	public Human findRandomEmptySpot()
	{
		int n;
		int m;
		Random rand1 = new Random();
		Random rand2 = new Random();
		do 
		{
			n = rand1.nextInt(c.horNumAgents);
			m = rand2.nextInt(c.verNumAgents);

		} while(world[n][m].alive); //c.percentagefilled != 100) 
		return world[n][m];
	}


	// for all humans, there is a 10% chance to die. Simply set the alive flag to false
	// to make him 'dead'
	private void death()
	{
		ArrayList<Human> aliveHumans = getAliveHumans();
		int n;
		for(int i = 0; i < aliveHumans.size(); i++)
		{
			n = ran.nextInt(100);
			if(n < c.deathChance)
			{
				Human h = aliveHumans.get(i);
				world[h.x][h.y].alive = false;
			}
		}
	}

	private void resetHumanInfo()
	{
		// store the 'old' info
		prev_ethnocentricBehaviour = ethnocentricBehaviour;
		prev_otherBehaviour = otherBehaviour;
		prev_numberOfDilemmas = numberOfDilemmas;
		
		// reset everything else.
		ethnocentricBehaviour = 0;
		otherBehaviour = 0;
		numberOfDilemmas = 0;
		
		ArrayList<Human> aliveHumans = getAliveHumans();
		for(int i = 0; i < aliveHumans.size(); i++)
		{
			aliveHumans.get(i).PTR = c.basePTR;
			aliveHumans.get(i).helped = new boolean[4];
		}
	}

	// returns a shuffled array of all alive humans. Stores the x and y coordinate in the human as we might need that.
	ArrayList<Human> getAliveHumans()
	{
		ArrayList<Human> result = new ArrayList<Human>();
		// return all alive humans. 
		for(int i = 0; i < c.horNumAgents; i++)
		{
			for(int j = 0; j < c.verNumAgents; j++)
			{
				if(world[i][j].alive) { 
					result.add(world[i][j]);
					world[i][j].x = i;
					world[i][j].y = j;
				}
			}
		}
		Collections.shuffle(result);
		return result;
	}

	public Human get(int i, int j)
	{
		return world[i][j];
	}

	public boolean paused()
	{
		return paused;
	}
	
	public void generateNew()
    {
		if(c.nurture) { generateWithClusters(); }
		else { generateWithoutClusters(); }
    }
	
	public void generateWithoutClusters()
	{
		ran = new Random();

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

				if(num < c.percentageFilled)
				{
					num = ran.nextInt(c.numGroups);
					world[i][j] = new Human(r, e, num, c.basePTR, c.nurture, ran.nextBoolean());
				} else { // create a dead human
					world[i][j] = new Human(r, e);
				}
			}
		}
	}
	
	public void generateWithClusters()
	{
		int num;
		ran = new Random();
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

			
				world[i][j] = new Human(r, e);
			}
		}
		
		int x,y;
		int times;
		int numClusters = (int) ((c.verNumAgents * c.horNumAgents) * (c.percentageFilled/100.0))/c.averageClusterSize;
		System.out.println(numClusters);
		for(int i = 0; i < numClusters; i++)
		{
			x = ran.nextInt(c.horNumAgents);
			y = ran.nextInt(c.verNumAgents);
			
			num = ran.nextInt(4);
			times = ran.nextInt(3) -1;
			// start mannetje van het cluster
    		world[x][y] = new Human(world[x][y].r, world[x][y].c, num, 13, false, false);
    		for(int j = 0; j < c.averageClusterSize + times; j++)
    		{
    			Human spot = findEmptySpotAround(x,y);
    			if(spot != null)
    			{
    				num = ran.nextInt(4);
    				world[spot.x][spot.y] = new Human(world[spot.x][spot.y].r, world[spot.x][spot.y].c, num, 13, false, false);
    			}
    		}
		}
	}
}
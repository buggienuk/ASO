package Program;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
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

	
	// basic model run, aseksual and nurture. 
	public void iteration()
	{
		immigration();		// works.
		//interaction(); // not yet finished
		reproduction();
		death();			// works.

		// this isn't defined in the paper, but this way its easy to change the ptr and interaction flags.
		resetHumanInfo();	// works
	}




	private void reproduction()
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
			Human emptySpot = findEmptySpotAround(parent.x, parent.y);
			if(emptySpot == null) { continue; }
			
			// we've found an empty spot, LEZGO! MAKE THAT BABY. 
			Human child = new Human(parent);
			
			// place him on the empty spot location, and we're done, for a single iteration. 
			world[emptySpot.x][emptySpot.y] = child;
		}
	}

	// find an empty spot around the human (9 squares)
	private Human findEmptySpotAround(int x, int y)
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

					// do the iterated prisoners dillema thingie. 
					// HOW DO I DO THIS?
					/**
					 * blue placeholder for todo-ness
					 */

				}
			}
		}
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

		world[i][j] = new Human(r, e, groupColors[num], groupColors[(num+1) % c.numGroups], num, c.basePTR, c.nurture, c.aseksual);
	}

	public void doIteration()
	{
		// for each human
		Human[] neighbors;
		for(int i = 0; i < c.horNumAgents; i++)
		{
			for(int j = 0; i < c.verNumAgents; i++)
			{
				if(world[i][j].alive){
					if(world[i][j].die())
					{
						System.out.println("die!");
						// human has died, update the world accordingly!!
						Rectangle r = new Rectangle(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
						Ellipse2D e = new Ellipse2D.Double(i*SQUARE_SIZE +1, j*SQUARE_SIZE +1, SQUARE_SIZE-2, SQUARE_SIZE-2);

						world[i][j] = new Human(r, e, groupColors[c.numGroups]);


					} else {
						System.out.println("trie breed");

						// human lives to breed another day.
						neighbors = getNeighbors(i,j);
						world[i][j].iterate(neighbors);
						Human child = world[i][j].breed(neighbors);
						// TODO: what to do with the child?

						findEmptySpot(child);

					}
				}
			}
		}
	}

	private Human[] getNeighbors(int x, int y)
	{
		// we go in a clock-wise direction in the array, e.g. left, up, right, down neighbors.
		Human[] result = new Human[4];
		result[EAST] = getNeighbor(x-1,y);
		result[NORTH] = getNeighbor(x,y-1);
		result[SOUTH] = getNeighbor(x+1,y);
		result[WEST] = getNeighbor(x,y+1);

		// TODO: remove out of bounds neighbors
		return result;
	}

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

		return world[x][y];
	}

	World(Config c)
	{
		this.c = c; 
		initColors(c.numGroups);
		initWorld(c);
		ran = new Random();
		paused = true;
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








	/**
	 * Kinderen moeten geplaatst worden naast de ouders, niet op een willekeurige plek.
	 * @param child2
	 */
	public void findEmptySpot(Human child2)
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
		world[n][m] = child2;

	}


	// for all humans, there is a 10% chance to die. Simply set the alive flag to false
	// to make him 'dead' and change his rectangle color. 
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
				world[h.x][h.y].col_r = empty;
			}
		}
	}

	private void resetHumanInfo()
	{
		ArrayList<Human> aliveHumans = getAliveHumans();
		for(int i = 0; i < aliveHumans.size(); i++)
		{
			aliveHumans.get(i).PTR = c.basePTR;
			aliveHumans.get(i).helped = new boolean[4];
		}
	}

	// returns a random array of humans. Stores the x and y coordinate in the human as we might need that.
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

		empty = new Color(255,255,255);
	}
	
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
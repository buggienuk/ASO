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
	int ethnocentricBehaviour;
	int prev_ethnocentricBehaviour;
	int otherBehaviour;
	int prev_otherBehaviour;

	public boolean breeded;
	
	
	// basic model run, aseksual and nurture. 
	public void iteration()
	{
		immigration();		// works.
		interaction(); 		// works
		if(c.aseksual)
		{
			reproduction_aseksual();		// works.
		} else {
			reproduction_seksual();
		}
		death();			// works.
		
		prev_ethnocentricBehaviour = ethnocentricBehaviour;
		prev_otherBehaviour = otherBehaviour;
		
		// this isn't defined in the paper, but this way its easy to change the ptr and interaction flags.
		resetHumanInfo();	// works
	}

	
	// TODO:
	/**
	 *  generate a 'random' world with groups of agents together
	 */

	
	// TODO:
	/**
	 * voor nurture, child word ergens willekeurig op t bord gespawnd.
	 */
	
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
			Human emptySpot = findEmptySpotAround(parent.x, parent.y);
			if(emptySpot == null) { continue; }
			
			// we've found an empty spot, LEZGO! MAKE THAT BABY. 
			Human child = new Human(parent, emptySpot, c, groupColors);
			
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
			
			// try to find an empty spot.
			//Human emptySpot = findEmptySpotAround(parent.x, parent.y);
			//if(emptySpot == null) { continue; }
			
			Human child;
			
			Human [] neighbors = getNeighbors(parent.x,parent.y);
			//child = emptySpot.breed(neighbors);


			//breed with all neighbors??

			int start = ran.nextInt(4);
			for(int k = 0; k < 4; k++)
			{
				// the neighbor on that side is dead.
				if(!neighbors[start].alive) { start = (start+1) % 4; continue; }		

				//same gender?
				if(neighbors[start].gender == parent.gender) { start = (start+1) % 4; continue; }

				// try to find an empty spot.
				Human emptySpot = findEmptySpotAround(parent.x, parent.y);
				if(emptySpot == null) { continue; }

				child = parent.breedAseksual(emptySpot, c, groupColors);
				
				// place him on the empty spot location, and we're done, for a single iteration. 
				// unless the child is nurtured right?
				if (child.getNurture()){
					Human[] newNeighbours = getNeighbors(emptySpot.x,emptySpot.y);
					child.nurture(newNeighbours);
				}
				world[emptySpot.x][emptySpot.y] = child;
				break;
			}
		}
		
	}

	// find an empty spot around the human (9 squares)
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
		
		
		world[i][j] = new Human(r, e, groupColors[num], groupColors[(num+1) % c.numGroups], num, c.basePTR, c.nurture, gender);
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
						// human has died, update the world accordingly!!
						Rectangle r = new Rectangle(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
						Ellipse2D e = new Ellipse2D.Double(i*SQUARE_SIZE +1, j*SQUARE_SIZE +1, SQUARE_SIZE-2, SQUARE_SIZE-2);

						world[i][j] = new Human(r, e, groupColors[c.numGroups]);


					} else {
						// human lives to breed another day.
						neighbors = getNeighbors(i,j);
						world[i][j].iterate(neighbors);
						Human child = world[i][j].breed(neighbors);

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

		world[x][y].x = x;
		world[x][y].y = y;
		return world[x][y];
	}

	World(Config c)
	{
		ethnocentricBehaviour = 0;
		otherBehaviour = 0;
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

		generateNew();
	}








	/**
	 *  zoek een random plek voor het kind (nurture!!)
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
		ethnocentricBehaviour = 0;
		otherBehaviour = 0;
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
					world[i][j] = new Human(r, e, groupColors[num], groupColors[(num+1) % c.numGroups], num, c.basePTR, c.nurture, ran.nextBoolean());
				} else { // create a dead human
					world[i][j] = new Human(r, e, groupColors[c.numGroups]);
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

			
				world[i][j] = new Human(r, e, groupColors[c.numGroups]);
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
    		world[x][y] = new Human(world[x][y].r, world[x][y].c, groupColors[num], groupColors[(num+1) % c.numGroups], num, 13, false, false);
    		for(int j = 0; j < c.averageClusterSize + times; j++)
    		{
    			Human spot = findEmptySpotAround(x,y);
    			if(spot != null)
    			{
    				num = ran.nextInt(4);
    				world[spot.x][spot.y] = new Human(world[spot.x][spot.y].r, world[spot.x][spot.y].c, groupColors[num], groupColors[(num+1) % c.numGroups], num, 13, false, false);
    			}
    		}
		}
	}
	
	public String generateAgentData()
	{
		String result = "";
		// total_num_agents, ethnocentric_behaviour_actions, cooperative_behaviour_actions, number_of_groups, num_agents_in_group_0, ... num_agents_in_group_n, num_agents_ethno_true, num_agents_ethno_false, num_agents_other_true, num_agents_other_false
		ArrayList<Human> humans = getAliveHumans();
		result += ", " + Integer.toString(humans.size());
		result += ", " + Integer.toString(prev_ethnocentricBehaviour);
		result += ", " + Integer.toString(prev_otherBehaviour);
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
}
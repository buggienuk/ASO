package Program;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;


public class Human {
	Rectangle r;
	Ellipse2D c;
	int x,y;
	public boolean alive;
	boolean nurture;
	boolean gender;
	public int group;
	int PTR;	// in integers, e.g. 13% is value 13.
	Random ran;
	boolean[] helped;
	public boolean strategyOwnColor;
	public boolean strategyOtherColor;
		
	public Human breedSeksual(Human otherParent, Human spot, Config c, Color[] groupColors)
	{
		Human child = new Human();
		child.r = spot.r;
		child.c = spot.c;
		child.x = spot.x;
		child.y = spot.y;
		child.alive = true;
		child.nurture = c.nurture;
		child.gender = ran.nextBoolean();
		child.PTR = c.basePTR;
				
		// todo: group, strategyOwnColor, strategyOtherColor
		if(c.nurture)
		{
			return child;
		}

		boolean takeParent = ran.nextBoolean();
		child.group = takeParent ? this.group : otherParent.group;
		takeParent = ran.nextBoolean();
		child.strategyOtherColor = takeParent ? this.strategyOtherColor : otherParent.strategyOtherColor;
		takeParent = ran.nextBoolean();
		child.strategyOwnColor = takeParent ? this.strategyOwnColor : otherParent.strategyOwnColor;
		
		// after getting everything we need, mutate the childs preferences. 
		child.mutate(c);
		
		return child;
	}
	
	public void nurture(Human[] neighbors, Config c) {
		ArrayList<Human> actualNeighbors = new ArrayList<Human>();
		for (int i=0; i<neighbors.length; i++){
			if (neighbors[i].alive()){
				actualNeighbors.add(neighbors[i]); 
			}
		}
		// human gets traits from the environment around him. Take the average of the strategies 
		// and round it to get the childrens strategy 
		if (actualNeighbors.size() > 0){
			int other = 0;
			int own = 0;
			int[] group = new int[c.numGroups];
			for(int i = 0;  i< actualNeighbors.size(); i++)
			{
				other = actualNeighbors.get(i).strategyOtherColor ? other + 1 : other;
				own = actualNeighbors.get(i).strategyOwnColor ? own + 1 : own;
				group[actualNeighbors.get(i).group]++;	
			}
			// average everything out.
			double averageOther = other / (double) actualNeighbors.size();
			double averageOwn   = own / (double) actualNeighbors.size();
			
			// find the most prevalent group. Start on a random position to allow for 
			// random outcomes when 2 groups are present with the same amount of agents. 
			int mostPrevalentGroup = ran.nextInt(c.numGroups);
			int highestGroupSize = 0;
			int start = ran.nextInt(c.numGroups);
			for(int i = 0; i < c.numGroups; i++)
			{
				if(group[i] > highestGroupSize)
				{
					highestGroupSize = group[(i + start)%c.numGroups];
					mostPrevalentGroup = (i + start)%c.numGroups;
				}
				start++;
			}
			
			this.strategyOtherColor = averageOther > 0.5 ? true : false;
			this.strategyOwnColor   = averageOwn > 0.5 ? true : false;
			this.group				= mostPrevalentGroup;
		} else {
			this.group = ran.nextInt(c.numGroups);
			this.strategyOtherColor = ran.nextBoolean();
			this.strategyOwnColor = ran.nextBoolean();
		}
	}
	
	Human(Rectangle r, Ellipse2D c, int group, int PTR, boolean nurture, boolean gender)
	{
		this.alive = true;
		this.r = r;
		this.c = c;
		this.group = group;
		this.PTR = PTR;
		this.nurture = nurture;
		this.gender = gender;
		
		this.helped = new boolean[4];
		
		ran = new Random();
		
		this.strategyOtherColor = ran.nextBoolean();
		this.strategyOwnColor = ran.nextBoolean();
	}

	private void mutate(Config c)
	{
		// there is a 0,5% mutation chance.
		int chance = ran.nextInt(1000);
		// mutation
		if(chance < 5) { 
			this.group = ran.nextInt(c.numGroups);
		} 

		chance = ran.nextInt(1000);
		if(chance < 5)
		{
			this.strategyOtherColor = ran.nextBoolean();
		}

		chance = ran.nextInt(1000);
		if(chance < 5)
		{
			this.strategyOwnColor = ran.nextBoolean();
		}
	}

	Human(Human h, Human old, Config c, Color[] cs)
	{
		ran = new Random();
		// non mutable stuff:
		this.r = old.r;
		this.c = old.c;
		this.x = old.x;
		this.y = old.y;
		this.alive = true;
		this.nurture = h.nurture;
		this.gender = h.gender;
		this.PTR = h.PTR;
		this.helped = new boolean[4];
		this.group = h.group;
		this.strategyOtherColor = h.strategyOtherColor;
		this.strategyOwnColor = h.strategyOwnColor;
		
		mutate(c);
		
		
		
		
		// copy this shit. aseksual reproduction
	}
	
	Human(Rectangle r, Ellipse2D c)
	{
		this.alive = false;
		this.r = r;
		this.c = c;
		
		ran = new Random();
	}
	
	// empty constructor, used for breeding seksually.
	Human() { 
		ran = new Random();
		helped = new boolean[4];
	} 
		
	/**
	 * Below are basic getters, they can be ignored for the most parts when adding to the code.
	 */
	public Rectangle rect()
	{
		return r;
	}
	
	public Ellipse2D circle()
	{
		return c;
	}
	
	public boolean alive()
	{
		return this.alive;
	}

	public boolean die() {
		if(ran.nextInt(10) == 0)
		{
			return true;
		}
		return false;
	}

	public boolean getNurture(){
		return nurture;
	}
	
	public boolean getSekse(){
		return gender;
	}
	
	public int getPTR()
	{
		return PTR;
	}
	
	public int getGroup()
	{
		return group;
	}
	
}
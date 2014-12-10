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
	Color col_r, col_c;
	boolean alive;
	boolean nurture;
	boolean gender;
	int group;
	int PTR;	// in integers, e.g. 13% is value 13.
	Random ran;
	boolean[] helped;
	boolean strategyOwnColor;
	boolean strategyOtherColor;
	
	boolean DEFECT = true, COOPERATE = false;
	
	public void reset()
	{
		helped = new boolean[4];
	}
	
	
	public Human breedAseksual(Human emptySpot, Config c, Color[] groupColors)
	{
		Human child = new Human(this, emptySpot, c, groupColors);
		return child;
	}
	
	public Human breed(Human[] neighbors){
		int p = ran.nextInt(100);
		int idx = ran.nextInt(neighbors.length);
		Human partner = neighbors[idx];
		//Human child = new Human(r, c, col_r, col_c, group, PTR, nurture, gender);
		
		
		return null;
	}
	
	public void nurture(Human[] neighbors) {
		ArrayList<Human> actualNeighbors = new ArrayList<Human>();
		for (int i=0; i<neighbors.length; i++){
			if (neighbors[i].alive()){
				actualNeighbors.add(neighbors[i]); 
			}
		}
		// human inherits traits randomly from new neighbors
		if (actualNeighbors.size() > 0){
			// TODO: alternative for random?
			this.group = actualNeighbors.get(ran.nextInt(actualNeighbors.size())).getGroup();
			this.strategyOtherColor = actualNeighbors.get(ran.nextInt(actualNeighbors.size())).strategyOtherColor;
			this.strategyOwnColor = actualNeighbors.get(ran.nextInt(actualNeighbors.size())).strategyOwnColor;
		}
	}
	
	public void iterate(Human[] neighbors) {
		// talk with neighbors, update temp PTR based accordingly
		int tempPTR = this.PTR;
		
		for (int i=0; i<neighbors.length; i++){
			//TODO: implements strategies
			if (this.group != neighbors[i].getGroup()){// humans of dissimilar groups do not cooporate

				tempPTR -= 1;
				
			}else{
				tempPTR += 3;
			}
		}
		
		// TODO: PTR should get updated at the end of each iteration
		PTR = tempPTR;
		// prisoners dilemma the west, north, east and south neighbors. 
	}
	
	public void prisonersDilemma(int neighborLocation, Human neighbor)
	{
		helped[(neighborLocation+2) %4] = true; 
		
		// do something with the neighbor. 
	}
	
	
	Human(Rectangle r, Ellipse2D c, Color col_r, Color col_c, int group, int PTR, boolean nurture, boolean gender)
	{
		this.alive = true;
		this.r = r;
		this.c = c;
		this.col_r = col_r;
		this.col_c = col_c;
		this.group = group;
		this.PTR = PTR;
		this.nurture = nurture;
		this.gender = gender;
		
		this.helped = new boolean[4];
		
		ran = new Random();
		
		this.strategyOtherColor = ran.nextBoolean();
		this.strategyOwnColor = ran.nextBoolean();
	}
	
	public Human mate(Human partner){
		
		
		return null;	}

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
		
		
		// there is a 0,5% mutation chance.
		int chance = ran.nextInt(1000);
		// mutation
		if(chance > 5) { 
			this.group = h.group;	
			this.col_r = h.col_r;
		}
		else {
			this.group = ran.nextInt(c.numGroups);
			this.col_r = cs[this.group];
		} 
		
		chance = ran.nextInt(1000);
		if(chance > 5)
		{
			this.strategyOtherColor = h.strategyOtherColor;
		} else {
			this.strategyOtherColor = ran.nextBoolean();
		}
		
		chance = ran.nextInt(1000);
		if(chance > 5)
		{
			this.strategyOwnColor = h.strategyOwnColor;
		} else {
			this.strategyOwnColor = ran.nextBoolean();
		}
		
		// copy this shit. aseksual reproduction
	}
	
	Human(Rectangle r, Ellipse2D c, Color col_r)
	{
		this.alive = false;
		this.r = r;
		this.c = c;
		this.col_r = col_r;
		
		ran = new Random();
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

	public boolean die() {
		if(ran.nextInt(10) == 0)
		{
			return true;
		}
		return false;
	}

	
}
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
	boolean aseksual;
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
	
	
	public Human breedAseksual(Human[] neighbors)
	{
		int p = ran.nextInt(100);
		
		
		return null;
	}
	
	public Human breed(Human[] neighbors){
		int p = ran.nextInt(100);
		int idx = ran.nextInt(neighbors.length);
		Human partner = neighbors[idx];
		
		if (p < PTR){ //reproduce or not, select partner randomly
			if (partner == null){// TODO: taking care of out of bounds neighbours
				return null;
			}
		}
		
		//create child, inherits from this parent..
		Ellipse2D c = this.c;
		Color col_r = this.col_r;
		Color col_c = this.col_c;
		int group = this.group;
		int PTR = this.PTR;
		boolean nurture = this.nurture;
		boolean aseksual = this.aseksual;
		
		
		//or from partner/randomly selected neighbor..
		if (!ran.nextBoolean()){c = partner.circle();}
		if (!ran.nextBoolean()){col_r = partner.colRect();}
		if (!ran.nextBoolean()){col_c = partner.colCircle();}
		if (!ran.nextBoolean()){group = partner.getGroup();}
		if (!ran.nextBoolean()){PTR = partner.getPTR();}
		if (!ran.nextBoolean()){nurture = partner.getNurture();}
		if (!ran.nextBoolean()){aseksual = partner.getAseksual();}
		
		// TODO: random mutations

		Human child = new Human(r, c, col_r, col_c, group, PTR, nurture, aseksual);
		return child;
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
		
		this.helped = new boolean[4];
		
		ran = new Random();
		
		this.strategyOtherColor = ran.nextBoolean();
		this.strategyOwnColor = ran.nextBoolean();
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
		this.aseksual = h.aseksual;
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
	
	public boolean getAseksual(){
		return aseksual;
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
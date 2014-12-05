package Program;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;


public class Human {
	Rectangle r;
	Ellipse2D c;
	Color col_r, col_c;
	boolean alive;
	boolean nurture;
	boolean aseksual;
	int group;
	int PTR;	// in integers, e.g. 13% is value 13.
	Random ran;
	boolean[] helped;
	
	public void reset()
	{
		helped = new boolean[4];
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
		
		ran = new Random();
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
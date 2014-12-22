package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JPanel;

import Program.Human;
import Program.World;


public class DrawPanel extends JPanel{
	private static final long serialVersionUID = 3688058972657195248L;
	ColorGroup colors;
	
	DrawPanel()
	{
		super();
		colors = new ColorGroup(4);
	}

	private void doDrawing(Graphics g, World world) throws Exception {
        Graphics2D g2d = (Graphics2D) g;
        drawWorld(g2d, world);
    }

	private void drawWorld(Graphics2D g2d, World world) throws Exception
	{
		Human h;
		
		for(int i = 0; i < world.c.horNumAgents; i++)
        {
        	for(int j = 0; j < world.c.verNumAgents; j++)
        	{
        		h = world.get(i,j);
        		
        		g2d.setColor(colors.getRect(h));
        		g2d.fill(world.get(i,j).rect());
        		if(world.get(i,j).alive()){
        			g2d.setColor(colors.getCirc(h));
        			g2d.draw(world.get(i,j).circle());
        		}
        	}
        }
	}

    public void paintComponent(Graphics g, World world) {
        super.paintComponent(g);
        try {
			doDrawing(g, world);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(-1);
		}
    }
}

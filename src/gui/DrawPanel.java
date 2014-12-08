package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JPanel;

import Program.World;


public class DrawPanel extends JPanel{
	private static final long serialVersionUID = 3688058972657195248L;

	private void doDrawing(Graphics g, World world) throws Exception {
        Graphics2D g2d = (Graphics2D) g;
        drawWorld(g2d, world);
    }

	private void drawWorld(Graphics2D g2d, World world) throws Exception
	{
		for(int i = 0; i < 50; i++)
        {
        	for(int j = 0; j < 50; j++)
        	{
        		
        		g2d.setColor(world.get(i,j).colRect());
        		g2d.fill(world.get(i,j).rect());
        		if(world.get(i,j).alive()){
        			g2d.setColor(world.get(i,j).colCircle());
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

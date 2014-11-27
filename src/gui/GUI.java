package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import Program.Config;
import Program.World;

public class GUI extends JFrame{
	
	Config c;
	DrawPanel dpnl;
	ValuePanel vpnl;
	
	static int PAUSE = 0;
	static int PLAY = 1;
	static int STEP = 2;
	static int RESET = 3;
	
	
	public GUI(int hor, int ver, Config c)
	{
		this.c = c;
		vpnl = new ValuePanel(c);
		initUI(hor, ver);	
	}
	
	public void updateGraphics(World w)
	{
		dpnl.paintComponent(dpnl.getGraphics(), w);
	}
	
	public Config update(World w)
	{
		c = vpnl.update(c);
		return c;
	}
	
	public final void initUI(int hor, int ver) {
        dpnl = new DrawPanel();
        dpnl.setSize(new Dimension(c.horNumAgents*10, c.verNumAgents*10));
        
        this.setLayout(new GridLayout(1,2));
        add(dpnl);
        add(vpnl);

        setSize(800, 500);
        setTitle("Iterated Prisoners Dilemma");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}
	
}

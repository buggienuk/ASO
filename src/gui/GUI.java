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
	ButtonPanel buttonPanel;
	DrawPanel dpnl;
	
	static int PAUSE = 0;
	static int PLAY = 1;
	static int STEP = 2;
	
	
	public GUI(int hor, int ver, Config c)
	{
		this.c = c;
		buttonPanel = new ButtonPanel();
		buttonPanel.setSize(200, 100);
		initUI(hor, ver);
		
	}
	
	public void updateGraphics(World w)
	{
		dpnl.paintComponent(dpnl.getGraphics(), w);
	}
	
	public final void initUI(int hor, int ver) {
        dpnl = new DrawPanel();
        dpnl.setSize(new Dimension(c.horNumAgents*10, c.verNumAgents*10));
        
        this.setLayout(new GridLayout(1,2));
        add(dpnl);
        add(buttonPanel);
        buttonPanel.drawButtons(this);

        setSize(800, 500);
        setTitle("Iterated Prisoners Dilemma");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
	
	public void checkInput()
	{
		boolean[] result = buttonPanel.getButtonStates();
	}

	public void setButtonState(int button, boolean state)
	{
		buttonPanel.setButton(button, state);
	}
	
	public void setVisibility( int button, boolean visible)
	{
		buttonPanel.setVisibility(button, visible);
	}
	
}

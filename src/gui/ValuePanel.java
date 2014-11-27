package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Program.Config;

/**
 * 
 * 	@author micha
 *
 *	Hier komen de sliders en textboxes in, komt boven de button panel te staan. 
 */
public class ValuePanel extends JPanel{
	ButtonPanel buttonPanel;
	SliderPanel sliders;
	
	ValuePanel(Config c)
	{
		buttonPanel = new ButtonPanel();
		buttonPanel.setSize(200, 100);
		sliders = new SliderPanel(c);

		init();
	}
	
	private void init()
	{
		add(buttonPanel);
        add(sliders);
        buttonPanel.drawButtons();
	}
	
	public boolean reset()
	{
		return buttonPanel.reset();
	}
	
	public Config update(Config c)
	{
		//buttonpanel doesnt need an update.. i think ... 
		//buttonPanel.update();
		sliders.update();
		c = sliders.updateConfig(c);
		
		return c;
	}

	public boolean step() {
		return buttonPanel.step();
	}
}

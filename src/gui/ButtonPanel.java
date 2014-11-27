package gui;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


public class ButtonPanel extends JPanel{
	private static final long serialVersionUID = 8844212199281701472L;
	JToggleButton play, pause, step, reset;
	ImageIcon playImage, pauseImage, stepImage, resetImage;
	ButtonGroup group;
	static int NUM_BUTTONS = 4;
	
	/**
	 * TODO: Stop, Restart buttons toevoegen!
	 */
	ButtonPanel()
	{
		group = new ButtonGroup();
		createButtonsAndAlignments();
	}
	
	private void createButtonsAndAlignments()
	{
		playImage  = new ImageIcon("images/play.png");
		pauseImage = new ImageIcon("images/pause.png");
		stepImage  = new ImageIcon("images/step.png");
		resetImage = new ImageIcon("images/reset.png");
		play = new JToggleButton("play", playImage);
		pause = new JToggleButton("pause", pauseImage);
		step = new JToggleButton("step", stepImage);
		reset = new JToggleButton("reset", resetImage);
		play.setVerticalTextPosition(AbstractButton.CENTER);
		play.setHorizontalAlignment(AbstractButton.LEADING);
		pause.setVerticalTextPosition(AbstractButton.CENTER);
		pause.setHorizontalAlignment(AbstractButton.LEADING);
		step.setVerticalTextPosition(AbstractButton.CENTER);
		step.setHorizontalAlignment(AbstractButton.LEADING);
		reset.setVerticalTextPosition(AbstractButton.CENTER);
		reset.setHorizontalAlignment(AbstractButton.LEADING);
		
		group.add(play);
		group.add(pause);
		group.add(step);
		group.add(reset);
		pause.setSelected(true);
	}
	
	public void drawButtons()
	{		
		add(play);
		add(pause);
		add(step);
		add(reset);
	}

	public boolean[] getButtonStates() {
		boolean[] result = new boolean[NUM_BUTTONS];
		result[GUI.PLAY] = play.isSelected();
		result[GUI.STEP] = step.isSelected();
		result[GUI.PAUSE]= pause.isSelected();
		result[GUI.RESET]= reset.isSelected();
		return result;
	}

	public void setButton(int b, boolean state) {
		button(b).setSelected(state);		
	}

	public void setVisibility(int b, boolean visible) {
		button(b).setVisible(visible);
		
	}
	
	private JToggleButton button(int button)
	{
		if(button == GUI.PLAY)
		{
			return play;
		}
		if(button == GUI.PAUSE)
		{
			return pause;
		}
		if(button == GUI.STEP)
		{
			return step;
		}
		return null;
	}

	public boolean reset() {
		if( reset.isSelected() )
		{
			pause.setSelected(true);
			return true;
		}
		return false;
	}

	public boolean step() {
		if( step.isSelected() )
		{
			pause.setSelected(true);
			return true;
		}
		return false;
	}
	
}

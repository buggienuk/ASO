import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


public class ButtonPanel extends JPanel{
	JToggleButton play, pause, step;
	ImageIcon playImage, pauseImage, stepImage;
	static int NUM_BUTTONS = 3;
	
	/**
	 * TODO: Stop, Restart buttons toevoegen!
	 */
	ButtonPanel()
	{
		playImage  = new ImageIcon("images/play.png");
		pauseImage = new ImageIcon("images/pause.png");
		stepImage  = new ImageIcon("images/step.png");
		play = new JToggleButton("play", playImage);
		pause = new JToggleButton("pause", pauseImage);
		step = new JToggleButton("step", stepImage);
		play.setVerticalTextPosition(AbstractButton.CENTER);
		play.setHorizontalAlignment(AbstractButton.LEADING);
		pause.setVerticalTextPosition(AbstractButton.CENTER);
		pause.setHorizontalAlignment(AbstractButton.LEADING);
		step.setVerticalTextPosition(AbstractButton.CENTER);
		step.setHorizontalAlignment(AbstractButton.LEADING);
	}
	
	public void drawButtons(JFrame f)
	{		
		add(play);
		add(pause);
		add(step);
	}

	public boolean[] getButtonStates() {
		boolean[] result = new boolean[NUM_BUTTONS];
		result[GUI.PLAY] = play.isSelected();
		result[GUI.STEP] = step.isSelected();
		result[GUI.PAUSE]= pause.isSelected();
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
	
}

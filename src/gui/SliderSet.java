package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import Program.Config;

public class SliderSet {
	JTextField text;
	JSlider slider;
	Config c;
	String name;
	int oldSlider, oldText;
	
	
	SliderSet(int value, String name)
	{
		// store info
		this.name = name;
		
		// create slider object, set starting tick to value
		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(5);
		slider.setValue(value);
		
		text = new JTextField();
		text.setSize(20,20);
		text.setText(Integer.toString(value));
	}
	
	SliderSet(int value, String name, int max)
	{
		// store info
		this.name = name;
		
		// create slider object, set starting tick to value
		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		slider.setMaximum(max);
		slider.setMajorTickSpacing(max/5);
		slider.setMinorTickSpacing(max/20);
		slider.setValue(value);
		
		text = new JTextField();
		text.setSize(20,20);
		text.setText(Integer.toString(value));
	}
	
	public void addTo(JPanel p)
	{
		JPanel temp = new JPanel();
		temp.add(new JLabel(name));
		temp.add(slider);
		temp.add(text);
		p.add(temp);
	}
	
	public void updateSlider()
	{
		int curSlider = slider.getValue();
		int curText = Integer.parseInt(text.getText());
		
		// slider was changed
		if(curSlider != oldSlider)
		{
			curText = curSlider;
			text.setText(Integer.toString(curSlider));
		} else if (curText != oldText)
		{
			curSlider = curText;
			slider.setValue(curText);
		}
		
		oldSlider = curSlider;
		oldText = curText;
	}
	
	int getValue()
	{
		return slider.getValue();
	}
}

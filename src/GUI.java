import javax.swing.JFrame;


public class GUI extends JFrame{
	
	DrawPanel dpnl;
	
	GUI(int hor, int ver)
	{
		initUI(hor, ver);
	}
	
	public void updateGraphics(World w)
	{
		dpnl.paintComponent(dpnl.getGraphics(), w);
	}
	
	public final void initUI(int hor, int ver) {
        dpnl = new DrawPanel();
        add(dpnl);

        setSize(600, 600);
        setTitle("Iterated Prisoners Dilemma");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
	
}

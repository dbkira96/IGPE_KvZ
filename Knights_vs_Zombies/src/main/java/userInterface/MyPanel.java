package userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.JPanel;

import gameManager.GameManager;
import interfaces.Renderable;
import rendering.ObjectRenderer;

public class MyPanel extends JPanel {

	Renderable target;
	
	public void setTarget(Renderable target) {
		this.target = target;
	}
	public Renderable getTarget() {
		return target ;
	}
	GameManager gm;

	private static final long serialVersionUID = 1L;

	
	
	
	public MyPanel(GameManager g,int height,int width)  {
		gm=g;
		
		//Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d= new Dimension(height,width);
		this.setPreferredSize(d);
		this.setBackground(Color.BLACK);
		//Dimension FullScreen = tk.getScreenSize();
				
		this.setDoubleBuffered(true);
		
		
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d= (Graphics2D)g;
		g2d.translate(-gm.getCamera().getPosX(), -gm.getCamera().getPosY());
		for(int i=0;i<target.getRenderers().size();i++) {
			
			target.getRenderers().get(i).DefaultRender(g2d);
			
		}
		g2d.translate(0,0);

	}
	

	
	public void render() {
		
		repaint();
	}
	
}

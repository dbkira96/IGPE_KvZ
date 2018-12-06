package userInterface;

import java.util.LinkedList;

import interfaces.Renderable;
import rendering.ObjectRenderer;

public class Painter {
	
	
	Renderable R;

	MyPanel p;
	public Painter() {
		// TODO Auto-generated constructor stub
	}

	

	

	public void setPanel(MyPanel pn) {
		p=pn;
		
	}

	public void render() {
		
		p.render();
	}

	public void setTarget(Renderable r) {
		p.setRenderers(r.getRenderers());
	}
	
	public LinkedList<ObjectRenderer> getRenderers() {
		// TODO Auto-generated method stub
		return R.getRenderers();
	}

	public MyPanel getPanel() {
		return p;
	}


	
}

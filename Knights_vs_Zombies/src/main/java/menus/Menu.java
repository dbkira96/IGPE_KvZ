package menus;
import gameManager.Action;
import gameManager.GameManager;
import interfaces.Renderable;
import objects.Background;
import objects.Control;
import objects.GameObject;
import objects.ObjectId;
import rendering.ObjectRenderer;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

public abstract class Menu extends GameObject implements Renderable {
	
	LinkedList<Control> controls;
	LinkedList<ObjectRenderer> renderers; 
	Background background;
	
	int selectedIndex=0;
	float distanceBetweenButton = 3;
	
	GameManager gm;
	
	double delay=0;
	double inputDelay=30;
	boolean ready;
	
	boolean loading;
	
	public Menu(GameManager gm) {
		super(0,0,300,300,ObjectId.MENU);
		this.gm = gm;
		
		controls=new LinkedList<Control>();
		renderers =  new LinkedList<ObjectRenderer>();
		background = new Background(width,height);
		ObjectRenderer RBG = new ObjectRenderer(background, gm);
		renderers.add(RBG); 
	}
	
	public Menu(float w,float h,GameManager gm)  {
		super(0,0,w,h,ObjectId.MENU);
		this.gm = gm;
		controls=new LinkedList<Control>();
		renderers =  new LinkedList<ObjectRenderer>();
		background = new Background(width,height);
		ObjectRenderer RBG = new ObjectRenderer(background, gm);
		renderers.add(RBG); 
		
	}
	public LinkedList<ObjectRenderer> getRenderers(){
		return renderers;
	}
	public void selectNext() {  
		
			controls.get(selectedIndex).setSelected(false);
			selectedIndex+=1;
			if(selectedIndex>controls.size()-1)
				selectedIndex=0;
			controls.get(selectedIndex).setSelected(true); 
	}
	public void selectPrev() { 
			controls.get(selectedIndex).setSelected(false);
			selectedIndex-=1;
			if(selectedIndex<0)
				selectedIndex=controls.size()-1;
			controls.get(selectedIndex).setSelected(true); 			
	}
	public Action selectedAction() {
		Action a =controls.get(selectedIndex).getAction();
		ready=false;
		return a;
	} 
	public void tick(double delta) {
		delay+=delta;
		if (delay>inputDelay) {
			ready=true;
			delay=0;
		}	
	}
	

	public boolean isReady() {
		return ready;
	}

	
	public void hold() {
		ready=false;	
	}
	public void keyPressed(int key) {
		if(ready) {
		if(key==Action.SELECT_MENU.key)
			gm.getEH().performAction(selectedAction());
		if(key==KeyEvent.VK_DOWN) 
			selectNext(); 
		if(key==KeyEvent.VK_UP) 
			selectPrev();		
		hold();
		}
	}

	public void loading() {
		loading=true;
		background.setWaiting(true);
	}
	public void stopLoading() {
		loading =false;
		background.setWaiting(false);
	}

	@Override
	public void tick(LinkedList<GameObject> objects, double delta) {
		// TODO Auto-generated method stub
		
	}

}

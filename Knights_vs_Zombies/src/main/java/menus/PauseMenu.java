package menus;

import gameManager.Action;
import gameManager.GameManager;
import objects.Button;
import objects.Control;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class PauseMenu extends Menu {

	public PauseMenu( GameManager gm) {
		super(gm);
		
		
		
		
		Control Continue = new Button(50,25,Action.RESUME);
		ObjectRenderer RContinue = new ControlRenderer(Continue, gm);
		Control Exit = new Button(50,25,Action.CLOSE_GAME);
		ObjectRenderer RExit = new ControlRenderer(Exit, gm);
		
		controls.add(Continue);
		controls.add(Exit);
		
		controls.get(selectedIndex).setSelected(true);
		
		renderers.add(RContinue);
		renderers.add(RExit);
		
		float posx = (width/2) - (controls.get(1).getWidth()/2);
		float posy = (height/2) - (((controls.size()-1)/2)*controls.get(1).getHeight());
		
		float pad = 10;
		
		for(int k=0; k<controls.size(); k++)
		{
			controls.get(k).setPosX(posx);
			controls.get(k).setPosY(posy +(pad*(k-1))+ (controls.get(k).getHeight()+(controls.get(k).getHeight()*(k-1))));
		}
	}
	

}

package menus;

import gameManager.Action;
import gameManager.GameManager;
import objects.Button;
import objects.Control;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class StartMenu extends Menu {

	public StartMenu( GameManager gm) {
		super(gm);
		

		
		selectedIndex = 0;
		Control Local = new Button(50,25,Action.MENU_START_LOCAL_GAME);
		ObjectRenderer RLocal = new ControlRenderer(Local, gm);
		Control Multiplayer = new Button(50,25,Action.MENU_START_MULTIPLAYER_GAME);
		ObjectRenderer RMultiplayer = new ControlRenderer(Multiplayer, gm);
		
		Control Exit = new Button(50,25,Action.MENU_CLOSE_GAME);
		ObjectRenderer RExit = new ControlRenderer(Exit, gm);
		
		controls.add(Local);
		controls.add(Multiplayer);
		controls.add(Exit);
		controls.get(selectedIndex).setSelected(true);
		
		renderers.add(RLocal);
		renderers.add(RMultiplayer);
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

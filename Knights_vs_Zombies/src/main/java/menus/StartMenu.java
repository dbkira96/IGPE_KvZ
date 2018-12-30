package menus;

import gameManager.Action;
import gameManager.GameManager;
import objects.Button;
import objects.Control;
import objects.MyImage;
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
		MyImage Title = new MyImage(105,10,90,110,"Title");
		
		Control Exit = new Button(50,25,Action.MENU_CLOSE_GAME);
		ObjectRenderer RExit = new ControlRenderer(Exit, gm);
		ObjectRenderer RTitle = new ObjectRenderer(Title , gm);
		
		controls.add(Local);
		controls.add(Multiplayer);
		controls.add(Exit);
		controls.get(selectedIndex).setSelected(true);
		
		renderers.add(RLocal);
		renderers.add(RMultiplayer);
		renderers.add(RExit);
		renderers.add(RTitle);

		float posx = (width/2) - (controls.get(1).getWidth()/2);
		float posy = (height/2) - (((controls.size()-1)/2)*controls.get(1).getHeight())+20;
		
		float pad = 10;
		
		for(int k=0; k<controls.size(); k++)
		{
			controls.get(k).setPosX(posx);
			controls.get(k).setPosY(posy +(pad*(k-1))+ (controls.get(k).getHeight()+(controls.get(k).getHeight()*(k-1))));
		}
		
	}
	

}

package menus;

import gameManager.Action;
import gameManager.GameManager;
import objects.Control;
import objects.Button;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class MultiStartMenu extends Menu {

	public MultiStartMenu( GameManager gm) {
		super( gm);
		
		selectedIndex = 0;
		Control CreaPartita = new Button(50,25,Action.CREAPARTITA);
		ObjectRenderer RCreaPartita = new ControlRenderer(CreaPartita, gm);
		Control Partecipa = new Button(50,25,Action.PARTECIPA);
		ObjectRenderer RPartecipa = new ControlRenderer(Partecipa, gm);
		Control Back = new Button(50,25,Action.BACKTOMENU);
		ObjectRenderer RBack = new ControlRenderer(Back, gm);
		
		controls.add(CreaPartita);
		controls.add(Partecipa);
		controls.add(Back);
		
		controls.get(selectedIndex).setSelected(true);
		
		renderers.add(RCreaPartita);
		renderers.add(RPartecipa);
		renderers.add(RBack);
		
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

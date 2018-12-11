package menus;

import java.awt.event.KeyEvent;


import gameManager.Action;
import gameManager.GameManager;
import gameManager.JAction;
import media.Media;
import objects.PlayerPreview;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class ChooseMultiMenu extends Menu {

	
	PlayerPreview pp;
	public ChooseMultiMenu( GameManager gm) {
		super( gm);
		
		
		pp= new PlayerPreview(30,200, Media.getCharactersName(), true);
		
		ObjectRenderer rPlayer1Preview = new ControlRenderer(pp, gm);
		

		pp.setPosX(50);
		pp.setPosY(80);
		
		
		controls.add(pp);
		
		renderers.add(rPlayer1Preview);
	}
	public void nextPlayer() 
	{
			pp.Next();					
	}
	public void prevPlayer() 
	{
		
		pp.Prev();
		
	}
	
	
	public void keyPressed(int key) {
		if(ready) {
		if(key==Action.SELECT_MENU.key) {
			
				JAction a= new JAction(Action.PLAYER_CHOOSED_MULTIPLAYER);
				a.put("playerName",pp.getSelectedPlayer());
				gm.getEH().performAction(a);
			}
		if(key==KeyEvent.VK_DOWN) 
			nextPlayer(); 
		if(key==KeyEvent.VK_UP) 
			prevPlayer();		
		hold();
		}
	}
	

	
	
}

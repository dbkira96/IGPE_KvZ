package menus;

import gameManager.Action;
import gameManager.GameManager;
import media.Media;
import objects.Button;
import objects.Control;
import objects.PlayerPreview;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class GameOverMenu extends Menu {

	public GameOverMenu(GameManager gm) {
		super(gm);
		
		selectedIndex = 0;
		PlayerPreview winner = new PlayerPreview(30,200, Media.getCharactersName(), true);
		ObjectRenderer rPreview = new ControlRenderer(winner, gm);

		
		winner.setPosX(60);
		winner.setPosY(50);
	
		
		controls.add(winner);
		renderers.add(rPreview);
		
		// PAUSE SELECTION MENU'
		Control Restart = new Button(50,25,Action.START_MULTIPLAYER_GAME);
		ObjectRenderer RRestart = new ControlRenderer(Restart, gm);
		Control Exit = new Button(50,25,Action.CLOSE_GAME);
		ObjectRenderer RExit = new ControlRenderer(Exit, gm);
		
		controls.add(Restart);
		controls.add(Exit);
		
		controls.get(selectedIndex).setSelected(true);
		
		renderers.add(RRestart);
		renderers.add(RExit);
		
		float posx = (width/2) - (controls.get(1).getWidth()/2);
		float posy = (height/2) - (((controls.size()-1)/2)*controls.get(1).getHeight());
		
		float pad = 10;
		
		for(int k=1; k<controls.size(); k++)
		{
			controls.get(k).setPosX(posx);
			controls.get(k).setPosY(posy +(pad*(k-1))+ (controls.get(k).getHeight()+(controls.get(k).getHeight()*(k-1))));
		}
	}
	

}

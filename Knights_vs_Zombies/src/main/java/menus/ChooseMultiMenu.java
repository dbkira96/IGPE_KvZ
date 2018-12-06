package menus;

import gameManager.GameManager;
import media.Media;
import objects.PlayerPreview;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class ChooseMultiMenu extends Menu {

	boolean PlayerChoosed = false;
	
	public ChooseMultiMenu( GameManager gm) {
		super( gm);
		
		PlayerPreview p = new PlayerPreview(30,200, Media.getCharactersName(), true);
		
		ObjectRenderer rPlayer1Preview = new ControlRenderer(p, gm);
		

		p.setPosX(50);
		p.setPosY(80);
		
		
		controls.add(p);
		
		renderers.add(rPlayer1Preview);
	}
	
}

package menus;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import gameManager.Action;
import gameManager.GameManager;
import gameManager.JAction;
import media.Media;
import objects.PlayerPreview;
import rendering.ControlRenderer;
import rendering.ObjectRenderer;

public class ChooseLocalMenu extends Menu {

int turn;
	

	ArrayList<PlayerPreview> playerPreviews;
	//boolean PlayerChoosed[] =  {false,false};
	
	
	public ChooseLocalMenu( GameManager gm) {
		super(gm);
		turn = 1;
		playerPreviews=new ArrayList<PlayerPreview>();
		
		PlayerPreview p = new PlayerPreview(30,200, Media.getCharactersName(), true);
		ObjectRenderer renderer = new ControlRenderer(p, gm);
		p.setPosX(50);
		p.setPosY(80);
		playerPreviews.add(p);
		controls.add(p);
		renderers.add(renderer);
		
		p = new PlayerPreview(30,200, Media.getCharactersName(), false);
		renderer = new ControlRenderer(p, gm);
		p.setPosX(200);
		p.setPosY(80);
		playerPreviews.add(p);
		controls.add(p);
		renderers.add(renderer);
	}
	
	public void nextPlayer() 
	{
			playerPreviews.get(turn-1).Next();					
	}
	public void prevPlayer() 
	{
		
		playerPreviews.get(turn-1).Prev();
		
	}
	public void nextPlayerSelectionTurn() 
	{
				if(turn < playerPreviews.size())
				{
					playerPreviews.get(turn-1).setActive(false);
					
					playerPreviews.get(turn).setActive(true);
					turn++;
				}
				else if (turn == playerPreviews.size())
				{
					turn = -1;
					playerPreviews.get(playerPreviews.size()-1).setActive(false);
				}
		
	}
	
	public void keyPressed(int key) {
		if(ready) {
		if(key==Action.SELECT_MENU.key)
			if(turn!=-1)
				nextPlayerSelectionTurn();
			else {
				JAction a= new JAction(Action.START_GAME);
				a.put("playerName",playerPreviews.get(0).getSelectedPlayer());
				a.put("player2Name",playerPreviews.get(1).getSelectedPlayer());
				gm.getEH().performAction(a);
			}
		if(key==KeyEvent.VK_DOWN) 
			nextPlayer(); 
		if(key==KeyEvent.VK_UP) 
			prevPlayer();		
		hold();
		}
	}
	public int getPlayerSelectionTurn() {return turn;}

	
	
	
	
}

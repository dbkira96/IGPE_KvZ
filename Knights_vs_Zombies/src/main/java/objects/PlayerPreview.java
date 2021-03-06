package objects;

import java.util.LinkedList;

public class PlayerPreview extends Control {

	LinkedList<String> list;
	int selectedIndex;
	Boolean active;
	public PlayerPreview(int width, int height, LinkedList<String> PlayersName, Boolean active) {
		super(width, height, null);
		list = PlayersName;
		selectedIndex = 0;
		this.active = active;
		// TODO Auto-generated constructor stub
	}
	public void Next()
	{
		if(selectedIndex < list.size()-1)
		{
			selectedIndex++;	
		}
		System.out.println(selectedIndex);
	}
	public void Prev()
	{
		if(selectedIndex > 0)
		{
			selectedIndex--;	
		}
		System.out.println(selectedIndex);

	}
	
	public void selectPlayer(String player) {
		
		selectedIndex = list.indexOf(player);
	}
	public String getSelectedPlayer() {return list.get(selectedIndex);}
	
	public void setActive(Boolean active) {this.active = active;}
	public Boolean getActive() {return active;}
}

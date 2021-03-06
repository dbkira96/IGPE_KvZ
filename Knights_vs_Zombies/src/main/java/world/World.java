package world;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import gameManager.Action;
import gameManager.EventHandler;
import gameManager.JAction;
import objects.GameObject;
import objects.ObjectId;
import objects.Player;

public class World extends GameObject{
	
	
	
	LinkedList<GameObject> objects;
	EventHandler ev;
	LinkedList<Player> players;
	
	
	public JSONObject getJSON() {
		JSONObject j=new JSONObject();
		j.put("width", width);
		j.put("height", height);
		
	
		JSONArray pl=new JSONArray();
		
		pl.put(this.players.get(0).getJSON());
		pl.put(this.players.get(1).getJSON());
		j.put("players",pl);
		return j;
	}
	public void sync(JSONObject j) {
		//width=j.getInt("width");
		//height=j.getInt("height");
		
		JSONArray ja= j.getJSONArray("players");
		players.get(0).sync( ja.getJSONObject(0));
		players.get(1).sync(ja.getJSONObject(1));
		
	}
	
 	public Player getPlayer(int n) {
		return players.get(n-1);
	}

	public void setPlayer(Player p,int n) {
		players.set(n-1, p);
	}
	
	
	

	public String getPlayerName(int i) {
		return players.get(i-1).getName();
	}

	public void setPlayerName(int i,String n) {
		this.players.get(i-1).setName(n);
	}



	
	public World(int w, int h, EventHandler e) {
		super(0, 0, w, h, ObjectId.WORLD);
		objects=new LinkedList<GameObject>();
		players=new LinkedList<Player>();
		players.add(new Player(0,0));
		players.add(new Player(0,0));
		
		ev=e;
	}
	
	
	
	public float getWidth() {	return width;}

	public void setWidth(int width) {this.width = width;}

	public float getHeight() {return height;}

	public void setHeight(int height) {this.height = height;}




public void Update(double delta) {
		for (int i=0;i<players.size();i++){
			Player p=players.get(i);
			if(p.isDead()&&p.getlives()==0) {
				JAction a = new JAction(Action.GAMEOVER);
				int w = 0;
				if(i==0) {w=1;}
				a.put("winner", players.get(w).getName());
				ev.performAction(a);
			}
		}
		for (int i=0;i<objects.size();i++) {
			objects.get(i).tick(objects,delta);
		}
	}

	public void addObject(GameObject o) {
		objects.add(o);
	}
	public LinkedList<Player> getPlayers() {
		// TODO Auto-generated method stub
		return players;
	}
	@Override
	public void tick(LinkedList<GameObject> objects, double delta) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}

package gameManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import org.json.JSONException;

import network.Client;
import network.Server;
import interfaces.Direction;

import menus.*;

public class GMEventHandler implements EventHandler {
	
	GameManager gm;
	
	
	public boolean[] keys;
	KeyAdapter kl;
	LinkedList<JAction> actions;
	public GMEventHandler(GameManager g) 
	{
		
		gm=g;
		initEH();
		keys=new boolean[KeyEvent.KEY_LAST];
	}
	
	private void initEH()
	{
		actions=new LinkedList<JAction>();
		gm.painter.setFocusable(true);
		gm.painter.requestFocusInWindow();
		gm.painter.addMouseListener(new MouseAdapter() {			
			
		});
			
		
	
		gm.painter.addKeyListener( new KeyAdapter() {
			private long lastPressProcessed = 0;
			@Override
			public void keyPressed(KeyEvent e) {
				
				 if(System.currentTimeMillis() - lastPressProcessed > 50) {
			            //Do your work here...
						int key = e.getKeyCode();
						keys[key]=true;
			            lastPressProcessed = System.currentTimeMillis();
			        }     
			}
			@Override
			public void keyReleased(KeyEvent e) {
				 {
						int key = e.getKeyCode();		
						keys[key]=false;	 
				}
			}
			
		});	
	}
	public boolean getKey(int k) {
		try {
			return keys[k];
		}
		finally {
			keys[k]=false;
		}
	}
	
	public void performAction(JAction a) {
		actions.push(a);		
	}
	public void performAction(Action a) {
		JAction ja;
		try {
			ja = new JAction(a);
			actions.push(ja);	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	public void performAction(Action a,int client) {
		JAction ja;
		try {
			ja = new JAction(a);
			ja.put("client",client);
			actions.push(ja);	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void resolveActions() {
		
		JAction a=actions.poll();
		if(a!=null)
		try {
			switch (a.getAction()) {
			case PLAYER_JUMP:
				gm.w.getPlayer(a.getInt("client")).jump();
				break;
			case PLAYER_ATTACK:
				gm.w.getPlayer(a.getInt("client")).toggleAttack(true);
			break;
			case PLAYER_MOVE_LEFT: 
				gm.w.getPlayer(a.getInt("client")).ChangeDirection(Direction.LEFT);
				break;	
			case PLAYER_MOVE_RIGHT:
				gm.w.getPlayer(a.getInt("client")).ChangeDirection(Direction.RIGHT);
				break;
			case PLAYER_MOVE_REST:
				gm.w.getPlayer(a.getInt("client")).ChangeDirection(Direction.REST);
				break;
			case PLAYER_CROUCH:
				gm.w.getPlayer(a.getInt("client")).toggleCrouch(true);
				break;
			case PLAYER_STAND:
				gm.w.getPlayer(a.getInt("client")).toggleCrouch(false);
				break;
			case OPEN_MENU:
				gm.openMenu();
				break;
			case CLOSE_GAME: 
				System.exit(0);
				break;
			case OPEN_SETTING:
				break;
			case SELECT_MENU:
				break;
			case START_GAME:
					gm.menu=new PauseMenu(gm);
					gm.inMenu=false;
					gm.inGame=true;
					gm.w.setPlayerName(1,a.getString("playerName"));
					gm.w.setPlayerName(2,a.getString("player2Name"));
					gm.loadLevel(new Level());
				break;
			case START_MULTIPLAYER_GAME:
					gm.menu=new PauseMenu(gm);
					gm.w.setPlayerName(1,a.getString("playerName"));
					gm.w.setPlayerName(2,a.getString("player2Name"));
					gm.inMenu=false;
					gm.multiplayerGame=true;
					gm.inGame=true;
					gm.loadLevel(new Level());
				break;
			case MENU_START_LOCAL_GAME:
				gm.menu=new ChooseLocalMenu(gm);
				gm.openMenu();	
				break;
			case MENU_START_MULTIPLAYER_GAME:
				gm.menu=new MultiStartMenu(gm);
				gm.openMenu();	
				break;
			case PAUSE:
				gm.menu=new PauseMenu(gm);
				gm.painter.setTarget(gm.menu);	
				break;
			case RESUME:
				gm.inMenu=false;
			
				gm.painter.setTarget(gm.worldRenderers);
				break;
			case CREAPARTITA:
				try 
				{	gm.menu.loading();
					gm.S = new Server();
					performAction(Action.PARTECIPA);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				break;
			case PARTECIPA:
				try 
				{
					gm.menu.loading();
					
					gm.waitingConnection = true;
					gm.openMenu();	
					gm.C = new Client();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case BACKTOMENU:
				gm.menu=new StartMenu(gm);
				gm.openMenu();
				break;
			case CHOOSE_PLAYER_MULTIPLAYER:
				gm.menu=new ChooseMultiMenu(gm);
				gm.myPlayer=a.getInt("target");
				gm.openMenu();
				break;
			case MENU_CLOSE_GAME:
				gm.frame.dispatchEvent(new WindowEvent(gm.frame, WindowEvent.WINDOW_CLOSING));
				break;
			
			case PLAYER_CHOOSED_MULTIPLAYER:
					try {
						gm.C.sendMessage(a.toString());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				break;
			case GAMEOVER:
				
					gm.inGame=false;
					String winner=a.getString("winner");
					gm.menu=new GameOverMenu(gm,winner);
					if (gm.multiplayerGame) {
						gm.multiplayerGame=false;
						gm.C.close();
						if (gm.S!=null) {
							gm.S.close();
						}
					}
					gm.openMenu();
				
				
				break;
			default:
				break;
			
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
}

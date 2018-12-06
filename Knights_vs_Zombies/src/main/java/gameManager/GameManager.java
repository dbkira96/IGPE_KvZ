package gameManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import interfaces.Renderable;
import media.Media;
import menus.Menu;
import menus.StartMenu;
import network.Client;
import network.Message;
import network.Server;
import objects.GameObject;
import objects.Player;
import rendering.Camera;
import rendering.ObjectRenderer;
import rendering.PlayerRenderer;
import userInterface.*;
import world.World;


public class GameManager extends Thread implements Runnable{
	public static void main(String[] args) {
		
		
		GameManager gm= new GameManager();
		Media.LoadMedia();
		
		gm.start();
	}
	
	int panelWidth=1440;	
	int panelHeight=900;

	Toolkit tk;

	Image BackgroundMenu;
	Menu menu;
	MyFrame frame;
	double draw;
	
	
	
	Renderable worldRenderers;
	LinkedList<GameObject> SavedObjects;
	

	
	
	private GMEventHandler ev;
	World w;
	
	
	Painter painter;
	Camera cam;
	
	Server S = null;
	Client C = null;
	int myPlayer=1;
	
	private boolean running=false;
	boolean inMenu;
	boolean inGame=false;
	boolean MuteSound = true;
	boolean multiplayerGame = false;
	boolean waitingConnection = false;
	boolean waitingChoosePlayer = false;
	public GameManager() 
	{
		this.setName("Game Manager");
		
		menu = new StartMenu(this);
		
		painter=new Painter();
		tk = Toolkit.getDefaultToolkit();
		initGui();
		
		openMenu();
		ev=new GMEventHandler(this);
		w=new World(0,0,getEH());
		
		
		
			
	}

	private void loadPlayerSpecs(Player obj)
	{
		
		obj.setBaseAttack(Media.getPlayerSpecs(obj.getName()).get("baseAttack"));
		obj.setStandHeight(Media.getPlayerSpecs(obj.getName()).get("standHeight"));
		obj.setAtkSpeed(Media.getPlayerSpecs(obj.getName()).get("atkSpeed"));
		obj.setAtkRange(Media.getPlayerSpecs(obj.getName()).get("atkRange"));
		obj.setWeight(Media.getPlayerSpecs(obj.getName()).get("weight"));
		
		
	}
	public void initGui() 
	{
		
		
		frame= new MyFrame(panelWidth,panelHeight);
		
		MyPanel pn=new MyPanel(this,panelWidth, panelHeight);
		JPanel content= new JPanel(new GridBagLayout());
		content.setBackground(Color.BLACK);
		
		content.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent c) {
				super.componentResized(c);
				draw=0;
				int W=16;
				int H=9;
				Rectangle b= c.getComponent().getBounds();
		
				
				MyPanel p=pn;
				p.setBorder(new EmptyBorder(0,0,0,0));
				if(b.width*H<=b.height*W) {
					int sH = b.width*H/W;
					int bH =b.height-sH;
					
				    p.setPreferredSize(new Dimension( b.width,sH));
				    
				    p.setBorder(new EmptyBorder(bH/2,0,bH/2,0));
				}
				else {
					int sW =b.height*W/H;
					int bW =b.width-sW;
					p.setPreferredSize(new Dimension(sW, b.height));
					p.setBorder(new EmptyBorder(0,bW/2,0,bW/2));
					
				}
				p.repaint();
			}
		});	
		GridBagConstraints gbc= new GridBagConstraints();
		gbc.anchor=GridBagConstraints.CENTER;
		
		content.add(pn,gbc);
		
	
		painter.setPanel(pn);
		frame.setContentPane(content);
		frame.setLocationRelativeTo(null);
		//frame.pack();
		frame.setVisible(true);
	}
	public void loadLevel(Level l) {
		
		
		String s2=w.getPlayerName(1);
		String s1=w.getPlayerName(2);
		w=new World(l.getWidth(),l.getHeight(),getEH());
		worldRenderers= new Renderable() {
			LinkedList<ObjectRenderer> l=new LinkedList<ObjectRenderer>();
			@Override
			public LinkedList<ObjectRenderer> getRenderers() {
				return l;
			}
		};
		LinkedList<ObjectRenderer> r=worldRenderers.getRenderers();
		Player p=new Player(50,0);
		Player p2 =new Player(200,0);
		
		w.addObject(p);
		w.addObject(p2);
		
		
		w.setPlayer(p,1);
		w.setPlayer(p2,2);
		w.getPlayer(1).setName(s1);
		w.getPlayer(2).setName(s2);
		
		loadPlayerSpecs(w.getPlayer(1));
		loadPlayerSpecs(w.getPlayer(2));
		
		cam=new Camera(w);
		
		cam.addAnchor(p);
		cam.addAnchor(p2);
		for(int i=0;i<l.objects.size();i++) {
			GameObject o= l.objects.get(i);
			w.addObject(o);
			r.add(new ObjectRenderer(l.objects.get(i),this));
		}
		
		r.add(new PlayerRenderer(p,this));	
		r.add(new PlayerRenderer(p2,this));
		
		painter.setTarget(worldRenderers);
		
	}
	public void start() {
		if(isRunning() )return;
		running=true;
		super.start();
	}
	public void run() {
	/*	double lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;*/
		draw=0;
		while(isRunning()){
			/*double now = System.nanoTime();
			delta = (now - lastTime) / ns;
			tick(delta );
			
			lastTime = now;*/
			
			Time.update();
			double delta=Time.deltaTime();
			draw+=delta;
			tick(delta );
			if(draw>1) {
				draw=0;
				painter.render();
				
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	public void tick(double delta) {	
		if(!multiplayerGame&&inGame)
		w.Update(delta);
		menu.tick(delta);
		cam.tick();
		checkInput();
		getEH().resolveActions();	
	}
	
	public void checkInput() {
		
		if(C!=null&&C.getStateClient()=="Connected") {
			checkServerInputs();
		}
		if(inMenu &&menu.isReady()) 
		{
			checkMenuInputs();
		}
		else if(inGame&&!multiplayerGame){	
			checkLocalGameInputs();		
		}	
		else if(inGame&&multiplayerGame){
			try {
				checkMultiplayerInputs();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	private void checkServerInputs(){
		
		
		try {
			String s = C.getMessage();
			if(s!=null) {
				
				Message m=new Message(s);
				s=m.getString("type");
				
				if (s.compareTo("message")==0) {
					System.out.println(m.get("content"));
				}
				else if(s.compareTo("action")==0) {
					JAction a=new JAction(m.toString());
					getEH().performAction(a);
				}
				else if(s.compareTo("sync")==0) {
					w.sync(m);
				}
			}
		} catch (JSONException e) {
			//e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		 
		
	}
	
	private void checkMenuInputs(){
		
		
			if(getEH().keys[Action.SELECT_MENU.key])
				menu.keyPressed(Action.SELECT_MENU.key);
			if(getEH().keys[KeyEvent.VK_RIGHT])
				menu.keyPressed(KeyEvent.VK_RIGHT) ;
			if(getEH().keys[KeyEvent.VK_LEFT]) 
				menu.keyPressed(KeyEvent.VK_LEFT);
			if(getEH().keys[KeyEvent.VK_DOWN]) 
				menu.keyPressed(KeyEvent.VK_DOWN) ;
			if(getEH().keys[KeyEvent.VK_UP]) 
				menu.keyPressed(KeyEvent.VK_UP);		
				
	}
	private void checkMultiplayerInputs() throws JSONException, InterruptedException {
		if( C.getStateClient() == "Connected") {
			if(getEH().keys[Action.PLAYER_ATTACK.key]&& !w.getPlayer(myPlayer).isAttacking()) 
				C.sendMessage(new JAction(Action.PLAYER_ATTACK).toString());
			if(getEH().keys[Action.PLAYER_MOVE_LEFT.key]&&!w.getPlayer(myPlayer).isMovingLeft()) {
				C.sendMessage(new JAction(Action.PLAYER_MOVE_LEFT).toString());
			}
			 if(getEH().keys[Action.PLAYER_MOVE_RIGHT.key]&&!w.getPlayer(myPlayer).isMovingRight()) {
				C.sendMessage(new JAction(Action.PLAYER_MOVE_RIGHT).toString());
			}
			 if(getEH().keys[Action.PLAYER_JUMP.key] && !w.getPlayer(myPlayer).isJumping()) {
				C.sendMessage(new JAction(Action.PLAYER_JUMP).toString());
			}
			 if(getEH().keys[Action.PLAYER_CROUCH.key]&& !w.getPlayer(myPlayer).isCrouching()) {
				C.sendMessage(new JAction(Action.PLAYER_CROUCH).toString());
			}
			 if(!getEH().keys[Action.PLAYER_CROUCH.key]&& w.getPlayer(myPlayer).isCrouching()) {
				C.sendMessage(new JAction(Action.PLAYER_STAND).toString());
			}
			 if(!w.getPlayer(myPlayer).isResting()&&!getEH().keys[Action.PLAYER_JUMP.key]&&!getEH().keys[Action.PLAYER_MOVE_RIGHT.key]&&!getEH().keys[Action.PLAYER_MOVE_LEFT.key]) {
				C.sendMessage(new JAction(Action.PLAYER_MOVE_REST).toString());				
			}
		}	
	}
	private void checkLocalGameInputs() {
		if(getEH().keys[Action.PAUSE.key])				
			getEH().performAction(Action.PAUSE);
		
		 if(getEH().keys[Action.PLAYER_ATTACK.key]&& !w.getPlayer(1).isAttacking()) 
			 getEH().performAction(Action.PLAYER_ATTACK,1);
		else if (!w.getPlayer(1).isMovingLeft()&&getEH().keys[Action.PLAYER_MOVE_LEFT.key])
			getEH().performAction(Action.PLAYER_MOVE_LEFT,1);
		else if(!w.getPlayer(1).isMovingRight()&&getEH().keys[Action.PLAYER_MOVE_RIGHT.key])
			getEH().performAction(Action.PLAYER_MOVE_RIGHT,1);
		else if(getEH().keys[Action.PLAYER_JUMP.key] && !w.getPlayer(1).isJumping())
			getEH().performAction(Action.PLAYER_JUMP,1);
		else if(getEH().keys[Action.PLAYER_CROUCH.key]&& !w.getPlayer(1).isCrouching())
			getEH().performAction(Action.PLAYER_CROUCH,1);
		else if(!getEH().keys[Action.PLAYER_CROUCH.key]&& w.getPlayer(1).isCrouching()) 
			getEH().performAction(Action.PLAYER_STAND,1);
		else if(!w.getPlayer(1).isResting()&&!getEH().keys[Action.PLAYER_JUMP.key]&&!getEH().keys[Action.PLAYER_MOVE_RIGHT.key]&&!getEH().keys[Action.PLAYER_MOVE_LEFT.key])
			getEH().performAction(Action.PLAYER_MOVE_REST,1);	
		else if(getEH().keys[Action.PLAYER2_ATTACK.key]&& !w.getPlayer(2).isAttacking())
			getEH().performAction(Action.PLAYER_ATTACK,2);
		else if(!w.getPlayer(2).isMovingLeft()&&getEH().keys[Action.PLAYER2_MOVE_LEFT.key])
			getEH().performAction(Action.PLAYER_MOVE_LEFT,2);
		else if(!w.getPlayer(2).isMovingRight()&&getEH().keys[Action.PLAYER2_MOVE_RIGHT.key])
			getEH().performAction(Action.PLAYER_MOVE_RIGHT,2);
		else if(getEH().keys[Action.PLAYER2_JUMP.key] && !w.getPlayer(2).isJumping())
			getEH().performAction(Action.PLAYER_JUMP,2);
		else if(getEH().keys[Action.PLAYER2_CROUCH.key]&&!w.getPlayer(2).isCrouching())
			getEH().performAction(Action.PLAYER_CROUCH,2);
		else if(!getEH().keys[Action.PLAYER2_CROUCH.key]&&w.getPlayer(2).isCrouching())
			getEH().performAction(Action.PLAYER_STAND,2);
		else if(!w.getPlayer(2).isResting()&&!getEH().keys[Action.PLAYER2_JUMP.key]&&!getEH().keys[Action.PLAYER2_MOVE_RIGHT.key]&&!getEH().keys[Action.PLAYER2_MOVE_LEFT.key])
			getEH().performAction(Action.PLAYER_MOVE_REST,2);
	}
	
	
	
	
	public float ConvertX(float wx) {
		return  ((wx*painter.getPanel().getWidth())/cam.getWidth()) ;
		
 	}
 	public float ConvertY(float wy) {
 		return  ((wy*painter.getPanel().getHeight())/cam.getHeight()) ;
 		
 	}
 	public float ConvertPosX(float wx) { 
 		return (int) (((wx)*painter.getPanel().getWidth())/cam.getWidth()) ;
 		
 	}
 	public float ConvertPosY(float wy) {
 		return  (((wy)*painter.getPanel().getHeight())/cam.getHeight()) ;
 		
 	}
 	public float ConvertPanelX(float px) {
 		return  ((px*cam.getWidth())/painter.getPanel().getWidth()) ;
 		
 	}
 	public float ConvertPanelY(float py) {
 		return  ((py*cam.getHeight())/painter.getPanel().getHeight()) ;
 	}
	public World getWorld() {return w;}
	
	public void setMenu(boolean m) {this.inMenu = m;}
	public boolean isRunning() {
		return running;
	}
	public void openMenu() {
		
		cam=new Camera(menu);
		cam.setFree(true);
		cam.center();
		inMenu=true;
		painter.setTarget(menu);
	}
	public Camera getCamera() {
		// TODO Auto-generated method stub
		return cam;
	}
	public GMEventHandler getEH() {
		return ev;
	}
	
	
}

package objects;

import java.util.LinkedList;

import org.json.JSONObject;

import interfaces.CanCrouch;
import interfaces.CanFight;
import interfaces.CanJump;
import interfaces.CanMove;
import interfaces.Collides;
import interfaces.Direction;
import interfaces.Drawable;
import interfaces.GravityDependent;
import objects.BoundingBox.Side;

public class Player extends DynamicGameObject implements Collides, CanFight, CanJump, Drawable, GravityDependent, CanMove,CanCrouch{

	String Name;
	
	
	float life=250.0f;
	float baseAttack=1.0f;
	float standHeight;
	
	
	float atkSpeed=10.0f;
	float atkRange=5.0f;
	float weight=0.0f;
	float damage=0.0f;
	
	int hitCount=0;
	int lives=1;
	
	Boolean falling=true;
	Boolean jumping=false;
	Boolean crouching=false;
	Boolean attacking=false;
	Boolean staggering=false;
	Boolean dead=false;
	
	double attackTimer=0;
	double staggerTimer=0;
	double hitTimer=0;
	
	
	HitBox h=null;
	
	Direction facing=Direction.RIGHT;
	
	public JSONObject getJSON() {
		JSONObject j=super.getJSON();
		j.put("name", Name);
		j.put("life", life);
		
		j.put("falling", falling.toString());
		j.put("jumping", jumping.toString());
		j.put("crouching", crouching.toString());
		j.put("attacking", attacking.toString());
		j.put("staggering", staggering.toString());
		j.put("facing", facing.name());
		
		return j;
		
	}
	public void sync(JSONObject o) {
		super.sync(o);
		falling=Boolean.valueOf(o.getString("falling"));
		jumping=Boolean.valueOf(o.getString("jumping"));
		crouching=Boolean.valueOf(o.getString("jumping"));
		attacking=Boolean.valueOf(o.getString("attacking"));
		staggering=Boolean.valueOf(o.getString("staggering"));
		facing=Direction.valueOf(o.getString("facing"));
	}
	
	public Player(float x,float y) 
	{
		super(x, y, ObjectId.PLAYER);
		this.height = 40;
		this.width = 20;
		standHeight=20;
		Name = "";
	}
	public Player(float x,float y, float Width,float Height) 
	{
		super(x, y, Width, Height, ObjectId.PLAYER);
		this.height = Height;
		this.width = Width;
		Name = "";
	}
	public void tick(LinkedList<GameObject> objects, double delta) {
		
		if(hitTimer>0)
		{
			hitTimer-=delta;
		}
		else if(hitTimer<0) {
			hitTimer=0;
			hitCount=0;
		}
		if(staggering) {
			stagger(delta);
		}
		else {
			move(delta);
			crouch(delta);
		
			if (attacking) {
				attack(objects,delta);
			}
			
			Collision(objects);
			fall(delta);
		}
	}
	@Override
	public void move() {
		super.move();
		if (dir==Direction.RIGHT) {
			velX+=moveSpeed;
			if(velX>maxMoveSpeed) {
				velX=maxMoveSpeed;
			}
			
		}
		if (dir==Direction.LEFT) {
			velX-=moveSpeed;
			if(velX<-maxMoveSpeed) {
			velX=-maxMoveSpeed;
			}
		}
			else if(dir==Direction.REST) {
				if(velX>=moveSpeed) {
					velX-=moveSpeed;
				}
				else  if(velX<=-moveSpeed) {
					velX+=moveSpeed;
				}
				else {
					velX=0;
				}
			}		
	}
    public void fall(double delta) {
    	if(falling) {
			velY+=gravity*delta;
		}
		if(velY>MAX_FALL_SPEED) {
		velY=MAX_FALL_SPEED;
		}
		
	}
	
    public void Collision(LinkedList<GameObject> list) {
    	for (int i=0;i<list.size();i++) {
			
			GameObject t=list.get(i);
			
			if(t.id==ObjectId.BORDER) {
				if(dead==false&& this.getBounds(Side.Bottom).intersects( ((Border)t).getBounds(Side.Left))) {
				dead=true;
				lives-=1;
				}
			}
			
			if(t.id==ObjectId.BLOCK ) {
				
				if(this.getBounds(Side.Bottom).intersects( ((Block)t).getBounds(Side.Top)) ){
					posY=t.posY-this.height;
					velY=0;
					falling=false;
					jumping=false;
					
				}
				else if(this.getBounds(Side.Top).intersects( ((Block)t).getBounds(Side.Left)) ){
					if(height<standHeight) {
						crouching=true;
					}
					//velY=0;
					posY=t.posY+t.height;
				}
				else if(this.getBounds(Side.Right).intersects( ((Block)t).getBounds(Side.Left)) ){
					//velX=0;
					posX=t.posX-width;	
				}
				else if(this.getBounds(Side.Left).intersects( ((Block)t).getBounds(Side.Left)) ){
					//velX=0;
					posX=t.posX+t.width;
				}
				else {
					falling=true;
				}
			}
			
			if(t.id==ObjectId.PLAYER ) {
				Player p=(Player)t;
				if(this != p)
				{
					if(this.getBounds(Side.Bottom).intersects( ((Player)t).getBounds(Side.Top)) ){
						posY=t.posY-this.height;
						velY=0;
						falling=false;
						jumping=false;
					}
					else if(this.getBounds(Side.Top).intersects( ((Player)t).getBounds(Side.Left)) ){
						velY=0;
						posY=t.posY+t.height;
					}
					else if(this.getBounds(Side.Right).intersects( ((Player)t).getBounds(Side.Left)) ){
						velX=0;
						posX=t.posX-width;	
					}
					else if(this.getBounds(Side.Left).intersects( ((Player)t).getBounds(Side.Right)) ){
						velX=0;
						posX=t.posX+t.width;
					}
					else {
						falling=true;
					}
				}
				
			}
		}
		
	}
	public Boolean Intersect(BoundingBox b) {
		return (b.intersects(this.getBounds(Side.Bottom))||b.intersects(this.getBounds(Side.Top))||b.intersects(this.getBounds(Side.Right))||b.intersects(this.getBounds(Side.Left)));
	}
	@Override
	public BoundingBox getBounds(Side s) {
		BoundingBox b=null;
		if (s==Side.Bottom) {
			b=new BoundingBox(this,(posX + (width/2)-(width/4)) ,(posY+height/2), width/2, height/2);
		}
		else if(s==Side.Top) {
			b=new BoundingBox(this,(posX + (width/2)-(width/4)) ,posY, width/2, height/2);
		}
		else if(s==Side.Right) {
			b=new BoundingBox(this,(posX+width )-1 ,(posY+(height/10)/2), 1, (height-height/10));
		}
		else if(s==Side.Left) {
			b=new BoundingBox(this,(posX ) ,(posY+(height/10)/2), width/10, (height-(height/10)));
		}
		return b;
	}
	
	public void jump() {
		if(!jumping) {
		 velY-=jumpVel;
		 falling=true;
		 jumping=true;
		}
	}
	private void move(double delta) {
		posX+=velX*delta;
		posY+=velY*delta;
		switch (dir){
		case RIGHT:
			facing=Direction.RIGHT;
			velX+=moveSpeed*delta;
			if(velX>maxMoveSpeed) 
				velX=maxMoveSpeed;
			break;
		case LEFT: 
			facing=Direction.LEFT;
			velX-=moveSpeed*delta;
			if(velX<-maxMoveSpeed) 
				velX=-maxMoveSpeed;
			break;
		case REST: 
				if(velX>=moveSpeed) 
					velX-=moveSpeed*delta;
				else  if(velX<=-moveSpeed) 
					velX+=moveSpeed*delta;
				else 
					velX=0;
		default:
			break;				
			}	
	}

	public void crouch(double delta) {
		if (!crouching){
			if(height<standHeight) {
				height+=crouchSpeed*delta;
				posY-=crouchSpeed*delta;
			}
			else {
				height=standHeight;
			}
		}
		else {
			if(height>standHeight/2) {
				height-=crouchSpeed*delta;
				posY+=crouchSpeed*delta;
			}
			else {
				height=standHeight/2;
			}
		}
	}
	public void stagger(double delta) {
		staggerTimer-=delta;
		if(staggerTimer<0) {
			staggering=false;
		}
		
	}
	public void hit(Player p) {
		
		
		p.hitCount++;
		p.hitTimer+=15.0f;
		
		p.velY-=1.0f;
		
		
		//se � stato hittato pi� volte consecutivamente
		
		if(p.hitCount>2) {
			p.velY-=0.8f+p.damage/10;
		if(facing==Direction.RIGHT)
			p.velX+=1.5f+p.damage/10;
		else
			p.velX-=1.5f+p.damage/10;
		}
		p.getDamage(baseAttack);
	}
	public Boolean isAttacking() {
		return attacking;
	}
	
	public void attack(LinkedList<GameObject> list,double delta) {
		attackTimer-=delta;
		if(attackTimer<0) 
		{
			for (int i=0;i<list.size();i++) 
			{
				GameObject t=list.get(i);
				switch (facing) {
					case LEFT:
						h=new HitBox(this, (posX-atkRange), (posY) ,  atkRange,  height/3);
						break;
					case RIGHT:
							h=new HitBox(this, (posX+width), (posY) ,  atkRange,  height/3);
						break;
					default:
						break;				
				}
				if (t!=this && t.id==ObjectId.PLAYER && ((Player)t).Intersect(h)) 
				{
					Player p= (Player)t;
					hit(p);
					
				}
			}
			attacking=false;
		}
			
	}

	public void toggleAttack(Boolean b) {
		if(attacking==false&&b) {
			attacking=true;
			attackTimer=atkSpeed;
		}	
	}
	public HitBox getHitBox() {
		return h;
	}
	
	public void getDamage(float dmg) {
		damage+=dmg;
		life-=dmg;
		staggering=true;
	}
	public PlayerState getState()
	{
		if (dead)
			return PlayerState.CROUCHINGBACK;
		if(attacking)
		{
			//System.out.println("Attacking");
			if(facing == Direction.LEFT)
				return PlayerState.ATTACKINGBACK;
			else if(facing == Direction.RIGHT)
				return PlayerState.ATTACKINGFORWARD;
		}
		else if(isMovingRight())
			return PlayerState.FORWARD;
		else if (isMovingLeft())
			return PlayerState.BACK;
		else if (falling&& velY<0)
		{
			if(facing == Direction.LEFT)
				return PlayerState.FALLINGBACK;
			else if(facing == Direction.RIGHT)
				return PlayerState.FALLINGFORWARD;
		}
		else if (crouching)
		{
			if(facing == Direction.LEFT)
				return PlayerState.CROUCHINGBACK;
			else if(facing == Direction.RIGHT)
				return PlayerState.CROUCHINGFORWARD;
		}
		else if (jumping)
		{
			if(facing == Direction.LEFT)
				return PlayerState.JUMPINGBACK;
			else if(facing == Direction.RIGHT)
				return PlayerState.JUMPINGFORWARD;
		}
		else if(dir==Direction.REST)
		{
			if(facing == Direction.LEFT)
				return PlayerState.STEADYBACK;
			else if(facing == Direction.RIGHT)
				return PlayerState.STEADYFORWARD;
		}
		
		return PlayerState.NULL;
	}
	public Boolean isResting() {
		return(dir==Direction.REST);
	}
	public Boolean isMovingLeft() {
		return(dir==Direction.LEFT);		
	}
	public Boolean isMovingRight() {
		return(dir==Direction.RIGHT);
	}
	
	public void toggleCrouch(Boolean b) {
		crouching=b;
		
	}
	public Boolean isCrouching() {
		return crouching;
	}
	
	public Boolean isJumping() {return jumping;}
	public void setName(String Name) {this.Name = Name;}
	public String getName() {return this.Name;}
	
	public float getBaseAttack() {
		return baseAttack;
	}
	public void setBaseAttack(float baseAttack) {
		this.baseAttack = baseAttack;
	}
	public float getStandHeight() {
		return standHeight;
	}
	public void setStandHeight(float standHeight) {
		this.standHeight = standHeight;
	}
	public float getAtkSpeed() {
		return atkSpeed;
	}
	public void setAtkSpeed(float atkSpeed) {
		this.atkSpeed = atkSpeed;
	}
	public float getAtkRange() {
		return atkRange;
	}
	public void setAtkRange(float atkRange) {
		this.atkRange = atkRange;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public Boolean isDead() {return dead;}
	public int getlives() {return lives;}
	
	
	
}
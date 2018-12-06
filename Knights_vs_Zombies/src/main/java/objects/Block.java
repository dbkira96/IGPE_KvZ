package objects;

import java.util.LinkedList;

import interfaces.Collides;
import interfaces.Drawable;
import objects.BoundingBox.Side;

public class Block extends GameObject implements Collides, Drawable{
	
	public Block(float x,float y) {
		super(x,y,ObjectId.BLOCK);
		width=12;
		height=12;
	}
	public Block(float x,float y,float w,float h) {
		super(x,y,w,h,ObjectId.BLOCK);
		
	}

//	@Override
//	public void tick(LinkedList<GameObject>objs) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public BoundingBox getBounds(Side s) {
		BoundingBox b=null;
		
		b=new BoundingBox(this,posX,posY,width,height);	
		return b;
	}

	

	@Override
	public void tick(LinkedList<GameObject> objects, double delta) {
		// TODO Auto-generated method stub
		
	}
	public void Collision(LinkedList<GameObject> g) {
		// TODO Auto-generated method stub
		
	}
	

}

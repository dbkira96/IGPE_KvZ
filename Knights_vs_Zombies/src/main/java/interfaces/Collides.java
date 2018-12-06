package interfaces;

import java.util.LinkedList;

import objects.BoundingBox;
import objects.GameObject;

public interface Collides {

	public BoundingBox getBounds(BoundingBox.Side s);
	public void Collision(LinkedList<GameObject> g);
	
}

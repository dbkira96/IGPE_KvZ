package interfaces;

import java.util.LinkedList;

import objects.GameObject;

public interface CanFight {

	public void attack(LinkedList<GameObject> list,double delta);
	public void toggleAttack(Boolean b);
	public void getDamage(float dmg);
	
}

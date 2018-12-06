package rendering;

import java.awt.Graphics;
import java.awt.Image;

import gameManager.GameManager;
import gameManager.Time;
import media.Media;
import objects.ObjectId;
import objects.Player;
import objects.PlayerState;

public class PlayerRenderer extends ObjectRenderer{

	Player p;
	double lastRender=12.0;
	double refreshRate=0.8;
	Image currentImage;
	int currentFrame=0;
	PlayerState lastState;
	public PlayerRenderer(Player p, GameManager g) {
		super(p, g);
		this.p=p;
	}
	@Override
	public void DefaultRender(Graphics g) {
		lastRender+=Time.deltaTime();
		if(lastRender>refreshRate) {
			currentFrame=getNextFrame();
			currentImage=Media.getImage(ObjectId.CHARACTER, p.getState(), p.getName(),currentFrame);
			lastRender=0;
		}	 
		g.drawImage(currentImage,(int)gm.ConvertPosX(obj.getPosX()),(int) gm.ConvertPosY(obj.getPosY()),(int) gm.ConvertX(obj.getWidth()),(int) gm.ConvertY(obj.getHeight()), null);
	}

	private int getNextFrame() {
		return (currentFrame+1) %Media.getImages(ObjectId.CHARACTER, p.getState(), p.getName()).size();
	}
}
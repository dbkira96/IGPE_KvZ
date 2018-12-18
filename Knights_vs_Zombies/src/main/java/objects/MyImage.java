package objects;

import java.util.LinkedList;

public class MyImage extends GameObject {
	
	
	String imageName;



	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public MyImage(float x, float y, float width, float height ,String imageN) {
		super(x, y, width, height, ObjectId.IMAGE);
		imageName = imageN;
		// TODO Auto-generated constructor stub
	}

	public MyImage(float x, float y, ObjectId id) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick(LinkedList<GameObject> objects, double delta) {
		// TODO Auto-generated method stub
		
	}
	
}

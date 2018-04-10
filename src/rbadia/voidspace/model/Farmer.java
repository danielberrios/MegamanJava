package rbadia.voidspace.model;

public class Farmer extends GameObject {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_SPEED = 5; 
	public static final int WIDTH = 45; 
	public static final int HEIGHT = 60;
	
	public Farmer (int xPos, int yPos)
	{
		super(xPos, yPos, Farmer.WIDTH, Farmer.HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
		
	}
	
	public int getDefaultSpeed()
	{
		return DEFAULT_SPEED; 
	}

	
}

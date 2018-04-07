package rbadia.voidspace.model;

public class PowerUp extends GameObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_SPEED = 4;
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	
	public PowerUp(int xPos, int yPos) {
		super(xPos, yPos, PowerUp.WIDTH, PowerUp.HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}

	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}


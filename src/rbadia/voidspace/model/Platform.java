package rbadia.voidspace.model;

import java.awt.Rectangle;

public class Platform extends Rectangle {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 44;
	private static final int HEIGHT = 4;
	private int PlatDir = 1;

	public Platform(int xPos, int yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
	}
	
	public int getDirection() {
		return PlatDir;
	}
	public void setDirection() {
		PlatDir = PlatDir* -1;
	}
}
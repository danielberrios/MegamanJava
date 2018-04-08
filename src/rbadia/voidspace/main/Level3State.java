package rbadia.voidspace.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level1State {

	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);

	}

	private static final long serialVersionUID = 1L;

	protected GameStatus status;
	protected Graphics2D g2d;
	protected Asteroid asteroid3;
	protected long lastAsteroidTime3;

	public Asteroid getAsteroid3() {return asteroid3; }



	@Override
	public void doStart() {
		super.doStart();			
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
	}

	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {
			asteroid.translate(-asteroid.getSpeed(), asteroid.getSpeed()/2);
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

				asteroid.setLocation(this.getWidth() - asteroid.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroid.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}	
	}

	@Override
	public Platform[] newPlatforms(int n) {
		platforms = new Platform[n];
		for(int i = 0; i < n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i < 4)  platforms[i].setLocation(25 + i * 50, getHeight()/2 + 140 - i * 20);
			if(i == 4) platforms[i].setLocation(50 + i * 50, getHeight()/2 + 140 - 3 * 40);
			if(i > 4){	
				int k = 4;
				platforms[i].setLocation(50 + i * 50, getHeight()/2 + 20 + (i - k) * 20 );
				k = k + 2;
			}
		}
		//translate.
		
		return platforms;
	}
	
	
//para background
//	protected void paintBackGround(Graphics backGround1Img) {
//		super.paintComponent(backGround1Img);
//		backGround1Img.drawImage(imgNew, 0, 0, this);
//	}

	
}



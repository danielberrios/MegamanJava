package rbadia.voidspace.main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State {

	private static final long serialVersionUID = 1L;
	protected Asteroid asteroidNum3;
	protected long lastAsteroidTimeNum3;
	
	
	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		
	}
	
	
	@Override
	public void doStart() {
		super.doStart();			
		thirdAsteroid(this);
		lastAsteroidTimeNum3 = -NEW_ASTEROID_DELAY;
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
	}
	

	@Override
	public Platform[] newPlatforms(int n) {
		platforms = new Platform[n];
		for(int i = 0; i < n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i < 4)  platforms[i].setLocation(50 +i*70, getHeight()/2 + 250 - 3*40);
			if(i == 4) platforms[i].setLocation(50 + i * 50, getHeight()/2 + 140 - i * 14);
			if(i > 4){	
				int k = 4;
				platforms[i].setLocation(50 + i * 50, getHeight()/2 + 20 + (i - k) * 20 );
				k = k + 2;
			}
		}
		return platforms;
	}

	public Asteroid thirdAsteroid(Level3State screen) {
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT - 32));
		asteroidNum3 = new Asteroid(xPos, yPos);
		return asteroidNum3;
		
	}
	
	
	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {
			asteroid.translate(-asteroid.getSpeed(), 0);
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				asteroid.setLocation(this.getWidth() - asteroid.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroid.getPixelsTall() - 32));
			}
		}
		if((asteroidNum2.getX() + asteroidNum2.getPixelsWide() >  0)) {
			asteroidNum2.translate(-asteroidNum2.getSpeed(), asteroidNum2.getSpeed()/2);
			getGraphicsManager().drawAsteroid(asteroidNum2, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTimeNum2) > NEW_ASTEROID_DELAY){
				asteroidNum2.setLocation(this.getWidth() - asteroidNum2.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroidNum2.getPixelsTall() - 32));
			}
		}
		if(asteroidNum3.getX() + asteroidNum3.getPixelsWide() > 0) {
			asteroidNum3.translate(-4, 3);
			getGraphicsManager().drawAsteroid(asteroidNum3, g2d, this); 
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTimeNum3) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTimeNum3 = currentTime;
				asteroidNum3.setLocation((int) (this.getWidth() - asteroidNum3.getPixelsWide()),
						(rand.nextInt((int) (this.getHeight() - asteroidNum3.getPixelsTall() - 32))));
			}
		}
	}
	
	public void removeAsteroidNum3(Asteroid asteroid){		
		Graphics2D g2d = getGraphics2D();
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.getPixelsWide(),
				asteroid.getPixelsTall());
		asteroid.setLocation(-asteroid.getPixelsWide(), -asteroid.getPixelsTall());
		this.getGameStatus().setNewAsteroid(true);
		lastAsteroidTimeNum3 = System.currentTimeMillis();
		this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
		// play asteroid explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	}
	
	@Override
	protected void checkAsteroidFloorCollisions() {
		for(int i=0; i<9; i++){
			if(asteroid.intersects(floor[i])){
				removeAsteroid(asteroid);
			}
			if(asteroidNum2.intersects(floor[i])){
				removeAsteroidNum2(asteroidNum2);
			}
			if(asteroidNum3.intersects(floor[i])){
				removeAsteroidNum3(asteroidNum3);
			}
		}
	}
	
	@Override
	protected void checkMegaManAsteroidCollisions() {
		GameStatus status = getGameStatus();
		if(asteroid.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid);
		}
		if(asteroidNum2.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroidNum2(asteroidNum2);
		}
		if(asteroidNum3.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroidNum3(asteroidNum3);
		}
	}
	
	@Override
	protected void checkBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
			}
			if(asteroidNum2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum2(asteroidNum2);
				damage=0;
			}
			if(asteroidNum3.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum3(asteroidNum3);
				damage=0;
			}
		}
	}
	
	
	
	@Override
	public boolean isLevelWon() {
		return levelAsteroidsDestroyed >= 12  || this.getInputHandler().isNPressed();
	}
}

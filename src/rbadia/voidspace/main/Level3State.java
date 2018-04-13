package rbadia.voidspace.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level1State {
	
	public BufferedImage imgNew;
	protected Asteroid asteroidNum2;
	protected long lastAsteroidTimeNum2;
	
	//Getter
	public Asteroid getAsteroidNum2() {return asteroidNum2; }
	
	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		try {
			imgNew = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/roosterbackground.png"));
		} catch (IOException e) {
			e.printStackTrace();

		}
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
		secondAsteroid(this);
		lastAsteroidTimeNum2 = -NEW_ASTEROID_DELAY;
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
	}	
	
	
	
	public Asteroid secondAsteroid(Level3State screen) {
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT - 32));
		asteroidNum2 = new Asteroid(xPos, yPos);
		return asteroidNum2;
		
	}
	
	public void removeAsteroidNum2(Asteroid asteroid){		
		Graphics2D g2d = getGraphics2D();
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.getPixelsWide(),
				asteroid.getPixelsTall());
		asteroid.setLocation(-asteroid.getPixelsWide(), -asteroid.getPixelsTall());
		this.getGameStatus().setNewAsteroid(true);
		lastAsteroidTimeNum2 = System.currentTimeMillis();
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
		}
	}
	
	@Override
	protected void checkBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
			if(asteroidNum2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum2(asteroidNum2);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
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
	
	
// for background
	protected void paintBackGround(Graphics roosterbackground) {
		super.paintComponent(roosterbackground);
		roosterbackground.drawImage(imgNew, 0, 0, this);
	}

	@Override
	public boolean isLevelWon() {
		return levelAsteroidsDestroyed >= 9  || this.getInputHandler().isNPressed();
	}
}



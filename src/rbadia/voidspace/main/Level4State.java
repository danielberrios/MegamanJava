package rbadia.voidspace.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State {

	private static final long serialVersionUID = 1L;
	protected Asteroid asteroidNum3;
	protected long lastAsteroidTimeNum3;


	public Level4State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		try {
			imgNew = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/sheep.png"));
		} catch (IOException e) {
			e.printStackTrace();

		}
	}


	@Override
	public void updateScreen() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		clearScreen();
		drawStars(50);
		paintBackGround(g2d);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();			
		drawBullets();
		drawBigBullets();
		checkBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();
		// new
		drawPowerUp();
		checkMegaManPowerCollisions();		

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}



	@Override
	public void doStart() {
		super.doStart();			
		thirdAsteroid(this);
		lastAsteroidTimeNum3 = -NEW_ASTEROID_DELAY;
		setStartState(GETTING_READY);
		setCurrentState(getStartState());		
	}

	// I give credit to Gabriela Santiago for helping me figure out how to make the platforms appear in the screen
	@Override
	protected void drawPlatforms() {
		//draw platforms
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){

			if((platforms[i].getY() + platforms[i].height < megaMan.height + 85 )) {
				platforms[i].setDirection() ;
			}
			else if((platforms[i].getY() + platforms[i].height > getWidth() - 150 )){
				platforms[i].setDirection();
			}
			if(megaMan.getX() > platforms[i].getX() && megaMan.getX() < platforms[i].getX()+ platforms[i].width && megaMan.getY()+ megaMan.height == platforms[i].getY()){
				megaMan.translate(0, platforms[i].getDirection());
			}
			platforms[i].translate(0, platforms[i].getDirection() );
			getGraphicsManager().drawPlatform(platforms[i], g2d, this,i);			
		}
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

	protected void paintBackGround(Graphics sheep) {
		super.paintComponent(sheep);
		sheep.drawImage(imgNew, 0, 0, this);
	}

	@Override
	public boolean isLevelWon() {
		return levelAsteroidsDestroyed >= 12  || this.getInputHandler().isNPressed();
	}
}

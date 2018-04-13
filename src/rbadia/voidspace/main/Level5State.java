package rbadia.voidspace.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Farmer;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level5State extends Level4State {
	
	private static final long serialVersionUID = 1L;
	protected Farmer farmeR;
	protected List<Bullet>farmerBullets;
	public int levelfarmerDestroyed = 0;
	protected long lastFarmerBulletTime = 0;
	protected Asteroid asteroidNum4;
	protected long lastAsteroidTimeNum4;
	
	//Getter
	public Asteroid getAsteroidNum4() {return asteroidNum4; }
	public Farmer getFarmer () {return farmeR;}
	public List <Bullet> getfarmerBullets() {return farmerBullets; }
	public BufferedImage imgNew;
	
	public Level5State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		try {
			imgNew = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/farmbackground.png"));
		} catch (IOException e) {
			e.printStackTrace();

		}
	}	
	
	
	@Override
	public void doStart() {	
		super.doStart();
		fourthAsteroid(this);
		lastAsteroidTimeNum4 = -NEW_ASTEROID_DELAY;
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
		farmerBullets = new ArrayList<Bullet>();
		newFarmer();
	}
	
	
	@Override
	public void updateScreen(){
		super.updateScreen();
		drawFarmer();
		drawFarmerBullets();
		checkFarmerBulletsCollision();
		checkFarmerManCollision();
		
	}
	
	public Asteroid fourthAsteroid(Level5State screen) {
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT - 32));
		asteroidNum4 = new Asteroid(xPos, yPos);
		return asteroidNum4;
		
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
			asteroidNum3.translate(-4, 0);
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
		if(asteroidNum4.getX() + asteroidNum4.getPixelsWide() >  0) {
			asteroidNum4.translate(4, 5);
			getGraphicsManager().drawAsteroid(asteroidNum4, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTimeNum4) > NEW_ASTEROID_DELAY){
				asteroidNum4.setLocation(this.getWidth() - asteroidNum4.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroidNum4.getPixelsTall() - 32));
			}
		}	
	}
		
	
	public void removeAsteroidNum4(Asteroid asteroid){		
		Graphics2D g2d = getGraphics2D();
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.getPixelsWide(),
				asteroid.getPixelsTall());
		asteroid.setLocation(-asteroid.getPixelsWide(), -asteroid.getPixelsTall());
		this.getGameStatus().setNewAsteroid(true);
		lastAsteroidTimeNum4 = System.currentTimeMillis();
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
			if(asteroidNum4.intersects(floor[i])){
				removeAsteroidNum4(asteroidNum4);
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
		if(asteroidNum4.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroidNum4(asteroidNum4);
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
			if(asteroidNum4.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum4(asteroidNum4);
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
			if(asteroidNum3.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum3(asteroidNum3);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
			if(asteroidNum4.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroidNum4(asteroidNum4);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	
	public Farmer newFarmer() {
		this.farmeR = new Farmer(getWidth()-50, 340);
		return farmeR; 
	}

	
	public void drawFarmer () {
		Graphics2D g2d = getGraphics2D();		
		getGraphicsManager().drawFarmer(farmeR, g2d, this);

	}
	
	
	protected void drawFarmerBullets() {
		Graphics2D g2d = getGraphics2D(); 
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastFarmerBulletTime) > 1500) {
			lastFarmerBulletTime = currentTime;
			fireFarmerBullet();
		}
		for (int i = 0; i <farmerBullets.size(); i++){
			Bullet farmerBullet = farmerBullets.get(i);
			getGraphicsManager().drawBullet(farmerBullet, g2d, this);
			boolean remove = this.moveFarmerBullet(farmerBullet);
			if (remove) {
				farmerBullets.remove(i);
				i--;
			}
		}
	}
	
	
	public boolean moveFarmerBullet (Bullet farmerBullet ) {
		if (farmerBullet.getY() - farmerBullet.getSpeed() >= 0) {
			farmerBullet.translate (-farmerBullet.getSpeed()/2 +2, 0);
			return false;
		}
		else {
			return true; 
		}
	}
	
	
	public void fireFarmerBullet () {
		Bullet farmerBullet = new Bullet (farmeR.x - Bullet.WIDTH/2, farmeR.y + Farmer.WIDTH/2 - Bullet.HEIGHT +2);
		farmerBullets.add(farmerBullet);
		this.getSoundManager().playBulletSound();
	}

	
	//Checks if any farmer bullets hit megaman
	public void checkFarmerManCollision() {
		GameStatus status = getGameStatus (); 
		for (int i=0; i <farmerBullets.size(); i++) {
			Bullet farmerBullet = farmerBullets.get(i);

			if(megaMan.intersects(farmerBullet)) {
				status.setLivesLeft(status.getLivesLeft() -1);
				farmerBullets.remove(i);
			}
		}	
	}
	
	
	//checks if megaman hits storm trooper
		protected void checkFarmerBulletsCollision () {
			GameStatus status = getGameStatus ();
			for (int i =0 ; i <bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				if(farmeR.intersects(bullet)) {
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 200); //Points for hitting Farmer
					bullets.remove(i);
					levelfarmerDestroyed ++;
					break;
				}
			}
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

	//for background
	protected void paintBackGround(Graphics farmbackground) {
		super.paintComponent(farmbackground);
		farmbackground.drawImage(imgNew, 0, 0, this);
	}
	
	@Override
	public boolean isLevelWon() {
		return levelAsteroidsDestroyed >= 18 ||  levelfarmerDestroyed == 15 || this.getInputHandler().isNPressed();
	}
	
}

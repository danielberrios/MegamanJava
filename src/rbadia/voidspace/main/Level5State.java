package rbadia.voidspace.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Farmer;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level5State extends Level1State {
	
	private static final long serialVersionUID = 1L;
	protected Farmer farmeR;
	protected List<Bullet>farmerBullets;
	public int levelfarmerDestroyed = 0;
	protected long lastFarmerBulletTime = 0;
	
	//Getter
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
		return  levelfarmerDestroyed == 15 || this.getInputHandler().isNPressed();
	}
	
}

import processing.core.PApplet;
import processing.core.PFont;
import spaceObjects.BlackHole;
import spaceObjects.PlanetBody;
import spaceObjects.ShipBody;
import spaceObjects.StarBody;

/**
 * Gravity Assist is a trajectory based arcade game with mechanics that simulate gravity. 
 * The goal is to launch a ship to another planet without missing or colliding with a black hole 
 * depicted by an orange circle. There is a star depicted by a white dot which has a gravitational 
 * pull that the player must use to reach the target planet without colliding with the black hole.
 * 
 * The game has three game states which are the start screen, the play screen, and the end screen.
 * Only a mouse is needed.
 * 
 * @author Nicolas Gonzalez
 *
 */
@SuppressWarnings("serial")
public class TrajectoryGame extends PApplet {
	public static void main(String[] args) {
		PApplet.main("TrajectoryGame");
	}

	// State of Game
	private int state = 0;
	
	// Colors
	private int BACKGROUND_COLOR = color(0, 0, 20);
	private int MENU_COLOR = color(168, 214, 255, 50);
	
	// Canvas Dimensions
	private final int X_MAX = 1000;
	private final int Y_MAX = 700;
	
	// Space Objects
	private PlanetBody home, target, nextTarget;
	private ShipBody ship;
	private StarBody star, nextStar;
	private BlackHole hole, nextHole;
	
	// Game State
	private boolean launch = false;
	
	// Score
	private int currentScore = 0;
	private int highScore = 0;
	private boolean newHigh = false;
	
	// Transitions
	private boolean transition = false;
	private final int TRANSITION_SPEED = Y_MAX / 100;
	
	// Font
	private PFont font = createFont("res/Audiowide.ttf", 200);
	
	//Button Setups
	private Button playButton = new Button(this, (X_MAX / 2), (Y_MAX / 2) + 100, 50, 100, "PLAY");
	private Button exitButton = new Button(this, (X_MAX / 2), (Y_MAX / 2) + 170, 50, 100, "EXIT");
	private Button playButton2 = new Button(this, (X_MAX / 2), ((Y_MAX / 2)) + 50, 50, 100, "PLAY");
	private Button mainMenuButton = new Button(this, (X_MAX / 2), ((Y_MAX / 2)) + 120, 50, 100, "MENU");
	
	/**
	 * Sets up Processing canvas.
	 */
	@Override
	public void setup() {
		size(X_MAX, Y_MAX);
		textFont(font);
		
		// Create planets, ship, stars
		initBodies();
	}
	
	/**
	 * Draws on Processing canvas.
	 */
	@Override
	public void draw() {
		
		if (displayHeight != X_MAX || displayWidth != Y_MAX) {
			size(X_MAX, Y_MAX);
		}
		
		background(BACKGROUND_COLOR);
		textSize(10);
		
		// Switches between game states
		switch(state) {
			case 0:
				menuScreen();
				break;
			case 1:
				playGame();
				break;
			case 2:
				endScreen();
				break;
		}
	}
	
	/**
	 * Checks whether mouse is clicked. Performs functions based on game state and what button is being pressed.
	 */
	@Override
	public void mousePressed() {
		// Play button in home screen
		if (state == 0 && playButton.getButtonOver()) {
			state = 1;
		}
		
		// Exit button in home screen
		else if (state == 0 && exitButton.getButtonOver()) {
			System.exit(0);
		}
		
		// Click to launch in game screen
		else if (state == 1 && launch == false) {
			launch = true;
		}
		
		// Play again button in end screen
		else if (state == 2 && playButton2.getButtonOver()) {
			state = 1;
			ship.clear();
			launch = false;
			initBodies();
			currentScore = 0;
			state = 1;
			newHigh = false;
		}
		
		// Main menu button on end screen
		else if (state == 2 && mainMenuButton.getButtonOver()) {
			state = 1;
			ship.clear();
			launch = false;
			initBodies();
			currentScore = 0;
			state = 0;
			newHigh = false;
		}
	}

	/**
	 * Displays home/menu screen. Includes the name of the game and buttons to exit and play.
	 * This is game state 0.
	 */
	public void menuScreen() {
		textSize(10);
		
		// Display Game Name
		fill(255, 255, 255);
		textAlign(CENTER);
		textSize(60);
		text("Gravity", (X_MAX / 2), (Y_MAX / 4));
		text("Assist", (X_MAX / 2), (Y_MAX / 4) + 50);
		
		// Display Buttons
		playButton.drawButton();
		exitButton.drawButton();
	}
	
	/**
	 * Displays the main game screen. This includes the home planet, target planet, star, 
	 * and black hole.
	 * This is game state 1.
	 */
	public void playGame() {
		textSize(10);
		
		// Gravity
		if (star.gRange(ship.getx(), ship.gety())) {
			// Check if ship in range
			star.showGRadius(100, 100, 100);
			// Add gravitational force to ship vectors
			ship.addForce(star.getGravity(ship.getx(), ship.gety(), ship.getMass()));
		}
		else {
			star.showGRadius(60, 60, 60);
		}
		
		// Launch
		if (launch == false && transition == false) {
			ship.aim(home.getx(), home.gety(), mouseX, mouseY, home.getr());
			stroke(255, 255, 255);
			line(home.getx(), home.gety(), mouseX, mouseY);
		}
		
		else if (launch == true) {
			ship.launch();
		}
		
		drawBodies();
		
		// Check if ship is out of bounds
		if (ship.getx() < 0 || ship.getx() > X_MAX 
				|| ship.gety() < 0 || ship.gety() > Y_MAX) {
			state = 2;
		}
		
		// Check if ship at black hole
		if (hole.getx() + hole.getr() >= ship.getx() && 
				hole.getx() - hole.getr() <= ship.getx() &&
				hole.gety() + hole.getr() >= ship.gety() &&
				hole.gety() - hole.getr() <= ship.gety()) {
			state = 2;
		}
		
		// Check if ship landed
		if (target.getx() + target.getr() >= ship.getx() && 
				target.getx() - target.getr() <= ship.getx() &&
				target.gety() + target.getr() >= ship.gety() &&
				target.gety() - target.getr() <= ship.gety()) {
			
			if (!transition) {
				currentScore++;
				if (currentScore > highScore) {
					highScore = currentScore;
					newHigh = true;
				}
			}
			
			// Start transition to next level
			launch = false;
			transition = true;
			initNextLevel();
		}	
		
		// Transition to the next level
		if (transition) {
			ship.drawPath();
			home.sety(home.gety() + TRANSITION_SPEED);
			target.sety(target.gety() + TRANSITION_SPEED);
			star.sety(star.gety() + TRANSITION_SPEED);
			hole.sety(hole.gety() + TRANSITION_SPEED);
			ship.vertShiftPath(TRANSITION_SPEED);
			
			nextTarget.sety(nextTarget.gety() + TRANSITION_SPEED);
			nextTarget.drawPlanet();
			nextStar.sety(nextStar.gety() + TRANSITION_SPEED);
			nextStar.drawStar();
			nextHole.sety(nextHole.gety() + TRANSITION_SPEED);
			nextHole.drawBlackHole();
		}

		// End transition
		if (target.gety() >= Y_MAX) {
			target.sety(Y_MAX);
			transition = false;

			home = target;
			target = nextTarget;
			star = nextStar;
			hole = nextHole;
			
			ship = new ShipBody(this, 500, 350, (Math.PI / 4));
		}
		
		// Display Current Score
		fill(255, 255, 255);
		textAlign(CENTER);
		textSize(50);
		text((currentScore), (X_MAX / 2), (100));
	}
		
	/**
	 * Displays game over screen. A box with a "Game Over" message, current and high scores, 
	 * and play again and menu buttons.
	 */
	public void endScreen() {
		drawBodies();
	
		// Draw Box
		int menuWidth = (X_MAX / 2);
		int menuHeight = (Y_MAX / 2);
		fill(MENU_COLOR);
		stroke(MENU_COLOR);
		rect(((X_MAX / 2) - (menuWidth / 2)),
				((Y_MAX / 2) - (menuHeight / 2)), 
				menuWidth, menuHeight, 28);
	
		// "Game Over" Message
		fill(255, 0, 0);
		textAlign(CENTER);
		textSize(40);
		text("GAME OVER", (X_MAX / 2), (Y_MAX / 4) - 20);
	
		// Display score
		fill(255, 255, 255);
		textAlign(CENTER);
		textSize(25);
		text(("SCORE\n" + currentScore), (X_MAX / 2), ((Y_MAX / 4) + 50));
		text(("HIGH\n" + highScore), (X_MAX / 2), ((Y_MAX / 4) + 120));
		
		// Display "New" if there is a new high score
		if (newHigh && highScore != 0) {
			fill(255, 0, 0);
			rect((X_MAX / 2) - 117, (Y_MAX / 4) + 99, 75, 25, 28);
			fill(255, 255, 255);
			text("NEW", (X_MAX / 2) - 80, ((Y_MAX / 4) + 120));
		}
		
		// Displays buttons
		playButton2.drawButton();
		mainMenuButton.drawButton();
		
		ship.stop();
	}	
		
	/**
	 * Initiates game objects. Creates new home planet, target planet, ship, star, and black hole.
	 */
	public void initBodies() {
		home = new PlanetBody(this, 0);
		target = new PlanetBody(this, 1);
		ship = new ShipBody(this, 500, 350, (Math.PI / 4));
		star = new StarBody(this, 150);
		hole = new BlackHole(this, target.getx(), target.gety(), home.getx(), home.gety());
	}
	
	/**
	 * Initiates next level. Creates a new target planet, star and black hole.
	 */
	public void initNextLevel() {
		nextTarget = new PlanetBody(this, 1);
		nextStar = new StarBody(this, 150);
		nextHole = new BlackHole(this, nextTarget.getx(), nextTarget.gety(), 
				target.getx(), Y_MAX);
		
		nextHole.sety(target.gety() - (Y_MAX - nextHole.gety()));
		nextTarget.sety(target.gety() - (Y_MAX - nextTarget.gety()));
		nextStar.sety(target.gety() - (Y_MAX - nextStar.gety()));
	}
	
	/**
	 * Displays game objects: star, home, target, and black hole.
	 */
	public void drawBodies() {
		star.drawStar();
		home.drawPlanet();
		target.drawPlanet();
		hole.drawBlackHole();
	}
	
	/*
	 * Nested Classes
	 */
	
	/**
	 * A class that creates and styles a button.
	 */
	public class Button {
		
		// Size/Position
		private PApplet canvas;
		private int posX, posY, x, y, height, width, col, r;
		private int heightHover, widthHover, colHover, textHover;
		private int textSize, colText, colHoverText;
		private boolean buttonHover = false;
		
		// Text
		private String text;

		/**
		 * Creates a theme button with the entered variables and no text.
		 * @param canvas
		 * @param posX
		 * @param posY
		 * @param height
		 * @param width
		 */
		public Button(PApplet canvas, int posX, int posY, int height, int width) {
			
			// Placement
			this.canvas = canvas;
			this.posX = posX;
			this.posY = posY;
			this.x = posX - (width / 2);
			this.y = posY - (height / 2);
			this.r = 28;
			
			// Size
			this.height = height;
			this.width = width;
			this.textSize = 20;
			
			double hoverScale = 1.2;
			this.widthHover = (int) (width * hoverScale);
			this.heightHover = (int) (height * hoverScale);
			this.textHover = (int) (textSize * hoverScale);
			
			//Colors
			col = canvas.color(168, 214, 255, 50);
			colText = canvas.color(255, 255, 255, 150);
			
			colHover = canvas.color(168, 214, 255, 80);
			colHoverText = canvas.color(255, 255, 255);	
		}
		
		/**
		 * Creates a theme button with the entered variables including text.
		 * @param canvas
		 * @param posX
		 * @param posY
		 * @param height
		 * @param width
		 * @param text
		 */
		public Button(PApplet canvas, int posX, int posY, int height, int width, String text) {
			this(canvas, posX, posY, height, width);
			this.text = text;
		}
		
		/**
		 * Draws and styles the button.
		 */
		public void drawButton() {
			update();
			
			int xOver = posX - (widthHover / 2);
			int yOver = posY - (heightHover / 2);
			
			// Hover
			if (buttonHover) {
				canvas.fill(colHover);
				canvas.stroke(colHover);
				canvas.rect(xOver, yOver, widthHover, heightHover, r);
				
				if (text != null) {
					canvas.fill(colHoverText);
					canvas.textSize(textHover);
					canvas.text(text, x + (width / 2), y + (height / 2) + 8);
				}
			}
			
			// No Hover
			else {
				canvas.fill(col);
				canvas.stroke(col);
				canvas.rect(x, y, width, height, r);
				
				if (text != null) {
					canvas.fill(colText);
					canvas.textSize(20);
					canvas.text(text, x + (width / 2), y + (height / 2) + 7);
				}
			}
		}
				
		/**
		 * Updates the status of the button (buttonHover)
		 */
		public void update() {
			if (overButton()) {
				buttonHover = true;
			}
			else {
				buttonHover = false;
			}
		}
		
		/**
		 * Checks if the cursor is hovering over the button.
		 * @return true if the cursor is over the button
		 */
		public boolean overButton() {
			if (canvas.mouseX >= x && canvas.mouseX <= x + width && 
					canvas.mouseY >= y && canvas.mouseY <= y + height) {
			    return true;
			} else {
				return false;
			}
		}
		
		/**
		 * @return buttonHover - The status of whether cursor is over the button
		 */
		public boolean getButtonOver() {
			return buttonHover;
		}
	}
}

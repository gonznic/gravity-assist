package spaceObjects;

import processing.core.PApplet;

/**
 * A class that creates a 2D space ship. 
 * The ship has velocity (a movement vector).
 * 
 * @author Nicolas Gonzalez
 *
 */
public class ShipBody extends SpaceBody{
	
	// Movement
	private double angle, xVect, yVect, launchFactor = 2.0;
	private float[][] path = new float[2000][4];
	
	//Gravity
	private int mass;
	
	//Size
	private float width;
	
	/**
	 * Creates a ship with the entered variables on the entered canvas.
	 * @param canvas
	 * @param x
	 * @param y
	 * @param angle - Where the ship is pointing (it can only move forward)
	 */
	public ShipBody(PApplet canvas, float x, float y, double angle) {
		this.canvas = canvas;
		this.x = x;
		this.y = y;
		this.width = 5;
		col = canvas.color(255, 255, 255);
		mass = 100;
	}
	
	/**
	 * Allows the user to determine what acceleration to apply 
	 * to the ship, which determines the ship's velocity.
	 * @param cx - Center Aim x
	 * @param cy - Center Aim y
	 * @param mx - Mouse x
	 * @param my - Mouse y
	 * @param planetR - Ship offset from Center Aim
	 */
	public void aim(float cx, float cy, float mx, float my, float planetR) {
		
		// Determine mouse angle from center.
		if (mx > cx) {
			this.angle = (Math.atan((cy - my)/(mx - cx)));
		}
		else {
			this.angle = ((Math.PI / 2) - (Math.atan((cy - my)/(cx - mx)))) + (Math.PI / 2);
		}
		
		//Determine ship location on planet
		x = cx + (float)(Math.cos(angle) * planetR);
		y = cy - (float)(Math.sin(angle) * planetR);
		
		// Draw ship
		canvas.stroke(col);
		canvas.fill(col);
		canvas.triangle(
				(float)((x) + (((Math.cos(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((y) - (((Math.sin(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + (((Math.cos(angle - (Math.PI / 2)))) * (width / 2))),  
				(float)((y) - (((Math.sin(angle - (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + ((Math.cos(angle)) * (width * 2))),
				(float)((y) - ((Math.sin(angle)) * (width * 2))));
		
		// Acceleration
		xVect = Math.cos(angle) * launchFactor;
		yVect = Math.sin(angle) * launchFactor;
	}
	
	/**
	 * Stops the ship.
	 */
	public void stop() {
		
		// Draw trajectory path
		canvas.stroke(255, 255, 255);
		drawPath();
		
		// Draw ship
		canvas.stroke(col);
		canvas.fill(col);
		canvas.triangle(
				(float)((x) + (((Math.cos(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((y) - (((Math.sin(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + (((Math.cos(angle - (Math.PI / 2)))) * (width / 2))),  
				(float)((y) - (((Math.sin(angle - (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + ((Math.cos(angle)) * (width * 2))),
				(float)((y) - ((Math.sin(angle)) * (width * 2))));
	}
	
	/**
	 * Launches ship. Adds acceleration making the ship move.
	 */
	public void launch() {
		
		// Determine angle.
		if (xVect > 0) this.angle = (Math.atan(yVect / xVect));
		else this.angle = (Math.atan(yVect / xVect)) - Math.PI;

		// Acceleration
		x += xVect;
		y -= yVect;
		
		// Flight path
		for (int i = 0; i < path.length; i++) {
		
			// Adds new segment
			if (path[i][0] == 0 && path[i][1] == 0 && 
					path[i + 1][0] == 0 && path[i + 1][1] == 0) {
				path[i][0] = x;
				path[i][1] = y;
				path[i][2] = (float)(x - xVect);
				path[i][3] = (float)(y + yVect);
				break;
			}
		}
		drawPath();
		
		// Draw ship
		canvas.stroke(col);
		canvas.fill(col);
		canvas.triangle(
				(float)((x) + (((Math.cos(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((y) - (((Math.sin(angle + (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + (((Math.cos(angle - (Math.PI / 2)))) * (width / 2))),  
				(float)((y) - (((Math.sin(angle - (Math.PI / 2)))) * (width / 2))), 
				(float)((x) + ((Math.cos(angle)) * (width * 2))),
				(float)((y) - ((Math.sin(angle)) * (width * 2))));
	}
	
	/**
	 * @return This ship's mass
	 */
	public int getMass() {
		return mass;
	}
	
	/**
	 * Adds the input vector to the ships current velocity. 
	 * To be used with gravitational pull.
	 * @param vector
	 */
	public void addForce(double[] vector) {
		xVect += vector[0];
		yVect -= vector[1];
	}
	
	/**
	 * Clears this ship's trajectory path.
	 */
	public void clear() {
		path = new float[2000][4];
	}
	
	/**
	 * Draws this ship's trajectory path.
	 */
	public void drawPath() {
		for (int i = 0; i < path.length; i++) {
			if (i % 10 >= 5) {
				canvas.stroke(255, 255, 255);
			}
			else {
				canvas.stroke(255, 255, 255, 0);
			}
			canvas.line(path[i][0], path[i][1], path[i][2], path[i][3]);
		}
	}
	
	/**
	 * Moves the path vertically by the entered y value.
	 * Used for level transition.
	 * @param y - Vertical translation value
	 */
	public void vertShiftPath(float y) {
		for (int i = 0; i < path.length; i++) {
			path[i][1] += y;
			path[i][3] += y;
		}
	}
}



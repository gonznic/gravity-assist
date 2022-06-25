package spaceObjects;

import processing.core.PApplet;

/**
 * A class that creates a 2D star. The star has a mass which determines 
 * its gravitational radius and force.
 * 
 * @author Nicolas Gonzalez
 *
 */
public class StarBody extends SpaceBody{
	private int gRadius; // Gravitational Radius
	private int mass;
	private int yPadding, xPadding = 100;
	private final double GCONSTANT = 0.02;

	/**
	 * Creates a star with the entered mass and a half random location on the entered canvas.
	 * @param canvas
	 * @param mass
	 */
	public StarBody(PApplet canvas, int mass) {
		//Size
		radius = 3;
		yPadding = 50 + gRadius;
		xPadding = 50 + gRadius;
		
		//Gravity
		this.mass = mass;
		gRadius = mass;
		
		// Location
		this.canvas = canvas;
		y = canvas.random((radius + yPadding),
				(canvas.height - (radius + yPadding) - 50));
		
		x = canvas.random((radius + xPadding),
				(canvas.width - (radius + xPadding)));
		
		// White
		col = canvas.color(255, 255, 255);
	}
	
	/**
	 * Draws this star. A circle.
	 */
	public void drawStar() {
		canvas.stroke(col);
		canvas.fill(col);
		canvas.ellipse(x, y, (radius * 2), (radius * 2));
	}
	
	/**
	 * Draws the perimeter of this object's gravitational range.
	 * Enter the color.
	 * @param r
	 * @param g
	 * @param b
	 */
	public void showGRadius(int r, int g, int b) {
		canvas.stroke(r, g, b);
		canvas.fill(0, 0, 0, 0);
		canvas.ellipse(x, y, (gRadius * 2), (gRadius * 2));
	}
	
	/**
	 * @return This object's mass
	 */
	public int getMass() {
		return mass;
	}
	
	/**
	 * Checks entered coordinates are within this object's gravitational range.
	 * @param x
	 * @param y
	 * @return true if entered coordinates are in this object's range
	 */
	public boolean gRange(float x, float y) {
		if ((float)Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) <= gRadius) {
			return true;
		}
		return false;
	}
	
	// Returns pull vector {x, y}
	/**
	 * Returns the gravitational force this object has on the entered object.
	 * @param x - X-Position of orbiting object
	 * @param y - Y-Position of orbiting object
	 * @param otherMass - Mass of orbiting object
	 * @return 2D gravitational pull vector in the form [x, y]
	 */
	public double[] getGravity(float x, float y, int otherMass) {
		double[] vector = new double[2];
		double angle = 0;
		double gRadius = (float)Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2));
		
		// Angle (Star center)
		angle = (Math.atan((this.y - y)/(this.x - x)));
		if (angle < 0) {
			angle = (2 * Math.PI) - angle;
		}	
		
		// Gravitational vector x
		if (x < this.x) {
			vector[0] = 1 * Math.cos(angle) * (GCONSTANT * ((mass * otherMass) / (gRadius * gRadius)));
		}
		else {
			vector[0] = -1 * Math.cos(angle) * (GCONSTANT * ((mass * otherMass) / (gRadius * gRadius)));
		}
		
		// Gravitational vector y
		if (y < this.y) {
			vector[1] = 1 * Math.sin(angle) * (GCONSTANT * ((mass * otherMass) / (gRadius * gRadius)));
		}
		else {
			vector[1] = -1 * Math.sin(angle) * (GCONSTANT * ((mass * otherMass) / (gRadius * gRadius)));
		}
		return vector;
	}
}

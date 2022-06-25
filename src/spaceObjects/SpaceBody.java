package spaceObjects;

import processing.core.PApplet;

/**
 * A class that represents an object in space. Every SpaceBody has x and y coordinates
 * 
 * @author Nicolas Gonzalez
 *
 */
public abstract class SpaceBody {
	protected PApplet canvas;
	protected float x, y;
	protected int col; // Color
	protected int radius; // influence radius (gravity)
	
	/**
	 * @return The X-Position of this object
	 */
	public float getx() {
		return x;
	}

	/**
	 * @return The Y-Position of this object
	 */
	public float gety() {
		return y;
	}
	
	/**
	 * @return The radius of this object
	 */
	public float getr() {
		return radius;
	}
	
	/**
	 * Moves the SpaceBody vertically by the entered y value.
	 * Used for level transition.
	 * @param y - Vertical translation value
	 */
	public void sety(float y) {
		this.y = y;
	}
}

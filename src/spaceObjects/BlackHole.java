package spaceObjects;

import processing.core.PApplet;

/**
 * A class that represents a black hole with no gravity.
 * 
 * @author Nicolas Gonzalez
 *
 */
public class BlackHole extends SpaceBody{
	
	/**
	 * Creates a black hole SpaceBody between the target planet and the home planet.
	 * This prevents straight shots from the home planet to the target planet.
	 * @param canvas
	 * @param x1 - Target x
	 * @param y1 - Target y
	 * @param x2 - Home x
	 * @param y2 - home y
	 */
	public BlackHole(PApplet canvas, float x1, float y1, float x2, float y2) {
		this.canvas = canvas;
		radius = (int)canvas.random(15, 25);
		col = canvas.color(255, 90, 0);
		x = x2 + ((x1 - x2) / 2);
		y = y1 + ((y2 - y1) / 2);
	}
	
	/**
	 * Draws a black hole, a thin orange circle.
	 */
	public void drawBlackHole() {
		canvas.stroke(col);
		canvas.fill(0, 0, 0, 0);
		canvas.ellipse(x, y, (radius * 2), (radius * 2));
	}
}

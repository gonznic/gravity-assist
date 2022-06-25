package spaceObjects;

import processing.core.PApplet;

/**
 * A Class that creates a planet SpaceBody. No gravity.
 * 
 * @author Nicolas Gonzalez
 *
 */
public class PlanetBody extends SpaceBody{
	
	/**
	 * Creates a planet of the entered type.
	 * Variations: 0 = home, 1 = target
	 * Home planet is always blue, target planet is random.
	 * @param canvas
	 * @param variation - 0 = home, 1 = target
	 */
	public PlanetBody(PApplet canvas, int variation) {
		this.canvas = canvas;
		int padding = 5;
	
		// Size and color
		if (variation == 0) {
			radius = 50;
			y = canvas.height;
			col = canvas.color(0, 0, 255);
		}
		if (variation == 1) {
			radius = (int)canvas.random(30, (canvas.height / 20));
			y = radius + padding;
			col = canvas.color(canvas.random(0, 255), 
					canvas.random(0, 255), canvas.random(0, 255));
		}
		
		// Position
		x = canvas.random((radius + padding),
				(canvas.width - (radius + padding)));
	}
	
	/**
	 * Draws this planet on the canvas.
	 */
	public void drawPlanet() {
		canvas.stroke(col);
		canvas.fill(col);
		canvas.ellipse(x, y, (radius * 2), (radius * 2));
	}	
}


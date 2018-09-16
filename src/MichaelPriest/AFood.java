package MichaelPriest;

import javafx.scene.shape.Circle;

/**This class represents the plants in the simulation that are eaten by the herbivores.
 * 
 * @author Michael Priest
 *
 */
public class AFood {
	
	
	private int x;
	private int y;
	/**The energy value is randomly generated and added to the
	 * herbivores total energy upon consumption
	 */
	private int calories;
	private Circle circle;
	/**Is true if the food is determined poison, there is a
	 * one in ten chance of this happening
	 */
	private boolean poison;
	
	
	public AFood() {
		this.circle = new Circle(8);
	}
	
	
	public AFood(int x, int y) {
		this.x = x;
		this.y = y;
		this.circle = new Circle(8);
	}
	
	
	public AFood(int x, int y, int calories) {
		this.x = x;
		this.y = y;
		this.circle = new Circle(8);
		if((int)randomWithRange(1,10) == 1) {
			this.poison = true;
			this.calories = -100;
		} else {
			this.poison = false;
			this.calories = calories;
		}
	}
	
	
	public boolean getPoison() {
		return this.poison;
	}
	
	
	public void setPoison(boolean p) {
		this.poison = p;
	}
	
	
	public Circle getCircle() {
		return this.circle;
	}
	
	
	public void setCircle(Circle c) {
		this.circle = c;
	}
	
	
	public void setX(int x) {
		this.x = x;
	}
	
	
	public int getX() {
		return this.x;
	}
	
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int getY() {
		return this.y;
	}
	
	
	public void setCalories(int calories) {
		this.calories = calories;
	}
	
	
	public int getCalories() {
		return this.calories;
	}
	
	
	public float randomWithRange(float min, float max)
	{
	   float range = (max - min) + 1;     
	   return (float)(Math.random() * range) + min;
	}
}


package MichaelPriest;

import javafx.scene.shape.Circle;

/**This class represents the rocks in the simulation, they do not move
 * and cannot be passed through by animals.
 * 
 * @author Michael Priest
 *
 */
public class ARock {
	
	
	private int x;
	private int y;
	private Circle circle;
	
	
	public ARock() {

	}
	
	
	public ARock(int x, int y) {
		this.x = x;
		this.y = y;
		this.circle = new Circle(16);
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
	
}

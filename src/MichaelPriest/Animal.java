package MichaelPriest;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**Abstract class that contains standard methods for the
 * subclasses Herbivore and Carnivore.
 * 
 * @author Michael Priest
 * @see Carnivore
 * @see Herbivore
 *
 */
abstract public class Animal {

	private String species;
	private String name;
	private String symbol;
	private int X;
	private int Y;
	private int energy;
	private float dx;
	private float dy;
	private Circle circle;
	private Label label;
	
	Animal(String sp, String n, String sy, int x, int y, int e) {
		this.species = sp;
		this.name = n;
		this.symbol = sy;
		this.X = x;
		this.Y = y;
		this.energy = e;
		this.circle = new Circle(16);
		this.dx = randomWithRange(-0.5f, 0.5f);
		this.dy = randomWithRange(-0.5f, 0.5f);
	}
	
	/**Returns a random number within a user determined range.
	 * Useful for generating random directions of movement upon construction.
	 * 
	 * @param min Minimum number
	 * @param max Maximum number
	 * @return float Randomised number
	 */
	public float randomWithRange(float min, float max)
	{
	   float range = (max - min) + 1;     
	   return (float)(Math.random() * range) + min;
	}
	
	public Label getLabel() {
		return this.label;
	}
	
	public void setLabel(Label l) {
		this.label = l;
	}
	
	public Circle getCircle() {
		return this.circle;
	}
	
	public void setCircle(Circle c) {
		this.circle = c;
	}
	
	public float getdx() {
		return this.dx;
	}
	
	public void setdx(float _dx) {
		this.dx = _dx;
	}
	
	public float getdy() {
		return this.dy;
	}
	
	public void setdy(float _dy) {
		this.dy = _dy;
	}
	
	public void setSpecies(String newSpecies) {
		this.species = newSpecies;
	}
	
	public String getSpecies() {
		return this.species;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSymbol(String newSymbol) {
		this.symbol = String.valueOf(newSymbol.charAt(0));
	}
	
	public String getSymbol() {
		return this.symbol;
	}
	
	public void setX(int newX) {
		this.X = newX;
	}
	
	public int getX() {
		return this.X;
	}
	
	public void setY(int newY) {
		this.Y = newY;
	}
	
	public int getY() {
		return this.Y;
	}
	
	public void setEnergy(int newEnergy) {
		if (newEnergy > 15) {
			newEnergy = 15;
		}
		this.energy = newEnergy;
	}
	
	public int getEnergy() {
		return this.energy;
	}
	
	/**Move in a direction specified by the enum class direction
	 * 
	 * @param world The world which the animal is within
	 * @param d Direction to move
	 */
	abstract public void move(World world, Direction d);
	
	/**Random move, no need for enum Direction class
	 * 
	 * @param world The world which the animal is within
	 */
	abstract public void randomMove(World world);
	
	/**Used to make animals eat food/other animals when in range
	 * 
	 * @param world The world which the animal is within
	 * @param foodIndex Index of the food
	 */
	abstract public void eat(World world, int foodIndex);
	
	/**Smell for food/other animals
	 * 
	 * @param world The world which the animal is within
	 * @param d Direction to smell in
	 * @return True if food is in specified direction and in range
	 */
	abstract public boolean smell(World world, Direction d);

}

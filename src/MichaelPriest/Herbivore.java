package MichaelPriest;

import java.util.Random;

/**Subclass representing a herbivore.
 * Contains methods that simulate a typical herbivore's behaviour
 * 
 * @author Michael Priest
 * @see Animal
 *
 */
public class Herbivore extends Animal {

	private static final int MAX_SMELLING_DISTANCE = 2;
	
	Herbivore(String sp, String n, String sy, int x, int y, int e) {
		super(sp, n, sy, x, y, e);
	}

	/**Tells a Herbivore to eat a specified AFood object
	 * 
	 * @see AFood
	 */
	@Override
	public void eat(World world, int foodIndex) {
		this.setEnergy(this.getEnergy() + world.getFoods().get(foodIndex).getCalories());
		if (this.getEnergy() > 20) {
			this.setEnergy(20);
		}
		System.out.println(this.getSymbol() + " energy = " + this.getEnergy());		
	}
	
	/**Smell for AFood objects in all enum Directions (NORTH, SOUTH, EAST, WEST)
	 * 
	 */
	@Override
	public boolean smell(World world, Direction d) {
		//smell each point in range
		for (int i = 0; i < this.MAX_SMELLING_DISTANCE; i++) {
			
			//check food list
			for (int j = 0; j < world.getFoods().size(); j++) {
				
				//check for matching co-ordinates
				if (this.getX() + d.getX() + (i*d.getX()) == world.getFoods().get(j).getX() && this.getY() + d.getY() + (i*d.getY()) == world.getFoods().get(j).getY()) {
					
					System.out.println(this.getName() + " smelt " + d);
					System.out.println("Food at: " + (this.getX() + d.getX() + (i*d.getX())) + ", " + (this.getY() + d.getY() + (i*d.getY())));
					return true;
				}
			}
		}
		return false;		
	}
	
	/**Move randomly if no food is in range
	 * 
	 */
	public void randomMove(World world) {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(8);
		int temp;
		
		switch (randomInt) {
			case 0: //East
				temp = (getX() + 1);
				if (!world.checkMove(temp, getY())) {
					this.randomMove(world);
					break;
				}
				this.setX(temp);
				//decrease energy
				this.setEnergy(this.getEnergy() - 1);
				break;
			case 1: //West
				temp = getX() - 1;
				if (!world.checkMove(temp, getY())) {
					this.randomMove(world);
					break;
				}
				this.setX(temp);
				//decrease energy
				this.setEnergy(this.getEnergy() - 1);
				break;
			case 2: //South
				temp = (getY() + 1);
				if (!world.checkMove(getX(), temp)) {
					this.randomMove(world);
					break;
				}
				this.setY(temp);
				//decrease energy
				this.setEnergy(this.getEnergy() - 1);
				break;
			case 3: //North
				temp = getY() - 1;
				if (!world.checkMove(getX(), temp)) {
					this.randomMove(world);
					break;
				}
				this.setY(temp);
				//decrease energy
				this.setEnergy(this.getEnergy() - 1);
				break;
			default: //No move
				System.out.println("no move");
				break;
		}
	}
	
	/**Move in the direction of food
	 * 
	 */
	public void move(World world, Direction d) {
		int temp;
		switch(d) {
			case NORTH:
				temp = (this.getY() - 1);
				if (!world.checkMove(this.getX(), temp)) {
					break;
				}
				this.setY(temp);
				break;
			case SOUTH:
				temp = (this.getY() + 1);
				if (!world.checkMove(this.getX(), temp)) {
					break;
				}
				this.setY(temp);
				break;
			case EAST:
				temp = (this.getX() + 1);
				if (!world.checkMove(temp, this.getY())) {
					break;
				}
				this.setX(temp);
				break;
			case WEST:
				temp = (this.getX() - 1);
				if (!world.checkMove(temp, this.getY())) {
					break;
				}
				this.setX(temp);
		}
	}
}

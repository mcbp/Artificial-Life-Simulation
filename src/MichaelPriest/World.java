package MichaelPriest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**Holds collections of critical data that the simulation needs to function,
 * including a list of herbivores, carnivores, food and rocks. 
 * 
 * @author Michael Priest
 * @see Carnivore
 * @see Herbivore
 * @see AFood
 * @see ARock
 *
 */
public class World {

	private int width;
	private int height;
	private int cycles;
	private int foodDensity;
	private int rockDensity;
	private ArrayList<Herbivore> herbs = new ArrayList<Herbivore>();
	private ArrayList<Carnivore> carns = new ArrayList<Carnivore>();
	private ArrayList<AFood> foods = new ArrayList<AFood>();
	private ArrayList<ARock> rocks = new ArrayList<ARock>();
	
	World(int w, int h, int c, int fd, int rd) {
		this.width = w;
		this.height = h;
		this.cycles = c;
		this.foodDensity = fd;
		this.rockDensity = rd;
	}
	
	public World(String fileName) {
		Config config = new Config();
		try {
			config.readFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.width = Integer.parseInt(config.getArrayData().get(0));		//width
		if (this.width > 1200) {
			this.width = 1200;
		}
		if (this.width < 300) {
			this.width = 300;
		}
		this.height = Integer.parseInt(config.getArrayData().get(1));		//height
		if (this.height > 900) {
			this.height = 900;
		}
		if (this.height < 300) {
			this.height = 300;
		}
		this.cycles = Integer.parseInt(config.getArrayData().get(2));		//cycles
		this.foodDensity = Integer.parseInt(config.getArrayData().get(3));	//food density
		this.rockDensity = Integer.parseInt(config.getArrayData().get(4));	//rock density
		//create herb or carn
		for (int i = 5; i < (config.getArrayData().size()-1); i = i + 7) {
			if (config.getArrayData().get(i+6).equals("herbivore")) {
				Herbivore herbivore = new Herbivore(config.getArrayData().get(i),						//species
													config.getArrayData().get(i+1),						//name
													config.getArrayData().get(i+2),						//symbol
													Integer.parseInt(config.getArrayData().get(i+3)),	//x pos
													Integer.parseInt(config.getArrayData().get(i+4)),	//y pos
													Integer.parseInt(config.getArrayData().get(i+5)));	//stamina
				//System.out.println(i + " herb");
				this.addHerb(herbivore);
			} else if (config.getArrayData().get(i+6).equals("carnivore")) {
				Carnivore carnivore = new Carnivore(config.getArrayData().get(i),						//species
													config.getArrayData().get(i+1),						//name
													config.getArrayData().get(i+2),						//symbol
													Integer.parseInt(config.getArrayData().get(i+3)),	//x pos
													Integer.parseInt(config.getArrayData().get(i+4)),	//y pos
													Integer.parseInt(config.getArrayData().get(i+5)));	//stamina
				//System.out.println(i + " carn");
				this.addCarn(carnivore);
			} else {
				
			}

		}
		this.generateWorld();
	}
	
	public World(int x, int y) {
		this.width = x;
		this.height = y;
		this.cycles = 10;
		this.foodDensity = 0;
		this.rockDensity = 0;
		this.generateWorld();
	}
	
	public World(int x, int y, int fd, int rd) {
		this.width = x;
		this.height = y;
		this.cycles = 10;
		this.foodDensity = fd;
		this.rockDensity = rd;
		this.generateWorld();
	}
	
	public World() {
		this.width = 900;
		this.height = 600;
		this.cycles = 10;
		this.foodDensity = 0;
		this.rockDensity = 0;
		this.generateWorld();
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getCycles() {
		return this.cycles;
	}
	
	public void setCycles(int c) {
		this.cycles = c;
	}
	
	public int getFoodDensity() {
		return this.foodDensity;
	}
	
	public int getRockdensity() {
		return this.rockDensity;
	}
	
	/**Add a Herbivore to ArrayList of Herbivores
	 * 
	 * @param animal Herbivore to add to list
	 * @see Herbivore
	 */
	public void addHerb(Herbivore animal) {
		this.herbs.add(animal);
	}
	
	/**Add a Carnivore to ArrayList of Carnivores
	 * 
	 * @param animal Carnivore to add to list
	 * @see Carnivore
	 */
	public void addCarn(Carnivore animal) {
		this.carns.add(animal);
	}
	
	/**Add a AFoods to ArrayList of AFoods
	 * 
	 * @param food AFood to add to list
	 * @see AFood
	 */
	public void addFoods(AFood food) {
		this.foods.add(food);
	}
	
	/**Add an ARock to ArrayList of ARocks
	 * 
	 * @param rock ARock to add to list
	 * @see ARock
	 */
	public void addRocks(ARock rock) {
		this.rocks.add(rock);
	}
	
	public ArrayList<AFood> getFoods() {
		return this.foods;
	}
	
	public ArrayList<ARock> getRocks() {
		return this.rocks;
	}
	
	public ArrayList<Herbivore> getHerbs() {
		return this.herbs;
	}
	
	public ArrayList<Carnivore> getCarns() {
		return this.carns;
	}
	
	public int getRandomWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
	/**Initialises all objects in the world.
	 * Sets random positions for food and rocks.
	 * Places animals at their specified co-ordinates
	 * 
	 */
	public void generateWorld() {
		
	int rndIntY = 0;
	int rndIntX = 0;
	int rndIntFood = 0;
	int rndRockY = 0;
	int rndRockX = 0;
	AFood tempAFood;
	ARock tempRock;
	Animal tempAnimal;
	
	//ROCKS
	for (int i = 0; i < this.rockDensity; i++) {
		
		//Random Y
		rndRockY = this.getRandomWithRange(80, this.getHeight()-81);
		//Random X
		rndRockX = this.getRandomWithRange(50, this.getWidth()-51);
		
		if (i == 0) {
			Animal tempAnimalZero;
			
			for (int z = 0; z < this.herbs.size(); z++) {
				tempAnimalZero = this.herbs.get(z);
			
				while (rndIntX == (int)tempAnimalZero.getX() && rndIntY == (int)tempAnimalZero.getY()) {
					//System.out.println("-- BUG " + rndIntX + " " + rndIntY + " BUG --");
					//Random Y
					rndRockY = this.getRandomWithRange(80, this.getHeight()-81);
					//Random X
					rndRockX = this.getRandomWithRange(50, this.getWidth()-51);
					z = 0;
				}
			}
		}
		
		//stop rock being placed on rock
		//cycle through rocks
		for (int m = 0; m < this.rocks.size(); m++) {
			
			//set random
			rndRockX = this.getRandomWithRange(50, this.getWidth()-51);
			rndRockY = this.getRandomWithRange(80, this.getHeight()-81);
		
			//compare against other rocks
			for (int n = 0; n < this.rocks.size(); n++) {
				tempRock = this.rocks.get(n);

				if (rndRockX == (int)tempRock.getX() && rndRockY == (int)tempRock.getY()) {
					//restart if rock is in list
					m = m - 1;
					//System.out.println("-- ROCK " + rndRockX + " " + rndRockY + " ROCK --");
				} else {
					
					//bug test
					for (int p = 0; p < this.herbs.size(); p++) {
						tempAnimal = this.herbs.get(p);
						
						if (rndRockX == (int)tempAnimal.getX() && rndRockY == (int)tempAnimal.getY()) {
							//restart if bug is in list
							m = m - 1;
							//System.out.println("-- BUG " + rndRockX + " " + rndRockY + " BUG --");
						}
					}
				}
			}
		}
		//Create ARock object
		this.addRocks(new ARock(rndRockX, rndRockY));
		//System.out.println((int)this.rocks.get(i).getX() + " " + (int)this.rocks.get(i).getY());
		//System.out.println();
	}
	
	//FOOD
	for (int i = 0; i < this.foodDensity; i++) {
		
		//Random Y
		rndIntY = this.getRandomWithRange(80, this.getHeight()-81);
		//Random X
		rndIntX = this.getRandomWithRange(50, this.getWidth()-51);
		//Random food calorie
		rndIntFood = this.getRandomWithRange(1, 9);
		
		if (i == 0) {
			ARock tempRockZero;
			
			for (int z = 0; z < this.rocks.size(); z++) {
				tempRockZero = this.rocks.get(z);
			
				while (rndIntX == (int)tempRockZero.getX() && rndIntY == (int)tempRockZero.getY()) {
					//System.out.println("-- ROCK " + rndIntX + " " + rndIntY + " ROCK --");
					//Random Y
					rndIntY = this.getRandomWithRange(80, this.getHeight()-81);
					//Random X
					rndIntX = this.getRandomWithRange(50, this.getWidth()-51);
					//Random food calorie
					rndIntFood = this.getRandomWithRange(1, 9);
					z = 0;
				}
			}
		}

		//stop food being placed on food
		for (int m = 0; m < this.foods.size(); m++) {
			
			//set random
			rndIntX = this.getRandomWithRange(50, this.getWidth()-51);
			rndIntY = this.getRandomWithRange(80, this.getHeight()-81);
			rndIntFood = this.getRandomWithRange(1, 9);
			
			for (int n = 0; n < this.foods.size(); n++) {
				tempAFood = this.foods.get(n);
				
				if (m == -1) {
					//do nothing
				} else {
					if (rndIntX == (int)tempAFood.getX() && rndIntY == (int)tempAFood.getY()) {
						//restart if food is in list
						m = m - 1;
						//System.out.println("-- FOOD " + rndIntX + " " + rndIntY + " FOOD --");
					} else {
					
						//Compare against rocks
						for (int p = 0; p < this.rocks.size(); p++) {
							tempRock = this.rocks.get(p);
							
							if (rndIntX == (int)tempRock.getX() && rndIntY == (int)tempRock.getY()) {
								//restart if rock is occupying
								m = m - 1;
								//System.out.println("-- ROCK " + rndIntX + " " + rndIntY + " ROCK --");
							}
						}
					}
				}
			}
		}
		//Create AFood object
		this.addFoods(new AFood(rndIntX, rndIntY, rndIntFood));
		//System.out.println((int)this.foods.get(i).getX() + " " + (int)this.foods.get(i).getY());
		//System.out.println(this.foods.get(i).getCalories() + " calories");
		//System.out.println();
		}
	}
	
	/**Displays the world in the console using ASCII characters.
	 * Not used for the JavaFX application.
	 */
	public void displayWorld() {
		Herbivore tempHerb;
		Carnivore tempCarn;
		AFood tempFood;
		ARock tempRock;
		String tempPrint = ". ";
		//rows
		for (int i = 0; i < height; i++) {						
			//columns
			for (int j = 0; j < width; j++) {
		        
				//mark food or empty
		        for (int l = 0; l < this.foods.size(); l++) {
		        	tempFood = this.foods.get(l);
		        	if (tempFood.getX() == j && tempFood.getY() == i) {
		        		tempPrint = String.valueOf(tempFood.getCalories()) + " ";
		        		l = this.foods.size();
		        	} else {	
		        		tempPrint = ". ";
		        	}
		        }
		        //if food list is empty the above for loop won't trigger
		        if (this.foods.size() == 0) {
		        	tempPrint = ". ";
		        }
		        //mark rock
		        for (int q = 0; q < this.rocks.size(); q++) {
		        	tempRock = this.rocks.get(q);
		        	if (tempRock.getX() == j && tempRock.getY() == i) {
		        		tempPrint = "X ";
		        		q = this.rocks.size();
		        	}
		        }
		        //mark herbs
		        for (int k = 0; k < this.herbs.size(); k++) {
		        	tempHerb = this.herbs.get(k);
		        	if (tempHerb.getX() == j && tempHerb.getY() == i) {
		        		tempPrint = tempHerb.getSymbol() + " ";
		        		k = this.herbs.size();
		        	}
		        }
		        
		        for (int k = 0; k < this.carns.size(); k++) {
		        	tempCarn = this.carns.get(k);
		        	if (tempCarn.getX() == j && tempCarn.getY() == i) {
		        		tempPrint = tempCarn.getSymbol() + " ";
		        		k = this.carns.size();
		        	}
		        }
		        System.out.print(tempPrint);
			}
	        System.out.println();
		}
	}
	
	/**Check if move is viable by checking co-ordinates that animal will move into
	 * Not used for the JavaFX application.
	 *  
	 * @param x Destination x co-ordinate
	 * @param y Destination y co-ordinate
	 * @return True if move is viable
	 */
	public boolean checkMove(int x, int y) {
		//Check x boundary
		if (x > (this.width-1) || x < 0) {
			return false;
		}
		//Check y boundary
		if ( y > (this.height-1) || y < 0) {
			return false;
		}
		//Check for other herbs
		Herbivore tempHerb;
		for (int j = 0; j < this.herbs.size(); j++) {
			tempHerb = this.herbs.get(j);
       		if(x == tempHerb.getX() && y == tempHerb.getY()) {
				return false;
			}
		}
		//Check for other carns
		Carnivore tempCarn;
		for (int j = 0; j < this.getCarns().size(); j++) {
			tempCarn = this.getCarns().get(j);
       		if(x == tempCarn.getX() && y == tempCarn.getY()) {
				return false;
			}
		}
		//Check for rocks
		ARock tempRock;
		for (int j = 0; j < this.rocks.size(); j++) {
			tempRock = this.rocks.get(j);
			if (x == tempRock.getX() && y == tempRock.getY()) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean checkMoveCarn(int x, int y) {
		//Check x boundary
		if (x > (this.getWidth()-1) || x < 0) {
			return false;
		}
		//Check y boundary
		if ( y > (this.getHeight()-1) || y < 0) {
			return false;
		}
		//Check for other carns
		Carnivore tempCarn;
		for (int j = 0; j < this.getCarns().size(); j++) {
			tempCarn = this.getCarns().get(j);
       		if(x == tempCarn.getX() && y == tempCarn.getY()) {
				return false;
			}
		}
		//Check for rocks
		ARock tempRock;
		for (int j = 0; j < this.getRocks().size(); j++) {
			tempRock = this.getRocks().get(j);
			if (x == tempRock.getX() && y == tempRock.getY()) {
				return false;
			}
		}
		return true;
	}
	
	
	public void herbCheckFood(Herbivore animal) {
		if (this.getFoods().size() != 0) {
			//Cycle food list
			for (int i = 0; i < this.getFoods().size(); i++) {
				//Check co-ordinates
				if (animal.getX() == (int)this.getFoods().get(i).getX() && animal.getY() == (int)this.getFoods().get(i).getY()) {
					//eat food
					animal.eat(this, i);
					//remove food
					this.getFoods().remove(i);
				}
			}
		}
	}
	
	
	public void carnCheckFood(Carnivore animal) {
		if (this.getHerbs().size() != 0) {
			//Cycle food list
			for (int i = 0; i < this.getHerbs().size(); i++) {
				//Check co-ordinates
				if (animal.getX() == (int)this.getHerbs().get(i).getX() && animal.getY() == (int)this.getHerbs().get(i).getY()) {
					//eat food
					animal.eat(this, i);
					//remove food
					this.getHerbs().remove(i);
				}
			}
		}
	}
	
	/**Check if animal is dead, used once every cycle.
	 * 
	 */
	public void checkDeath() {
		for (int i = 0; i < this.getHerbs().size(); i++) {
			if (this.getHerbs().get(i).getEnergy() < 1) {
				this.getHerbs().remove(i);
			}
		}
		for (int i = 0; i < this.getCarns().size(); i++) {
			if (this.getCarns().get(i).getEnergy() < 1) {
				this.getCarns().remove(i);
			}
		}
	}
	
	/**Cycles the world once, allowing all animals to move one space
	 * and eat food they are in contact with.
	 * 
	 * @return true if cycle was successful
	 */
	public boolean cycleWorld() {
		ArrayList<Direction> tempDirList = new ArrayList<Direction>();
		if (this.getCycles() != 0) {
			//for each herbivore
			for (int i = 0; i < this.getHerbs().size(); i++) {
				//smell food
				for (Direction d : Direction.values()) {
					if (this.getHerbs().get(i).smell(this, d)) {
						tempDirList.add(d);
					}
				}
				//move
				if (tempDirList.size() == 0) {
					//random move
					this.getHerbs().get(i).randomMove(this);
					System.out.println(this.getHerbs().get(i).getName() + " moved to " + String.valueOf(this.getHerbs().get(i).getX()) + " " + String.valueOf(this.getHerbs().get(i).getY()));
				} else {
					//towards food move
					this.getHerbs().get(i).move(this, tempDirList.get(this.getRandomWithRange(0, (tempDirList.size()-1))));
					this.herbCheckFood(this.getHerbs().get(i));
				}
			}
			tempDirList.clear();
			//for each carnivore
			for (int i = 0; i < this.getCarns().size(); i++) {
				//smell herbs
				for (Direction d : Direction.values()) {
					if (this.getCarns().get(i).smell(this, d)) {
						tempDirList.add(d);
					}
				}
				//move
				if (tempDirList.size() == 0) {
					//random move
					this.getCarns().get(i).randomMove(this);
					System.out.println(this.getCarns().get(i).getName() + " moved to " + String.valueOf(this.getCarns().get(i).getX()) + " " + String.valueOf(this.getCarns().get(i).getY()));
				} else {
					//towards herb move
					this.getCarns().get(i).move(this, tempDirList.get(this.getRandomWithRange(0, (tempDirList.size()-1))));
					this.carnCheckFood(this.getCarns().get(i));
				}
			}
			this.checkDeath();
			this.displayWorld();
			System.out.println("------------------------------");
			this.setCycles(this.getCycles() - 1);;
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}

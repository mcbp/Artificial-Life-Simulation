package MichaelPriest;


import java.util.Random;

/**enum class that contains values for NORTH, SOUTH, EAST and WEST.
 * 
 * @author Michael Priest
 *
 */
public enum Direction {
	
	NORTH	(0, -1),
	SOUTH	(0, 1),
	EAST	(1, 0),
	WEST	(-1, 0);
	
	
	private int x;
	private int y;
	
	
	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public int getX() {
		return this.x;
	}
	
	
	public int getY() {
		return this.y;
	}
	
	
    private static Random rnd = new Random();

    /**Gets a random enum Direction
     * 
     * @return Random Direction
     */
    static public Direction randomDirection() {
        return Direction.values()[rnd.nextInt(4)];
    }
}

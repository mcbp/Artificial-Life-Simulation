package MichaelPriest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**This class handles all the files used in the application, either when files are being written (saving) or read (loading).
 * 
 * <p>Text files (.txt) are used as the file type for saving/loading in the format below:</p>
 * <ul>
 * <li>World Width</li>
 * <li>World Height</li>
 * <li>Cycles</li>
 * <li>Food Density</li>
 * <li>Rock Density</li>
 * <li>Animal1 Species</li>
 * <li>Animal1 Name</li>
 * <li>Animal1 Symbol</li>
 * <li>Animal1 X Position</li>
 * <li>Animal1 Y Position</li>
 * <li>Animal1 Energy</li>
 * <li>Animal1 Type</li>
 * <li>Animal2 Species</li>
 * <li>etc...</li>
 * </ul>
 * 
 * 
 * @author Michael Priest
 *
 */
public class Config {

	/**This is a list of all the contents of a loaded file
	 * 
	 */
	private ArrayList<String> readArray = new ArrayList<String>();
	
	public ArrayList<String> getArrayData() {
		return this.readArray;
	}
	
	/**Method to read file that configure the world
	 * 
	 * @param fileName Name of file to read
	 * @throws IOException If file not found
	 */
	public void readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
		    String line = br.readLine();
		    //read line by line
		    while (line != null) {
		        this.readArray.add(line);
		        line = br.readLine();
		    }
		} finally {
			System.out.println("reading " + fileName);
		    br.close();
		}
	}
	
	/**Method to write to the file if user asks for their configuration to be saved
	 * 
	 * @param w Refernces the world with data to save
	 * @param fileName Name to save the file as
	 * @throws IOException If file not found
	 */
	public void writeFile(World w, String fileName) throws IOException {
		
		System.out.println("writeFile " + fileName);
		
		//create or overwrite file
		File f = new File(fileName);
		if(!f.exists()) {
			f.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
		//write world info
		bw.write(Integer.toString(w.getWidth()));
		bw.newLine();
		bw.write(Integer.toString(w.getHeight()));
		bw.newLine();
		bw.write(Integer.toString(w.getCycles()));
		bw.newLine();
		bw.write(Integer.toString(w.getFoods().size()));
		bw.newLine();
		bw.write(Integer.toString(w.getRocks().size()));
		bw.newLine();
		
		//write life info herbs
		for (int i = 0; i < w.getHerbs().size(); i++) {
			String id = Integer.toString(i+1);
			bw.write("HSpecies" + id);
			bw.newLine();
			bw.write("HName" + id);
			bw.newLine();
			bw.write("H");
			bw.newLine();
			int x = (int)w.getHerbs().get(i).getCircle().getCenterX() + (int)w.getHerbs().get(i).getCircle().getTranslateX();
			if (x == 0) {
				bw.write(Integer.toString(w.getHerbs().get(i).getX()));
			} else {
				bw.write(Integer.toString(x));
			}
			bw.newLine();
			int y = (int)w.getHerbs().get(i).getCircle().getCenterY() + (int)w.getHerbs().get(i).getCircle().getTranslateY();
			if (y == 0) {
				bw.write(Integer.toString(w.getHerbs().get(i).getY()));
			} else {
				bw.write(Integer.toString(y));
			}
			bw.newLine();
			bw.write(Integer.toString(w.getHerbs().get(i).getEnergy()));
			bw.newLine();
			bw.write("herbivore");
			bw.newLine();
		}
		
		//write life info carns
		for (int i = 0; i < w.getCarns().size(); i++) {
			String id = Integer.toString(i+1);
			bw.write("CSpecies" + id);
			bw.newLine();
			bw.write("CName" + id);
			bw.newLine();
			bw.write("C");
			bw.newLine();
			int x = (int)w.getCarns().get(i).getCircle().getCenterX() + (int)w.getCarns().get(i).getCircle().getTranslateX();
			if (x == 0) {
				bw.write(Integer.toString(w.getCarns().get(i).getX()));
			} else {
				bw.write(Integer.toString(x));
			}
			bw.newLine();
			int y = (int)w.getCarns().get(i).getCircle().getCenterY() + (int)w.getCarns().get(i).getCircle().getTranslateY();
			if (y == 0) {
				bw.write(Integer.toString(w.getCarns().get(i).getY()));
			} else {
				bw.write(Integer.toString(y));
			}
			bw.newLine();
			bw.write(Integer.toString(w.getCarns().get(i).getEnergy()));
			bw.newLine();
			bw.write("carnivore");
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}

}

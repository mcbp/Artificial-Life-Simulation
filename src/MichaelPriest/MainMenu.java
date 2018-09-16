package MichaelPriest;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Main class, implements the JavaFX library to create a menu to allow the user to interact
 * with the application.
 * 
 * @author Michael Priest
 *
 */
public class MainMenu extends Application {
	
	
	protected Stage window;
	/**The JFX contains all the animation for the simulation
	 * and this variable provides a link to that.
	 */
	protected JFX jfx;
	protected World world;
	protected String loadedFile;
	protected Timeline susLine;
	protected Scene scene;
	/**The root of the scene. Two elements are placed in the VBox,
	 * the MenuBar at the top then the pane which contains all
	 * the animation below
	 */
	protected VBox root;
	
	/**Initialisation method, creates the MenuBar and root element and displays the to the user.
	 * All MenuItems will now have functionality 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		loadedFile = "config.txt";
		world = new World("config.txt");
		
		root = new VBox();
		
		scene = new Scene(root, world.getWidth(), world.getHeight());
		primaryStage.setResizable(false);
		
		MenuBar menuBar = new MenuBar();
		root.getChildren().add(menuBar);
		
		//File
		Menu menuFile = new Menu("File");
		MenuItem newConfig = new MenuItem("New configuration");
		newConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.setStop(true);
				}
				loadedFile = "new";
				menuNotify("New configuration selected");
			}
		});

		MenuItem openConfig = new MenuItem("Open configuration");
		openConfig.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if(jfx != null) {
					jfx.setStop(true);
				}
				TextInputDialog openDialog = new TextInputDialog();
				openDialog.setTitle("Open");
				openDialog.setContentText("File name:");
				openDialog.setHeaderText("Open Configuration File");
				Optional<String> result;
				do {
					result = openDialog.showAndWait();
				} while (!checkFile(result.get()));
				if (result.isPresent()){
					loadedFile = result.get();
					menuNotify("Loaded " + loadedFile);
				}
				if(jfx != null) {
					jfx.setStop(false);
				}
				System.out.println(loadedFile);
				world = new World(loadedFile);
			}
		});
		MenuItem save = new MenuItem("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				Config config = new Config();
				try {
					config.writeFile(world, loadedFile);
					if (jfx != null) {
						String mapInfoString = 	"Saved as " + loadedFile + "\n";
						jfx.addJFXLabel(mapInfoString, 20, 3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		MenuItem saveAs = new MenuItem("Save as");
		saveAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				TextInputDialog openDialog = new TextInputDialog();
				openDialog.setTitle("Save as");
				openDialog.setContentText("File name: (include .txt)");
				openDialog.setHeaderText("Save configuration file");
				Optional<String> result;
				do {
					result = openDialog.showAndWait();
				} while (!checktxt(result.get()));
				if (result.isPresent()){
					Config config = new Config();
					try {
						config.writeFile(world, result.get());
						if (jfx != null) {
							String mapInfoString = 	"Saved as " + result.get() + "\n";
							jfx.addJFXLabel(mapInfoString, 20, 3);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					Config config = new Config();
					try {
						config.writeFile(world, "config.txt");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
		menuFile.getItems().addAll(newConfig, openConfig, save, saveAs, new SeparatorMenuItem(), exit);

		
		
		//View
		Menu menuView = new Menu("View");
		MenuItem lifeInfo = new MenuItem("Life form info");
		lifeInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					String lifeInfoString = "Herbivores: " + Integer.toString(world.getHerbs().size()) + "\n" +
											"Carnivores: " + Integer.toString(world.getCarns().size()) + "\n ";
					try {
						jfx.addJFXLabel(lifeInfoString, 20 ,3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		MenuItem mapInfo = new MenuItem("Map info");
		mapInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					String mapInfoString = 	"Map size: " + Integer.toString(world.getWidth()) + "x" + Integer.toString(world.getHeight()) + "\n" +
											"Number of Plants: " + Integer.toString(world.getFoods().size()) + "\n" +
											"Number of Rocks: " + Integer.toString(world.getRocks().size()) + "\n ";
					try {
						jfx.addJFXLabel(mapInfoString, 20, 3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		menuView.getItems().addAll(lifeInfo, mapInfo);

		
		
		//Edit Life
		Menu menuEditLife = new Menu("Edit Life");
		CheckMenuItem sustain = new CheckMenuItem("Self-sustain");
		sustain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					if (sustain.isSelected()) {
						if (jfx != null) {
							KeyFrame kf = new KeyFrame(Duration.millis(16),
									new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent t) {
											
											if(world.getCarns().size() < 2) {
												jfx.addCarn(world);
											}
											if(world.getHerbs().size() < 8) {
												jfx.addHerb(world);
											}
											if(world.getFoods().size() < 20) {
												jfx.addFood(world);
											}
											
										}
									});
							susLine = TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(kf).build();;
							susLine.play();
						}
					} else {
						susLine.stop();
					}
				} else {
					sustain.setSelected(false);
				}
			}
		});
		MenuItem addC = new MenuItem("Add carnivore");
		addC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.addCarn(world);
				}
			}
		});
		MenuItem addTenC = new MenuItem("Add 10 carnivores");
		addTenC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					for (int m = 0; m < 10; m++) {
						jfx.addCarn(world);
					}
				}
			}
		});
		MenuItem removeC = new MenuItem("Remove carnivore");
		removeC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null && world.getCarns().size() > 0) {
					jfx.removeCarn(world);
				}
			}
		});
		MenuItem addH = new MenuItem("Add herbivore");
		addH.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.addHerb(world);
				}
			}
		});
		MenuItem addTenH = new MenuItem("Add 10 herbivores");
		addTenH.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					for (int m = 0; m < 10; m++) {
						jfx.addHerb(world);
					}
				}
			}
		});
		MenuItem removeH = new MenuItem("Remove herbivore");
		removeH.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null && world.getHerbs().size() > 0) {
					jfx.removeHerb(world);
				}
			}
		});
		menuEditLife.getItems().addAll(sustain, new SeparatorMenuItem(), addC, addTenC, removeC,  new SeparatorMenuItem(), addH, addTenH, removeH);

		
		
		//Edit World
		Menu menuEditWorld = new Menu("Edit World");
		MenuItem addF = new MenuItem("Add food");
		addF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.addFood(world);
				}
			}
		});
		MenuItem addTenF = new MenuItem("Add 10 food");
		addTenF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					for (int m = 0; m < 10; m++) {
						jfx.addFood(world);
					}
				}
			}
		});
		MenuItem removeF = new MenuItem("Remove food");
		removeF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null && world.getFoods().size() > 0) {
					jfx.removeFood(world);
				}
			}
		});
		MenuItem addR = new MenuItem("Add rock");
		addR.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.addRock(world);
				}
			}
		});
		MenuItem addTenR = new MenuItem("Add 10 rocks");
		addTenR.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					for (int m = 0; m < 10; m++) {
						jfx.addRock(world);
					}
				}
			}
		});
		MenuItem removeR = new MenuItem("Remove rock");
		removeR.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null && world.getFoods().size() > 0) {
					jfx.removeRock(world);
				}
			}
		});
		menuEditWorld.getItems().addAll(addF, addTenF, removeF, new SeparatorMenuItem(), addR, addTenR, removeR);

		
		
		//Simulation
		Menu menuSim = new Menu("Simulation");
		MenuItem run = new MenuItem("Run/Reset");
		run.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				//check for file name
				if (checkFile(loadedFile)) {
					System.out.println("found file");
					try {
						//new sim/jfx if one dooesn't exist yet, e.g first time using upon open
						if (jfx == null) {
							world = new World(loadedFile);
							root = new VBox();
							root.getChildren().add(menuBar);
							scene = new Scene(root, world.getWidth(), world.getHeight());
							jfx = new JFX(primaryStage, scene, world, root);
						} else {
							jfx.setStop(true);
							world = new World(loadedFile);
							root = new VBox();
							root.getChildren().add(menuBar);
							scene = new Scene(root, world.getWidth(), world.getHeight());
							jfx = new JFX(primaryStage, scene, world, root);
							jfx.setStop(false);
						}
						System.out.println("running " + loadedFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (loadedFile.equals("new")) {
					world = new World();
					root = new VBox();
					root.getChildren().add(menuBar);
					scene = new Scene(root, world.getWidth(), world.getHeight());
					try {
						jfx = new JFX(primaryStage, scene, world, root);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					menuNotify("No file selected");
				}
				
			}
		});
		MenuItem stop = new MenuItem("Stop");
		stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					jfx.setStop(true);
				}
				world = new World(loadedFile);
			}
		});
		MenuItem pause = new MenuItem("Pause/Restart");
		pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					if (jfx.getPause()) {
						jfx.setPause(false);
					} else if (!jfx.getPause()){
						jfx.setPause(true);
					}
				}
			}
		});
		CheckMenuItem iteration = new CheckMenuItem("Display Iterations");
		iteration.setSelected(true);
		iteration.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					if (iteration.isSelected()) {
						jfx.setInvisible(true);
					} else {
						jfx.setInvisible(false);
					}
				}
			}
		});
		menuSim.getItems().addAll(run, stop, pause, iteration);
		/*CheckMenuItem sustain = new CheckMenuItem("Self-Sustain");
		sustain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (sustain.isSelected()) {*/

		
		
		//About
		Menu menuAbout = new Menu("About");
		MenuItem appInfo = new MenuItem("Application info");
		appInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					String mapInfoString = 	"This application simulates artificial life, more specifically " + "\n" +
											"it simulates how herbivores and carnivores interact" + "\n" +
											"with each other in a closed environment" + "\n ";
					try {
						jfx.addJFXLabel(mapInfoString, 20, 5);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		MenuItem authorInfo = new MenuItem("Author info");
		authorInfo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				if (jfx != null) {
					String mapInfoString = 	"Created by Michael Priest at Reading University 2015" + "\n ";
					try {
						jfx.addJFXLabel(mapInfoString, 20, 3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		menuAbout.getItems().addAll(appInfo, authorInfo);

		
		menuBar.getMenus().addAll(menuFile, menuView, menuEditLife, menuEditWorld, menuSim, menuAbout);
		
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	/**Add a tempoary label to the root to notify the user of key infomation
	 * like loading configuration, creating new configurations, etc
	 * 
	 * @param message Message of the notification
	 */
	public void menuNotify(String message) {
		Label label = new Label(message);
		label.setFont(Font.font(null, FontWeight.BOLD, 20));
		label.setTranslateX(30);
		label.setTranslateY(10);
		root.getChildren().add(label);
		FadeTransition fade = new FadeTransition(Duration.seconds(0.5), label);
		fade.setDelay(Duration.seconds(2));
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.play();
		fade.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().remove(label);
			}
		});
	}
	
	/**Check if file exists
	 * 
	 * @param fileName Name of file to check
	 * @return True if file exists
	 */
	public boolean checkFile(String fileName) {
		File f = new File(fileName);
		if(f.exists()) {
			//exists
			return true;
		} else {
			//does not exist
			return false;
		}
	}
	
	/**Check that file extension is .txt,
	 * used when user inputs a save name.
	 * 
	 * @param fileName Name to check
	 * @return True if valid
	 */
	public boolean checktxt(String fileName) {
		int length = fileName.length();
		if (length < 5) {
			return false;
		}
		char dot = fileName.charAt(length-4);
		char t1 = fileName.charAt(length-3);
		char x = fileName.charAt(length-2);
		char t2 = fileName.charAt(length-1);
		
		if (dot == '.' && t1 == 't' && x == 'x' && t2 == 't') {
			return true;
		} else {
			return false;
		}
	}
	
	public void resetWorld(World world) {
		world = new World("config.txt");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

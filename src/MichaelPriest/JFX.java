package MichaelPriest;


import java.awt.geom.Point2D;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**This class handles the simulation and all the animation used in the main pane.
 * 
 * @author Michael Priest
 *
 */
public class JFX extends Application {
	
	/**Pauses animation if false
	 */
	boolean pause;
	/**Stops animation if false
	 */
	boolean stop;
	private long holdTime = 0;
	private long holdTimeCarn = 0;
	/**Timeline for the main animation
	 */
	private Timeline animation;
	private Pane pane;
	private StackPane stackMessage;
	private VBox vboxMessage;
	/**Positive bound for max animals can move at
	 */
	private float upperSpeed = 0.5f;
	/**Negative bound for max velocity animals can move at
	 */
	private float lowerSpeed = -0.5f;

	public JFX(Stage stage, Scene scene, World world, VBox root) throws Exception {
		this.stop = false;
		this.pause = false;
		start(stage, scene, world, root);
	}
	
	/**Main animation is contained within this method.
	 * 
	 * @param primaryStage Stage where animation is displayed
	 * @param primaryScene Scene where animation is displayed
	 * @param world World to be simulated and animated
	 * @param root Root object of the scene
	 * @throws Exception Runtime Exception
	 */
	public void start(Stage primaryStage, Scene primaryScene, World world, VBox root) throws Exception {
				
		primaryStage.setTitle("Artificial World WX");

		//pane is where the animation is located
		pane = new Pane();
		pane.setPrefSize(world.getWidth(), world.getHeight() - 20);
		pane.setBackground(new Background(new BackgroundFill(Color.web("#c3ffa3"), CornerRadii.EMPTY, Insets.EMPTY)));
		root.getChildren().add(pane);
		
		//for notifications messages
		stackMessage = new StackPane();
		vboxMessage = new VBox();
		stackMessage.getChildren().add(vboxMessage);
		pane.getChildren().add(stackMessage);
		
		//draw world
		redrawWorld(pane, world);
				
		KeyFrame keyframe = new KeyFrame(Duration.millis(16),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						if(stop) {
							pane.getChildren().clear();
							root.getChildren().remove(pane);
							animation.stop();
						}
						
						//main loop
						if(!pause) {
							
							//herbivores
							for (int i = 0; i < world.getHerbs().size(); i++) {
								world.getHerbs().get(i).getCircle().setTranslateX(world.getHerbs().get(i).getCircle().getTranslateX() + world.getHerbs().get(i).getdx());
								world.getHerbs().get(i).getCircle().setTranslateY(world.getHerbs().get(i).getCircle().getTranslateY() + world.getHerbs().get(i).getdy());
								//left & right borders
								if (world.getHerbs().get(i).getCircle().getCenterX() + world.getHerbs().get(i).getCircle().getTranslateX() < world.getHerbs().get(i).getCircle().getRadius()
									|| world.getHerbs().get(i).getCircle().getCenterX() + world.getHerbs().get(i).getCircle().getTranslateX() + world.getHerbs().get(i).getCircle().getRadius() > primaryScene.getWidth()) {
									world.getHerbs().get(i).setdx(world.getHerbs().get(i).getdx()*(-1));
								}
								//up & down borders
								if (world.getHerbs().get(i).getCircle().getCenterY() + world.getHerbs().get(i).getCircle().getTranslateY() < world.getHerbs().get(i).getCircle().getRadius()
									|| world.getHerbs().get(i).getCircle().getCenterY() + world.getHerbs().get(i).getCircle().getTranslateY() + world.getHerbs().get(i).getCircle().getRadius() > primaryScene.getHeight()-30) {
									world.getHerbs().get(i).setdy(world.getHerbs().get(i).getdy()*(-1));
								}
								//rock collision
								for (int  j = 0; j < world.getRocks().size(); j++) {
									if (Math.abs(world.getHerbs().get(i).getCircle().getCenterX() + world.getHerbs().get(i).getCircle().getTranslateX() - world.getRocks().get(j).getCircle().getCenterX()) < world.getHerbs().get(i).getCircle().getRadius() + world.getRocks().get(j).getCircle().getRadius()
										&& Math.abs(world.getHerbs().get(i).getCircle().getCenterY() + world.getHerbs().get(i).getCircle().getTranslateY() - world.getRocks().get(j).getCircle().getCenterY()) < world.getHerbs().get(i).getCircle().getRadius() + world.getRocks().get(j).getCircle().getRadius()) {
							    		//dx
								    	world.getHerbs().get(i).setdx(world.getHerbs().get(i).getdx()*(-1));
							    		//dy
								    	world.getHerbs().get(i).setdy(world.getHerbs().get(i).getdy()*(-1));
									}
								}

								double tempHerbX = 	world.getHerbs().get(i).getCircle().getCenterX() +
													world.getHerbs().get(i).getCircle().getTranslateX();
								double tempHerbY = 	world.getHerbs().get(i).getCircle().getCenterY() +
													world.getHerbs().get(i).getCircle().getTranslateY();
								
								for (int h = 0; h < world.getFoods().size(); h++) {
									double tempFoodX =	world.getFoods().get(h).getCircle().getCenterX() +
														world.getFoods().get(h).getCircle().getTranslateX();
									double tempFoodY =	world.getFoods().get(h).getCircle().getCenterY() +
														world.getFoods().get(h).getCircle().getTranslateY();
									//if within smell range
									if (Math.abs(world.getHerbs().get(i).getCircle().getCenterX()
										+ world.getHerbs().get(i).getCircle().getTranslateX()
										- world.getFoods().get(h).getCircle().getCenterX())
										< world.getHerbs().get(i).getCircle().getRadius()
										+ world.getFoods().get(h).getCircle().getRadius() + 50
										&& Math.abs(world.getHerbs().get(i).getCircle().getCenterY()
										+ world.getHerbs().get(i).getCircle().getTranslateY()
										- world.getFoods().get(h).getCircle().getCenterY())
										< world.getHerbs().get(i).getCircle().getRadius()
										+ world.getFoods().get(h).getCircle().getRadius() + 50) {
										//move towards food
										if (tempHerbX > tempFoodX) {
											world.getHerbs().get(i).setdx(world.getHerbs().get(i).getdx() - 1.0f
													* (float)Math.pow((float)world.getHerbs().get(i).getCircle().getRadius()
													+ (float)world.getFoods().get(h).getCircle().getRadius(),-1));
										} else {
											world.getHerbs().get(i).setdx(world.getHerbs().get(i).getdx() + 1.0f
													* (float)Math.pow((float)world.getHerbs().get(i).getCircle().getRadius()
													+ (float)world.getFoods().get(h).getCircle().getRadius(),-1));
										}
										if (tempHerbY > tempFoodY) {
											world.getHerbs().get(i).setdy(world.getHerbs().get(i).getdy() - 1.0f
													* (float)Math.pow((float)world.getHerbs().get(i).getCircle().getRadius()
													+ (float)world.getFoods().get(h).getCircle().getRadius(),-1));
										} else {
											world.getHerbs().get(i).setdy(world.getHerbs().get(i).getdy() + 1.0f
													* (float)Math.pow((float)world.getHerbs().get(i).getCircle().getRadius()
													+ (float)world.getFoods().get(h).getCircle().getRadius(),-1));
										}
																				
									} else {
										//random movement every 5 secs if no food in range
									    if (System.currentTimeMillis() > holdTime + 5000) {
									    	holdTime = System.currentTimeMillis();
									    	for (int z = 0; z < world.getHerbs().size(); z++) {
									    		//dx
										    	world.getHerbs().get(z).setdx(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getHerbs().get(z).getCircle().setTranslateX(world.getHerbs().get(z).getCircle().getTranslateX() + world.getHerbs().get(z).getdx());
									    		//dy
										    	world.getHerbs().get(z).setdy(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getHerbs().get(z).getCircle().setTranslateY(world.getHerbs().get(z).getCircle().getTranslateY() + world.getHerbs().get(z).getdy());
									    		//energy
									    		world.getHerbs().get(z).setEnergy(world.getHerbs().get(z).getEnergy() - 1);
									    	}
									    }
									}
									
									//if animal is on food eat
									if (Math.abs(world.getHerbs().get(i).getCircle().getCenterX()
											+ world.getHerbs().get(i).getCircle().getTranslateX()
											- world.getFoods().get(h).getCircle().getCenterX())
											< world.getHerbs().get(i).getCircle().getRadius()
											+ world.getFoods().get(h).getCircle().getRadius() - 5
											&& Math.abs(world.getHerbs().get(i).getCircle().getCenterY()
											+ world.getHerbs().get(i).getCircle().getTranslateY()
											- world.getFoods().get(h).getCircle().getCenterY())
											< world.getHerbs().get(i).getCircle().getRadius()
											+ world.getFoods().get(h).getCircle().getRadius()- 5) {
										//set animal to stay still
										world.getHerbs().get(i).setdx(0);
										world.getHerbs().get(i).setdy(0);
										//remove food
										if (!world.getFoods().get(h).getPoison()) {
											eatenAnim(	world.getFoods().get(h).getCircle().getCenterX() + world.getFoods().get(h).getCircle().getTranslateX(),
														world.getFoods().get(h).getCircle().getCenterY() + world.getFoods().get(h).getCircle().getTranslateY(),
														8, Color.web("#096600"), Color.web("#096600"));
										}
										pane.getChildren().remove(world.getFoods().get(h).getCircle());
										world.getHerbs().get(i).setEnergy(world.getFoods().get(h).getCalories() + world.getHerbs().get(i).getEnergy());
										world.getFoods().get(h).setCircle(null);
										world.getFoods().remove(h);
										holdTime = 4000;
									}
									if (world.getFoods().size() == 0) {
										//random movement every 5 secs if no food in range
									    if (System.currentTimeMillis() > holdTime + 5000) {
									    	holdTime = System.currentTimeMillis();
									    	for (int z = 0; z < world.getHerbs().size(); z++) {
									    		//dx
										    	world.getHerbs().get(z).setdx(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getHerbs().get(z).getCircle().setTranslateX(world.getHerbs().get(z).getCircle().getTranslateX() + world.getHerbs().get(z).getdx());
									    		//dy
										    	world.getHerbs().get(z).setdy(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getHerbs().get(z).getCircle().setTranslateY(world.getHerbs().get(z).getCircle().getTranslateY() + world.getHerbs().get(z).getdy());
									    		//energy
									    		world.getHerbs().get(z).setEnergy(world.getHerbs().get(z).getEnergy() - 1);
									    	}
									    }
									}
								}
								//energy
								if (world.getHerbs().get(i).getEnergy() < 1) {
									if (world.getHerbs().get(i).getEnergy() < -10) {
										deathAnim(	world.getHerbs().get(i).getCircle().getCenterX() + world.getHerbs().get(i).getCircle().getTranslateX(),
													world.getHerbs().get(i).getCircle().getCenterY() + world.getHerbs().get(i).getCircle().getTranslateY(),
													Color.web("#ffff4d"), Color.PURPLE, true);
									}
									pane.getChildren().remove(world.getHerbs().get(i).getCircle());
									world.getHerbs().remove(i);
								}
							}
							
							
							
							
							//carnivores
							for (int i = 0; i < world.getCarns().size(); i++) {
								world.getCarns().get(i).getCircle().setTranslateX(world.getCarns().get(i).getCircle().getTranslateX() + world.getCarns().get(i).getdx());
								world.getCarns().get(i).getCircle().setTranslateY(world.getCarns().get(i).getCircle().getTranslateY() + world.getCarns().get(i).getdy());
								//left & right borders
								if (world.getCarns().get(i).getCircle().getCenterX() + world.getCarns().get(i).getCircle().getTranslateX() < world.getCarns().get(i).getCircle().getRadius()
									|| world.getCarns().get(i).getCircle().getCenterX() + world.getCarns().get(i).getCircle().getTranslateX() + world.getCarns().get(i).getCircle().getRadius() > primaryScene.getWidth()) {
									world.getCarns().get(i).setdx(world.getCarns().get(i).getdx()*(-1));
								}
								//up & down borders
								if (world.getCarns().get(i).getCircle().getCenterY() + world.getCarns().get(i).getCircle().getTranslateY() < world.getCarns().get(i).getCircle().getRadius()
									|| world.getCarns().get(i).getCircle().getCenterY() + world.getCarns().get(i).getCircle().getTranslateY() + world.getCarns().get(i).getCircle().getRadius() > primaryScene.getHeight()-30) {
									world.getCarns().get(i).setdy(world.getCarns().get(i).getdy()*(-1));
								}
								//rock collision
								for (int  j = 0; j < world.getRocks().size(); j++) {
									if (Math.abs(world.getCarns().get(i).getCircle().getCenterX() + world.getCarns().get(i).getCircle().getTranslateX() - world.getRocks().get(j).getCircle().getCenterX()) < world.getCarns().get(i).getCircle().getRadius() + world.getRocks().get(j).getCircle().getRadius()
										&& Math.abs(world.getCarns().get(i).getCircle().getCenterY() + world.getCarns().get(i).getCircle().getTranslateY() - world.getRocks().get(j).getCircle().getCenterY()) < world.getCarns().get(i).getCircle().getRadius() + world.getRocks().get(j).getCircle().getRadius()) {
							    		//dx
								    	world.getCarns().get(i).setdx(world.getCarns().get(i).getdx()*(-1));
							    		//dy
								    	world.getCarns().get(i).setdy(world.getCarns().get(i).getdy()*(-1));
									}
								}
							}
							//if food in range
							for (int i = 0; i < world.getCarns().size(); i++) {
								double tempCarnX = 	world.getCarns().get(i).getCircle().getCenterX() +
													world.getCarns().get(i).getCircle().getTranslateX();
								double tempCarnY = 	world.getCarns().get(i).getCircle().getCenterY() +
													world.getCarns().get(i).getCircle().getTranslateY();
								
								for (int j = 0; j < world.getHerbs().size(); j++) {
									double tempHerbFoodX =	world.getHerbs().get(j).getCircle().getCenterX() +
															world.getHerbs().get(j).getCircle().getTranslateX();
									double tempHerbFoodY =	world.getHerbs().get(j).getCircle().getCenterY() +
															world.getHerbs().get(j).getCircle().getTranslateY();
									//if within smell range
									if (Math.abs(world.getCarns().get(i).getCircle().getCenterX()
										+ world.getCarns().get(i).getCircle().getTranslateX()
										- (world.getHerbs().get(j).getCircle().getCenterX()
										+ world.getHerbs().get(j).getCircle().getTranslateX()))
										< world.getCarns().get(i).getCircle().getRadius()
										+ world.getHerbs().get(j).getCircle().getRadius() + 100
										&& Math.abs(world.getCarns().get(i).getCircle().getCenterY()
										+ world.getCarns().get(i).getCircle().getTranslateY()
										- (world.getHerbs().get(j).getCircle().getCenterY()
										+ world.getHerbs().get(j).getCircle().getTranslateY()))
										< world.getCarns().get(i).getCircle().getRadius()
										+ world.getHerbs().get(j).getCircle().getRadius() + 100) {
										//move towards food
										if (tempCarnX > tempHerbFoodX) {
											world.getCarns().get(i).setdx(world.getCarns().get(i).getdx() - 1.0f
													* (float)Math.pow((float)world.getCarns().get(i).getCircle().getRadius()
													+ (float)world.getHerbs().get(j).getCircle().getRadius(),-1));
										} else {
											world.getCarns().get(i).setdx(world.getCarns().get(i).getdx() + 1.0f
													* (float)Math.pow((float)world.getCarns().get(i).getCircle().getRadius()
													+ (float)world.getHerbs().get(j).getCircle().getRadius(),-1));
										}
										if (tempCarnY > tempHerbFoodY) {
											world.getCarns().get(i).setdy(world.getCarns().get(i).getdy() - 1.0f
													* (float)Math.pow((float)world.getCarns().get(i).getCircle().getRadius()
													+ (float)world.getHerbs().get(j).getCircle().getRadius(),-1));
										} else {
											world.getCarns().get(i).setdy(world.getCarns().get(i).getdy() + 1.0f
													* (float)Math.pow((float)world.getCarns().get(i).getCircle().getRadius()
													+ (float)world.getHerbs().get(j).getCircle().getRadius(),-1));
										}
																				
									} else {
										//random movement every 5 secs if no food in range
									    if (System.currentTimeMillis() > holdTimeCarn + 5000) {
									    	holdTimeCarn = System.currentTimeMillis();
									    	for (int z = 0; z < world.getCarns().size(); z++) {
									    		//dx
										    	world.getCarns().get(z).setdx(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getCarns().get(z).getCircle().setTranslateX(world.getCarns().get(z).getCircle().getTranslateX() + world.getCarns().get(z).getdx());
									    		//dy
										    	world.getCarns().get(z).setdy(randomWithRange(lowerSpeed,upperSpeed));
									    		world.getCarns().get(z).getCircle().setTranslateY(world.getCarns().get(z).getCircle().getTranslateY() + world.getCarns().get(z).getdy());
									    		//energy
									    		world.getCarns().get(z).setEnergy(world.getCarns().get(z).getEnergy() - 1);
									    	}
									    }
									}
									
									//if animal is on food eat
									if (Math.abs(world.getCarns().get(i).getCircle().getCenterX()
											+ world.getCarns().get(i).getCircle().getTranslateX()
											- (world.getHerbs().get(j).getCircle().getCenterX()
											+ world.getHerbs().get(j).getCircle().getTranslateX()))
											< world.getCarns().get(i).getCircle().getRadius()
											+ world.getHerbs().get(j).getCircle().getRadius() - 5
											&& Math.abs(world.getCarns().get(i).getCircle().getCenterY()
											+ world.getCarns().get(i).getCircle().getTranslateY()
											- (world.getHerbs().get(j).getCircle().getCenterY()
											+ world.getHerbs().get(j).getCircle().getTranslateY()))
											< world.getCarns().get(i).getCircle().getRadius()
											+ world.getHerbs().get(j).getCircle().getRadius()- 5) {
										//set animal to stay still
										world.getCarns().get(i).setdx(0);
										world.getCarns().get(i).setdy(0);
										world.getHerbs().get(j).setdx(0);
										world.getHerbs().get(j).setdy(0);
										//remove food
										world.getCarns().get(i).setEnergy(world.getCarns().get(i).getEnergy() + 10);
										eatenAnim(	world.getHerbs().get(j).getCircle().getCenterX() + world.getHerbs().get(j).getCircle().getTranslateX(),
													world.getHerbs().get(j).getCircle().getCenterY() + world.getHerbs().get(j).getCircle().getTranslateY(),
													16, Color.web("#ffff4d"), Color.web("#806e00"));
										pane.getChildren().remove(world.getHerbs().get(j).getCircle());										world.getHerbs().get(j).setCircle(null);
										world.getHerbs().get(j).setCircle(null);
										world.getHerbs().remove(j);
										holdTimeCarn = 4000;
										
									}
								}
								//random move
								if (world.getHerbs().size() == 0) {
									//random movement every 5 secs if no food in range
								    if (System.currentTimeMillis() > holdTimeCarn + 5000) {
								    	holdTimeCarn = System.currentTimeMillis();
								    	for (int z = 0; z < world.getCarns().size(); z++) {
								    		//dx
									    	world.getCarns().get(z).setdx(randomWithRange(lowerSpeed,upperSpeed));
								    		world.getCarns().get(z).getCircle().setTranslateX(world.getCarns().get(z).getCircle().getTranslateX() + world.getCarns().get(z).getdx());
								    		//dy
									    	world.getCarns().get(z).setdy(randomWithRange(lowerSpeed,upperSpeed));
								    		world.getCarns().get(z).getCircle().setTranslateY(world.getCarns().get(z).getCircle().getTranslateY() + world.getCarns().get(z).getdy());
								    		//energy
								    		world.getCarns().get(z).setEnergy(world.getCarns().get(z).getEnergy() - 1);
								    	}
								    }
								}
								//energy
								if (world.getCarns().get(i).getEnergy() < 1) {
									pane.getChildren().remove(world.getCarns().get(i).getCircle());
									world.getCarns().remove(i);
								}
							}	
						}
					}
		});
		animation = TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(keyframe).build();
		
		animation.play();
		
		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}
	
	/**Draws the world initially, could be used for redrawing but is not currently,
	 * other methods are used
	 * 
	 * @param pane Pane to draw on
	 * @param world World to get data from
	 */
	public void redrawWorld(Pane pane, World world) {
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				
				//initialisation
				//herbivore
				for (int k = 0; k < world.getHerbs().size(); k++) {
					if (world.getHerbs().get(k).getX() == j && world.getHerbs().get(k).getY() == i) {
						world.getHerbs().get(k).getCircle().setCenterX(j);
						world.getHerbs().get(k).getCircle().setCenterY(i);
						world.getHerbs().get(k).getCircle().setFill(Color.web("#ffff4d"));
						pane.getChildren().add(world.getHerbs().get(k).getCircle());
					}
				}
				//carnivores
				for (int k = 0; k < world.getCarns().size(); k++) {
					if (world.getCarns().get(k).getX() == j && world.getCarns().get(k).getY() == i) {
						world.getCarns().get(k).getCircle().setCenterX(j);
						world.getCarns().get(k).getCircle().setCenterY(i);
						world.getCarns().get(k).getCircle().setFill(Color.web("#ff471a"));
						pane.getChildren().add(world.getCarns().get(k).getCircle());
					}
				}
				//foods
				for (int k = 0; k < world.getFoods().size(); k++) {
					if (world.getFoods().get(k).getX() == j && world.getFoods().get(k).getY() == i) {
						
						world.getFoods().get(k).getCircle().setCenterX(j);
						world.getFoods().get(k).getCircle().setCenterY(i);
						if(world.getFoods().get(k).getPoison() == true) {
							world.getFoods().get(k).getCircle().setFill(Color.web("#A33EA1"));
						} else {
							world.getFoods().get(k).getCircle().setFill(Color.web("#096600"));
						}
						pane.getChildren().add(world.getFoods().get(k).getCircle());
					}
				}
				//rocks
				for (int k = 0; k < world.getRocks().size(); k++) {
					if (world.getRocks().get(k).getX() == j && world.getRocks().get(k).getY() == i) {
						
						world.getRocks().get(k).getCircle().setCenterX(j);
						world.getRocks().get(k).getCircle().setCenterY(i);
						world.getRocks().get(k).getCircle().setFill(Color.web("#807373"));
						pane.getChildren().add(world.getRocks().get(k).getCircle());
					}
				}
			}
		}
	}
	
	/**Add temporary notification label to the scene.
	 * 
	 * @param text Text to display
	 * @param textSize Size of text
	 * @param displaySeconds Display duration in seconds
	 */
	public void addJFXLabel(String text, int textSize, int displaySeconds) {
		//style label
		Label label = new Label();
		label.setTranslateX(10);
		label.setLayoutX(10);
		label.setTranslateY(10);
		label.setLayoutY(10);
		label.setText(text);
		label.setFont(Font.font(null, FontWeight.BOLD, textSize));
		label.setTextFill(Color.web("#000040"));
		vboxMessage.getChildren().add(label); 
		label.toFront();
		//create timeline for fade animation
		SequentialTransition sq = new SequentialTransition();
		FadeTransition stall = new FadeTransition(Duration.seconds(displaySeconds), label);
		stall.setFromValue(1);
		stall.setToValue(1);
		FadeTransition fade = new FadeTransition(Duration.seconds(1), label);
		fade.setFromValue(1);
		fade.setToValue(0);
		sq.getChildren().addAll(stall, fade);
		stackMessage.toFront();
		vboxMessage.toFront();
		sq.play();
		//delete on finish
		sq.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				vboxMessage.getChildren().remove(label);
			}
		});
	}
	
	/**Animation for death of animals or plants.
	 * 
	 * @param x x co-ordinate of death site
	 * @param y y co-ordinate of death site
	 * @param newc Color to fade from
	 * @param oldc Color to fade to
	 * @param poison If true, animal has been poisoned and animation becomes poison specific
	 */
	public void deathAnim(double x, double y, Color newc, Color oldc, boolean poison) {
		Circle c = new Circle(16);
		c.setCenterX(x);
		c.setCenterY(y);
		SequentialTransition sq = new SequentialTransition();
		FillTransition to = new FillTransition(Duration.millis(16), c);
		to.setToValue(newc);
		PauseTransition p1 = new PauseTransition(Duration.millis(250));
		FillTransition from = new FillTransition(Duration.millis(16), c);
		from.setToValue(oldc);
		PauseTransition p2 = new PauseTransition(Duration.millis(250));

		sq.getChildren().addAll(to, p1, from, p2);
		sq.setCycleCount(2);
		
		pane.getChildren().add(c);
		
		sq.play();
		
		//delete on finish
		sq.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pane.getChildren().remove(c);
			}
		});
		
		if(poison) {
			deathNote(c.getCenterX(), c.getCenterY(), "poisoned!", Color.PURPLE, 1000);
		}
	}
	
	/**Creates temporary note on screen when a death occurs, displaying the type of death that happened
	 * 
	 * @param x x co-ordinate of death site
	 * @param y y co-ordinate of death site
	 * @param text Text to display
	 * @param p Color of text
	 * @param dur Duration of message in seconds
	 */
	public void deathNote(double x, double y, String text, Paint p, int dur) {
		Label label = new Label(text);
		label.setTextFill(p);
		label.setTranslateX(x+20);
		label.setTranslateY(y-40);
		label.setFont(Font.font(null, 18));
		
		ParallelTransition pa = new ParallelTransition();
		FadeTransition fade = new FadeTransition(Duration.millis(dur), label);
		fade.setFromValue(1);
		fade.setToValue(0);
		TranslateTransition tt = new TranslateTransition(Duration.millis(dur), label);
		tt.setFromY(y);
		tt.setToY(y-30);
		
		pa.setCycleCount(1);
		pa.getChildren().addAll(fade, tt);
		pane.getChildren().add(label);
		pa.play();
		//delete on finish
		pa.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pane.getChildren().remove(label);
			}
		});
	}
	
	/**Animation for when a herbivore or plant(AFood) is eaten.
	 * 
	 * @param x x co-ordinate of death site
	 * @param y y co-ordinate of death site
	 * @param radius Radius of circle
	 * @param original Color of original circle
	 * @param text Color of text
	 */
	public void eatenAnim(double x, double y, int radius, Color original, Color text) {
		Circle c = new Circle(radius);
		c.setCenterX(x);
		c.setCenterY(y);
		
		ParallelTransition pa = new ParallelTransition();
		FadeTransition fade = new FadeTransition(Duration.millis(1000), c);
		fade.setFromValue(1);
		fade.setToValue(0);
		FillTransition fill = new FillTransition(Duration.millis(1000), c);
		fill.setFromValue(original);
		fill.setToValue(text);
		
		pa.getChildren().addAll(fade, fill);
		pane.getChildren().add(c);
		pa.play();
		//delete on finish
		pa.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pane.getChildren().remove(c);
			}
		});
		deathNote(c.getCenterX(), c.getCenterY(), "eaten!", text, 1000);
	}
	
	public float randomWithRange(float min, float max)
	{
	   float range = (max - min) + 1;     
	   return (float)(Math.random() * range) + min;
	}
	
	public Pane getPane() {
		return this.pane;
	}
	
	public boolean getPause() {
		return this.pause;
	}
	
	public void setPause(boolean p) {
		this.pause = p;
	}
	
	public boolean getStop() {
		return this.stop;
	}
	
	public void setStop(boolean s) {
		this.stop = s;
	}
	
	/**Add carnivore to the simulation
	 * 
	 * @param world World to add to
	 */
	public void addCarn(World world) {
		Carnivore c = new Carnivore(" ", " ", " ", 	(int)randomWithRange(50, world.getWidth() - 50),
													(int)randomWithRange(80, world.getHeight() - 80), 15);
		world.getCarns().add(c);
		int size = world.getCarns().size() - 1;
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				if (world.getCarns().get(size).getX() == j && world.getCarns().get(size).getY() == i) {
					world.getCarns().get(size).getCircle().setCenterX(j);
					world.getCarns().get(size).getCircle().setCenterY(i);
					world.getCarns().get(size).getCircle().setFill(Color.web("#ff471a"));
					pane.getChildren().add(world.getCarns().get(size).getCircle());
				}
			}
		}
	}
	
	/**Add herbivore to the simulation
	 * 
	 * @param world World to add to
	 */
	public void addHerb(World world) {
		Herbivore h = new Herbivore(" ", " ", " ", 	(int)randomWithRange(50, world.getWidth() - 50),
													(int)randomWithRange(80, world.getHeight() - 80), 15);
		world.getHerbs().add(h);
		int size = world.getHerbs().size() - 1;
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				if (world.getHerbs().get(size).getX() == j && world.getHerbs().get(size).getY() == i) {
					world.getHerbs().get(size).getCircle().setCenterX(j);
					world.getHerbs().get(size).getCircle().setCenterY(i);
					world.getHerbs().get(size).getCircle().setFill(Color.web("#ffff4d"));
					pane.getChildren().add(world.getHerbs().get(size).getCircle());
				}
			}
		}
	}
	
	/**Add food to the simulation
	 * 
	 * @param world World to add to
	 */
	public void addFood(World world) {
		AFood f = new AFood((int)randomWithRange(50, world.getWidth() - 50),
							(int)randomWithRange(80, world.getHeight() - 80),
							(int)randomWithRange(1,9));
		world.getFoods().add(f);
		int size = world.getFoods().size() - 1;
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				if (world.getFoods().get(size).getX() == j && world.getFoods().get(size).getY() == i) {
					world.getFoods().get(size).getCircle().setCenterX(j);
					world.getFoods().get(size).getCircle().setCenterY(i);
					if(world.getFoods().get(size).getPoison() == true) {
						world.getFoods().get(size).getCircle().setFill(Color.web("#A33EA1"));
					} else {
						world.getFoods().get(size).getCircle().setFill(Color.web("#096600"));
					}
					pane.getChildren().add(world.getFoods().get(size).getCircle());
				}
			}
		}
	}
	
	/**Add a rock to the simulation
	 * 
	 * @param world World to add to
	 */
	public void addRock(World world) {
		ARock r = new ARock((int)randomWithRange(50, world.getWidth() - 50),
							(int)randomWithRange(80, world.getHeight() - 80));
		world.getRocks().add(r);
		int size = world.getRocks().size() - 1;
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				if (world.getRocks().get(size).getX() == j && world.getRocks().get(size).getY() == i) {
					world.getRocks().get(size).getCircle().setCenterX(j);
					world.getRocks().get(size).getCircle().setCenterY(i);
					world.getRocks().get(size).getCircle().setFill(Color.web("#807373"));
					pane.getChildren().add(world.getRocks().get(size).getCircle());
				}
			}
		}
	}
	
	/**Remove carnivore from the simulation
	 * 
	 * @param world World to remove from
	 */
	public void removeCarn(World world) {
		int i = (int)randomWithRange(0,world.getCarns().size()-1);
		pane.getChildren().remove(world.getCarns().get(i).getCircle());
		world.getCarns().remove(i);
	}
	
	/**Remove herbivore from the simulation
	 * 
	 * @param world World to remove from
	 */
	public void removeHerb(World world) {
		int i = (int)randomWithRange(0,world.getHerbs().size()-1);
		pane.getChildren().remove(world.getHerbs().get(i).getCircle());
		world.getHerbs().remove(i);
	}
	
	/**Remove food from the simulation
	 * 
	 * @param world World to remove from
	 */
	public void removeFood(World world) {
		int i = (int)randomWithRange(0,world.getFoods().size()-1);
		pane.getChildren().remove(world.getFoods().get(i).getCircle());
		world.getFoods().remove(i);
	}
	
	/**Remove a rock from the simulation
	 * 
	 * @param world World to remove from
	 */
	public void removeRock(World world) {
		int i = (int)randomWithRange(0,world.getRocks().size()-1);
		pane.getChildren().remove(world.getRocks().get(i).getCircle());
		world.getRocks().remove(i);
	}
	
	/**Set the simulation to visible or not
	 * 
	 * @param a Set invisible if true, set visible if false
	 */
	public void setInvisible(boolean a) {
		if (a) {
			pane.setVisible(true);
		} else {
			pane.setVisible(false);
		}
	}

	@Override
	public void start(Stage arg0) throws Exception {
		
	}

}

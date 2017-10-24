package tatai.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tatai.model.LevelSelection;
import tatai.model.Stats;


/**
 * This controller belongs to the score fxml. It has a "Try Again" button, "Next Level" Button.
 * This scene gets loaded when the user has complete the 10 games . It shows their score for that particular game,
 * the score then gets saved into the ResultsArrayList and used for further calculations for statistics.
 * @author se206
 *
 */
public class ScoreController{
	
	//new high score components
	@FXML private ImageView _tahi;
	@FXML private ImageView _amazing;
	
	
	@FXML private Label _scoreLbl;
	@FXML private Label _goodEffortLbl;
	@FXML 
	private Scene scoreScene;
	@FXML 
	private Scene levelScene;
	@FXML
	private Stage window;
	@FXML
	private Scene menuScene;

	private LevelSelection _levelSelected;
	private int _score;
	private int _index;
	@FXML
	private Button nextlevelBtn;
	@FXML
	private Label scoreLbl;
	@FXML 
	private Button tryagainBtn;
	private Scene scene;
	
	@FXML
	private void initialize() {
		_tahi.setVisible(false);;
		_amazing.setVisible(false);
		_scoreLbl.setText("You scored");
	}

	//If this button is clicked, the scene will load back to the start screen of that particular level.
	public void tryAgainBtn(ActionEvent event) throws IOException{

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
		Parent number = loader.load();
		scene = new Scene(number);
		StartController c = loader.getController();
		c.setLevel(_levelSelected, _index);

		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	//This button only appears if the user has scored 8 or more in the current level. It loads the next level up
	//if clicked.
	public void nextLevelBtn(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
		Parent number = loader.load();
		scene = new Scene(number);
		StartController c = loader.getController();
		if (_levelSelected == LevelSelection.EASY){
			c.setLevel(LevelSelection.MEDIUM);
		}
		else if (_levelSelected == LevelSelection.MEDIUM){
			c.setLevel(LevelSelection.HARD);
		}
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	//Sends the user back to the main menu page
	public void menuBtn(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		scene = new Scene(menu);

		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setTitle("Tatai");
		window.show();
	}

	/**
	 * The score is passed in through the NumberDisplayController and is set for the specified resultArray for that level
	 * If the user achieves a high score then the "Amazing" label will appear
	 * @param score
	 * @param level
	 */
	public void setScoreAndLevel(int score, LevelSelection level){
		_levelSelected = level;
		if (level.equals(LevelSelection.HARD) || score < 8 || level.equals(LevelSelection.CUSTOM)) {
			nextlevelBtn.setVisible(false);
		} else {
			nextlevelBtn.setVisible(true);
		}
		scoreLbl.setText(score + " out of 10");

		//get stats instance
		Stats stats = Stats.getInstance();
		//only show new high score if they get a new high score and it's not their first attempt
		if (stats.getMax(_levelSelected) != -1) {
			if (score > stats.getMax(_levelSelected)) {
				_goodEffortLbl.setVisible(false);
				_scoreLbl.setText("New High Score!");
				_tahi.setVisible(true);
				_amazing.setVisible(true);
			}
		}		
		//update stats object
		stats.addResult(score, _levelSelected);
	}


}

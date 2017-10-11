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
import javafx.stage.Stage;
import tatai.model.LevelSelection;

public class ScoreController{

	@FXML 
	private Scene scoreScene;
	@FXML 
	private Scene levelScene;
	@FXML
	private Stage window;
	@FXML
	private Scene menuScene;
	
	private LevelSelection _levelSelected;
	@FXML
	private Button nextlevel;
	@FXML
	private Label scorelabel;
	@FXML 
	private Button tryagainbtn;
	
	public void tryAgainBtn(ActionEvent event) throws IOException{
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Level.fxml"));
			Parent number = loader.load();
			scoreScene = new Scene(number);
			LevelController c = loader.getController();
			c.setLevel(_levelSelected);
			
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scoreScene);
			window.show();
	}

	public void nextLevelBtn(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Level.fxml"));
		Parent number = loader.load();
		levelScene = new Scene(number);
		LevelController c = loader.getController();
		c.setLevel(LevelSelection.HARD);
		
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(levelScene);
		window.show();
	}

	public void menuBtn(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("TataiOverview.fxml"));
		menuScene = new Scene(menu);

		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menuScene);
		window.show();
	}
	
	public void setScoreAndLevel(int score, LevelSelection level){
		_levelSelected = level;
		if (level.equals(LevelSelection.HARD) || score < 8) {
			nextlevel.setVisible(false);
		} else {
			nextlevel.setVisible(true);
		}
		scorelabel.setText(score + " out of 10");
	}

}

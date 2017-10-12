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

public class StartController {
	
	private LevelSelection _level;
	@FXML
	private Button back;
	private Scene menuScene;
	private Scene numberScene;
	private Stage window;
	@FXML 
	Label label;
	

	public void setLevel(LevelSelection level){
		_level = level;
		
		if (_level.equals(LevelSelection.EASY)){
			label.setText("Level: Easy");
		}
		else if(_level.equals(LevelSelection.MEDIUM)){
			label.setText("Level: Medium");
		}
		else if(_level.equals(LevelSelection.PRACTISE)){
			label.setText("Practise");
		}
		else{
			label.setText("Level: Hard");
		}
		
	}
	
	public void startBtn(ActionEvent event) throws IOException{
				
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NumberDisplay.fxml"));
		Parent number = loader.load();
		numberScene = new Scene(number);
		NumberDisplayController c = loader.getController();
		c.setLabelText(_level);
		
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setOnCloseRequest(e -> {
			c.killProcesses();
		});
		window.setScene(numberScene);
		window.show();

	}
	
	public void menuButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(menuScene);
		window.show();
	}
}

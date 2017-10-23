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
import tatai.Main;
import tatai.model.CustomLevel;
import tatai.model.CustomLists;
import tatai.model.EasyLevel;
import tatai.model.HardLevel;
import tatai.model.Level;
import tatai.model.LevelSelection;
import tatai.model.MediumLevel;
import tatai.model.Practise;

public class StartController {
	
	private LevelSelection _levelSelection;
	private Level _level; //level object
	private int _index = -1; //-1 means it's not a custom game
	@FXML
	private Button back;
	private Scene menuScene;
	private Stage window;
	@FXML private Label _label;
	

	public void setLevel(LevelSelection level, int index){
		_levelSelection = level;
		_index = index;
		
		_label.setText("level: " + _levelSelection.toString());		
	}
	
	public void startBtn(ActionEvent event) throws IOException{
		//set the level
		if (_levelSelection.equals(LevelSelection.EASY)){
			_level = new EasyLevel();
		}
		else if(_levelSelection.equals(LevelSelection.MEDIUM)){
			_level = new MediumLevel();
		}
		else if(_levelSelection.equals(LevelSelection.PRACTISE)){
			_level = new Practise();
		} else if (_levelSelection.equals(LevelSelection.CUSTOM)) {
			_level = new CustomLevel(_index);
		}
		else{
			_level = new HardLevel();
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/NumberDisplay.fxml"));
		Parent numDisplay = loader.load();
		loader.<NumberDisplayController>getController().setup(_levelSelection,_level);
		
		Scene numDisplayScene = new Scene(numDisplay);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(numDisplayScene);
	}
	
	public void menuButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menuScene);
		window.setTitle("Tatai");
	}
}

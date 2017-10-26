package tatai.view.play;

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
import tatai.model.levels.EasyLevel;
import tatai.model.levels.HardLevel;
import tatai.model.levels.Level;
import tatai.model.levels.LevelSelection;
import tatai.model.levels.MediumLevel;
import tatai.model.levels.Practise;
import tatai.model.levels.customLevel.CustomLevel;

/**
 * This controller is associated with the Start fxml. This gets loaded when the user first chooses a level
 * so that they can get ready before they start the game. 
 * @author stan557
 *
 */
public class StartController {
	
	private LevelSelection _levelSelection; //level enum
	private Level _level; //level object
	private int _index = -1; //-1 means it's not a custom game
	@FXML private Button back;
	@FXML private Scene menuScene;
	@FXML private Stage window;
	@FXML private Label _label;
	

	/**
	 * Receives the selected level as a parameter and sets the "Level" label with the correct string.
	 * @param level
	 * @param index
	 */
	public void setLevel(LevelSelection level, int index){ //Used for custom level as it needs an integer
		_levelSelection = level;
		_index = index;
		
		_label.setText("Level: " + _levelSelection.toString());		
	}
	
	public void setLevel(LevelSelection level){ //Overloaded for the other levels which are not custom
		_levelSelection = level;
		
		_label.setText("Level: " + _levelSelection.toString());		
	}
	
	/**
	 * When clicked, sends the user to the first question generated for that level.
	 * @param event
	 * @throws IOException
	 */
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
		
		//loads the numberdisplay
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/tatai/view/play/NumberDisplay.fxml"));
		Parent numDisplay = loader.load();
		loader.<NumberDisplayController>getController().setup(_levelSelection,_level);
		
		Scene numDisplayScene = new Scene(numDisplay);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(numDisplayScene);
	}
	
	//Sends the user back to the main menu
	public void menuButtonClicked(ActionEvent event) throws IOException{
		Parent menu = FXMLLoader.load(getClass().getResource("/tatai/view/MainMenu.fxml"));
		menuScene = new Scene(menu);
		
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menuScene);
		window.setTitle("T\u0101tai");
	}
}

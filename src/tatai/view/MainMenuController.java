package tatai.view;

import java.io.IOException;
import tatai.Main;
import tatai.model.levels.LevelSelection;
import tatai.view.play.StartController;
import tatai.view.statistics.StatisticsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainMenuController {
	
	//help components
	@FXML private Button _helpBtn;
	@FXML private ImageView _tahi; //Tahi that appears first
	@FXML private Group _speech; //contains speech bubble, tahi and text
	@FXML private TextArea _instructions; //contains instructions
	@FXML private Button _nextBtn;
	private int _clicks = 0; //checks how many times next has been clicked

	private Stage _window;
	private Scene _scene;
	
	@FXML
	private void initialize() {
		//hide all help component
		_speech.setVisible(false);
	}
	
	/**
	 * loadManage will take an fxmlfile and create a scene with it
	 * @param fxmlFile
	 * @return
	 * @throws IOException
	 */
	private Scene loadManage(String fxmlFile) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(fxmlFile));
		Parent parent = loader.load();
		Scene scene = new Scene(parent);
		return scene;
	}

	/**
	 * when the list button has been clicked, switch over to the list screen
	 * @param event
	 */
	@FXML
	private void listsClick(ActionEvent event) {
		try {
			Scene listScene = loadManage("/tatai/view/list/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setTitle("View Lists");
			window.setScene(listScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * When the play button has been clicked, changed to the level screen
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void playClick(ActionEvent event) throws IOException {
		Parent menu;
		menu = FXMLLoader.load(getClass().getResource("play/Level.fxml"));
		_scene = new Scene(menu);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();

		_window.setScene(_scene);
		_window.setTitle("Level");
		_window.show();
	}
	
	/**
	 * When the practise button has been clicked, changed to the start page for practice
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void practiseClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/play/Start.fxml"));
        Parent start = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.PRACTISE, -1);
        
		_scene = new Scene(start);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();
		_window.setScene(_scene);
		_window.setTitle("Practise");
	}
	
	/**
	 * When the statistics button has been clicked, changes to the scene with the barchart and results from 
	 * current and previous scores.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void statisticsClick(ActionEvent event) throws IOException {
	FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("view/statistics/Statistics.fxml"));
    Parent stats = loader.load();
    loader.<StatisticsController>getController().setStats();
    
	_scene = new Scene(stats);
	_window = (Stage)((Node)event.getSource()).getScene().getWindow();
	_window.setScene(_scene);
	_window.setTitle("Statistics");
	_window.show();
	}
	
	/**
	 * Activates Tahi for help
	 */
	@FXML 
	private void helpClick() {
		//make sure text in speech is initial one
		_tahi.setImage(new Image("/main/resources/happytahi.png"));
		_instructions.setText("Hi, my name is Tahi and I'll be helping you along your T\u0101tai! journey.");
		_speech.setLayoutX(36);
		_speech.setLayoutY(200);
		_helpBtn.setDisable(true);
		_nextBtn.setText("Next");
		_speech.setVisible(true);
	}
	
	/**
	 * Moves Tahi around the screen for instructions 
	 */
	@FXML
	private void nextClick() {
		if (_clicks == 0) {
			//show and set speech
			_tahi.setImage(new Image("/main/resources/IMG_0262.png"));
			_instructions.setText("Click here to practise saying 1-99 in Maori.");
			_speech.setLayoutX(20);
			_speech.setLayoutY(115);
			_clicks++;
		} else if (_clicks == 1) {
			//show and set speech
			_instructions.setText("Click here to start working on your math skills.");
			_speech.setLayoutY(14);
			_clicks++;
		} else if (_clicks == 2) {
			//set speech
			_tahi.setImage(new Image("/main/resources/IMG_0263.png"));
			_instructions.setText("Click here to create your own math questions.");
			_speech.setLayoutX(428);
			_speech.setLayoutY(121);
			_clicks++;
		} else if (_clicks == 3) {
			_nextBtn.setText("Done!");
			//set speech
			_instructions.setText("Lastly, click here to see your past scores.");
			_speech.setLayoutY(23);
			_clicks++;
		} else if (_clicks == 4) {
			//hide help components
			_speech.setVisible(false);
			_helpBtn.setDisable(false);//reenable help button
			_clicks = 0;
		}
	}
}
package tatai.view;

import java.io.IOException;

import tatai.Main;
import tatai.model.LevelSelection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainMenuController {
	
	//help components
	@FXML private Button _helpBtn;
	@FXML private ImageView _firstTahi; //Tahi that appears first
	@FXML private ImageView _leftTahi; //Tahi that appears on the left
	@FXML private ImageView _rightTahi; //Tahi that appears on the right
	//speech stuff
	@FXML private Group _speech; //contains speech bubble and text
	@FXML private TextArea _instructions; //contains instructions
	@FXML private Button _nextBtn;
	private int _clicks = 0; //checks how many times next has been clicked

	private Stage _window;
	private Scene _scene;
	
	@FXML
	private void initialize() {
		//hide all help components
		_firstTahi.setVisible(false);
		_leftTahi.setVisible(false);
		_rightTahi.setVisible(false);
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
			Scene listScene = loadManage("/tatai/view/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(listScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void playClick(ActionEvent event) throws IOException {
		Parent menu;
		menu = FXMLLoader.load(getClass().getResource("Level.fxml"));
		_scene = new Scene(menu);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();

		_window.setScene(_scene);
		_window.setTitle("Level");
		_window.show();
	}
	
	@FXML
	private void practiseClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Start.fxml"));
        Parent start = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.PRACTISE, -1);
        
		_scene = new Scene(start);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();
		_window.setScene(_scene);
		_window.setTitle("Practise");
		_window.show();
	}
	
	@FXML
	private void statisticsClick(ActionEvent event) throws IOException {
	FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("view/Statistics.fxml"));
    Parent stats = loader.load();
    loader.<StatisticsController>getController().setStats();
    
	_scene = new Scene(stats);
	_window = (Stage)((Node)event.getSource()).getScene().getWindow();
	_window.setScene(_scene);
	_window.setTitle("Practise");
	_window.show();
	}
	
	@FXML 
	private void helpClick() {
		//make sure text in speech is initial one
		_instructions.setText("Hi, my name is Tahi and I'll be helping you along your T\u0101tai! journey.");
		_speech.setLayoutX(36);
		_speech.setLayoutY(147);
		_helpBtn.setDisable(true);
		_nextBtn.setText("next");
		_firstTahi.setVisible(true);
		_speech.setVisible(true);
	}
	
	@FXML
	private void nextClick() {
		if (_clicks == 0) {
			//hide first tahi and speech
			_firstTahi.setVisible(false);
			_speech.setVisible(false);
			//show and position left tahi
			_leftTahi.setLayoutX(47);
			_leftTahi.setLayoutY(147);
			_leftTahi.setVisible(true);
			//show and set speech
			_instructions.setText("Click here to practise saying 1-99 in Maori.");
			_speech.setLayoutX(58);
			_speech.setLayoutY(14);
			_speech.setVisible(true);
			_clicks++;
		} else if (_clicks == 1) {
			//hide speech
			_speech.setVisible(false);
			//position left tahi
			_leftTahi.setLayoutY(250);
			//show and set speech
			_instructions.setText("Click here to start working on your math skills.");
			_speech.setLayoutY(115);
			_speech.setVisible(true);
			_clicks++;
		} else if (_clicks == 2) {
			//hide left tahi and speech
			_leftTahi.setVisible(false);
			_speech.setVisible(false);
			//show and position right tahi
			_rightTahi.setLayoutX(464);
			_rightTahi.setLayoutY(147);
			_rightTahi.setVisible(true);
			//show and set speech
			_instructions.setText("Click here to create your own math questions.");
			_speech.setLayoutX(428);
			_speech.setLayoutY(23);
			_speech.setVisible(true);
			_clicks++;
		} else if (_clicks == 3) {
			_nextBtn.setText("done");
			//hide speech
			_speech.setVisible(false);
			//position right tahi
			_rightTahi.setLayoutY(250);
			//show and set speech
			_instructions.setText("Lastly, click here to see your past scores.");
			_speech.setLayoutY(121);
			_speech.setVisible(true);
			_clicks++;
		} else if (_clicks == 4) {
			//hide help components
			_rightTahi.setVisible(false);
			_speech.setVisible(false);
			_helpBtn.setDisable(false);//reenable help button
			_clicks = 0;
		}
	}
}


package tatai.view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.LevelSelection;

/**
 * This is the controller for the level fxml. It allows the user to select which level they would like to play
 * depending on the level they select. It also has the option of returning back to the main menu and the help option
 * with Tahi. On button clicks, Tahi directs the user and appears on different areas of the screen for instructions.
 * @author stan557
 *
 */
public class LevelController {

	@FXML private Button _helpBtn;

	//components of help
	@FXML private Group _speech;
	@FXML private TextArea _instructions;
	@FXML private Button _nextBtn;
	private int _clicks = 0;

	private Scene _scene;
	private Stage window;
	public String level;

	@FXML 
	private void initialize() {
		//hide help components
		_speech.setVisible(false);
	}

	//Changes scene to Easy level
	@FXML
	private void easyButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent easyStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.EASY);


		_scene = new Scene(easyStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Easy");
	}
	
	//Changes scene to Medium level
	@FXML
	private void mediumButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent mediumStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.MEDIUM);

		_scene = new Scene(mediumStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Medium");
	}

	//Changes scene to Hard level
	@FXML
	private void hardButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent hardStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.HARD);

		_scene = new Scene(hardStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Hard");
	}
	
	//Changes scene to the Custom level
	@FXML
	private void customClick(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/SelectList.fxml"));
		Parent menu = loader.load();

		_scene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Custom");
		window.show();
	}

	//Returns to the main menu
	@FXML
	private void menuButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MainMenu.fxml"));
		Parent menu = loader.load();

		_scene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("T\u0101tai");
		window.show();
	}

	//Activates Tahi, the help guide, and sets him on different areas of the screen depending on the number
	//of button clicks to indicate help process.
	@FXML
	private void helpClick() {
		//show and initialize all help components
		_clicks = 0;
		_helpBtn.setDisable(true);//disable help button so that user can't click it again
		_speech.setLayoutX(50);
		_speech.setLayoutY(0);
		_instructions.setText("Easy is for addition and subtraction where the answer is between 1 and 9.");
		_nextBtn.setText("Next");
		_speech.setVisible(true);
	}

	//Moves Tahi depending on the number of clicks recorded.
	@FXML 
	private void nextClick() {
		//run through all the instructions of the help button
		if (_clicks == 0) {
			//set speech
			_instructions.setText("Medium is for addition and subtraction where the answer is between 1 and 99.");
			_speech.setLayoutY(60);
			_clicks++;
		} else if (_clicks == 1) {
			//set speech
			_instructions.setText("Hard is like medium but it also has multiplication and division.");
			_speech.setLayoutY(140);
			_clicks++;
		} else if (_clicks == 2) {
			//set speech
			_instructions.setText("Custom has questions that have been specially written.");
			_speech.setLayoutX(330);
			_nextBtn.setText("Done!");
			_clicks++;
		} else if (_clicks == 3) {
			//hide help components
			_speech.setVisible(false);
			_helpBtn.setDisable(false);//reenable help button
			_clicks = 0;//reset instructions
		}
	}

}

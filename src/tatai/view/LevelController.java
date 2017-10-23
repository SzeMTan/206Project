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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.LevelSelection;

public class LevelController {

	@FXML
	private Button easyButton;
	@FXML
	private Button hardButton;

	@FXML private Button _helpBtn;

	//components of help
	@FXML private ImageView _tahi;
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
		_tahi.setVisible(false);
		_speech.setVisible(false);
	}

	@FXML
	private void easyButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent easyStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.EASY, -1);


		_scene = new Scene(easyStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Easy");
	}

	@FXML
	private void hardButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent hardStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.HARD, -1);

		_scene = new Scene(hardStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Hard");
	}

	@FXML
	private void mediumButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/Start.fxml"));
		Parent mediumStart = loader.load();
		loader.<StartController>getController().setLevel(LevelSelection.MEDIUM, -1);

		_scene = new Scene(mediumStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Medium");
	}

	@FXML
	private void menuButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/MainMenu.fxml"));
		Parent menu = loader.load();

		_scene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Tatai");
		window.show();
	}

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

	@FXML
	private void helpClick() {
		//show and initialize all help components
		_clicks = 0;
		_helpBtn.setDisable(true);//disable help button so that user can't click it again
		_tahi.setLayoutX(115);
		_tahi.setLayoutY(110);
		_speech.setLayoutX(75);
		_speech.setLayoutY(0);
		_instructions.setText("Easy is for addition and subtraction where the answer is between 1 and 9.");
		_nextBtn.setText("next");
		_tahi.setVisible(true);
		_speech.setVisible(true);
	}

	@FXML 
	private void nextClick() {
		//run through all the instructions of the help button
		if (_clicks == 0) {
			//position tahi
			_tahi.setLayoutY(190);
			//set speech
			_instructions.setText("Medium is for addition and subtraction where the answer is between 1 and 99.");
			_speech.setLayoutY(70);
			_clicks++;
		} else if (_clicks == 1) {
			//position tahi
			_tahi.setLayoutY(280);
			//set speech
			_instructions.setText("Hard is like medium but it also has multiplication and division.");
			_speech.setLayoutY(150);
			_clicks++;
		} else if (_clicks == 2) {
			//show and position tahi
			_tahi.setLayoutX(400);
			//set speech
			_instructions.setText("Custom has questions that have been specially written.");
			_speech.setLayoutX(400);
			_nextBtn.setText("done!");
			_clicks++;
		} else if (_clicks == 3) {
			//hide help components
			_tahi.setVisible(false);
			_speech.setVisible(false);
			_helpBtn.setDisable(false);//reenable help button
			_clicks = 0;//reset instructions
		}
	}

}

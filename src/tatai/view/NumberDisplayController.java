package tatai.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.concurrent.Task;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tatai.model.Stats;
import tatai.model.Number;
import tatai.model.NumberOutOfBoundsException;
import tatai.model.LevelSelection;
import tatai.model.Recording;

public class NumberDisplayController implements Initializable {

	@FXML
	private Button _recordBtn;
	@FXML
	private Button _playBtn;
	@FXML
	private Button _backBtn;
	@FXML
	private Button _okBtn;
	private Scene menuScene;
	private Stage window;
	@FXML 
	private AnchorPane _backgroundPane;
	@FXML
	private Label _label;
	@FXML
	private Label _numberLbl;
	@FXML 
	private Label _questionLbl;
	@FXML
	private Label _scoreLbl;

	private Number _num;
	private int _score = 0;
	private int _question = 1;
	private int _numIncorrect = 0;

	private Scene endScene;

	private LevelSelection _levelSelected;
	private Recording _recording = new Recording();

	private int _countDown; //how long recording is

	private boolean _recordTaskExist = false; //true if there is a recordTask in progress
	private Timer _recordTimer; //timer for recording
	private Task<Void> _recordTask; 

	private boolean _playTaskExist = false; //true if there's something playing
	private Task<Void> _task;//play task

	/**
	 * This will kill all processes inside this controller
	 */
	public void killProcesses() {
		if (_recordTaskExist && _levelSelected.equals(LevelSelection.EASY)) {
			_recording.killRecordEasy();
			_recordTimer.cancel();
		}
		else if(_recordTaskExist && _levelSelected.equals(LevelSelection.HARD)) {
			_recording.killRecordHard();
			_recordTimer.cancel();
		}
		if (_playTaskExist) {
			_recording.killPlay();
		}
	}
	
	//Loads the controller for the main menu 
	public void menuButtonClicked(ActionEvent event) throws IOException{
		killProcesses();

		Parent menu;
		menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		menuScene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);

		window.show();
	}

	public void recordClicked(ActionEvent event) throws IOException{
		_recordBtn.setDisable(true);
		_recordTaskExist = true;
		countDown();
		_recordTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				if (_levelSelected.equals(LevelSelection.EASY)) {
					_recording.recordEasy();
				}
				else {
					_recording.recordHard();
				}
				return null;
			}
			@Override
			public void done() {
				Platform.runLater(() -> {
					_recordTaskExist = false;

					if (_num.compare(_recording.getWord()) || _numIncorrect == 1) {//check if user has said correct word of if they've already gotten it wrong once
						//generate new question
						_question++;
						_numIncorrect = 0;
						_okBtn.setText("Next");

						if (_num.compare(_recording.getWord())) {
							setCorrectScene();
							_score++;
						} else {
							setNiceTryScene();
						}
					} 
					//User has answered wrong once, they get second try
					else {
						setTryAgainScene();
						_okBtn.setText("Retry");
						_numIncorrect = 1;

					}
					_numberLbl.setFont(Font.font("Berlin Sans FB",50));
					_recordBtn.setVisible(false);
					_backBtn.setVisible(true);
					_okBtn.setVisible(true);
					_playBtn.setVisible(true);
				});
			}
		};
		Thread recordThread = new Thread(_recordTask);
		recordThread.setDaemon(true);
		recordThread.start();
	}
	
	public void countDown() {
		if(_levelSelected.equals(LevelSelection.EASY)) {
			_countDown = 2;
		}
		else {
			_countDown = 5;
		}
		_recordTimer = new Timer();
		_recordTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						_recordBtn.setText("" + _countDown);
						_countDown--;
						if (_countDown < 0) {
							_recordTimer.cancel();
						} 
					}
				});
			}
		}, 0, 1000); //every one second
	}

	public void setLabelText(LevelSelection _level) throws IOException{
		_levelSelected = _level; 
		_questionLbl.setText("Question number: " + _question);
		_scoreLbl.setText("Score: " + _score);
		if (_level.equals(LevelSelection.EASY)){
			_label.setText("Level: Easy");
			
			_num = new Number(_level);
		}
		else if(_level.equals(LevelSelection.MEDIUM)){
			_label.setText("Level: Medium");
			try {
				_num = new Number(1,99,_level);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		else if(_level.equals(LevelSelection.HARD)){
			_label.setText("Level: Hard");
			try {
				_num = new Number(1,99,_level);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		else{
			_label.setText("Practise");
			try {
				_num = new Number(1,99);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		_numberLbl.setText(_num.getEquation());
		if (_level.equals(LevelSelection.PRACTISE)){
			_numberLbl.setText(_num.getQuiz().toString());
		}

	}

	public void setCorrectScene(){
		_numberLbl.setText("Well Done!");
		_backgroundPane.setStyle("-fx-background-color: #66ff66");
	}

	public void setTryAgainScene(){
		_numberLbl.setText("Try Again!");
		_backgroundPane.setStyle("-fx-background-color: #ff6666");
	}

	public void setNiceTryScene(){
		_numberLbl.setText("Next Time!");
		_backgroundPane.setStyle("-fx-background-color: #ff6666");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		_okBtn.setVisible(false);
		_playBtn.setVisible(false);
	}

	@FXML
	private void okBtnClicked(ActionEvent event) throws IOException{
		
		//reset the record button
		_recordBtn.setText("Record");
		_recordBtn.setDisable(false);
		_recordBtn.setVisible(true);
		
		_backBtn.setVisible(true);
		if (_question == 11) {

			//update stats object
			Stats stats = Stats.getInstance();
			stats.addResult(_score, _levelSelected);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Score.fxml"));
			Parent number = loader.load();
			endScene = new Scene(number);
			ScoreController c = loader.getController();
			c.setScoreAndLevel(_score, _levelSelected);
			window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(endScene);

			window.show();
		}

		_backgroundPane.setStyle("-fx-background-color: #84e1e0");
		_numberLbl.setFont(Font.font("Berlin Sans FB", 96));
		_okBtn.setVisible(false);
		_playBtn.setVisible(false);
		_recordBtn.setVisible(true);

		if (_numIncorrect != 1){
			_num.generateEquation();
			if (_levelSelected.equals(LevelSelection.PRACTISE)){
				_num.generateNumber();
			}
			_questionLbl.setText("Question number: " + _question);
			_scoreLbl.setText("Score: " + _score);
		}
		if (_levelSelected.equals(LevelSelection.PRACTISE)){
			_numberLbl.setText(_num.getQuiz().toString());
		}
		_numberLbl.setText(_num.getEquation());
	}

	public void playBtnClicked(){
		//make sure these buttons cannot be pressed while recording is playing
		_okBtn.setDisable(true);
		_playBtn.setDisable(true);

		_task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				_playTaskExist = true;
				_recording.play();
				return null;
			}
			@Override
			public void done() {
				Platform.runLater(() -> {
					//re-enable the buttons
					_playTaskExist = false;
					_okBtn.setDisable(false);
					_playBtn.setDisable(false);
				});
			}
		};
		Thread playThread = new Thread(_task);
		playThread.setDaemon(true);
		playThread.start();
	}
}

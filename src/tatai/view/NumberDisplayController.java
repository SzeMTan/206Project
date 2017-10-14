package tatai.view;

import java.io.IOException;

import javafx.concurrent.Task;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tatai.model.Stats;
import tatai.model.Number;
import tatai.model.NumberOutOfBoundsException;
import tatai.Main;
import tatai.model.LevelSelection;
import tatai.model.Recording;

public class NumberDisplayController {

	//components of both scenes
	@FXML private AnchorPane _backgroundPane;
	@FXML private Label _levelLbl; //tells user what level they are on
	@FXML private Label _equationLbl; //shows equation to user and tells user if they're correct or not
	@FXML private Label _questionLbl; //tells user how many questions they have done
	@FXML private Label _scoreLbl; // tells user what their score is

	//recording scene components
	@FXML private Button _recordBtn; //button which user is to hold down to record

	//feedback scene components
	@FXML private Button _playBtn; //button which plays back the user's recording
	@FXML private Button _nextBtn; //button to go to next question
	@FXML private TextField _userAnswer; // shows user what they said
	@FXML private Label _answerLabel;


	@FXML
	private Button _recordBtn;
	@FXML
	private Button _playBtn;
	@FXML
	private Button _backBtn;
	@FXML
	private Button _okBtn;
	private Stage window;

	private Number _num;
	private int _score = 2; //////////////////////////////////////////////// WAS 0
	private int _question = 1;
	private int _numIncorrect = 0;

	private Scene endScene;

	private LevelSelection _levelSelected;
	private Recording _recording = new Recording();

	private boolean _playTaskExist = false; //true if there's something playing
	private Task<Void> _task;//play task

	private Task<Void> _recordTask;

	@FXML
	public void initialize() {
		//hide all components which aren't part of recording scene
		_nextBtn.setVisible(false);
		_playBtn.setVisible(false);
		_answerLabel.setVisible(false);
		_userAnswer.setVisible(false);
	}


	//actions for when home is clicked
	public void homeClick(ActionEvent event) throws IOException{
		//load pop up asking for user confirmation that they want to leave

		if (!_levelSelected.equals(LevelSelection.PRACTISE)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("./view/QuitPlayConfirm.fxml"));
			Parent parent = loader.load();
			Scene confirmScene = new Scene(parent);

			Stage confirmWindow = new Stage();
			confirmWindow.setScene(confirmScene);
			confirmWindow.initModality(Modality.APPLICATION_MODAL);//makes it so that user can't click on main window

			loader.<QuitPlayConfirmController>getController().getYesBtn().setOnAction(e -> { //user wishes to quit
				try {
					//change to main menu scene
					FXMLLoader loaderMenu = new FXMLLoader();
					loaderMenu.setLocation(Main.class.getResource("./view/MainMenu.fxml"));
					Parent parentMenu = loaderMenu.load();
					Scene sceneMenu = new Scene(parentMenu);
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setScene(sceneMenu);

					confirmWindow.close();//close pop up window

					killProcesses();//make sure to stop anything that is playing
				} catch (IOException e1) {
					e1.printStackTrace();
				}			
			});

			loader.<QuitPlayConfirmController>getController().getNoBtn().setOnAction(e -> { //user doesn't wish to quit
				confirmWindow.close();
			});
			confirmWindow.show();
		} else { //user is practicing and there's no need to ask them for leave confirmation
			//change to main menu scene
			FXMLLoader loaderMenu = new FXMLLoader();
			loaderMenu.setLocation(Main.class.getResource("./view/MainMenu.fxml"));
			Parent parentMenu = loaderMenu.load();
			Scene sceneMenu = new Scene(parentMenu);
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(sceneMenu);

			killProcesses();//make sure to stop anything that is playing
		}
	}

	//actions for when next is clicked
	@FXML
	private void okBtnClicked(ActionEvent event) throws IOException{

		//reset the record button
		_recordBtn.setText("Record");
		_recordBtn.setDisable(false);
		_recordBtn.setVisible(true);
		_backBtn.setVisible(true);
		
		_question = 11;  ;
		if (_question == 11) {
			Stats stats = null;
			//update stats object
			if (_levelSelected.equals(LevelSelection.EASY)){
				stats = Stats.getEasyInstance();
			}
			else if (_levelSelected.equals(LevelSelection.MEDIUM)){
				stats = Stats.getMediumInstance();
			}
			else if (_levelSelected.equals(LevelSelection.HARD)){
				stats = Stats.getHardInstance();
			}
			else{
				stats = Stats.getCustomInstance();
			}
			stats.addResult(_score, _levelSelected);

			//change to score scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Score.fxml"));
			Parent number = loader.load();
			endScene = new Scene(number);
			ScoreController c = loader.getController();
			c.setScoreAndLevel(_score, _levelSelected);
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(endScene);
		} else { //change back to recording scene
			_backgroundPane.setStyle("-fx-background-color: #afeeee"); //change colour back
			_equationLbl.setFont(Font.font("Berlin Sans FB", 96)); //make question large again

			//get rid of feedback components
			_nextBtn.setVisible(false);
			_playBtn.setVisible(false);
			_userAnswer.setVisible(false);
			_answerLabel.setVisible(false);

		_backgroundPane.setStyle("-fx-background-color: #afeeee");
		_numberLbl.setFont(Font.font("Berlin Sans FB", 96));
		_okBtn.setVisible(false);
		_playBtn.setVisible(false);
		_recordBtn.setVisible(true);

		if (_numIncorrect != 1){
			if (_levelSelected.equals(LevelSelection.PRACTISE)){
				_equationLbl.setText(_num.getQuiz().toString());
			}
			else {
				_num.generateEquation();
			}
			_questionLbl.setText("Question number: " + _question);
			_scoreLbl.setText("Score: " + _score);
		}
		if (_levelSelected.equals(LevelSelection.PRACTISE)){
			_numberLbl.setText(_num.getQuiz().toString());
		}
		else {
			_numberLbl.setText(_num.getEquation());
		}
	}

	//actions for when play is clicked
	public void playClick(){
		//make sure user can't press play when something is already playing
		_playBtn.setDisable(true);

		_task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				_playTaskExist = true;
				_recording.play(); //play recording
				return null;
			}
			@Override
			public void done() {
				Platform.runLater(() -> {
					//signal that play has finished
					_playTaskExist = false;
					_playBtn.setDisable(false);
				});
			}
		};
		Thread playThread = new Thread(_task);
		playThread.setDaemon(true);
		playThread.start();
	}

	//actions for when record is pressed
	public void recordPressed() {	
		_recordBtn.setText("recording");
		_recordTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				_recording.record(); //will record audio while button is held down
				return null;
			}
			@Override
			public void done() {
				Platform.runLater(() -> {
					_recordBtn.setText("record");
					_recording.recognize();//get what user said

					if (_num.compare(_recording.getWord()) || _numIncorrect == 1) {//check if user has said correct word of if they've already gotten it wrong once
						//generate new question
						_question++;
						_numIncorrect = 0;
						_nextBtn.setText("Next");
						if (_num.compare(_recording.getWord())) { //user was right
							setCorrectScene();
							_score++;
							_scoreLbl.setText("Score: " + _score);
						} else { //user was wrong for second time
							setNiceTryScene();
						}
					} 
					//User has answered wrong once, they get second try
					else {
						setTryAgainScene();
						_nextBtn.setText("Retry");
						_numIncorrect = 1;

					}
					_equationLbl.setFont(Font.font("Berlin Sans FB",50)); //make text smaller so that it's readible
					
					//hide recording components
					_recordBtn.setVisible(false);
					
					//get feedback components
					_nextBtn.setVisible(true);
					_playBtn.setVisible(true);
					_userAnswer.setText(_recording.getWord());
					_userAnswer.setVisible(true);
					_answerLabel.setVisible(true);
				});
			}
		};
		Thread recordThread = new Thread(_recordTask);
		recordThread.setDaemon(true);
		recordThread.start();
	}

	//actions for when record is released
	public void recordReleased() {
		_recording.killRecord(); //stop recording
	}

	//actions for when help is clicked
	public void helpClick() {

	}

	/**
	 * sets questions based on level the user has selected or if they are practicing
	 * @param _level
	 * @throws IOException
	 */
	public void setLabelText(LevelSelection _level) throws IOException{
		_levelSelected = _level; //set level user has selected

		if (_level.equals(LevelSelection.EASY)){//set equations for easy
			_levelLbl.setText("Level: Easy");
			_num = new Number(_level);
		}
		else if(_level.equals(LevelSelection.MEDIUM)){//set equations for medium
			_levelLbl.setText("Level: Medium");
			try {
				_num = new Number(1,99,_level);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		else if(_level.equals(LevelSelection.HARD)){//set equations for hard
			_levelLbl.setText("Level: Hard");
			try {
				_num = new Number(1,99,_level);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		else{
			_levelLbl.setText("Practise");//set questions for practice
			try {
				_num = new Number(1,99);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		_equationLbl.setText(_num.getEquation());
		if (_level.equals(LevelSelection.PRACTISE)){
			_equationLbl.setText(_num.getQuiz().toString()); //display question
		}

	}

	public void setCorrectScene(){
		_equationLbl.setText("Well Done!");
		_backgroundPane.setStyle("-fx-background-color: #66ff66");
	}

	public void setTryAgainScene(){
		_equationLbl.setText("Try Again!");
		_backgroundPane.setStyle("-fx-background-color: #ff6666");
	}

	public void setNiceTryScene(){
		_equationLbl.setText("Next Time!");
		_backgroundPane.setStyle("-fx-background-color: #ff6666");
	}

	/**
	 * This will kill all processes inside this controller
	 */
	public void killProcesses() {
		if (_playTaskExist) {
			_recording.killPlay();
		}
	}

}
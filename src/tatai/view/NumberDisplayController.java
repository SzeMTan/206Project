package tatai.view;

import java.io.IOException;
import org.controlsfx.control.PopOver;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.Level;
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
	private PopOver _fail;

	//feedback scene components
	@FXML private Button _playBtn1;
	@FXML private Button _playBtn2;//button which plays back the user's recording
	@FXML private Button _nextBtn; //button to go to next question
	@FXML private TextField _userAnswer; // shows user what they said
	@FXML private TextField _correctAnswer;
	@FXML private Label _userAnswerLabel;
	@FXML private Label _correctAnswerLabel;
	@FXML private Button _submitBtn;

	private Stage window;

	private Number _num;
	private int _score = 0;
	private int _question = 1;
	private int _numIncorrect = 0; //signals how many times user has gotten same question wrong

	private Scene endScene;

	private LevelSelection _levelSelected;
	private Recording _recording = new Recording();

	private boolean _playTaskExist = false; //true if there's something playing
	private Task<Void> _task;//play task

	private Task<Void> _recordTask;

	private Level _level;
	
	/**
	 * sets questions based on level the user has selected or if they are practicing
	 * @param _level
	 * @throws IOException
	 */
	public void setup(LevelSelection levelSelected, Level level) throws IOException{
		_levelSelected = levelSelected; //set level user has selected
		_level = level;

		_levelLbl.setText("Level: " + levelSelected.name());
		_level.generateQuestion();
		_equationLbl.setText(_level.getQuestion());
	}

	@FXML
	public void initialize() {
		//hide all components which aren't part of recording scene
		_playBtn1.setDisable(true);
		_submitBtn.setDisable(true);

		setFeedbackVisibility(false);
		_correctAnswerLabel.setVisible(false);
		_correctAnswer.setVisible(false);

		//setup recording fail popover
		_fail = new PopOver();
		_fail.setDetachable(false);
		//add message to popOver
		TextArea text = new TextArea("oops looks like it didn't record properly. Make sure to hold the button down for at least a second. Try again.");
		text.setEditable(false);
		text.setPrefWidth(250);
		text.setPrefHeight(100);
		text.setWrapText(true);
		Font font = new Font(14);
		text.setFont(font);
		_fail.setContentNode(text);
	}

	//actions for when record is pressed. While the record button is held down, audio should be recorded
	@FXML
	private void recordPressed() {	
		_recordBtn.setText("recording");

		_recordTask = new Task<Void>() {
			int exit = -1;
			@Override
			protected Void call() throws Exception {
				_fail.hide();
				exit = _recording.record(); //will record audio while button is held down
				return null;
			}
			@Override
			public void done() {
				Platform.runLater(() -> {
					_recordBtn.setText("Record");
					if (exit == 255) {//means recording completed properly
						_playBtn1.setDisable(false);
						_submitBtn.setDisable(false);
					} else {
						_playBtn1.setDisable(true);
						_submitBtn.setDisable(true);
						_fail.show(_recordBtn);
					}

				});
			}
		};
		Thread recordThread = new Thread(_recordTask);
		recordThread.setDaemon(true);
		recordThread.start();
	}

	//actions for when record is released
	@FXML
	private void recordReleased() {
		_recording.killRecord(); //stop recording
	}

	//actions for when next is clicked
	@FXML
	private void nextClick(ActionEvent event) throws IOException{
		if (_question == 11) { //means user has completed 10 questions and is hence finished
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
			setFeedbackVisibility(false);
			_correctAnswerLabel.setVisible(false);
			_correctAnswer.setVisible(false);

			//get recording components
			_recordBtn.setText("Record");
			setFrontVisibility(true);
			_playBtn1.setDisable(true);
			_submitBtn.setDisable(true);

			if (_numIncorrect != 1){ //means new question has to be generated
				_level.generateQuestion();
				_questionLbl.setText("Question number: " + _question);
				_scoreLbl.setText("Score: " + _score);
			}
			_equationLbl.setText(_level.getQuestion());
		}
	}

	//actions for when play is clicked
	@FXML
	private void playClick(){
		//make sure user can't press play when something is already playing
		_playBtn2.setDisable(true);
		setFrontEnabling(true);

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
					_playBtn2.setDisable(false);
					setFrontEnabling(false);
				});
			}
		};
		Thread playThread = new Thread(_task);
		playThread.setDaemon(true);
		playThread.start();
	}

	//to submit the users answer for checking
	@FXML
	private void submitClick() {
		_recordBtn.setText("Record");
		_recording.recognize();//get what user said

		if (_level.compare(_recording.getWord()) || _numIncorrect == 1) {//check if user has said correct word of if they've already gotten it wrong once
			if (!_levelSelected.equals(LevelSelection.PRACTISE)) {
				_question++;//indicate moving on to next question
			}
			_numIncorrect = 0;//reset number of incorrects
			_nextBtn.setText("Next");
			if (_level.compare(_recording.getWord())) { //user was right
				setCorrectScene();
				_score++; //update score
				_scoreLbl.setText("Score: " + _score);
			} else { //user was wrong for second time
				setNiceTryScene();
			}
			//show user the correct answer
			_correctAnswer.setText(_level.getMaori());
			_correctAnswerLabel.setVisible(true);
			_correctAnswer.setVisible(true);
		} 
		//User has answered wrong once, they get second try
		else {
			setTryAgainScene();
			//make sure they can't see the correct answer
			_correctAnswerLabel.setVisible(false);
			_correctAnswer.setVisible(false);

			_nextBtn.setText("Retry");
			_numIncorrect++; //increment number of incorrect attempts
		}
		_equationLbl.setFont(Font.font("Berlin Sans FB",35)); //make text smaller so that it's readable

		//hide recording components
		setFrontVisibility(false);

		//get feedback components
		setFeedbackVisibility(true);
		_userAnswer.setText(_recording.getWord());
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

	/**
	 * This will show or hide the feedback components depending on the boolean input parameter
	 */
	private void setFeedbackVisibility(boolean b){
		_playBtn2.setVisible(b);
		_userAnswerLabel.setVisible(b);
		_userAnswer.setVisible(b);
		_nextBtn.setVisible(b);
	}

	/**
	 * This will disable or enable the front components depending on the boolean input parameter
	 */
	private void setFrontEnabling(boolean b) {
		_playBtn1.setDisable(b);
		_submitBtn.setDisable(b);
		_recordBtn.setDisable(b);
	}

	/**
	 * This will show or hide the front components depending on the boolean input parameter
	 */
	private void setFrontVisibility(boolean b) {
		_playBtn1.setVisible(b);
		_submitBtn.setVisible(b);
		_recordBtn.setVisible(b);
	}
	//actions for when help is clicked
	@FXML
	private void helpClick() {

	}

	//actions for when home is clicked
	@FXML
	private void homeClick(ActionEvent event) throws IOException{
		if (!_levelSelected.equals(LevelSelection.PRACTISE)) {
			//load pop up asking for user confirmation that they want to leave
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/QuitPlayConfirm.fxml"));
			Parent parent = loader.load();
			Scene confirmScene = new Scene(parent);

			Stage confirmWindow = new Stage();
			confirmWindow.setScene(confirmScene);
			confirmWindow.initModality(Modality.APPLICATION_MODAL);//makes it so that user can't click on main window

			loader.<QuitPlayConfirmController>getController().getYesBtn().setOnAction(e -> { //user wishes to quit
				try {
					//change to main menu scene
					FXMLLoader loaderMenu = new FXMLLoader();
					loaderMenu.setLocation(Main.class.getResource("/tatai/view/MainMenu.fxml"));
					Parent parentMenu = loaderMenu.load();
					Scene sceneMenu = new Scene(parentMenu);
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setTitle("Tatai");
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
			loaderMenu.setLocation(Main.class.getResource("/tatai/view/MainMenu.fxml"));
			Parent parentMenu = loaderMenu.load();
			Scene sceneMenu = new Scene(parentMenu);
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(sceneMenu);

			killProcesses();//make sure to stop anything that is playing
		}
	}

}
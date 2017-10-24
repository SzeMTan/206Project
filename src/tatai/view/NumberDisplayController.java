package tatai.view;

import java.io.IOException;
import org.controlsfx.control.PopOver;

import javafx.concurrent.Task;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.Level;
import tatai.model.LevelSelection;
import tatai.model.Recording;


/**
 * The controller for the NumberDisplay fxml, it controls the loading of scenes for the equations of the different
 * levels and responsible for the buttons to record, submit and play recording.
 * @author se206
 *
 */
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
	@FXML private Button _playBtn1;//button which plays back the user's recording in the front screen
	@FXML private Button _playBtn2;//button which plays back the user's recording in the feedback screen
	@FXML private Button _nextBtn; //button to go to next question
	@FXML private TextField _userAnswer; // shows user what they said
	@FXML private TextField _correctAnswer;
	@FXML private Label _userAnswerLabel;
	@FXML private Label _correctAnswerLabel;
	@FXML private Button _submitBtn;
	
	//help components
	@FXML private Button _helpBtn;
	@FXML private ImageView _tahi;
	@FXML private Group _speech;
	@FXML private TextArea _instructions;
	@FXML private Button _helpNextBtn;
	private int _clicks;
	
	private Stage window;

	private int _score = 0; //user's score
	private int _question = 1; //what question the user is on
	private int _numIncorrect = 0; //signals how many times user has gotten same question wrong

	private Scene endScene;

	private LevelSelection _levelSelected;//level user is doing
	private Level _level;//level object generates questions
	
	private Recording _recording = new Recording();//recording object to do all the recording
	private Task<Void> _recordTask;//recording task
	
	private boolean _playTaskExist = false; //true if there's something playing
	private Task<Void> _task;//play task
	
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
		
		//hide all help components except the button
		_tahi.setVisible(false);
		_speech.setVisible(false);

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
			_helpBtn.setVisible(true);
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
		//hide help stuff
		_tahi.setVisible(false);
		_speech.setVisible(false);
		
		_helpBtn.setVisible(false);
		_helpBtn.setDisable(false);
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

	//sets the screen for when the user answers correctly
	public void setCorrectScene(){
		_equationLbl.setText("Well Done!");
		_backgroundPane.setStyle("-fx-background-color: #66ff66");
	}

	//sets the screen for when the user answers incorrectly the first time
	public void setTryAgainScene(){
		_equationLbl.setText("Try Again!");
		_backgroundPane.setStyle("-fx-background-color: #ff6666");
	}

	//sets the screen for when the user answers incorrectly the second and final time
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
		_helpBtn.setDisable(true);
		_instructions.setText("Welcome! When you�re ready to answer just hold down the record button and say your answer.");
		_nextBtn.setText("next");
		_tahi.setVisible(true);
		_speech.setVisible(true);
		
		_clicks = 0;
	}
	
	@FXML
	private void helpNextClick() {
		//on each next click, show the next set of instructions
		if (_clicks == 0) {
			_instructions.setText("Make sure to say it in a nice clear voice.");
			_clicks++;
		} else if (_clicks == 1) {
			_instructions.setText("When you are done, you can press the play button to hear what you said.");
			_clicks++;
		} else if (_clicks == 2) {
			_instructions.setText("If you are not happy with your answer, just hold the record button to answer again.");
			_clicks++;
		} else if (_clicks == 3) {
			_instructions.setText("Otherwise click the submit button to enter your answer.");
			_clicks++;
		} else if (_clicks == 4) {
			_instructions.setText("If you get an answer wrong, do not worry about it.");
			_clicks++;
		} else if (_clicks == 5) {
			_instructions.setText("You get two tries for every question and there will be ten questions in total unless you're on practise.");
			_clicks++;
		} else if (_clicks == 6) {
			_instructions.setText("On practise you get as many questions as you want until you quit.");
			_clicks++;
		} else if (_clicks == 7) {
			_instructions.setText("The important thing is to learn and have fun!");
			_nextBtn.setText("done!");
			_clicks++;
		} else if (_clicks == 8) {
			_clicks = 0;
			_tahi.setVisible(false);
			_speech.setVisible(false);
			_helpBtn.setDisable(false);
		}
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
					window.setTitle("T\u0101tai");
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
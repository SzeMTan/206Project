package tatai.view;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

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
import tatai.model.CustomLists;
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

	private CustomLists _customLists = CustomLists.getInstance();
	private int _index = -1; //-1 means it's not a custom game, otherwise it's what list to play
	private int _equationIndex = -1;

	@FXML
	public void initialize() {
		//hide all components which aren't part of recording scene
		_playBtn1.setDisable(true);
		_submitBtn.setDisable(true);
		
		setFeedbackVisibility(false);
	}

	//actions for when record is pressed. While the record button is held down, audio should be recorded
		@FXML
		private void recordPressed() {	
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
						_recordBtn.setText("Record");
						_playBtn1.setDisable(false);
						_submitBtn.setDisable(false);
						
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
			Stats stats = Stats.getInstance();
			//update stats object
			stats.addResult(_score, _levelSelected);

			//change to score scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Score.fxml"));
			Parent number = loader.load();
			endScene = new Scene(number);
			ScoreController c = loader.getController();
			c.setScoreAndLevel(_score, _levelSelected, _index);
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(endScene);
		} else { //change back to recording scene
			_backgroundPane.setStyle("-fx-background-color: #afeeee"); //change colour back
			_equationLbl.setFont(Font.font("Berlin Sans FB", 96)); //make question large again

			//get rid of feedback components
			setFeedbackVisibility(false);

			//get recording components
			_recordBtn.setText("Record");
			_recordBtn.setVisible(true);

			if (_numIncorrect != 1){ //means new question has to be generated
				if (_levelSelected.equals(LevelSelection.PRACTISE)){
					_num.generateNumber();
				} else if (_levelSelected.equals(LevelSelection.CUSTOM)) {
					_equationIndex = ThreadLocalRandom.current().nextInt(0, _customLists.getEquations(_index).size());
					_num = new Number(_customLists.getAnswer(_index).get(_equationIndex));
				}
				else if(_levelSelected.equals(LevelSelection.CUSTOM)){ //custom question generated
					
				}
				else{
					_num.generateEquation();
				}
				_questionLbl.setText("Question number: " + _question);
				_scoreLbl.setText("Score: " + _score);
			}
			if (_levelSelected.equals(LevelSelection.PRACTISE)){//means user gets another try at same question
				_equationLbl.setText(_num.getQuiz().toString());
			} else if (_levelSelected.equals(LevelSelection.CUSTOM)) {
				_equationLbl.setText(_customLists.getEquations(_index).get(_equationIndex));
			}

			else if (_levelSelected.equals(LevelSelection.CUSTOM)){
				
			}
			else{
				_equationLbl.setText(_num.getEquation());
			}
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

		if (_num.compare(_recording.getWord()) || _numIncorrect == 1) {//check if user has said correct word of if they've already gotten it wrong once
			_question++;//indicate question is complete
			_numIncorrect = 0;
			_nextBtn.setText("Next");
			if (_num.compare(_recording.getWord())) { //user was right
				setCorrectScene();
				_score++; //update score
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
		_equationLbl.setFont(Font.font("Berlin Sans FB",35)); //make text smaller so that it's readable

		//hide recording components
		_recordBtn.setVisible(false);
		_playBtn1.setVisible(false);
		_submitBtn.setVisible(false);

		//get feedback components
		setFeedbackVisibility(true);
		_userAnswer.setText(_recording.getWord());
	}

	//sets index if playing a custom list
	public void setList(int index) {
		_index = index;
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
		} else if (_level.equals(LevelSelection.CUSTOM)){
			_levelLbl.setText("Level: " + _customLists.getLists().get(_index));
			_equationIndex = ThreadLocalRandom.current().nextInt(0, _customLists.getEquations(_index).size());
			_equationLbl.setText(_customLists.getEquations(_index).get(_equationIndex));
			_num = new Number(_customLists.getAnswer(_index).get(_equationIndex));
		}
		else if(_level.equals(LevelSelection.PRACTISE)){
			_levelLbl.setText("Practise");//set questions for practice
			try {
				_num = new Number(1,99);
			} catch (NumberOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		if (_level.equals(LevelSelection.PRACTISE)){
			_equationLbl.setText(_num.getQuiz().toString()); //display question
		} else if (!_level.equals(LevelSelection.CUSTOM)){
			_equationLbl.setText(_num.getEquation());
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
	
	/**
	 * This will show or hide the feedback components depending on the boolean input parameter
	 */
	private void setFeedbackVisibility(boolean b){
		_playBtn2.setVisible(b);
		_userAnswerLabel.setVisible(b);
		_userAnswer.setVisible(b);
		_correctAnswerLabel.setVisible(b);
		_correctAnswer.setVisible(b);
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
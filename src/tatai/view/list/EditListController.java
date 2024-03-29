package tatai.view.list;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.controlsfx.control.PopOver;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.levels.customLevel.CustomLists;
import tatai.view.popup.ConfirmPopupController;

/**
 * Controller for the scene where the user wants to edit or create a new list of equations
 * @author lucy
 *
 */
public class EditListController {

	@FXML private TabPane _tabPane;

	//components in naming tab
	@FXML private TextField _name;

	//components in comments tab
	@FXML private TextArea _comments;

	//components in equation adding tab
	@FXML private ListView<String> _equationList;
	@FXML private TextField _numberOne;
	@FXML private TextField _numberTwo;
	@FXML private ComboBox<String> _operation;
	@FXML private TextField _answer;
	@FXML private Button _removeBtn;
	@FXML private Button _addBtn;
	@FXML private Button _doneBtn;

	//help components
	@FXML private Button _helpOne;
	@FXML private Button _helpTwo;
	@FXML private Button _helpThree;
	@FXML private Group _speechOne;
	@FXML private Group _speechTwo;
	@FXML private Group _speechThree;
	@FXML private Button _nextOne;
	@FXML private Button _nextTwo;
	@FXML private Button _nextThree;
	@FXML private TextArea _instructionsOne;
	@FXML private TextArea _instructionsTwo;
	@FXML private TextArea _instructionsThree;
	private int _clicks = 0;

	//error message popOver
	private PopOver _popOver = new PopOver();
	private final String POPOVERTITLE = "Error Message";
	private Label _message = new Label();

	private int _index; //keep track of which list is being edited. If = -1, then it's a new list

	private CustomLists _customList = CustomLists.getInstance(); //contains all custom lists
	private ListProperty<String> _listProperty = new SimpleListProperty<>(); //sets list view
	private ArrayList<String> _equations = new ArrayList<String>(); //contains equations of viewed list
	private ArrayList<Integer> _answers = new ArrayList<Integer>(); //contains answers to equations of viewed list

	private Boolean _madeChanges = false; //keep track of whether user has made any changes to list

	public EditListController(int index) {
		_index = index;
	}

	@FXML
	private void initialize() {
		//hide all help components
		_speechOne.setVisible(false);
		_speechTwo.setVisible(false);
		_speechThree.setVisible(false);
		
		//means user has selected to view list
		if (_index != -1) {
			_equations = _customList.getEquations(_index);
			_answers = _customList.getAnswer(_index);

			//display name of list
			_name.setText(_customList.getLists().get(_index));

			//display equations
			_listProperty.set(FXCollections.observableArrayList(_equations));
			_equationList.itemsProperty().bind(_listProperty);

			//display comments
			_comments.setText(_customList.getComment(_index));
		}

		//make it so that only numbers can be entered into this textField
		_numberOne.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d+")) { //check if newValue is number
					_numberOne.setText(newValue.replaceAll("[^\\d]", "")); //remove all non-number characters
				}
			}
		});

		//make it so that only numbers can be entered into this textField
		_numberTwo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d+")) { //check if newValue is number
					_numberTwo.setText(newValue.replaceAll("[^\\d]", "")); //remove all non-number characters
				}
			}
		});

		//add operations to _operations comboBox. Note that "+" has already been added in the fxml file
		_operation.getItems().add("-");
		_operation.getItems().add("\u00F7");
		_operation.getItems().add("\u00D7");

		//if name has been entered, update _madeChanges to indicate the user has made some changes
		_name.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (! oldValue.equals(newValue)) {
					_madeChanges = true;
				}
			}	
		});

		//if comments have been entered, update _madeChanges to indicate the user has made some changes
		_comments.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (! oldValue.equals(newValue)) {
					_madeChanges = true;
				}
			}
		});

		//disable delete button when list is empty
		_removeBtn.disableProperty().bind(Bindings.isEmpty(_equationList.getItems()));

		//setup popOver
		_popOver.setDetachable(false);
		//allow user to close
		_popOver.setCloseButtonEnabled(true);
		//set title
		_popOver.setHeaderAlwaysVisible(true);
		_popOver.setTitle(POPOVERTITLE);
		//add message to popOver
		_popOver.setContentNode(_message);
	}

	/**
	 * loadManage will take an fxml file and create a scene with it
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

	//when the next button is clicked
	@FXML
	private void nextClick() {
		_tabPane.getSelectionModel().selectNext();
	}

	//when the help button on name tab is clicked.
	@FXML
	private void helpClick(ActionEvent event) {
		_helpOne.setDisable(true);
		_helpTwo.setDisable(true);
		_helpThree.setDisable(true);
		
		_clicks = 0;
		
		if (event.getSource().equals(_helpOne)) { //user clicked help on name tab
			_nextOne.setText("Next");
			_instructionsOne.setText("The name of your list goes here.");
			_speechOne.setVisible(true);
		} else if (event.getSource().equals(_helpTwo)) { //user clicked help on comments tab
			_nextTwo.setText("Next");
			_instructionsTwo.setText("Comments about the list go here.");
			_speechTwo.setVisible(true);
		} else if (event.getSource().equals(_helpThree)) { //user clicked help on equations tab
			_nextThree.setText("Next");
			_instructionsThree.setText("Here is where new equations are added to the list.");
			_speechThree.setLayoutX(260);
			_speechThree.setVisible(true);
		}
	}

	//when the nextHelp button on name tab is clicked.
	@FXML
	private void helpOneNextClick() {
		if (_clicks == 0) {
			_instructionsOne.setText("There are a number of rules the name has to follow.");
		} else if (_clicks == 1) {
			_instructionsOne.setText("It cannot be the same name as another list");
		} else if (_clicks == 2) {
			_instructionsOne.setText("And it can only contain letters, numbers, underscores or dashes");
			_nextOne.setText("Done!");
		} else if (_clicks == 3) {
			_helpOne.setDisable(false);
			_helpTwo.setDisable(false);
			_helpThree.setDisable(false);
			_speechOne.setVisible(false);
		}
		_clicks++;
	}

	//when the nextHelp button on comments tab is clicked.
	@FXML
	private void helpTwoNextClick() {
		//run through all the instructions
		if (_clicks == 0) {
			_instructionsTwo.setText("These comments can be about what type of questions are in the list");
		} else if (_clicks == 1) {
			_instructionsTwo.setText("or what the goals are for this list for example.");
			_nextTwo.setText("done!");
		} else if (_clicks == 2) {
			_helpOne.setDisable(false);
			_helpTwo.setDisable(false);
			_helpThree.setDisable(false);
			_speechTwo.setVisible(false);
		}
		_clicks++;
	}

	//when the nextHelp button on equations tab is clicked.
	@FXML
	private void helpThreeNextClick() {
		//run through all the instructions
		if (_clicks == 0) {
			_instructionsThree.setText("Just type a number into each of the two boxes labelled number.");
		} else if (_clicks == 1) {
			_instructionsThree.setText("then pick an operator from the drop down list.");
		} else if (_clicks == 2) {
			_instructionsThree.setText("If you are happy with the equation, click add and it will appear above.");
		} else if (_clicks == 3) {
			_instructionsThree.setText("There are some rules about the equations though.");
		} else if (_clicks == 4) {
			_instructionsThree.setText("It cannot already be in the list");
		} else if (_clicks == 5) {
			_instructionsThree.setText("and the answer has to be a whole number between 1 and 99.");
		} else if (_clicks == 6) {
			_speechThree.setLayoutX(380);
			_instructionsThree.setText("To remove a question just select it and then click remove.");
		} else if (_clicks == 7) {
			_instructionsThree.setText("Once you are happy with everything, click done to finish.");
			_nextThree.setText("done!");
		} 
		else if (_clicks == 8) {
			_helpOne.setDisable(false);
			_helpTwo.setDisable(false);
			_helpThree.setDisable(false);
			_speechThree.setVisible(false);
		}
		_clicks++;
	}

	//when the back button is clicked
	@FXML
	private void backClick(ActionEvent event) throws IOException {

		//if user has made any changes
		if (_madeChanges) {
			//open up a popup window asking for confirmation that they want to leave
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/popup/ConfirmPopup.fxml"));
			Parent parent = loader.load();
			Scene confirmationScene = new Scene(parent);

			Stage popupWindow = new Stage();
			popupWindow.initModality(Modality.APPLICATION_MODAL);//stops user from pressing main window
			popupWindow.setScene(confirmationScene);
			popupWindow.setResizable(false);
			
			loader.<ConfirmPopupController>getController().setPopup("Are you sure you want to go back?", 
					"All changes will be deleted");
			
			//if yes button is clicked, close the popup window and go back to list main page scene
			loader.<ConfirmPopupController>getController().getYesBtn().setOnAction(e -> {
				try {
					Scene listScene = loadManage("/tatai/view/list/Lists.fxml");
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setTitle("View Lists");
					window.setScene(listScene);
					popupWindow.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}			
			});

			//if no button is clicked, just close the popup window
			loader.<ConfirmPopupController>getController().getNoBtn().setOnAction(e -> {
				popupWindow.close();		
			});
			popupWindow.show();
		} else {
			//user hasn't made any changes and therefore there's no point to a pop up
			Scene listScene = loadManage("/tatai/view/list/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setTitle("View Lists");
			window.setScene(listScene);
		}
	}

	//when the add button is clicked 
	@FXML
	private void addClick() {
		//notify that the user hasn't created any equation
		if (_numberOne.getText().equals("") || _numberTwo.getText().equals("")) {	
			//display popOver with user message
			_message.setText("oops, looks like you haven't entered two numbers");
			_popOver.show(_addBtn);
		} else {
			try {
				//calculate answer to the question the user has entered
				int numberOne = Integer.parseInt(_numberOne.getText());
				int numberTwo = Integer.parseInt(_numberTwo.getText());
				double answer = 0;
				if (_operation.getSelectionModel().getSelectedItem().equals("+")) {
					answer = Math.addExact(numberOne, numberTwo);//throws arithmetic exception for overflow
				} else if(_operation.getSelectionModel().getSelectedItem().equals("-")) {
					answer = numberOne - numberTwo;
				} else if(_operation.getSelectionModel().getSelectedItem().equals("\u00F7")){
					answer = (double)numberOne / numberTwo;
				} else {
					answer = Math.multiplyExact(numberOne, numberTwo);//throws arithmetic exception for overflow
				}

				//equation user has entered
				String equation = _numberOne.getText() + " " + _operation.getSelectionModel().getSelectedItem() +
						" " + _numberTwo.getText();

				//round double to 2dp
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.CEILING);

				//show answer to the user
				_answer.setText(df.format(answer));

				//check if answer is valid and if equation is a duplicate
				if (answer % 1 == 0 && answer >= 1 && answer <= 99 && !_equations.contains(equation)) { 
					//indicate user has made changes
					_madeChanges = true;
					//show new equation to user
					_equationList.getItems().add(equation);

					//add equations to list
					_equations.add(equation);
					_answers.add((int)answer);
				} else if (answer % 1 != 0) {
					//display popOver with error message
					_message.setText("oops, the answer has to be a whole number");
					_popOver.show(_addBtn);
				} else if (answer < 1) {
					//display popOver with error message
					_message.setText("oops, the answer has to be greater than 0");
					_popOver.show(_addBtn);
				} else if (answer > 99) {
					//display popOver with error message
					_message.setText("oops, the answer has to be less than 99");
					_popOver.show(_addBtn);
				} else {
					//display popOver with error message
					_message.setText("oops, looks like that equation has already been added");
					_popOver.show(_addBtn);
				}
			} catch (NumberFormatException | ArithmeticException e) { //occurs when number user has entered it too large
				//display popOver with error message
				_message.setText("oops, a number you have entered is too big");
				_popOver.show(_addBtn);
			}
		}
	}

	//when remove button is clicked
	@FXML
	private void removeClick() {
		//check if user has selected any equation
		if (_equationList.getSelectionModel().getSelectedIndex() != -1) {
			//indicate user has made a change
			_madeChanges = true;

			//get selected equation
			int index = _equationList.getSelectionModel().getSelectedIndex();

			//remove equation from gui
			_equationList.getItems().remove(index);

			//remove equation from lists
			_equations.remove(index);
			_answers.remove(index);
		} else {
			_message.setText("oops, looks like you haven't selected any equation");
			_popOver.show(_removeBtn);
		}
	}

	//when done is clicked
	@FXML
	private void doneClick(ActionEvent event) {
		//get name of list
		String name = _name.getText();
		//make sure that the name of the list is valid and that there is at least one equation
		if (!name.equals("") && name.matches("[a-zA-Z0-9_-]*") && !_equationList.getItems().isEmpty()) { 
			//make sure name doesn't already belong to another list
			if ((_index == -1 && !_customList.getLists().contains(name)) || _index != -1) {
				//update this list
				_customList.updateList(_index, name, _comments.getText(), _equations, _answers);
				try {
					//go back to list main page scene
					Scene listScene = loadManage("/tatai/view/list/Lists.fxml");
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setTitle("View Lists");
					window.setScene(listScene);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				//display popOver with error message
				_message.setText("oops, looks like the name of your list is already taken");
				_popOver.show(_doneBtn);
			}
		} else if (name.equals("")) {
			//display popOver with error message
			_message.setText("oops, looks like you haven't entered a name for your list");
			_popOver.show(_doneBtn);
		} else if(!name.matches("[a-zA-Z0-9_-]*")) {
			//display popOver with error message
			_message.setText("oops, looks like the name you've entered contains invalid characters");
			_popOver.show(_doneBtn);
		} else {
			//display popOver with error message
			_message.setText("oops, looks like your list doesn't have any equations");
			_popOver.show(_doneBtn);
		}
	}

}

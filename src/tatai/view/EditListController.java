package tatai.view;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.CustomLists;

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
	public void nextClick() {
		_tabPane.getSelectionModel().selectNext();
	}

	//when the help button is clicked. Note that it has not been implemented yet
	public void helpClick() {

	}

	//when the cancel button is clicked
	public void backClick(ActionEvent event) throws IOException {

		//if user has made any changes
		if (_madeChanges) {
			//open up a popup window asking for confirmation that they want to leave
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/BackConfirmation.fxml"));
			Parent parent = loader.load();
			Scene confirmationScene = new Scene(parent);

			Stage popupWindow = new Stage();
			popupWindow.initModality(Modality.APPLICATION_MODAL);//stops user from pressing main window
			popupWindow.setScene(confirmationScene);

			//if yes button is clicked, close the popup window and go back to list main page scene
			loader.<BackConfirmationController>getController().getYesButton().setOnAction(e -> {
				try {
					Scene listScene = loadManage("/tatai/view/Lists.fxml");
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setScene(listScene);
					popupWindow.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}			
			});

			//if no button is clicked, just close the popup window
			loader.<BackConfirmationController>getController().getNoButton().setOnAction(e -> {
				popupWindow.close();		
			});
			popupWindow.show();
		} else {
			//user hasn't made any changes and therefore there's no point to a pop up
			Scene listScene = loadManage("/tatai/view/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(listScene);
		}
	}

	//when the add button is clicked 
	public void addClick() {
		//notify that the user hasn't created any equation
		if (_numberOne.getText().equals("") || _numberTwo.getText().equals("")) {
			System.out.println("user hasn't entered any numbers");
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
				}
			} catch (NumberFormatException | ArithmeticException e) { //occurs when number user has entered it too large
				System.out.println("oops, the number you have entered is too big");
			}
		}
	}

	//when remove button is clicked
	public void removeClick() {
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
		}
	}

	//when done is clicked
	public void doneClick(ActionEvent event) {
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
					Scene listScene = loadManage("/tatai/view/Lists.fxml");
					Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
					window.setScene(listScene);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

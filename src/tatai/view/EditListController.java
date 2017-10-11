package tatai.view;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import tatai.Main;

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

	@FXML
	public void initialize() {
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

	//when the next button is clicked
	public void nextClick() {
		_tabPane.getSelectionModel().selectNext();
	}

	//when the help button is clicked
	public void helpClick() {

	}

	//when the cancel button is clicked
	public void cancelClick(ActionEvent event) {
		//open up a are you sure popup
		//if yes is clicked
		try {
			Scene listScene = loadManage("./view/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(listScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//when the add button is clicked 
	public void addClick() {
		if (_numberOne.getText().equals("") || _numberTwo.getText().equals("")) {
			System.out.println("user hasn't entered any numbers");
		} else {
			try {
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

				//round double to 2dp
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.CEILING);

				//show answer to the user
				_answer.setText(df.format(answer));

				if (answer % 1 == 0 && answer >= 1 && answer <= 99) { //means answer is valid
					_equationList.getItems().add(_numberOne.getText() + " " + _operation.getSelectionModel().getSelectedItem() +
							" " + _numberTwo.getText());
				}
			} catch (NumberFormatException | ArithmeticException e) {
				System.out.println("oops, the number you have entered is too big");
			}
		}
	}

	//when remove button is clicked
	public void removeClick() {
		if (_equationList.getSelectionModel().getSelectedIndex() != -1) {
			_equationList.getItems().remove(_equationList.getSelectionModel().getSelectedIndex());
		}
	}

	//when done is clicked
	public void doneClick(ActionEvent event) {
		//make sure that the name of the list is valid
		if (!_name.getText().equals("") && _name.getText().matches("[a-zA-Z0-9_-]*")) { //gotta add and if name doesn't already exist
			try {
				Scene listScene = loadManage("./view/Lists.fxml");
				Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
				window.setScene(listScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

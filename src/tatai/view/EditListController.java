package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class EditListController {
	
	//components in equation list creating scene
	@FXML ListView<String> _equationList;
	@FXML ComboBox<String> _numberOne;
	@FXML ComboBox<String> _numberTwo;
	@FXML ComboBox<String> _operation;
	@FXML TextArea _answer;
	
	@FXML
	public void initialize() {
	}

}

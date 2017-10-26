package tatai.view.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmPopupController {
	@FXML private Button _yesBtn;
	@FXML private Button _noBtn;
	@FXML private Label _question;
	@FXML private Label _consequence;
	
	public void setPopup(String question, String consequence) {
		_question.setText(question);
		_consequence.setText(consequence);
	}
	
	public Button getYesBtn() {
		return _yesBtn;
	}
	
	public Button getNoBtn() {
		return _noBtn;
	}
}

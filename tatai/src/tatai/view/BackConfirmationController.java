package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BackConfirmationController {

	@FXML private Button _yesBtn;
	@FXML private Button _noBtn;
	
	public Button getYesButton() {
		return _yesBtn;
	}
	
	public Button getNoButton() {
		return _noBtn;
	}
}

package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuitPlayConfirmController {

	@FXML private Button _yesBtn;
	@FXML private Button _noBtn;
	
	public Button getYesBtn() {
		return _yesBtn;
	}
	
	public Button getNoBtn() {
		return _noBtn;
	}
}

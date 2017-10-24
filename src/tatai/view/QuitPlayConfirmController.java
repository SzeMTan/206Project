package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * This controller belongs to the fxml pop-up for when the user wants to exit current game. It asks
 * for confirmation to exit and tells the user they will lose all current data,
 * "Yes" will return the user to the main menu and "No" will just close the current
 * pop-up and continue the application.
 * @author se206
 *
 */
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

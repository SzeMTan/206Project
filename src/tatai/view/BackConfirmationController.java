package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * This is the controller for the fxml that appears when a user would like to return to the main menu mid-game.
 * It notifies the user that they will lose all current stats if they choose to go back. 
 * @author se206
 *
 */
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

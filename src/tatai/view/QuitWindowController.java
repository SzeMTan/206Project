package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * This controller belongs to the fxml pop-up for when the user wants to exit the Main window. It asks
 * for confirmation to exit, "Yes" will save all the data into JSON objects and "No" will just close the current
 * pop-up and continue the application.
 * @author se206
 *
 */
public class QuitWindowController {
	
	@FXML private Button _noBtn;
	@FXML private Button _yesBtn;

	public Button getYesBtn(){
		return _yesBtn;
	}
	
	public Button getNoBtn(){
		return _noBtn;
	}
	
}

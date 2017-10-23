package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import tatai.model.Stats;

public class QuitWindowController {
	
	@FXML
	private Button _noBtn;
	@FXML
	private Button _yesBtn;

	public Button getYesBtn(){
		return _yesBtn;
	}
	
	public Button getNoBtn(){
		return _noBtn;
	}
	
}

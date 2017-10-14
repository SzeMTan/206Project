package tatai.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuitWindowController {
	
	@FXML
	private Button _noBtn;
	@FXML
	private Button _cancelBtn;

	public void yesBtn(){
		
	}
	
	public Button getNoBtn(){
		return _noBtn;
	}
	
	public Button getCancelBtn(){
		return _cancelBtn;
	}
}

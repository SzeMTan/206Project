package tatai.view;

import java.io.IOException;

import tatai.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MainMenuController {
	
	@FXML
	ListView<String> _list; //list holding all the lists of equations
	
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

	/**
	 * when the list button has been clicked, switch over to the list screen
	 * @param event
	 */
	public void listsClick(ActionEvent event) {
		try {
			Scene listScene = loadManage("./view/Lists.fxml");
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(listScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playClick(ActionEvent event) {
		
	}
	
	public void practiseClick(ActionEvent event) {
		
	}
	
	public void statisticsClick(ActionEvent event) {
		
	}
	
	public void helpClick(ActionEvent event) {
		
	}
}


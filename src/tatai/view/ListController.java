package tatai.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tatai.Main;

public class ListController {
	
	@FXML
	ListView<String> _list;
	
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

	public void editClick(ActionEvent event) {
		try {
			Scene editListScene = loadManage("./view/EditList.fxml");
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(editListScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newClick(ActionEvent event) {
		
	}
	
	public void deleteClick(ActionEvent event) {
		
	}
}

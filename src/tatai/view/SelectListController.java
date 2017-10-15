package tatai.view;

import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.CustomLists;
import tatai.model.LevelSelection;

/**
 * Controller for page where user selects which custom list they want to play
 * @author lucy
 *
 */
public class SelectListController {
	@FXML private ListView<String> _lists;
	
	private CustomLists _customList = CustomLists.getInstance();
	private ListProperty<String> _listProperty = new SimpleListProperty<>();
	
	@FXML
	private void initialize() {
		_listProperty.set(FXCollections.observableArrayList(_customList.getLists()));
		_lists.itemsProperty().bind(_listProperty);
	}
	
	@FXML
	private void homeClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/MainMenu.fxml"));
			Parent parent = loader.load();
			Scene mainMenuScene = new Scene(parent);
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(mainMenuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void helpClick() {
		
	}
	
	@FXML
	private void startClick(ActionEvent event) throws IOException {
		int index = _lists.getSelectionModel().getSelectedIndex();
		if (index != -1) {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("/tatai/view/Start.fxml"));
	        Parent easyStart = loader.load();
	        loader.<StartController>getController().setLevel(LevelSelection.CUSTOM, index);
	        
			Scene scene = new Scene(easyStart);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.setTitle(_customList.getLists().get(index));
		}
	}
}

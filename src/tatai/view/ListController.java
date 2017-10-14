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

public class ListController {

	@FXML
	private ListView<String> _list;
	private ListProperty<String> _listProperty = new SimpleListProperty<>();

	private CustomLists _customLists = CustomLists.getInstance();

	@FXML
	private void initialize() {
		_listProperty.set(FXCollections.observableArrayList(_customLists.getLists()));
		_list.itemsProperty().bind(_listProperty);
	}

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

	@FXML
	private void viewClick(ActionEvent event) {
		int index = _list.getSelectionModel().getSelectedIndex();
		if (index != -1) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("./view/EditList.fxml"));
				loader.setControllerFactory(c -> {
					return new EditListController(index);
				});
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				stage.setScene(scene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void newClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("./view/EditList.fxml"));
			loader.setControllerFactory(c -> {
				return new EditListController(-1);
			});
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void deleteClick(ActionEvent event) {
		int index = _list.getSelectionModel().getSelectedIndex();
		if (index != -1) {
			_customLists.deleteList(index);
			_list.getItems().remove(index);
		}
	}

	public void helpclick() {

	}

	public void homeClick(ActionEvent event) {
		try {
			Scene mainMenuScene = loadManage("./view/MainMenu.fxml");
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(mainMenuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

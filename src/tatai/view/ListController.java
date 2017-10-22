package tatai.view;

import java.io.IOException;

import org.controlsfx.control.PopOver;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.CustomLists;

/**
 * Controller for list main page
 * @author lucy
 *
 */
public class ListController {

	@FXML
	private ListView<String> _list;
	private ListProperty<String> _listProperty = new SimpleListProperty<>();

	@FXML private Button _deleteBtn;
	@FXML private Button _viewBtn;

	//contains all the lists
	private CustomLists _customLists = CustomLists.getInstance();
	
	//error message to user
	PopOver _popOver = new PopOver();
	Label _message = new Label();

	@FXML
	private void initialize() {
		_listProperty.set(FXCollections.observableArrayList(_customLists.getLists()));
		_list.itemsProperty().bind(_listProperty);

		//disable delete button when list is empty
		_deleteBtn.disableProperty().bind(Bindings.isEmpty(_list.getItems()));

		//disable view button when list is empty
		_viewBtn.disableProperty().bind(Bindings.isEmpty(_list.getItems()));
		
		//setup popOver
		_popOver.setDetachable(false);
		//allow user to close
		_popOver.setCloseButtonEnabled(true);
		//set title
		_popOver.setHeaderAlwaysVisible(true);
		_popOver.setTitle("error message");
		//set error message to user
		_message.setText("oops, looks like you haven't selected any list");
		//add message to popOver
		_popOver.setContentNode(_message);
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
				loader.setLocation(Main.class.getResource("/tatai/view/EditList.fxml"));
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
		} else {
			_popOver.show(_viewBtn);
		}
	}

	@FXML
	private void newClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/EditList.fxml"));
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
		} else {
			_popOver.show(_deleteBtn);
		}
	}

	public void helpclick() {
	}

	public void homeClick(ActionEvent event) {
		try {
			Scene mainMenuScene = loadManage("/tatai/view/MainMenu.fxml");
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setScene(mainMenuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

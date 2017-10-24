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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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

	//help components
	@FXML private Button _helpBtn;
	@FXML private Group _speech;
	@FXML private Button _nextBtn;
	@FXML private TextArea _instructions;
	private int _clicks = 0;

	//contains all the lists
	private CustomLists _customLists;

	//error message to user
	PopOver _popOver = new PopOver();
	Label _message = new Label();

	@FXML
	private void initialize() {
		//hide help components
		_speech.setVisible(false);

		_customLists = CustomLists.getInstance();

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
		//list has been selected
		if (index != -1) {
			try {
				//load scene showing the list
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/tatai/view/EditList.fxml"));
				loader.setControllerFactory(c -> {
					return new EditListController(index);
				});
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				stage.setTitle(_list.getSelectionModel().getSelectedItem());
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
				return new EditListController(-1); //indicate it's a new list
			});
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("new custom list");
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void deleteClick(ActionEvent event) {
		int index = _list.getSelectionModel().getSelectedIndex();
		if (index != -1) {//means user has selected something
			_customLists.deleteList(index);
			_list.getItems().remove(index);
		} else { //user hasn't selected anything
			_popOver.show(_deleteBtn);
		}
	}
	
	@FXML
	private void helpClick(){
		//disable help button
		_helpBtn.setDisable(true);
		
		//setup help 
		_instructions.setText("Welcome to the lists page");
		_speech.setLayoutY(0);
		_speech.setVisible(true);
		
		_clicks = 0;
	}
	
	@FXML 
	private void nextClick() {
		if (_clicks == 0) {
			_instructions.setText("Here you can create, view or delete your custom lists.");
			_clicks++;
		} else if (_clicks == 1) {
			_instructions.setText("In these lists will be the equations displayed when the user selects to play custom level.");
			_clicks++;
		} else if (_clicks == 2) {
			_instructions.setText("Select a list then click here to view and edit it.");
			_clicks++;
		} else if (_clicks == 3) {
			//move help
			_speech.setLayoutY(100);
			_instructions.setText("Create a new list by clicking here.");
			_clicks++;
		} else if (_clicks == 4) {
			//move help
			_speech.setLayoutY(190);
			_instructions.setText("If you want to delete a list, select it then click here.”");
			_clicks++;
			_nextBtn.setText("done!");
		} else if (_clicks == 5) {
			//reenable help button
			_helpBtn.setDisable(false);
			//hide help
			_speech.setVisible(false);
		}
	}


	public void homeClick(ActionEvent event) {
		try {
			Scene mainMenuScene = loadManage("/tatai/view/MainMenu.fxml");
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("Tatai");
			stage.setScene(mainMenuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

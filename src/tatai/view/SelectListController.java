package tatai.view;

import java.io.IOException;

import org.controlsfx.control.PopOver;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import tatai.model.LevelSelection;

/**
 * Controller for page where user selects which custom list they want to play
 * @author lucy
 *
 */
public class SelectListController {
	@FXML private ListView<String> _lists;
	@FXML private TextArea _comments;
	@FXML private Button _startBtn;

	//help components
	@FXML private Button _helpBtn;
	@FXML private Group _speech;
	@FXML private Button _nextBtn;
	@FXML private TextArea _instructions;
	private int _clicks = 0;

	private CustomLists _customList = CustomLists.getInstance();//contains all the lists
	private ListProperty<String> _listProperty = new SimpleListProperty<>();

	@FXML
	private void initialize() {
		
		//hide help stuff
		_speech.setVisible(false);
		
		_listProperty.set(FXCollections.observableArrayList(_customList.getLists()));
		_lists.itemsProperty().bind(_listProperty);

		//disable start button when _lists is empty
		_startBtn.disableProperty().bind(Bindings.isEmpty(_lists.getItems()));

		_lists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int index = _lists.getSelectionModel().getSelectedIndex();
				//notify user that there's no comments for the selected list
				if (_customList.getComment(index).equals("")) {
					_comments.setText("there are no comments to show");
				} else {
					//display comments of selected list to user
					_comments.setText(_customList.getComment(_lists.getSelectionModel().getSelectedIndex()));
				}
			}
		});
	}

	@FXML
	private void homeClick(ActionEvent event) {
		try {
			//load main menu scene
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/MainMenu.fxml"));
			Parent parent = loader.load();
			Scene mainMenuScene = new Scene(parent);
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.setTitle("T\u0101tai");
			stage.setScene(mainMenuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void helpClick() {
		_helpBtn.setDisable(true);

		_clicks = 0;

		_nextBtn.setText("next");
		_instructions.setText("This is where you select the list you want to play.");

		_speech.setVisible(true);
	}

	@FXML
	private void nextClick() {
		if (_clicks == 0) {
			_instructions.setText("When you click on a list, its comments will appear on the right");
		} else if (_clicks == 1) {
			_instructions.setText("When you have selected the list you want to play, just click start");
			_nextBtn.setText("done!");
		} else if (_clicks == 2) {
			_helpBtn.setDisable(false);
			_speech.setVisible(false);
		}
		_clicks++;
	}

	@FXML
	private void startClick(ActionEvent event) throws IOException {
		int index = _lists.getSelectionModel().getSelectedIndex();
		//user has selected a list
		if (index != -1) {
			//load start scene
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tatai/view/Start.fxml"));
			Parent customStart = loader.load();
			loader.<StartController>getController().setLevel(LevelSelection.CUSTOM, index);

			Scene scene = new Scene(customStart);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.setTitle(_customList.getLists().get(index));
		} else {
			//setup popOver
			PopOver popOver = new PopOver();
			popOver.setDetachable(false);
			//set error message to user
			Label label = new Label("Oops, it looks like you haven't selected any list");
			//add message to popOver
			popOver.setContentNode(label);
			popOver.show(_startBtn);
		}
	}
}

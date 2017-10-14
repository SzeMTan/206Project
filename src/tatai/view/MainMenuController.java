package tatai.view;

import java.io.IOException;

import tatai.Main;
import tatai.model.LevelSelection;
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
	private Stage _window;
	private Scene _scene;
	
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
	
	public void playClick(ActionEvent event) throws IOException {
		Parent menu;
		menu = FXMLLoader.load(getClass().getResource("Level.fxml"));
		_scene = new Scene(menu);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();

		_window.setScene(_scene);
		_window.setTitle("Level");
		_window.show();
	}
	
	public void practiseClick(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Start.fxml"));
        Parent start = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.PRACTISE);
        
		_scene = new Scene(start);
		_window = (Stage)((Node)event.getSource()).getScene().getWindow();
		_window.setScene(_scene);
		_window.setTitle("Practise");
		_window.show();
	}
		
	public void scoresClick(ActionEvent event) throws IOException {
	FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("view/Statistics.fxml"));
    Parent stats = loader.load();
    loader.<StatisticsController>getController().setStats();
    
	_scene = new Scene(stats);
	_window = (Stage)((Node)event.getSource()).getScene().getWindow();
	System.out.println("before scene");
	_window.setScene(_scene);
	System.out.println("after scene");
	_window.setTitle("Practise");
	_window.show();
	}
	
	public void helpClick() {
		
	}
}


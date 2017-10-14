package tatai.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tatai.Main;
import tatai.model.LevelSelection;

public class LevelController {

	@FXML
	private Button easyButton;
	@FXML
	private Button hardButton;
	
	private Scene _scene;
	private Stage window;
	public String level;

	@FXML
	private void easyButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Start.fxml"));
        Parent easyStart = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.EASY, -1);
        
        
		_scene = new Scene(easyStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Easy");
	}
	
	@FXML
	private void hardButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Start.fxml"));
        Parent hardStart = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.HARD, -1);
        
		_scene = new Scene(hardStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Hard");
	}
	
	@FXML
	private void mediumButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Start.fxml"));
        Parent mediumStart = loader.load();
        loader.<StartController>getController().setLevel(LevelSelection.MEDIUM, -1);
        
		_scene = new Scene(mediumStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Medium");
	}
	
//	public void statisticsButtonClicked(ActionEvent event) throws IOException{
//		FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("view/Statistics.fxml"));
//        Parent stats = loader.load();
//        loader.<StatisticsController>getController().setStats();
//        
//		_scene = new Scene(stats);
//		window = (Stage)((Node)event.getSource()).getScene().getWindow();
//		window.setScene(_scene);
//		window.show();
//	}
	
	@FXML
	private void menuButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/MainMenu.fxml"));
        Parent menu = loader.load();
        
		_scene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Tatai");
		window.show();
	}
	
	@FXML
	private void customClick(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/SelectList.fxml"));
        Parent menu = loader.load();
        
		_scene = new Scene(menu);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(_scene);
		window.setTitle("Custom");
		window.show();
	}
	
}

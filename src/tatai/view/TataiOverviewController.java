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

public class TataiOverviewController {

	@FXML
	private Button easyButton;
	@FXML
	private Button hardButton;
	
	private Scene easyStartScene;
	private Scene hardStartScene;
	private Scene statsScene;
	private Stage window;
	public String level;

	
	public void easyButtonClicked(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Level.fxml"));
        Parent easyStart = loader.load();
        loader.<LevelController>getController().setLevel(LevelSelection.EASY);
        
		
		easyStartScene = new Scene(easyStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(easyStartScene);
		window.show();
	}
	
	public void hardButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Level.fxml"));
        Parent hardStart = loader.load();
        loader.<LevelController>getController().setLevel(LevelSelection.HARD);
        
		hardStartScene = new Scene(hardStart);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(hardStartScene);
		window.show();
	}
	
	public void statisticsButtonClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Statistics.fxml"));
        Parent stats = loader.load();
        loader.<StatisticsController>getController().setStats();
        
		statsScene = new Scene(stats);
		window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(statsScene);
		window.show();
	}
}

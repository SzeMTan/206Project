package tatai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tatai.model.CustomLists;
import tatai.model.Stats;
import tatai.view.QuitWindowController;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	/**
	 * This class sets up the primary window stage and loads the main menu scene. 
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		//load main menu
		Parent root = FXMLLoader.load(getClass().getResource("/tatai/view/MainMenu.fxml"));
		Scene mainMenu = new Scene(root);
		primaryStage.setScene(mainMenu);
		primaryStage.setTitle("Tatai");
		
		//make sure the window is not resizable
		primaryStage.setResizable(false);

		/**
		 * The method invoked creates a pop-up message to ask the user whether or not they want to quit the game. 
		 * If they do, then the stats object and custom lists object will be stored into a JSON object in the file system,
		 * to be restored the next time the user comes back on.
		 */
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent event) {
				//open quit confirmation window
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/tatai/view/QuitWindow.fxml"));
				try {
					Parent parent = loader.load();
					Scene exitScene = new Scene(parent);
					Stage exitWindow = new Stage();
					exitWindow.setScene(exitScene);
					exitWindow.setTitle("Quit");
					exitWindow.initModality(Modality.APPLICATION_MODAL);
					loader.<QuitWindowController>getController().getYesBtn().setOnAction(e -> { //user wishes to quit        
				        try {
				        	//save stats
					        Stats stats = Stats.getInstance(); // retrieves the current stats object that we want to store
				        	Gson gson = new Gson();  ///Creates new Gson object to load to Json
				        	FileWriter statsWriter = new FileWriter(new File("stats.json"));
				            gson.toJson(stats, statsWriter);
				            statsWriter.close();
				       
				            //save custom lists
				            CustomLists customLists = CustomLists.getInstance();
				            Gson listGson = new Gson();
				            FileWriter listsWriter = new FileWriter(new File("customLists.json"));
				            listGson.toJson(customLists, listsWriter);
				            listsWriter.close();
				        } catch (IOException err) {
				            err.printStackTrace();
				        }
						exitWindow.close();		
						
					});
					loader.<QuitWindowController>getController().getNoBtn().setOnAction(e -> { //user wants to remain in the game.
						exitWindow.close();
						event.consume();
					});				
					exitWindow.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}

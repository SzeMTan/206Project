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
import tatai.model.Stats;
import tatai.model.levels.customLevel.CustomLists;
import tatai.view.popup.ConfirmPopupController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
/**
 * Tatai is a program designed to be a math learning aid for children ages between 7-10
 * and are native Maori speakers.
 * @authors stan557 ljia374
 *
 */

public class Main extends Application {
	
	static {
		Font.loadFont(Main.class.getResource("/main/resources/Berlin-Sans-FB_6747.ttf").toExternalForm(),10);
	}

	/**
	 * This class sets up the primary window stage and loads the main menu scene. 
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		//load main menu
		Parent root = FXMLLoader.load(getClass().getResource("/tatai/view/MainMenu.fxml"));
		Scene mainMenu = new Scene(root);
		primaryStage.setScene(mainMenu);
		primaryStage.setTitle("T\u0101tai");

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
				loader.setLocation(Main.class.getResource("/tatai/view/popup/ConfirmPopup.fxml"));
				try {
					//open are you sure you wish to quit window
					Parent parent = loader.load();
					Scene exitScene = new Scene(parent);
					Stage exitWindow = new Stage();

					exitWindow.setScene(exitScene);
					exitWindow.setTitle("Quit");
					exitWindow.initModality(Modality.APPLICATION_MODAL);
					exitWindow.setResizable(false);

					loader.<ConfirmPopupController>getController().setPopup("Are you sure you wish to quit?" , "");

					//When user clicks yes
					loader.<ConfirmPopupController>getController().getYesBtn().setOnAction(e -> {
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
						} catch (IOException exception) {
							exception.printStackTrace();
						}

						primaryStage.close();
						event.consume();

						exitWindow.close();
					});	

					//When user clicks no
					loader.<ConfirmPopupController>getController().getNoBtn().setOnAction(e -> {
						event.consume();

						exitWindow.close();
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
		//launch application
		launch(args);
	}
}

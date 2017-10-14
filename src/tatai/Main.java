package tatai;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tatai.view.QuitWindowController;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("./view/MainMenu.fxml"));
		Scene mainMenu = new Scene(root);
		primaryStage.setScene(mainMenu);
		primaryStage.setTitle("Tatai");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("./view/QuitWindow.fxml"));
				try {
					Parent parent = loader.load();
					Scene exitScene = new Scene(parent);
					Stage confirmWindow = new Stage();
					confirmWindow.setScene(exitScene);
					confirmWindow.setTitle("Quit");
					confirmWindow.initModality(Modality.APPLICATION_MODAL);
					loader.<QuitWindowController>getController().getNoBtn().setOnAction(e -> { //user wishes to quit
						System.out.println("no button");
						confirmWindow.close();		
					});
					loader.<QuitWindowController>getController().getCancelBtn().setOnAction(e -> { //user wishes to quit
						System.out.println("confirm window closing");
						confirmWindow.close();	
						event.consume();
					});
					
					confirmWindow.showAndWait();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
